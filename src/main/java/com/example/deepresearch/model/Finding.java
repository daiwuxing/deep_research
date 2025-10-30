package com.example.deepresearch.model;

import java.util.List;

/**
 * A normalized atomic finding extracted from sources, with citations and confidence.
 */
public record Finding(
        String topic,
        String conclusion,
        double confidence,
        List<String> citations // URLs or references
) {
}


