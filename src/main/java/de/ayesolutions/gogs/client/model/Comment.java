package de.ayesolutions.gogs.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class Comment {

    private Long id;

    private User poster;

    private String body;

    @JsonProperty("created_at")
    private Date created;

    @JsonProperty("updated_at")
    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getPoster() {
        return poster;
    }

    public void setPoster(User poster) {
        this.poster = poster;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
