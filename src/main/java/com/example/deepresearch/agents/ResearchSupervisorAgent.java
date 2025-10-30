package com.example.deepresearch.agents;

import com.example.deepresearch.config.ModelConfig;
import com.example.deepresearch.model.Brief;
import com.example.deepresearch.model.TopicTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Decomposes a brief into topic tasks. Placeholder until ADK prompts are added.
 */
public final class ResearchSupervisorAgent {
    private final ModelConfig modelConfig;

    public ResearchSupervisorAgent(ModelConfig modelConfig) {
        this.modelConfig = modelConfig;
    }

    public List<TopicTask> plan(Brief brief) {
        List<TopicTask> tasks = new ArrayList<>();
        int priority = 1;
        for (String topic : brief.initialTopics()) {
            tasks.add(new TopicTask(topic, "Research: " + topic + " for goal: " + brief.clarifiedGoal(), priority++, 1));
        }
        return tasks;
    }
}


