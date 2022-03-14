package domain;

import java.util.Objects;

public class AuthenticationCredentials {

    private String userName;
    protected String password;

    public AuthenticationCredentials(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticationCredentials that = (AuthenticationCredentials) o;
        return userName.equals(that.userName) && password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password);
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
