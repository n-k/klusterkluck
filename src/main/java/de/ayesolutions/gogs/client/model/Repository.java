package de.ayesolutions.gogs.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * repository model class.
 *
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class Repository {

    /**
     * repository permission class.
     */
    public class RepositoryPermission {
        private boolean admin;

        private boolean push;

        private boolean pull;

        public boolean isAdmin() {
            return admin;
        }

        public void setAdmin(boolean admin) {
            this.admin = admin;
        }

        public boolean isPush() {
            return push;
        }

        public void setPush(boolean push) {
            this.push = push;
        }

        public boolean isPull() {
            return pull;
        }

        public void setPull(boolean pull) {
            this.pull = pull;
        }
    }

    private Long id;

    private User owner;

    private String name;

    @JsonProperty("full_name")
    private String fullName;

    private String description;

    @JsonProperty("private")
    private Boolean privateRepository;

    private Boolean fork;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("ssh_url")
    private String sshUrl;

    @JsonProperty("clone_url")
    private String cloneUrl;

    private String website;

    @JsonProperty("stars_count")
    private Integer starsCount;

    @JsonProperty("forks_count")
    private Integer forks;

    @JsonProperty("watchers_count")
    private Integer watchers;

    @JsonProperty("open_issues_count")
    private Integer openIssues;

    @JsonProperty("default_branch")
    private String defaultBranch;

    @JsonProperty("created_at")
    private Date created;

    @JsonProperty("updated_at")
    private Date updated;

    private RepositoryPermission permissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getPrivateRepository() {
        return privateRepository;
    }

    public void setPrivateRepository(Boolean privateRepository) {
        this.privateRepository = privateRepository;
    }

    public Boolean getFork() {
        return fork;
    }

    public void setFork(Boolean fork) {
        this.fork = fork;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getSshUrl() {
        return sshUrl;
    }

    public void setSshUrl(String sshUrl) {
        this.sshUrl = sshUrl;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getStarsCount() {
        return starsCount;
    }

    public void setStarsCount(Integer starsCount) {
        this.starsCount = starsCount;
    }

    public Integer getForks() {
        return forks;
    }

    public void setForks(Integer forks) {
        this.forks = forks;
    }

    public Integer getWatchers() {
        return watchers;
    }

    public void setWatchers(Integer watchers) {
        this.watchers = watchers;
    }

    public Integer getOpenIssues() {
        return openIssues;
    }

    public void setOpenIssues(Integer openIssues) {
        this.openIssues = openIssues;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
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

    public RepositoryPermission getPermissions() {
        return permissions;
    }

    public void setPermissions(RepositoryPermission permissions) {
        this.permissions = permissions;
    }
}
