package org.acme;

import com.amadeus.Amadeus;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Singleton
public class AmadeusProducer {

    @ConfigProperty(name = "api-key")
    String apiKey;

    @ConfigProperty(name = "api-secret")
    String apiSecret;

    @Produces
    public Amadeus createAmadeus() {
        return Amadeus
                .builder(apiKey, apiSecret)
                .build();
    }

}
