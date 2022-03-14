package utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class DurationCalculator {

    private DurationCalculator() {
    }

    public static long findDurationBetweenDatesInMinutes(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes();
    }

    public static long findDurationBetweenDatesInSeconds(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toSeconds();
    }
}
