package com.jpmc.theater;

import java.time.LocalDateTime;

import java.util.Objects;

public class Showing {
    private Movie movie;
    private Theater theater;
    /*
     * sequenceOfTheDay should not exist as it could be inconsistent with the
     * actual showing order. Instead, it should be calculated based on the
     * container it occupies.
     */
    private LocalDateTime showStartTime;

    public Showing(Movie movie, Theater theater, LocalDateTime showStartTime) {
        this.movie = movie;
        this.theater = theater;
        this.showStartTime = showStartTime;
        Theater.addShowingToSchedule(this);
    }

    public Theater getTheater() {
        return theater;
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalDateTime getStartTime() {
        return showStartTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || o.getClass() != getClass()) {
            return false;
        }

        Showing s = (Showing) o;
        return Objects.equals(s.movie, this.movie) && Objects.equals(s.showStartTime, this.showStartTime);
    }

    public double calculateTicketPrice() {
        return theater.calculateTicketPrice(this);
    }
}
