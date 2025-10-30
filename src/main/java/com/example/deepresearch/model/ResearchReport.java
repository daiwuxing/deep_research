package com.example.deepresearch.model;

import java.time.Instant;
import java.util.List;

/**
 * Final synthesized report (markdown content with citations).
 */
public record ResearchReport(
        String title,
        String markdown,
        List<String> citations,
        Instant generatedAt
) {
}


