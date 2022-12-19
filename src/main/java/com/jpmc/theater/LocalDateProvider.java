package com.jpmc.theater;

import java.time.LocalDate;

/* I'm not sure why this is built this way if currentDate returns DateTime.now() 
which already is a static function. I will not be using this class in the 
implementation */

public class LocalDateProvider {
    private static LocalDateProvider instance = null;

    /**
     * @return make sure to return singleton instance
     */
    public static LocalDateProvider singleton() {
        if (instance == null) {
            instance = new LocalDateProvider();
        }
        return instance;
    }

    public LocalDate currentDate() {
        return LocalDate.now();
    }
}
