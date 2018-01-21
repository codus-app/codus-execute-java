const fs = require('fs');
const path = require('path');
const javaExec = require('..');

const problem = JSON.parse(fs.readFileSync(path.join(__dirname, '1/tests.json'), 'UTF-8'));
const userCode = fs.readFileSync(path.join(__dirname, '1/Solution.java'), 'UTF-8');

console.log(

`
\x1b[32mTesting user code:\x1b[0m

${userCode.trim().split('\n').map(line => `  ${line}`).join('\n')}

\x1b[32magainst problem tests:\x1b[0m ${JSON.stringify(problem.testCases)}
`

);


javaExec(problem, userCode).then(result => console.log(
  '\x1b[31mResult:\x1b[0m', JSON.stringify(result)
));
