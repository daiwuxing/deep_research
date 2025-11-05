package com.example.deepresearch;

import com.example.deepresearch.config.ModelConfig;
import com.example.deepresearch.model.ResearchReport;
import com.example.deepresearch.orchestration.DeepResearchOrchestrator;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(name = "deep-research",
        mixinStandardHelpOptions = true,
        version = "0.1.0",
        description = "Deep research CLI using Google ADK (Java)")
public class Main implements Callable<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

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
        logger.info("Starting deep research process");
        logger.debug("Query: {}, Model: {}, Max Parallel: {}, Interactive: {}, Output: {}", 
                query, model, maxParallel, interactive, out != null ? out : "stdout");

        try {
            ModelConfig modelConfig = ModelConfig.fromEnvironment(model);
            logger.debug("Model configuration loaded: model={}", modelConfig.modelName());
            
            DeepResearchOrchestrator orchestrator = new DeepResearchOrchestrator(modelConfig);
            ResearchReport report = orchestrator.run(query, new DeepResearchOrchestrator.Options(interactive, maxParallel));

            logger.info("Research completed successfully. Citations count: {}", report.citations().size());

            if (out != null) {
                writeFile(out, report.markdown());
                logger.info("Report written to file: {}", out);
            } else {
                System.out.println(report.markdown());
                logger.debug("Report written to stdout");
            }
            
            logger.info("Deep research process completed successfully");
            return 0;
        } catch (Exception e) {
            logger.error("Error during deep research process", e);
            throw e;
        }
    }

    private static void writeFile(Path file, String content) throws IOException {
        logger.debug("Writing report to file: {}", file);
        Path parent = file.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
            logger.debug("Created directory: {}", parent);
        }
        Files.writeString(file, content);
        logger.debug("File written successfully, size: {} bytes", content.length());
    }

    public static void main(String[] args) {
        logger.info("Deep Research CLI starting...");
        int code = new CommandLine(new Main()).execute(args);
        if (code != 0) {
            logger.warn("Process exited with code: {}", code);
        }
        System.exit(code);
    }
}


