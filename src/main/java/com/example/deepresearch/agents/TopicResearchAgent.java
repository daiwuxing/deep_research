package com.example.deepresearch.agents;

import com.example.deepresearch.config.ModelConfig;
import com.example.deepresearch.model.Finding;
import com.example.deepresearch.model.TopicTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Executes a single topic task. Placeholder without ADK web tools.
 */
public final class TopicResearchAgent {
    private static final Logger logger = LoggerFactory.getLogger(TopicResearchAgent.class);
    
    private final ModelConfig modelConfig;

    public TopicResearchAgent(ModelConfig modelConfig) {
        this.modelConfig = modelConfig;
        logger.debug("TopicResearchAgent initialized");
    }

    public Finding execute(TopicTask task) {
        logger.debug("Executing research task: {} (priority: {})", task.topic(), task.priority());
        // TODO: Use ADK GoogleSearchTool + WebReaderTool.
        Finding finding = new Finding(task.topic(), "Initial finding synthesized for: " + task.topic(), 0.4, List.of());
        logger.debug("Research task completed for topic: {}", task.topic());
        return finding;
    }
}


