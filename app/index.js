const path = require('path');
const util = require('util');
const stream = require('stream');
const exec = util.promisify(require('child_process').exec);
const tar = require('tar-stream');
const Docker = require('dockerode');


// Stream that does nothing when written to
const nullStream = stream.Writable();
nullStream._write = (chunk, encoding, next) => next();

// Stream that concatenates everything written to it onto a property of itself
const concatStream = stream.Writable();
concatStream.out = '';
concatStream._write = (chunk, encoding, next) => { concatStream.out += chunk.toString('UTF-8'); next(); }


// Initialize Docker
const docker = new Docker();

/** Make sure that the image is built */
async function preflight() {
  const images = await docker.listImages();
  // Check for the presence of an the codus-execute-java image
  if (!images.filter(i => i.RepoTags.includes('codus-execute-java:latest')).length) {
    // If none is present, build the image
    // FIXME: do this right. waiting on https://github.com/apocas/dockerode/issues/432
    const command = `docker build -t codus-execute-java ${path.join(__dirname, '..')}`;
    const { stdout, stderr } = await exec(command);
  }
}


/**
 * The main function implementing the entire functionality of this package.
 * This function performs all of the following:
 *   0. Build the image if it can't be found
 *   1. Create a container from the image
 *   2. Copy problem info into the container
 *   3. Copy the user's solution into the container
 *   4. Start the container. This will automatically compile, run and test the solution as defined
 *      in the Dockerfile from which the image was built.
 *   5. Copy the results out of the container
 *   6. Destroy the container
 * @param {Object} problem - JSON representation of the problem
 * @param {String} solution - the user's Java code
 */
module.exports = async function main(problem, solution) {
  // Build image if not present
  await preflight();

  // Create container
  const container = await docker.createContainer({
    Image: 'codus-execute-java',
    Env: [ `PROBLEM_NAME=${problem.name}` ],
  });

  // Create tar archive for copying files into container
  const files = tar.pack();
  files.add = util.promisify(files.entry);
  await files.add({ name: 'tests.json' }, JSON.stringify(problem)); // Add problem info
  await files.add({ name: `${problem.name}.java` }, solution);      // Add user's code
  files.finalize();
  // Copy into the container
  await container.putArchive(files, { path: '/app'});

  // Attach streams for output
  const containerStream = await container.attach({ stream: true, stdout: true, stderr: true});
  // Wrap concat-stream in promise
  container.modem.demuxStream(containerStream, nullStream, concatStream); // Untangle stream, sending stdout nowhere and concatenating stderr

  // Start container
  await container.start();
  // Wait for execution to finish
  await container.wait();
  // If stderr is full just return that as an error
  const stderr = concatStream.out;
  if (stderr) {
    await container.remove();
    return { error: stderr };
  }

  // Get the results of the tests.
  // Get tar file of out.json file
  const resultsTar = await container.getArchive({ path: '/app/out.json' });
  // Pull file out of tar archive
  const results = await new Promise((resolve) => {
    const extract = tar.extract();
    extract.on('entry', (header, stream, next) => {
      // Build Buffer from stream chunks
      const chunks = [];
      stream.on('data', chunk => chunks.push(chunk));
      stream.on('end', () => resolve(Buffer.concat(chunks))); // Resolve after first entry because there's only one
    });
    // Send resultsTar to the extractor
    resultsTar.pipe(extract);
  }).then(b => b.toString('UTF-8')).then(JSON.parse);

  // Remove container
  await container.remove();


  return results;
}
