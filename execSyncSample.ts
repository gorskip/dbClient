import { execSync } from 'child_process';

let buffer = execSync("echo 'Hello execSync!'");
console.log('Buffer length: ', buffer.length);
console.log('Buffer', buffer.toString());
