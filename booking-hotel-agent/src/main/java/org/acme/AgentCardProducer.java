package org.acme;

import io.a2a.server.agentexecution.AgentExecutor;
import io.a2a.server.agentexecution.RequestContext;
import io.a2a.server.events.EventQueue;
import io.a2a.spec.A2A;
import io.a2a.spec.AgentCapabilities;
import io.a2a.spec.AgentCard;
import io.a2a.spec.AgentSkill;
import io.a2a.spec.JSONRPCError;
import io.a2a.spec.PublicAgentCard;
import io.a2a.spec.UnsupportedOperationError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class AgentCardProducer {

    @Produces
    @PublicAgentCard
    public AgentCard agentCard() {
        return new AgentCard.Builder()
                .name("Agent using Amadeus Hotel Search")
                .description("Agent using Amadeus developer API to get hotels from locations at given date")
                .url("http://localhost:9998")
                .version("1.0.0")
                .documentationUrl("https://developers.amadeus.com/")
                .capabilities(new AgentCapabilities.Builder()
                        .streaming(true)
                        .pushNotifications(false)
                        .stateTransitionHistory(false)
                        .build())
                .defaultInputModes(Collections.singletonList("text"))
                .defaultOutputModes(Collections.singletonList("text"))
                .skills(Collections.singletonList(new AgentSkill.Builder()
                        .id("find_hotels")
                        .name("find hotels in given city and dates")
                        .description("Returns if possible hotels from given city and dates")
                        .tags(Collections.singletonList("find hotels"))
                        .examples(List.of("what hotel options are in Barcelona for entering at 04/12/2025 and leaving at 06/12/2025?"))
                        .build()))
                .build();
    }

}
