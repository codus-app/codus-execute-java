const path = require('path');
const Docker = require('dockerode');


// Initialize Docker
const docker = new Docker();

/** Make sure that the image is built */
function preflight() {
  docker.listImages().then(images => {
    // Check for the presence of an image tagged codus-execute-java:latest
    if (!images.filter(i => i.RepoTags.includes('codus-execute-java:latest')).length) {
      // If none is present, build the image
      // FIXME: waiting on https://github.com/apocas/dockerode/issues/432
      docker.buildImage({
        context: path.join(__dirname, '..'),
        src: ['Dockerfile'],
      }, {
        t: 'codus-execute-java'
      }).then(n => n.pipe(process.stdout));
    }
  });
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
  preflight();


}
