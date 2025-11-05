package com.example.deepresearch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Centralizes model-related configuration and credentials resolution.
 */
public final class ModelConfig {

    private static final Logger logger = LoggerFactory.getLogger(ModelConfig.class);

    public static final String DEFAULT_MODEL = "gemini-2.0-flash";
    public static final String GOOGLE_API_KEY_ENV = "GOOGLE_API_KEY";

    private final String modelName;
    private final String googleApiKey;

    private ModelConfig(String modelName, String googleApiKey) {
        this.modelName = Objects.requireNonNullElse(modelName, DEFAULT_MODEL);
        this.googleApiKey = Objects.requireNonNull(googleApiKey, "GOOGLE_API_KEY is required");
        logger.debug("ModelConfig created: model={}, apiKeyLength={}", 
                this.modelName, googleApiKey != null ? googleApiKey.length() : 0);
    }

    public static ModelConfig fromEnvironment(String overrideModel) {
        logger.debug("Loading ModelConfig from environment. Override model: {}", overrideModel);
        String apiKey = System.getenv(GOOGLE_API_KEY_ENV);
        if (apiKey == null || apiKey.isBlank()) {
            logger.error("Missing GOOGLE_API_KEY environment variable");
            throw new IllegalStateException("Missing GOOGLE_API_KEY environment variable");
        }
        String finalModel = overrideModel == null || overrideModel.isBlank() ? DEFAULT_MODEL : overrideModel;
        logger.info("ModelConfig loaded successfully: model={}", finalModel);
        return new ModelConfig(finalModel, apiKey);
    }

    public String modelName() {
        return modelName;
    }

    public String googleApiKey() {
        return googleApiKey;
    }
}


