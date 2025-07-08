package org.acme;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referencedata.Locations;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.Location;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.faulttolerance.Retry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Singleton
public class FlightSearcher {

    @Inject
    Amadeus amadeus;

    public Trip findTrip(String departureCity, String destinationCity, LocalDate departureDate, LocalDate returnDate) throws ResponseException {
        String departureCode = this.findAirportCode(departureCity);
        String destinationCode = this.findAirportCode(destinationCity);

        return this.findFlightsForATrip(departureCode, destinationCode, departureDate, returnDate);
    }

    // WE PUT RETRIES BECAUSE TEST AMADEUS API ONLY LET YO UDO ONE REQUEST EVERY 100MS

    @Retry(maxRetries = 2, delay = 400, delayUnit = ChronoUnit.MILLIS)
    protected String findAirportCode(String location) throws ResponseException {
        Location[] locations = amadeus.referenceData.locations.get(Params
                .with("keyword", location)
                .and("subType", Locations.AIRPORT));

        return  locations[0].getIataCode();
    }

    @Retry(maxRetries = 2, delay = 400, delayUnit = ChronoUnit.MILLIS)
    protected Trip findFlightsForATrip(String originCode, String destinationCode, LocalDate departureDate, LocalDate returnDate) throws ResponseException {
        FlightOfferSearch[] flightOffersSearches = amadeus.shopping.flightOffersSearch.get(
                Params.with("originLocationCode", originCode)
                        .and("destinationLocationCode", destinationCode)
                        .and("departureDate", DateTimeFormatter.ISO_LOCAL_DATE.format(departureDate))
                        .and("returnDate", DateTimeFormatter.ISO_LOCAL_DATE.format(returnDate))
                        .and("adults", 1)
                        .and("max", 1));

        // We are returning max 1 result
        FlightOfferSearch flightOffersSearch = flightOffersSearches[0];
        FlightOfferSearch.Itinerary[] itineraries = flightOffersSearch.getItineraries();

        FlightOfferSearch.Itinerary departureItinerary = itineraries[0];
        Segment departureFlight = getFlight(departureItinerary);

        FlightOfferSearch.Itinerary returnItinerary = itineraries[1];
        Segment returnFlight = getFlight(returnItinerary);

        return new Trip(departureFlight, returnFlight);
    }

    private static Segment getFlight(FlightOfferSearch.Itinerary departureItinerary) {

        // Simple direct flight with no stops

        FlightOfferSearch.SearchSegment firstFlight = departureItinerary.getSegments()[0];
        return new Segment(firstFlight.getDeparture().getIataCode(),
                firstFlight.getArrival().getIataCode(),
                firstFlight.getDeparture().getTerminal(),
                firstFlight.getArrival().getTerminal(),
                firstFlight.getDeparture().getAt(),
                firstFlight.getDuration(), firstFlight.getCarrierCode());
    }

}
