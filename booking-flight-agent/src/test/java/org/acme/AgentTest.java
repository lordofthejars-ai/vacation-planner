package org.acme;

import io.a2a.client.A2AClient;
import io.a2a.spec.A2A;
import io.a2a.spec.A2AServerException;
import io.a2a.spec.AgentCard;
import io.a2a.spec.Message;
import io.a2a.spec.MessageSendParams;
import io.a2a.spec.Part;
import io.a2a.spec.SendMessageResponse;
import io.a2a.spec.TextPart;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;


public class AgentTest {

    @Test
    public void shouldConnectToAgent() throws A2AServerException {

        AgentCard publicAgentCard = A2A.getAgentCard("http://localhost:9999");

        A2AClient client = new A2AClient(publicAgentCard);
        Message message = A2A.toUserMessage("what flight options do we have to go from Barcelona to Paris with departure date 04/12/2025 and returning at 06/12/2025?");
        MessageSendParams params = new MessageSendParams.Builder()
                .message(message)
                .build();
        SendMessageResponse response = client.sendMessage(params);
        System.out.println("Message sent with ID: " + response.getId());
        System.out.println("Response: " + extractTextFromMessage((Message)response.getResult()));

    }

    private String extractTextFromMessage(Message message) {
        StringBuilder textBuilder = new StringBuilder();
        if (message.getParts() != null) {
            for (Part part : message.getParts()) {
                if (part instanceof TextPart textPart) {
                    textBuilder.append(textPart.getText());
                }
            }
        }
        return textBuilder.toString();
    }



}
