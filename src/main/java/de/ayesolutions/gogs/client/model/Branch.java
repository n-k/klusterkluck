package de.ayesolutions.gogs.client.model;

/**
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class Branch {

    private String name;

    private PayloadCommit commit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PayloadCommit getCommit() {
        return commit;
    }

    public void setCommit(PayloadCommit commit) {
        this.commit = commit;
    }
}
