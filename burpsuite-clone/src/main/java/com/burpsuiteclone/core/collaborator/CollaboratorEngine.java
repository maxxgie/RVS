package com.burpsuiteclone.core.collaborator;

/**
 * Collaborator — out-of-band interaction testing.
 */
public interface CollaboratorEngine {
    CollaboratorClient createClient();
    CollaboratorClient restoreClient(String clientSecret);
    PayloadGenerator defaultPayloadGenerator();
}

interface CollaboratorClient {
    String generatePayload();
    java.util.List<Interaction> pollInteractions();
    void close();
}

record Interaction(
    String type,
    String payload,
    String sourceIp,
    java.time.Instant timestamp,
    String rawRequest
) {}

interface PayloadGenerator {
    String generate();
}
