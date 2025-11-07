package com.example.deepresearch.agents;

import com.example.deepresearch.config.AgentFactory;
import com.google.adk.agents.BaseAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * An agent that clarifies the user's query using an LLM.
 * If the query is clear, it returns a simple confirmation.
 * If the query is ambiguous, it asks a clarifying question.
 */
public final class UserClarifierAgent {
    private static final Logger logger = LoggerFactory.getLogger(UserClarifierAgent.class);

    private static final String OK_RESPONSE = "OK";
    private static final String INSTRUCTION = """
            You are an expert at refining user requests.
            Analyze the following user query.
            If the query is clear and actionable for a research agent, respond only with the word "OK".
            If the query is vague, ambiguous, or missing key information, ask a single, concise clarifying question to the user.
            Do not answer the query, only ask a question to help the user improve it.
            """;

    private final AgentFactory agentFactory;
    private final BaseAgent clarifierAgent;
    private final boolean interactive;

    public UserClarifierAgent(AgentFactory agentFactory, boolean interactive) {
        this.agentFactory = agentFactory;
        this.interactive = interactive;
        this.clarifierAgent = agentFactory.createBasicAgent(
                "Query Clarifier",
                "An agent that analyzes a user's query and asks clarifying questions if it is ambiguous.",
                INSTRUCTION
        );
        logger.debug("UserClarifierAgent initialized with interactive={}", interactive);
    }

    public ClarificationResult clarify(String query) {
        logger.info("Clarifying user query with LLM: {}", query);

        String response = agentFactory.invokeAgent(clarifierAgent, query).trim();

        if (response.equalsIgnoreCase(OK_RESPONSE)) {
            logger.info("Query is clear and actionable.");
            // TODO: In a future version, the agent could suggest assumptions based on a clear query.
        } else {
            logger.info("LLM suggested clarification: {}", response);
            if (interactive) {
                // TODO: Implement interactive conversation with the user to get answers.
                System.out.println("Clarification needed: " + response);
            }
        }

        // For now, we'll continue to use default assumptions after clarification attempt.
        // The clarified goal remains the original query until interactive mode is fully implemented.
        List<String> assumptions = List.of("Internet sources are allowed", "Cite URLs where possible");
        ClarificationResult result = new ClarificationResult(query, assumptions, List.of());

        logger.info("Query clarification process completed.");
        return result;
    }

    public record ClarificationResult(String clarifiedGoal, List<String> assumptions, List<String> constraints) {}
}


