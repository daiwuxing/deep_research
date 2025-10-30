package com.example.deepresearch.model;

import java.util.List;

/**
 * Clarified brief that seeds the research process.
 */
public record Brief(
        String originalQuery,
        String clarifiedGoal,
        List<String> assumptions,
        List<String> constraints,
        List<String> initialTopics
) {
}


