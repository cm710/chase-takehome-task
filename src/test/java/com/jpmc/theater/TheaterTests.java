package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TheaterTests {
    // Add showings in random order and verify that they are sorted in the order
    // they appear.
    @Test
    void testShowOrdering() {
        Theater theater = new Theater(LocalDate.of(2022, 12, 15));

        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, Movie.MOVIE_CODE_SPECIAL);

        LocalDateTime times[] = {
                LocalDateTime.of(2022, 12, 15, 9, 0),
                LocalDateTime.of(2022, 12, 15, 11, 0),
                LocalDateTime.of(2022, 12, 15, 12, 50),
                LocalDateTime.of(2022, 12, 15, 14, 30),
                LocalDateTime.of(2022, 12, 15, 16, 10),
                LocalDateTime.of(2022, 12, 15, 17, 50),
                LocalDateTime.of(2022, 12, 15, 19, 30),
                LocalDateTime.of(2022, 12, 15, 21, 10),
                LocalDateTime.of(2022, 12, 15, 23, 0)
        };

        int indexes[] = { 8, 4, 1, 6, 3, 0, 7, 2, 5 };
        Showing random_order_showings[] = new Showing[times.length];
        Showing in_order_showings[] = new Showing[times.length];

        for (int i : indexes) {
            Showing current_showing = new Showing(spiderMan, theater, times[i]);
            random_order_showings[i] = current_showing;
        }

        for (Showing showing : random_order_showings) {
            in_order_showings[theater.getShowingSequence(showing)] = showing;
        }

        for (int i = 0; i < in_order_showings.length - 1; i++) {
            assertTrue(in_order_showings[i].getStartTime().compareTo(in_order_showings[i + 1].getStartTime()) <= 0);
        }
    }

    @Test
    void invalidReservation() {
        Theater theater = new Theater(LocalDate.now());
        theater.InitForTesting();
        Customer customer = new Customer("John Doe", "not-used-id");

        boolean caughtException = false;
        // Reserving a negative index showing
        try {
            theater.reserve(customer, -1, 2);
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), "not able to find any showing for given sequence -1");
            caughtException = true;
        }

        assertTrue(caughtException);

        caughtException = false;

        // Reserving a showing with too large of an index
        try {
            theater.reserve(customer, 123, 2);
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), "not able to find any showing for given sequence 123");
            caughtException = true;
        }

        assertTrue(caughtException);

        caughtException = false;

        // Reserving a showing with zero tickets (the constructor from the showing
        // should throw the exception)
        try {
            theater.reserve(customer, 1, 0);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "An audience of zero or less people is not allowed");
            caughtException = true;
        }

        assertTrue(caughtException);

        caughtException = false;

        // Reserving a showing with negative audience
        try {
            theater.reserve(customer, 1, -14);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "An audience of zero or less people is not allowed");
            caughtException = true;
        }

        assertTrue(caughtException);

        caughtException = false;

        // Valid reservation - no exception should be thrown
        try {
            theater.reserve(customer, 1, 2);
        } catch (Exception e) {
            caughtException = true;
        }

        assertTrue(caughtException == false);
    }

    @Test
    void printMovieSchedule() {
        Theater theater = new Theater(LocalDate.now());
        theater.InitForTesting();
        theater.printSchedule();
        assertEquals(theater.scheduleToJSON(),
                "{\"Showing\":[{\"sequence\":0,\"StartTime\":\"2022-12-18T09:00\",\"MovieTitle\":\"Turning Red\",\"Runtime\":\"(1 hour 25 minutes)\",\"TicketPrice\":\"$11.0\"},{\"sequence\":1,\"StartTime\":\"2022-12-18T11:00\",\"MovieTitle\":\"Spider-Man: No Way Home\",\"Runtime\":\"(1 hour 30 minutes)\",\"TicketPrice\":\"$12.5\"},{\"sequence\":2,\"StartTime\":\"2022-12-18T12:50\",\"MovieTitle\":\"The Batman\",\"Runtime\":\"(1 hour 35 minutes)\",\"TicketPrice\":\"$9.0\"},{\"sequence\":3,\"StartTime\":\"2022-12-18T14:30\",\"MovieTitle\":\"Turning Red\",\"Runtime\":\"(1 hour 25 minutes)\",\"TicketPrice\":\"$11.0\"},{\"sequence\":4,\"StartTime\":\"2022-12-18T16:10\",\"MovieTitle\":\"Spider-Man: No Way Home\",\"Runtime\":\"(1 hour 30 minutes)\",\"TicketPrice\":\"$12.5\"},{\"sequence\":5,\"StartTime\":\"2022-12-18T17:50\",\"MovieTitle\":\"The Batman\",\"Runtime\":\"(1 hour 35 minutes)\",\"TicketPrice\":\"$9.0\"},{\"sequence\":6,\"StartTime\":\"2022-12-18T19:30\",\"MovieTitle\":\"Turning Red\",\"Runtime\":\"(1 hour 25 minutes)\",\"TicketPrice\":\"$11.0\"},{\"sequence\":7,\"StartTime\":\"2022-12-18T21:10\",\"MovieTitle\":\"Spider-Man: No Way Home\",\"Runtime\":\"(1 hour 30 minutes)\",\"TicketPrice\":\"$12.5\"},{\"sequence\":8,\"StartTime\":\"2022-12-18T23:00\",\"MovieTitle\":\"The Batman\",\"Runtime\":\"(1 hour 35 minutes)\",\"TicketPrice\":\"$9.0\"}]}");
    }
}
