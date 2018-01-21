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
[
  { "value": "cat dog", "expected": "cat dog", "pass": true },
  { "value": "wow, it works", "expected": "wow, it works", "pass": true }
]
```
