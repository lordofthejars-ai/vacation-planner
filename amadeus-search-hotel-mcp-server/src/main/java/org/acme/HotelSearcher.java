package org.acme;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referencedata.Locations;
import com.amadeus.resources.Hotel;
import com.amadeus.resources.Location;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.faulttolerance.Retry;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Singleton
public class HotelSearcher {

    @Inject
    Amadeus amadeus;

    public List<org.acme.Hotel> findHotel(String city) throws ResponseException {
        Location.GeoCode cityGeo = findCityGeo(city);
        return this.findHotelInCity(cityGeo);
    }

    // WE PUT RETRIES BECAUSE TEST AMADEUS API ONLY LET YO UDO ONE REQUEST EVERY 100MS

    @Retry(maxRetries = 2, delay = 400, delayUnit = ChronoUnit.MILLIS)
    protected Location.GeoCode findCityGeo(String location) throws ResponseException {
        Location[] locations = amadeus.referenceData.locations.get(Params
                .with("keyword", location)
                .and("subType", Locations.CITY));

        return  locations[0].getGeoCode();
    }

    @Retry(maxRetries = 2, delay = 400, delayUnit = ChronoUnit.MILLIS)
    protected List<org.acme.Hotel> findHotelInCity(Location.GeoCode location) throws ResponseException {
        Hotel[] hotels = amadeus.referenceData.locations.hotels.byGeocode.get(
                Params.with("latitude", location.getLatitude())
                        .and("longitude", location.getLongitude())
                        .and("ratings", "4"));

        return Arrays.stream(hotels)
                .map(h ->
                        new org.acme.Hotel(h.getName(), h.getGooglePlaceId())
                )
                .limit(3)
                .toList();
    }

}
