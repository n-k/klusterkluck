package de.ayesolutions.gogs.client.model;

import java.util.List;

/**
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class UserSearchResult {

    private List<User> data;

    private boolean ok;

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}
