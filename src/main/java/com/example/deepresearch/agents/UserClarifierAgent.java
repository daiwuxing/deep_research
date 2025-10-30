package com.example.deepresearch.agents;

import com.example.deepresearch.config.ModelConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Placeholder clarifier. Will be upgraded to ADK-powered agent; for now it proposes sane defaults.
 */
public final class UserClarifierAgent {
    private final ModelConfig modelConfig;
    private final boolean interactive;

    public UserClarifierAgent(ModelConfig modelConfig, boolean interactive) {
        this.modelConfig = modelConfig;
        this.interactive = interactive;
    }

    public ClarificationResult clarify(String query) {
        // TODO: If interactive, ask questions; else return default assumptions.
        List<String> assumptions = new ArrayList<>();
        assumptions.add("Internet sources allowed");
        assumptions.add("Cite URLs where possible");
        return new ClarificationResult(query, assumptions, List.of());
    }

    public record ClarificationResult(String clarifiedGoal, List<String> assumptions, List<String> constraints) {}
}


