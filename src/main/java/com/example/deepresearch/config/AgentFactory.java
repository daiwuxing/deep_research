package com.example.deepresearch.config;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.InvocationContext;
import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.RunConfig;
import com.google.adk.artifacts.BaseArtifactService;
import com.google.adk.artifacts.InMemoryArtifactService;
import com.google.adk.events.Event;
import com.google.adk.memory.BaseMemoryService;
import com.google.adk.memory.InMemoryMemoryService;
import com.google.adk.sessions.BaseSessionService;
import com.google.adk.sessions.InMemorySessionService;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Factory for creating and configuring LlmAgent instances.
 */
public final class AgentFactory {

    private static final Logger logger = LoggerFactory.getLogger(AgentFactory.class);

    private final ModelConfig modelConfig;
    private final BaseSessionService sessionService;
    private final BaseArtifactService artifactService;
    private final BaseMemoryService memoryService;

    public AgentFactory(ModelConfig modelConfig) {
        this.modelConfig = modelConfig;
        this.sessionService = new InMemorySessionService();
        this.artifactService = new InMemoryArtifactService();
        this.memoryService = new InMemoryMemoryService();
        logger.debug("AgentFactory initialized with model: {}", modelConfig.modelName());
    }

    /**
     * Creates a basic LlmAgent without tools.
     */
    public BaseAgent createBasicAgent(String name, String description, String instruction) {
        logger.debug("Creating basic agent: {}", name);
        var builder = LlmAgent.builder()
                .name(name)
                .description(description)
                .model(modelConfig.modelName())
                .instruction(instruction);
        return builder.build();
    }

    /**
     * Creates an LlmAgent with GoogleSearchTool for web research.
     */
    public BaseAgent createResearchAgent(String name, String description, String instruction) {
        logger.debug("Creating research agent with GoogleSearchTool: {}", name);
        var searchTool = new com.google.adk.tools.GoogleSearchTool();
        var builder = LlmAgent.builder()
                .name(name)
                .description(description)
                .model(modelConfig.modelName())
                .instruction(instruction)
                .tools(searchTool);
        return builder.build();
    }

    /**
     * Creates an LlmAgent with custom tools.
     */
    public BaseAgent createAgentWithTools(String name, String description, String instruction, Object... tools) {
        logger.debug("Creating agent with custom tools: {}", name);
        var builder = LlmAgent.builder()
                .name(name)
                .description(description)
                .model(modelConfig.modelName())
                .instruction(instruction);

        if (tools != null && tools.length > 0) {
            builder.tools(tools);
        }

        return builder.build();
    }

    /**
     * Invokes an agent with a prompt and returns the response.
     * This method handles the actual API call to Google Gemini.
     */
    public String invokeAgent(BaseAgent agent, String prompt) {
        logger.debug("Invoking agent: {} with prompt length: {}", agent.name(), prompt.length());
        try {
            // Create content from prompt
            Part textPart = Part.builder().text(prompt).build();
            Content userContent = Content.builder()
                    .parts(textPart)
                    .build();

            // Create a session asynchronously and block for result
            String userId = "deep-research-user";
            String appName = "deep-research-cli";
            Session session = sessionService.createSession(userId, appName)
                    .timeout(10, TimeUnit.SECONDS)
                    .blockingGet();

            // Create invocation context
            RunConfig runConfig = RunConfig.builder().build();
            InvocationContext context = InvocationContext.create(
                    sessionService,
                    artifactService,
                    prompt,
                    agent,
                    session,
                    userContent,
                    runConfig
            );

            // Run agent asynchronously and collect events
            Flowable<Event> events = agent.runAsync(context);

            // Collect the final response from events
            List<String> responseParts = new ArrayList<>();
            events
                    .timeout(120, TimeUnit.SECONDS) // 2 minute timeout
                    .blockingSubscribe(
                            event -> {
                                if (event.content().isPresent()) {
                                    Content content = event.content().get();
                                    for (Part part : content.parts().get()) {
                                        part.text().ifPresent(responseParts ::add);
                                    }
                                }
                                // Check if turn is complete
                                if (event.turnComplete().orElse(false)) {
                                    logger.debug("Agent turn completed");
                                }
                            },
                            error -> {
                                logger.error("Error during agent execution", error);
                                throw new RuntimeException("Agent execution failed", error);
                            }
                    );

            String response = String.join("", responseParts);
            logger.debug("Agent response received, length: {}", response.length());
            return response;

        } catch (Exception e) {
            logger.error("Error invoking agent: {}", agent.name(), e);
            throw new RuntimeException("Failed to invoke agent: " + agent.name(), e);
        }
    }
}