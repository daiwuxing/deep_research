package com.example.deepresearch.config;

import java.util.Objects;

/**
 * Centralizes model-related configuration and credentials resolution.
 */
public final class ModelConfig {

    public static final String DEFAULT_MODEL = "gemini-2.0-flash";
    public static final String GOOGLE_API_KEY_ENV = "GOOGLE_API_KEY";

    private final String modelName;
    private final String googleApiKey;

    private ModelConfig(String modelName, String googleApiKey) {
        this.modelName = Objects.requireNonNullElse(modelName, DEFAULT_MODEL);
        this.googleApiKey = Objects.requireNonNull(googleApiKey, "GOOGLE_API_KEY is required");
    }

    public static ModelConfig fromEnvironment(String overrideModel) {
        String apiKey = System.getenv(GOOGLE_API_KEY_ENV);
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Missing GOOGLE_API_KEY environment variable");
        }
        return new ModelConfig(overrideModel == null || overrideModel.isBlank() ? DEFAULT_MODEL : overrideModel, apiKey);
    }

    public String modelName() {
        return modelName;
    }

    public String googleApiKey() {
        return googleApiKey;
    }
}


