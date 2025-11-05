package com.example.deepresearch.agents;

import com.example.deepresearch.config.ModelConfig;
import com.example.deepresearch.model.Brief;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts a clarified goal into a research brief. Placeholder until ADK prompts are added.
 */
public final class BriefGeneratorAgent {
    private static final Logger logger = LoggerFactory.getLogger(BriefGeneratorAgent.class);
    
    private final ModelConfig modelConfig;

    public BriefGeneratorAgent(ModelConfig modelConfig) {
        this.modelConfig = modelConfig;
        logger.debug("BriefGeneratorAgent initialized");
    }

    public Brief generate(String originalQuery, String clarifiedGoal, List<String> assumptions, List<String> constraints) {
        logger.info("Generating research brief for goal: {}", clarifiedGoal);
        logger.debug("Original query: {}, Assumptions: {}, Constraints: {}", 
                originalQuery, assumptions, constraints);
        List<String> topics = new ArrayList<>();
        topics.add("Overview");
        topics.add("Key considerations");
        Brief brief = new Brief(originalQuery, clarifiedGoal, assumptions, constraints, topics);
        logger.info("Brief generated with {} topics", topics.size());
        return brief;
    }
}


