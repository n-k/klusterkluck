package de.ayesolutions.gogs.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * model class for web hook data.
 *
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class WebHook {

    private Long id;

    private String type;

    private String url;

    private Map<String, String> config = new HashMap<>();

    private List<String> events = new ArrayList<>();

    private Boolean active;

    @JsonProperty("created_at")
    private Date created;

    @JsonProperty("updated_at")
    private Date updated;

    /**
     * default constructor.
     */
    public WebHook() {
    }

    /**
     * constructor with required parameters.
     *
     * @param type        webhook type (gogs or slack)
     * @param url         trigger url on event
     * @param contentType content type (json or form)
     */
    public WebHook(final String type, final String url, final String contentType) {
        this.type = type;
        this.config.put("url", url);
        this.config.put("content_type", contentType);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
