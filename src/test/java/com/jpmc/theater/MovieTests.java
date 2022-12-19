package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovieTests {
   /* The only module that interacts outside this class is the discount
   * calculation. It interfaces with the showing and the theater objects in
   * order to arrive at a final price. The title, description and running times
   * are not interacting with any other modules, so no tests are required for
   * now.*/

    @Test
    void testDiscounts() {
        Theater theater = new Theater(LocalDate.now());

        Movie firstSecondThrid = new Movie("Eins, Zwei, Drei", Duration.ofMinutes(104), 4.8, Movie.MOVIE_CODE_SPECIAL);
        // Discounts for first showing, second showing, special code, time of day and
        // day of month - first showing discount should apply
        Showing firstShowing = new Showing(firstSecondThrid, theater,
                LocalDateTime.of(1962, 1, 7, 12, 0, Movie.MOVIE_CODE_NOT_SPECIAL));
        // Discounts for second showing, special code, time of day and day of month -
        // second showing discount should apply
        Showing secondShowing = new Showing(firstSecondThrid, theater,
                LocalDateTime.of(1962, 1, 7, 12, 0, Movie.MOVIE_CODE_NOT_SPECIAL));
        // Discounts for special code, time of day and day of month - time of day
        // discount should apply
        Showing thirdShowing = new Showing(firstSecondThrid, theater,
                LocalDateTime.of(1962, 1, 7, 12, 0, Movie.MOVIE_CODE_NOT_SPECIAL));
        // Discounts for special code and day of month - special code should apply
        Showing fourthShowing = new Showing(firstSecondThrid, theater,
                LocalDateTime.of(1962, 1, 7, 17, 0, Movie.MOVIE_CODE_NOT_SPECIAL));
        // Discount for special code only
        Showing fifthShowing = new Showing(firstSecondThrid, theater,
                LocalDateTime.of(1962, 1, 8, 17, 0, Movie.MOVIE_CODE_NOT_SPECIAL));
        Movie noSpecialMovie = new Movie("Avatar", Duration.ofMinutes(162), 10, Movie.MOVIE_CODE_NOT_SPECIAL);
        // No Discounts applied
        Showing sixthShowing = new Showing(noSpecialMovie, theater,
                LocalDateTime.of(2009, 12, 18, 17, 0, Movie.MOVIE_CODE_NOT_SPECIAL));

        assertEquals(1.8, theater.calculateTicketPrice(firstShowing));
        assertEquals(2.8, theater.calculateTicketPrice(secondShowing));
        assertEquals(3.6, theater.calculateTicketPrice(thirdShowing));
        assertEquals(3.8, theater.calculateTicketPrice(fourthShowing));
        assertEquals(3.84, theater.calculateTicketPrice(fifthShowing));
        assertEquals(10, theater.calculateTicketPrice(sixthShowing));
    }
}
