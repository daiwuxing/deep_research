package com.example.deepresearch.agents;

import com.example.deepresearch.config.ModelConfig;
import com.example.deepresearch.model.Finding;

import java.util.List;

/**
 * Synthesizes a markdown report from findings. Placeholder until ADK prompt added.
 */
public final class ReportWriterAgent {
    private final ModelConfig modelConfig;

    public ReportWriterAgent(ModelConfig modelConfig) {
        this.modelConfig = modelConfig;
    }

    public String write(String query, List<Finding> findings) {
        StringBuilder md = new StringBuilder();
        md.append("# Deep Research\n\n");
        md.append("Query: ").append(query).append("\n\n");
        for (Finding f : findings) {
            md.append("## ").append(f.topic()).append("\n");
            md.append("- ").append(f.conclusion()).append("\n\n");
        }
        return md.toString();
    }
}


