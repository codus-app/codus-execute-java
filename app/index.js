const path = require('path');
const Docker = require('dockerode');


// Initialize Docker
const docker = new Docker();

// Make sure that the image is built
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
