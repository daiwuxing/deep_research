package com.example.deepresearch.agents;

import com.example.deepresearch.config.ModelConfig;
import com.example.deepresearch.model.Brief;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts a clarified goal into a research brief. Placeholder until ADK prompts are added.
 */
public final class BriefGeneratorAgent {
    private final ModelConfig modelConfig;

    public BriefGeneratorAgent(ModelConfig modelConfig) {
        this.modelConfig = modelConfig;
    }

    public Brief generate(String originalQuery, String clarifiedGoal, List<String> assumptions, List<String> constraints) {
        List<String> topics = new ArrayList<>();
        topics.add("Overview");
        topics.add("Key considerations");
        return new Brief(originalQuery, clarifiedGoal, assumptions, constraints, topics);
    }
}


