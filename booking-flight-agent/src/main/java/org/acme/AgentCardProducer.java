package org.acme;

import io.a2a.spec.AgentCapabilities;
import io.a2a.spec.AgentCard;
import io.a2a.spec.AgentSkill;
import io.a2a.spec.PublicAgentCard;
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
                .name("Agent using Amadeus Flight Search")
                .description("Agent using Amadeus developer API to get flights from locations at given date")
                .url("http://localhost:9999")
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
                        .id("find_flights")
                        .name("find flights between cities")
                        .description("Returns if possible flights from one airport to another in the given dates")
                        .tags(Collections.singletonList("find flights"))
                        .examples(List.of("what flight options are to go from Barcelona to Paris with departure date 04/12/2025 and returning at 06/12/2025?"))
                        .build()))
                .build();
    }

}
