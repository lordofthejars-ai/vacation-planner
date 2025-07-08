package org.acme;

import com.amadeus.exceptions.ResponseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Singleton
public class FlightSearcherMcpServer {

    @Inject
    FlightSearcher flightSearcher;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    Logger logger;

    @Tool(description = """
            Finds a flight departing from given city and arriving to another city. 
            Departure and return dates are in yyyy-MM-dd format.
            """)
    public ToolResponse searchFlightForGivenCitiesAndDates(

            @ToolArg(name = "departureCity", description =
                    "Flight departure city")
            String departureCity,

            @ToolArg(name = "destinationCity", description =
                    "Flight destination city")
            String destinationCity,

            @ToolArg(name = "departureDate", description =
                    "Departure date in yyyy-MM-dd format")
            String departureDate,

            @ToolArg(name = "returnDate", description =
                    "Return date in yyyy-MM-dd format")
            String returnDate
    ) {

        try {
            Trip trip = flightSearcher.findTrip(departureCity, destinationCity,
                    LocalDate.parse(departureDate, DateTimeFormatter.ISO_LOCAL_DATE),
                    LocalDate.parse(returnDate, DateTimeFormatter.ISO_LOCAL_DATE));

            return ToolResponse.success(
                    new TextContent(objectMapper.writeValueAsString(trip))
            );

        } catch (ResponseException | JsonProcessingException | DateTimeException e) {
            logger.error(e);
            return ToolResponse.error(e.getMessage());
        }
    }


}
