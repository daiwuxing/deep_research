# Deep Research CLI (Java + Google ADK)

A CLI skeleton for a deep-research workflow (Scope → Research → Write) using the Google Agent Development Kit for Java.

- Scope: clarify user request, generate a brief
- Research: supervisor creates topics; sub-agents gather findings
- Write: synthesize a cited markdown report

Docs: see `google/adk-java` [repository](https://github.com/google/adk-java).

## Build

```bash
mvn -q -DskipTests package
```

## Run

> **Note:** You cannot run Java classes directly with `/usr/bin/env`. Use one of the methods below.

### Option 1: Run via JAR file (Recommended)

```bash
export GOOGLE_API_KEY=YOUR_KEY
java -jar target/deep-research-cli-0.1.0.jar \
  --query "What are practical approaches to RAG evaluation?" \
  --model gemini-2.0-flash \
  --max-parallel 4 \
  --interactive=false \
  --out report.md
```

### Option 2: Run via script (Easiest)

The `run.sh` script automatically detects and uses the JAR file or compiled classes:

```bash
export GOOGLE_API_KEY=YOUR_KEY
./run.sh --query "What are practical approaches to RAG evaluation?" --model gemini-2.0-flash --max-parallel 4 --interactive=false --out report.md
```

**Quick test:**
```bash
export GOOGLE_API_KEY=YOUR_KEY
./run.sh --help
```

### Option 3: Run in IDE (IntelliJ IDEA / VS Code)

**IntelliJ IDEA:**

1. Right-click on `Main.java` → Run 'Main.main()'
2. Edit Run Configuration:

   - Environment variables: Add `GOOGLE_API_KEY=YOUR_KEY`
   - Program arguments: `--query "Your query here" --out report.md`

3. Run the configuration

**VS Code:**

1. Create `.vscode/launch.json`:

```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Run Main",
      "request": "launch",
      "mainClass": "com.example.deepresearch.Main",
      "projectName": "deep-research-cli",
      "args": "--query 'Your query here' --out report.md",
      "env": {
        "GOOGLE_API_KEY": "YOUR_KEY"
      }
    }
  ]
}
```

2. Press F5 or click Run

This scaffolding currently returns placeholder content; wire the agents to ADK tools to enable live research.

## Notes
- Requires Java 17+
- Uses Picocli for CLI handling
- Output defaults to stdout; use `--out` for a file
