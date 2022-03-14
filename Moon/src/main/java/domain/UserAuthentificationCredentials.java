package domain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class UserAuthentificationCredentials extends AuthenticationCredentials {

    private byte[] salt;

    public UserAuthentificationCredentials(String email, String password) {
        super(email, password);
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private String hashPassword(String password, byte[] salt) {

        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public String getSalt() {
        return Base64.getEncoder().encodeToString(this.salt);
    }

    public void setSalt(String saltt) {
        this.salt = Base64.getDecoder().decode(saltt);
    }

    public boolean checkIfPasswordMatches(String pass) {
        return this.password.equals(hashPassword(pass, salt));
    }

    public void hashYourOwnPassword() {
        this.salt = generateSalt();
        this.password = hashPassword(this.password, this.salt);
    }
}
