//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.nk.klusterfuck.admin.tools.gogs;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Map;

public class Repository {
    private Long id;
    private Map owner;
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
    private Repository.RepositoryPermission permissions;

    public Repository() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map getOwner() {
        return this.owner;
    }

    public void setOwner(Map owner) {
        this.owner = owner;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getPrivateRepository() {
        return this.privateRepository;
    }

    public void setPrivateRepository(Boolean privateRepository) {
        this.privateRepository = privateRepository;
    }

    public Boolean getFork() {
        return this.fork;
    }

    public void setFork(Boolean fork) {
        this.fork = fork;
    }

    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getSshUrl() {
        return this.sshUrl;
    }

    public void setSshUrl(String sshUrl) {
        this.sshUrl = sshUrl;
    }

    public String getCloneUrl() {
        return this.cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getStarsCount() {
        return this.starsCount;
    }

    public void setStarsCount(Integer starsCount) {
        this.starsCount = starsCount;
    }

    public Integer getForks() {
        return this.forks;
    }

    public void setForks(Integer forks) {
        this.forks = forks;
    }

    public Integer getWatchers() {
        return this.watchers;
    }

    public void setWatchers(Integer watchers) {
        this.watchers = watchers;
    }

    public Integer getOpenIssues() {
        return this.openIssues;
    }

    public void setOpenIssues(Integer openIssues) {
        this.openIssues = openIssues;
    }

    public String getDefaultBranch() {
        return this.defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return this.updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Repository.RepositoryPermission getPermissions() {
        return this.permissions;
    }

    public void setPermissions(Repository.RepositoryPermission permissions) {
        this.permissions = permissions;
    }

    public class RepositoryPermission {
        private boolean admin;
        private boolean push;
        private boolean pull;

        public RepositoryPermission() {
        }

        public boolean isAdmin() {
            return this.admin;
        }

        public void setAdmin(boolean admin) {
            this.admin = admin;
        }

        public boolean isPush() {
            return this.push;
        }

        public void setPush(boolean push) {
            this.push = push;
        }

        public boolean isPull() {
            return this.pull;
        }

        public void setPull(boolean pull) {
            this.pull = pull;
        }
    }
}
