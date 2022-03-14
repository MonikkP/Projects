package domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class Message extends Entity<Integer>{

    private User from;
    private List<User> to;
    private String message;
    private LocalDateTime date;
    private Message reply;

    public Message(User from, List<User> to, String message, LocalDateTime date) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        this.reply = null;
    }

    public Message(User from, List<User> to, String message, LocalDateTime date, Message reply) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        this.reply = reply;
    }


    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public List<User> getTo() {
        return to;
    }

    public void setTo(List<User> to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Message getReply() {
        return reply;
    }

    public void setReply(Message reply) {
        this.reply = reply;
    }

    public boolean containsUser(User user) {
        return getFrom().equals(user) || getTo().contains(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Message message1 = (Message) o;
        return Objects.equals(from, message1.from) && Objects.equals(to, message1.to) && Objects.equals(message, message1.message) && Objects.equals(date, message1.date) && Objects.equals(reply, message1.reply);
    }

    @Override
    public String toString() {
        String output = "";
        output += "ID: " + getId() + "\n";
        output += " From: " + "<" + getFrom().getId() + ">" + " " + getFrom().getFullName() + "\n";
        for (User user : getTo())
            output += " To: " + "<" + user.getId() + ">" + " " + user.getFullName() + "\n";
        output += " Date: " + getDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd h:mm a")) + "\n";
        output += " Message: " + getMessage() +"\n";
        if (getReply() != null) {
            output += "Reply: ID - " + getReply().getId() + " | \"" + getReply().getMessage() + "\"\n";
        }
        return output;
    }

    public String toStringForPDF() {
        String output = "";
        output += " From: "  + getFrom().getFullName() + "\n";
        output += " Date: " + getDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd h:mm a")) + "\n";
        output += " Message: " + getMessage() +"\n";
        return output;
    }



    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), from, to, message, date, reply);
    }
}
