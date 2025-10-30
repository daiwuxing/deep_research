package com.example.deepresearch.agents;

import com.example.deepresearch.config.ModelConfig;
import com.example.deepresearch.model.Finding;
import com.example.deepresearch.model.TopicTask;

import java.util.List;

/**
 * Executes a single topic task. Placeholder without ADK web tools.
 */
public final class TopicResearchAgent {
    private final ModelConfig modelConfig;

    public TopicResearchAgent(ModelConfig modelConfig) {
        this.modelConfig = modelConfig;
    }

    public Finding execute(TopicTask task) {
        // TODO: Use ADK GoogleSearchTool + WebReaderTool.
        return new Finding(task.topic(), "Initial finding synthesized for: " + task.topic(), 0.4, List.of());
    }
}


