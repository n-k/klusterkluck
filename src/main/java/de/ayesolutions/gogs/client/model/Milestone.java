package de.ayesolutions.gogs.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class Milestone {

    private Long id;

    private String title;

    private String description;

    private String state;

    @JsonProperty("open_issues")
    private Integer openIssues;

    @JsonProperty("closed_issues")
    private Integer closedIssues;

    @JsonProperty("closed_at")
    private Date closed;

    @JsonProperty("due_on")
    private Date deadline;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getOpenIssues() {
        return openIssues;
    }

    public void setOpenIssues(Integer openIssues) {
        this.openIssues = openIssues;
    }

    public Integer getClosedIssues() {
        return closedIssues;
    }

    public void setClosedIssues(Integer closedIssues) {
        this.closedIssues = closedIssues;
    }

    public Date getClosed() {
        return closed;
    }

    public void setClosed(Date closed) {
        this.closed = closed;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
}
