package com.example.deepresearch.agents;

import com.example.deepresearch.config.ModelConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Placeholder clarifier. Will be upgraded to ADK-powered agent; for now it proposes sane defaults.
 */
public final class UserClarifierAgent {
    private static final Logger logger = LoggerFactory.getLogger(UserClarifierAgent.class);
    
    private final ModelConfig modelConfig;
    private final boolean interactive;

    public UserClarifierAgent(ModelConfig modelConfig, boolean interactive) {
        this.modelConfig = modelConfig;
        this.interactive = interactive;
        logger.debug("UserClarifierAgent initialized with interactive={}", interactive);
    }

    public ClarificationResult clarify(String query) {
        logger.info("Clarifying user query: {}", query);
        // TODO: If interactive, ask questions; else return default assumptions.
        List<String> assumptions = new ArrayList<>();
        assumptions.add("Internet sources allowed");
        assumptions.add("Cite URLs where possible");
        logger.debug("Generated {} assumptions", assumptions.size());
        ClarificationResult result = new ClarificationResult(query, assumptions, List.of());
        logger.info("Query clarification completed");
        return result;
    }

    public record ClarificationResult(String clarifiedGoal, List<String> assumptions, List<String> constraints) {}
}


