# codus-execute-java
> [`codus-engine`](https://github.com/arkis/codus-engine) component for securely compiling and
testing user Java code

This plugin is used by [`codus-engine`](https://github.com/arkis/codus-engine) to facilitate the
testing of user-supplied Java code safely. It has two main components, which work together to
maximize safety and ease of use.

The testing code is written in Java, and gets compiled with the user's solution on each run. In
order to protect against malicious code, all compilation and execution happens inside Docker
containers. The second component is the the JavaScript interface, which is responsible for
orchestrating containers. It can be easily required and used as a JavaScript module.

## Example
```js
const javaExec = require('codus-execute-java');

const problem = {
  "parameterTypes": ["String", "String"], "resultType": "String",
  "testCases": [
    { "parameters": ["Cat", "Dog"], "result": "cat dog" },
    { "parameters": ["Wow,", "it works"], "result": "wow, it works" }
  ]
}
const solution = `
public class Solution {
  public String main(String a, String b) {
    return a.toLowerCase() + " " + b.toLowerCase();
  }
}
`

javaExec(problem, solution).then((results) => {
  // Do something
});
```
In this case, `results` would be set to this array of test results:
```json
{
  "data": [
    { "value": "cat dog", "expected": "cat dog", "pass": true },
    { "value": "wow, it works", "expected": "wow, it works", "pass": true }
  ]
}
```

## Implementation
The JS module performs the following steps each time a request is made:
1. Build the codus-execute-java image if it can't be found. The image is built from the Dockerfile,
and contains the `jdk` as well as all of the Java code to run tests.
2. Create a Docker container from the image
3. Copy problem info (as JSON) and the user's solution into the container. These aren't included in
the image because building the image takes more than an order of magnitude longer than creating a
container from the image. It would take far too long to rebuild the image for each new user
solution, so instead each container is initialized from a pre-built image, and all case-specific
information is copied in as files after the container is created.
5. Start the container. On startup, the container:
  1. Compiles the user code alongside the testing code
  2. Runs and tests the user's solution
  3. Saves the test results to an `out.json` file
6. Read the `out.json` file out of the container. Since the container is sandboxed from access to
the parent filesystem, the results have to be saved within the container and then read out by the JS
library
7. Destroy the container
