package org.acme;

public record Segment(String departureAirport,
                      String arrivalAirport,
                      String departureTerminal,
                      String arrivalTerminal, String at,
                      String duration, String carrier) {
}
