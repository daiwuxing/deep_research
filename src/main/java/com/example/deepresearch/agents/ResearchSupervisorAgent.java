package com.example.deepresearch.agents;

import com.example.deepresearch.config.ModelConfig;
import com.example.deepresearch.model.Brief;
import com.example.deepresearch.model.TopicTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Decomposes a brief into topic tasks. Placeholder until ADK prompts are added.
 */
public final class ResearchSupervisorAgent {
    private static final Logger logger = LoggerFactory.getLogger(ResearchSupervisorAgent.class);
    
    private final ModelConfig modelConfig;

    public ResearchSupervisorAgent(ModelConfig modelConfig) {
        this.modelConfig = modelConfig;
        logger.debug("ResearchSupervisorAgent initialized");
    }

    public List<TopicTask> plan(Brief brief) {
        logger.info("Planning research tasks from brief with {} topics", brief.initialTopics().size());
        List<TopicTask> tasks = new ArrayList<>();
        int priority = 1;
        for (String topic : brief.initialTopics()) {
            TopicTask task = new TopicTask(topic, "Research: " + topic + " for goal: " + brief.clarifiedGoal(), priority++, 1);
            tasks.add(task);
            logger.debug("Created task {}: {}", priority - 1, topic);
        }
        logger.info("Planned {} research tasks", tasks.size());
        return tasks;
    }
}


