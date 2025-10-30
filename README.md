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

```bash
export GOOGLE_API_KEY=YOUR_KEY
java -jar target/deep-research-cli-0.1.0-shaded.jar \
  --query "What are practical approaches to RAG evaluation?" \
  --model gemini-2.0-flash \
  --max-parallel 4 \
  --interactive=false \
  --out report.md
```

This scaffolding currently returns placeholder content; wire the agents to ADK tools to enable live research.

## Notes
- Requires Java 17+
- Uses Picocli for CLI handling
- Output defaults to stdout; use `--out` for a file
