package domain;

import javafx.scene.control.Button;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Integer>{

    private String firstName;
    private String lastName;
    private LocalDate dateBirth;
    private String country;
    private UserAuthentificationCredentials userAuthentificationCredentials;

    public User(String firstName, String lastName, LocalDate dateBirth, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateBirth = dateBirth;
        this.country = country;
    }

    public User() {

    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateBirth() {
        return dateBirth;
    }

    public String getCountry() {
        return country;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public UserAuthentificationCredentials getUserAuthentificationCredentials() {
        return userAuthentificationCredentials;
    }

    public void setUserAuthentificationCredentials(UserAuthentificationCredentials userAuthentificationCredentials) {
        this.userAuthentificationCredentials = userAuthentificationCredentials;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateBirth(LocalDate dateBirth) {
        this.dateBirth = dateBirth;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return
                "id=" + id +
                ", firstName=" + firstName +
                ", lastName=" + lastName  +
                ", dateBirth=" + dateBirth +
                ", country=" + country;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof User)) return false;
        User that = (User) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) &&
                Objects.equals(dateBirth, that.dateBirth) && Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash( firstName, lastName, dateBirth, country);
    }
}
