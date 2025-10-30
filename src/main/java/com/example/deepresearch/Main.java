package com.example.deepresearch;

import com.example.deepresearch.config.ModelConfig;
import com.example.deepresearch.model.ResearchReport;
import com.example.deepresearch.orchestration.DeepResearchOrchestrator;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.concurrent.Callable;

@Command(name = "deep-research",
        mixinStandardHelpOptions = true,
        version = "0.1.0",
        description = "Deep research CLI using Google ADK (Java)")
public class Main implements Callable<Integer> {

    @Option(names = {"-q", "--query"}, required = true, description = "User query to research")
    String query;

    @Option(names = "--model", defaultValue = ModelConfig.DEFAULT_MODEL, description = "Model name (default: ${DEFAULT-VALUE})")
    String model;

    @Option(names = "--max-parallel", defaultValue = "4", description = "Max parallel topic agents")
    int maxParallel;

    @Option(names = "--interactive", defaultValue = "false", description = "Interactive clarification mode")
    boolean interactive;

    @Option(names = "--out", description = "Output markdown file (stdout if omitted)")
    Path out;

    @Override
    public Integer call() throws Exception {
        ModelConfig modelConfig = ModelConfig.fromEnvironment(model);
        DeepResearchOrchestrator orchestrator = new DeepResearchOrchestrator(modelConfig);
        ResearchReport report = orchestrator.run(query, new DeepResearchOrchestrator.Options(interactive, maxParallel));

        if (out != null) {
            writeFile(out, report.markdown());
        } else {
            System.out.println(report.markdown());
        }
        return 0;
    }

    private static void writeFile(Path file, String content) throws IOException {
        Path parent = file.getParent();
        if (parent != null) Files.createDirectories(parent);
        Files.writeString(file, content);
    }

    public static void main(String[] args) {
        int code = new CommandLine(new Main()).execute(args);
        System.exit(code);
    }
}


