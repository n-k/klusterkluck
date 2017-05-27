package de.ayesolutions.gogs.client.model;

/**
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class Email {
    private String email;

    private boolean verified;

    private boolean primary;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }
}
