package org.acme;

import com.amadeus.exceptions.ResponseException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class FlightSearcherTest {

    @Inject
    FlightSearcher flightSearcher;

    @Test
    void shouldFindATrip() throws ResponseException {

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate returnDate = tomorrow.plusDays(4);

        Trip trip = flightSearcher
                .findTrip("Barcelona", "London", tomorrow, returnDate);

        assertThat(trip.departure().departureAirport()).isEqualToIgnoringCase("BCN");
        assertThat(trip.departure().arrivalAirport()).containsAnyOf("LHR", "LGW", "LCY");

    }

}
