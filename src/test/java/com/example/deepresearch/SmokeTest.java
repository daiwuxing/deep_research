package com.example.deepresearch;

import com.example.deepresearch.config.ModelConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SmokeTest {
    @Test
    void defaultModelPresent() {
        assertEquals("gemini-2.0-flash", ModelConfig.DEFAULT_MODEL);
        assertNotNull(ModelConfig.GOOGLE_API_KEY_ENV);
    }
}


