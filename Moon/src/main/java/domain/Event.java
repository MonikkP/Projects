package domain;

import java.time.LocalDateTime;
import java.util.List;

public class Event extends Entity<Integer>{

    private User owner;
    private String name;
    private String description;
    private String location;
    private LocalDateTime dateTime;
    private List<User> participants;

    public Event(User owner, String name, String description, String location, LocalDateTime dateTime,List<User> mem) {
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.participants = mem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public User getOwner() {
        return owner;
    }
}
