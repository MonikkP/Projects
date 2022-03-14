package domain;

public class DatabaseCredentials extends AuthenticationCredentials {

    public String getURL() {
        return URL;
    }

    private String URL;

    public DatabaseCredentials(String URL, String userName, String password) {
        super(userName, password);
        this.URL = URL;
    }

}
