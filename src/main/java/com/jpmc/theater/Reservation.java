package com.jpmc.theater;

public class Reservation {
    private Customer customer;
    private Showing showing;
    private int audienceCount;

    public Reservation(Customer customer, Showing showing, int audienceCount) {
        this.customer = customer;
        this.showing = showing;
        this.audienceCount = audienceCount;

        if (audienceCount <= 0) {
            throw new IllegalArgumentException("An audience of zero or less people is not allowed");
        }
    }

    public double totalFee() {
        return showing.calculateTicketPrice() * audienceCount;
    }
}