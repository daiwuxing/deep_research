package com.example.deepresearch.agents;

import com.example.deepresearch.config.ModelConfig;
import com.example.deepresearch.model.Finding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Synthesizes a markdown report from findings. Placeholder until ADK prompt added.
 */
public final class ReportWriterAgent {
    private static final Logger logger = LoggerFactory.getLogger(ReportWriterAgent.class);
    
    private final ModelConfig modelConfig;

    public ReportWriterAgent(ModelConfig modelConfig) {
        this.modelConfig = modelConfig;
        logger.debug("ReportWriterAgent initialized");
    }

    public String write(String query, List<Finding> findings) {
        logger.info("Synthesizing report from {} findings for query: {}", findings.size(), query);
        StringBuilder md = new StringBuilder();
        md.append("# Deep Research\n\n");
        md.append("Query: ").append(query).append("\n\n");
        for (Finding f : findings) {
            md.append("## ").append(f.topic()).append("\n");
            md.append("- ").append(f.conclusion()).append("\n\n");
            logger.debug("Added finding for topic: {}", f.topic());
        }
        logger.info("Report synthesized successfully. Markdown length: {} characters", md.length());
        return md.toString();
    }
}


