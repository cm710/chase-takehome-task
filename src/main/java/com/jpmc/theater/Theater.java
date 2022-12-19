package com.jpmc.theater;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.math.BigDecimal;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class Theater {

    // No reason it shouldn't be private
    private LocalDate provider;
    private List<Showing> schedule; // This list should be sorted at all times.
    private Comparator<Showing> showingComparator = new Comparator<Showing>() {
        @Override
        public int compare(Showing a, Showing b) {
            return a.getStartTime().compareTo(b.getStartTime());
        }
    };

    /*
     * The LocalDateProvider class does not make sense. currentTime returns
     * DateTime.now() which is already a static function. The way the theater
     * object is written is that it should display movies for a set date (and print
     * them similarly). DateTime.now() provides different values for subsequent
     * calls.
     */
    public Theater(LocalDate provider) {
        this.provider = provider;
        this.schedule = new LinkedList<Showing>();
    }

    public void InitForTesting() {
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, Movie.MOVIE_CODE_SPECIAL);
        Movie turningRed = new Movie("Turning Red", Duration.ofMinutes(85), 11, Movie.MOVIE_CODE_NOT_SPECIAL);
        Movie theBatMan = new Movie("The Batman", Duration.ofMinutes(95), 9, Movie.MOVIE_CODE_NOT_SPECIAL);

        new Showing(turningRed, this, LocalDateTime.of(provider, LocalTime.of(9, 0)));
        new Showing(spiderMan, this, LocalDateTime.of(provider, LocalTime.of(11, 0)));
        new Showing(theBatMan, this, LocalDateTime.of(provider, LocalTime.of(12, 50)));
        new Showing(turningRed, this, LocalDateTime.of(provider, LocalTime.of(14, 30)));
        new Showing(spiderMan, this, LocalDateTime.of(provider, LocalTime.of(16, 10)));
        new Showing(theBatMan, this, LocalDateTime.of(provider, LocalTime.of(17, 50)));
        new Showing(turningRed, this, LocalDateTime.of(provider, LocalTime.of(19, 30)));
        new Showing(spiderMan, this, LocalDateTime.of(provider, LocalTime.of(21, 10)));
        new Showing(theBatMan, this, LocalDateTime.of(provider, LocalTime.of(23, 0)));
    }

    // Making sure a showing cannot be added to the wrong theater.
    public static void addShowingToSchedule(Showing s) {
        s.getTheater().addShowing(s);
    }

    private void addShowing(Showing s) {
        schedule.add(s);
        Collections.sort(schedule, showingComparator);
    }

    public int getShowingSequence(Showing s) {
        for (int i = 0; i < schedule.size(); i++) {
            if (schedule.get(i) == s) {
                return i;
            }
        }

        throw new IllegalArgumentException("Showing not found in the schedule");
    }

    public double getMovieTicketPrice(Showing s) {
        return s.getMovie().getTicketPrice();
    }

    public double getMovieDiscount(Showing s) {
        double discount = 0;

        double specialDiscount = 0;
        if (Movie.MOVIE_CODE_SPECIAL == s.getMovie().getSpecialCode()) {
            specialDiscount = s.getMovie().getTicketPrice() * 0.2d; // 20% discount for special movie
        }

        double sequenceDiscount = 0;
        if (getShowingSequence(s) == 0) {
            sequenceDiscount = 3; // $3 discount for 1st show
        } else if (getShowingSequence(s) == 1) {
            sequenceDiscount = 2; // $2 discount for 2nd show
        }

        discount = Math.max(specialDiscount, sequenceDiscount);

        double afternoonDiscount = 0;
        if (s.getStartTime().getHour() >= 11 && s.getStartTime().getHour() < 16) {
            afternoonDiscount = s.getMovie().getTicketPrice() * 0.25; // 25% discount for movies during the day
        }

        discount = Math.max(discount, afternoonDiscount);

        double dayOfMonthDiscount = 0;
        if (s.getStartTime().getDayOfMonth() == 7) {
            dayOfMonthDiscount = 1; // $1 for the 7th day of the month
        }

        discount = Math.max(discount, dayOfMonthDiscount);

        return discount;
    }

    public double calculateTicketPrice(Showing s) {
        // Using bigdecimal to account for precision errors.
        return new BigDecimal(Double.toString(getMovieTicketPrice(s)))
                .subtract(new BigDecimal(Double.toString(getMovieDiscount(s)))).doubleValue();
    }

    public Reservation reserve(Customer customer, int sequence, int howManyTickets) {
        Showing showing;
        try {
            showing = schedule.get(sequence - 1);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            throw new IllegalStateException("not able to find any showing for given sequence " + sequence);
        }
        return new Reservation(customer, showing, howManyTickets);
    }

    public String scheduleToJSON() {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (Showing s : schedule) {
            arrayBuilder.add(Json.createObjectBuilder()
                    .add("sequence", getShowingSequence(s))
                    .add("StartTime", s.getStartTime().toString())
                    .add("MovieTitle", s.getMovie().getTitle())
                    .add("Runtime", humanReadableFormat(s.getMovie().getRunningTime()))
                    .add("TicketPrice", "$" + getMovieTicketPrice(s)));
        }

        objectBuilder.add("Showing", arrayBuilder);

        JsonObject jsonObject = objectBuilder.build();

        return jsonObject.toString();
    }

    public void printSchedule() {
        System.out.println(provider);
        System.out.println("===================================================");
        schedule.forEach(s -> System.out
                .println(getShowingSequence(s) + ": " + s.getStartTime() + " " + s.getMovie().getTitle() + " "
                        + humanReadableFormat(s.getMovie().getRunningTime()) + " $" + getMovieTicketPrice(s)));
        System.out.println("===================================================\n");
    }

    public String humanReadableFormat(Duration duration) {
        long hour = duration.toHours();
        long remainingMin = duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours());

        return String.format("(%s hour%s %s minute%s)", hour, handlePlural(hour), remainingMin,
                handlePlural(remainingMin));
    }

    // (s) postfix should be added to handle plural correctly
    private String handlePlural(long value) {
        if (value == 1) {
            return "";
        } else {
            return "s";
        }
    }

    public static void main(String[] args) {
        Theater theater = new Theater(LocalDate.now());
        theater.InitForTesting();
        theater.printSchedule();

        System.out.println(theater.scheduleToJSON());
    }
}
