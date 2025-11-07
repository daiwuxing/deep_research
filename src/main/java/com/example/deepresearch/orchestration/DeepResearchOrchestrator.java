package com.example.deepresearch.orchestration;

import com.example.deepresearch.config.AgentFactory;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(DeepResearchOrchestrator.class);

    public static final class Options {
        public final boolean interactive;
        public final int maxParallel;

        public Options(boolean interactive, int maxParallel) {
            this.interactive = interactive;
            this.maxParallel = maxParallel;
        }
    }

    private final ModelConfig modelConfig;
    private final AgentFactory agentFactory;

    public DeepResearchOrchestrator(ModelConfig modelConfig) {
        this.modelConfig = modelConfig;
        this.agentFactory = new AgentFactory(modelConfig);
    }

    public ResearchReport run(String query, Options options) {
        logger.info("Starting research orchestration for query: {}", query);
        logger.debug("Options: interactive={}, maxParallel={}", options.interactive, options.maxParallel);

        // SCOPE
        logger.info("Phase 1: Scope - Clarifying user query and generating brief");
        UserClarifierAgent clarifier = new UserClarifierAgent(agentFactory, options.interactive);
        var clarification = clarifier.clarify(query);
        logger.debug("Query clarified: {}", clarification.clarifiedGoal());
        logger.debug("Assumptions: {}", clarification.assumptions());
        logger.debug("Constraints: {}", clarification.constraints());

        BriefGeneratorAgent briefGen = new BriefGeneratorAgent(modelConfig);
        Brief brief = briefGen.generate(
                query,
                clarification.clarifiedGoal(),
                clarification.assumptions(),
                clarification.constraints()
        );
        logger.info("Brief generated with {} initial topics", brief.initialTopics().size());
        logger.debug("Initial topics: {}", brief.initialTopics());

        // RESEARCH
        logger.info("Phase 2: Research - Planning and executing research tasks");
        ResearchSupervisorAgent supervisor = new ResearchSupervisorAgent(modelConfig);
        List<TopicTask> tasks = supervisor.plan(brief);
        logger.info("Planned {} research tasks", tasks.size());
        logger.debug("Tasks: {}", tasks.stream().map(TopicTask::topic).toList());

        List<Finding> findings = new ArrayList<>();
        TopicResearchAgent researcher = new TopicResearchAgent(modelConfig);
        ExecutorService pool = Executors.newFixedThreadPool(Math.max(1, options.maxParallel));
        logger.debug("Created executor pool with {} threads", options.maxParallel);
        try {
            List<CompletableFuture<Finding>> futures = tasks.stream()
                    .map(t -> CompletableFuture.supplyAsync(() -> {
                        logger.debug("Starting research for topic: {}", t.topic());
                        Finding finding = researcher.execute(t);
                        logger.debug("Completed research for topic: {}", t.topic());
                        return finding;
                    }, pool))
                    .toList();
            
            int completed = 0;
            for (CompletableFuture<Finding> f : futures) {
                findings.add(f.join());
                completed++;
                logger.debug("Progress: {}/{} tasks completed", completed, tasks.size());
            }
            logger.info("All research tasks completed. Total findings: {}", findings.size());
        } finally {
            pool.shutdown();
            logger.debug("Executor pool shut down");
        }

        // WRITE
        logger.info("Phase 3: Write - Synthesizing report from findings");
        ReportWriterAgent writer = new ReportWriterAgent(modelConfig);
        String markdown = writer.write(query, findings);
        logger.info("Report synthesized successfully. Markdown length: {} characters", markdown.length());
        
        ResearchReport report = new ResearchReport("Deep Research Report", markdown, List.of(), Instant.now());
        logger.info("Research orchestration completed successfully");
        return report;
    }
}


