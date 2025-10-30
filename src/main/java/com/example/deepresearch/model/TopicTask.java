package com.example.deepresearch.model;

/**
 * A decomposed topic/task produced by the supervisor for sub-agent execution.
 */
public record TopicTask(
        String topic,
        String prompt,
        int priority,
        int depth
) {
}


