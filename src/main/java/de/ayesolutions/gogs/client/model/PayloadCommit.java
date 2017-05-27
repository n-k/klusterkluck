package de.ayesolutions.gogs.client.model;

import java.util.Date;

/**
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class PayloadCommit {
    private String id;

    private String message;

    private String url;

    private PayloadUser author;

    private PayloadUser committer;

    private Date timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PayloadUser getAuthor() {
        return author;
    }

    public void setAuthor(PayloadUser author) {
        this.author = author;
    }

    public PayloadUser getCommitter() {
        return committer;
    }

    public void setCommitter(PayloadUser committer) {
        this.committer = committer;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
