package com.example.deepresearch.orchestration;

import com.example.deepresearch.config.ModelConfig;
import com.example.deepresearch.agents.UserClarifierAgent;
import com.example.deepresearch.agents.BriefGeneratorAgent;
import com.example.deepresearch.agents.ResearchSupervisorAgent;
import com.example.deepresearch.agents.TopicResearchAgent;
import com.example.deepresearch.agents.ReportWriterAgent;
import com.example.deepresearch.model.Brief;
import com.example.deepresearch.model.Finding;
import com.example.deepresearch.model.ResearchReport;
import com.example.deepresearch.model.TopicTask;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Coordinates the Scope -> Research -> Write flow.
 * This is a scaffolding class; agent implementations will replace the TODO blocks.
 */
public final class DeepResearchOrchestrator {

    public static final class Options {
        public final boolean interactive;
        public final int maxParallel;

        public Options(boolean interactive, int maxParallel) {
            this.interactive = interactive;
            this.maxParallel = maxParallel;
        }
    }

    private final ModelConfig modelConfig;

    public DeepResearchOrchestrator(ModelConfig modelConfig) {
        this.modelConfig = modelConfig;
    }

    public ResearchReport run(String query, Options options) {
        // SCOPE
        UserClarifierAgent clarifier = new UserClarifierAgent(modelConfig, options.interactive);
        var clarification = clarifier.clarify(query);
        BriefGeneratorAgent briefGen = new BriefGeneratorAgent(modelConfig);
        Brief brief = briefGen.generate(
                query,
                clarification.clarifiedGoal(),
                clarification.assumptions(),
                clarification.constraints()
        );

        // RESEARCH
        ResearchSupervisorAgent supervisor = new ResearchSupervisorAgent(modelConfig);
        List<TopicTask> tasks = supervisor.plan(brief);

        List<Finding> findings = new ArrayList<>();
        TopicResearchAgent researcher = new TopicResearchAgent(modelConfig);
        ExecutorService pool = Executors.newFixedThreadPool(Math.max(1, options.maxParallel));
        try {
            List<CompletableFuture<Finding>> futures = tasks.stream()
                    .map(t -> CompletableFuture.supplyAsync(() -> researcher.execute(t), pool))
                    .toList();
            for (CompletableFuture<Finding> f : futures) {
                findings.add(f.join());
            }
        } finally {
            pool.shutdown();
        }

        // WRITE
        ReportWriterAgent writer = new ReportWriterAgent(modelConfig);
        String markdown = writer.write(query, findings);
        return new ResearchReport("Deep Research Report", markdown, List.of(), Instant.now());
    }
}


