import fs from 'fs';
import path from 'path';
import { execSync } from 'child_process';

const PROJECT_ROOT = path.resolve(import.meta.dirname, '..');
const SKELETON = path.join(PROJECT_ROOT, 'skeleton');
const OUTPUT = path.join(PROJECT_ROOT, 'scaffolded-app');

// --- Template parameters (edit these to test different combinations) ---
const values = {
  componentName: 'todo-service',
  owner: 'team-platform',
  groupId: 'com.example',
  artifactId: 'todo-service',
  packageName: 'com.example',
  packagePath: 'com/example',
  description: 'A Spring Boot application generated from Backstage',
};

function walk(dir) {
  let results = [];
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    const full = path.join(dir, entry.name);
    if (entry.isDirectory()) results = results.concat(walk(full));
    else results.push(full);
  }
  return results;
}

// Clean output directory if it exists
if (fs.existsSync(OUTPUT)) {
  fs.rmSync(OUTPUT, { recursive: true });
}
fs.mkdirSync(OUTPUT, { recursive: true });

let count = 0;

for (const tplPath of walk(SKELETON)) {
  const relPath = path.relative(SKELETON, tplPath);
  const content = fs.readFileSync(tplPath, 'utf8');

  // Replace ${{ values.<key> }} patterns with concrete values
  // This avoids conflicts with GitHub Actions ${{ }} expressions (e.g. hashFiles, runner.os)
  let rendered = content;
  for (const [key, val] of Object.entries(values)) {
    rendered = rendered.replaceAll(`\${{ values.${key} }}`, val);
  }

  // Resolve ${{ values.packagePath }} in the output path
  let outRel = relPath.replace(/\$\{\{ values\.packagePath \}\}/g, values.packagePath);

  const outPath = path.join(OUTPUT, outRel);
  fs.mkdirSync(path.dirname(outPath), { recursive: true });
  fs.writeFileSync(outPath, rendered.trimEnd() + '\n');
  console.log(`  ✓ ${outRel}`);
  count++;
}

console.log(`\n${count} files generated`);
console.log(`Output: ${OUTPUT}`);
console.log(`\nRunning mvn clean verify...\n`);

try {
  execSync('mvn clean verify -B', { cwd: OUTPUT, stdio: 'inherit' });
  console.log('\n✅ BUILD SUCCESS — all tests pass, JaCoCo coverage met');
} catch (e) {
  console.error('\n❌ BUILD FAILED — see output above');
  process.exit(1);
}
