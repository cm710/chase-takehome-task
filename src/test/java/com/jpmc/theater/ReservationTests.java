package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReservationTests {

    @Test
    void totalFee() {
        Theater theater = new Theater(LocalDate.of(2009, 12, 18));
        theater.InitForTesting();

        var customer = new Customer("John Doe", "unused-id");
        var showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, Movie.MOVIE_CODE_SPECIAL), theater,
                LocalDateTime.of(2021, 12, 18, 17, 0, 0));

        var showing_no_discount = new Showing(
                new Movie("Avatar", Duration.ofMinutes(162), 12.5, Movie.MOVIE_CODE_NOT_SPECIAL), theater,
                LocalDateTime.of(2009, 12, 18, 17, 0, 0));

        assertEquals(37.5, new Reservation(customer, showing_no_discount, 3).totalFee());
        assertEquals(30, new Reservation(customer, showing, 3).totalFee());
    }

    @Test
    void invalidAudience() {
        Theater theater = new Theater(LocalDate.now());
        theater.InitForTesting();

        var customer = new Customer("John Doe", "unused-id");
        var showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, Movie.MOVIE_CODE_SPECIAL), theater,
                LocalDateTime.of(2021, 12, 18, 17, 0, 0));
        boolean caughtException = false;

        try {
            new Reservation(customer, showing, 0);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "An audience of zero or less people is not allowed");
            caughtException = true;
        }

        assertTrue(caughtException);

        caughtException = false;

        try {
            new Reservation(customer, showing, -123);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "An audience of zero or less people is not allowed");
            caughtException = true;
        }

        assertTrue(caughtException);
    }
}
