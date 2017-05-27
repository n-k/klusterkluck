package de.ayesolutions.gogs.client.model;

import java.util.Base64;

/**
 * model class for access token.
 *
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public final class AccessToken {

    private String name;

    private String sha1;

    private transient String username;

    private transient String password;

    /**
     * default constructor.
     */
    public AccessToken() {
    }

    /**
     * default constructor.
     *
     * @param name token name.
     * @param sha1 sha1 token.
     */
    public AccessToken(final String name, final String sha1) {
        this(name, sha1, null, null);
    }

    /**
     * default constructor.
     *
     * @param name tokenname.
     * @param sha1 sha1 token.
     * @param username http username.
     * @param password http password.
     */
    public AccessToken(final String name, final String sha1, final String username, final String password) {
        setName(name);
        setSha1(sha1);
        setUsername(username);
        setPassword(password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTokenAuthorization() {
        return "token " + getSha1();
    }

    public String getBasicAuthorization() {
        return "Basic " + new String(Base64.getEncoder().encode((username + ":" + password).getBytes()));
    }
}
