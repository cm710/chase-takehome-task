package com.jpmc.theater;

import java.time.Duration;
import java.util.Objects;

public class Movie {
    // No reason this should not be private - it may even be a property of Theater
    public static int MOVIE_CODE_SPECIAL = 1;

    // code that will be always not special, in case we decide to add other codes
    public static int MOVIE_CODE_NOT_SPECIAL = 0;

    private String title;
    private String description;
    private Duration runningTime;
    private double ticketPrice;
    private int specialCode;

    public Movie(String title, Duration runningTime, double ticketPrice, int specialCode) {
        this.title = title;
        this.runningTime = runningTime;
        this.ticketPrice = ticketPrice;
        this.specialCode = specialCode;
    }

    public String getTitle() {
        return title;
    }

    public Duration getRunningTime() {
        return runningTime;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    /*
     * There are several things wrong here:
     * 1. The showing argument may not even show the same movie we're
     * processing
     * 2. This function should be called from the top layer - the theater
     * should have a function called calculateTicketPrice(Showing showing) and
     * we will get automatic consistency as Theater > Showing > Movie. Also, if
     * the discount strategy changes, we won't need to add any arguments to this
     * method as theater already has that information available.
     * 3. showing.getSequenceOfTheDay has a similar problem - that needs to be
     * calculated at the container level.
     * 
     * That being said, for a scalable program, various classes will eventually
     * require having access to the object that encapsulates them (e.g. Showing
     * could hold a reference to the Theater object that contains it). Still,
     * this can be done without introducing inconsistencies in the function call
     * hyerarchy design.
     */

    public int getSpecialCode() {
        return this.specialCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Movie movie = (Movie) o;
        return Double.compare(movie.ticketPrice, ticketPrice) == 0
                && Objects.equals(title, movie.title)
                && Objects.equals(description, movie.description)
                && Objects.equals(runningTime, movie.runningTime)
                && Objects.equals(specialCode, movie.specialCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, runningTime, ticketPrice, specialCode);
    }
}