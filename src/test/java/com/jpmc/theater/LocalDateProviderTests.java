package com.jpmc.theater;

import org.junit.jupiter.api.Test;

public class LocalDateProviderTests {
    // Not using this class.
    @Test
    void makeSureCurrentTime() {
        System.out.println("current time is - " + LocalDateProvider.singleton().currentDate());
    }
}
