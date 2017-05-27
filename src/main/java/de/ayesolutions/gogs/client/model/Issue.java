package de.ayesolutions.gogs.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class Issue {
    private Long id;

    private Long index;

    private User poster;

    private String title;

    private String body;

    private List<IssueLabel> labels;

    private Milestone milestone;

    private User assignee;

    private String state;

    private Integer comments;

    @JsonProperty("created_at")
    private Date created;

    @JsonProperty("updated_at")
    private Date updated;

    @JsonProperty("pull_request")
    private PullRequest pullRequest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public User getPoster() {
        return poster;
    }

    public void setPoster(User poster) {
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<IssueLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<IssueLabel> labels) {
        this.labels = labels;
    }

    public Milestone getMilestone() {
        return milestone;
    }

    public void setMilestone(Milestone milestone) {
        this.milestone = milestone;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
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

    public PullRequest getPullRequest() {
        return pullRequest;
    }

    public void setPullRequest(PullRequest pullRequest) {
        this.pullRequest = pullRequest;
    }
}
