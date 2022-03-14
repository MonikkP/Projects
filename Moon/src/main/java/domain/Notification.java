package domain;

import utils.DurationCalculator;

import java.time.LocalDateTime;

public class Notification {
    private final Event attendingEvent;

    private final long secondsDelay;

    public Notification(Event attendingEvent, long secondsDelay) {
        this.attendingEvent = attendingEvent;
        this.secondsDelay = secondsDelay;
    }

    public Event getAttendingEvent() {
        return attendingEvent;
    }

    public String getNotification() {
        return String.format("You are due to participate to %s in %d seconds.", attendingEvent.getName(), secondsDelay);
    }
}
