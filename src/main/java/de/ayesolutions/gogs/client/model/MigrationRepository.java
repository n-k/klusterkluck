package de.ayesolutions.gogs.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class MigrationRepository {

    @JsonProperty("clone_addr")
    private String cloneUrl;

    @JsonProperty("auth_username")
    private String username;

    @JsonProperty("auth_password")
    private String password;

    @JsonProperty("uid")
    private Long userId;

    @JsonProperty("repo_name")
    private String name;

    private Boolean mirror;

    @JsonProperty("private")
    private Boolean privateRepository;

    private String description;

    public String getCloneUrl() {
        return cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getMirror() {
        return mirror;
    }

    public void setMirror(Boolean mirror) {
        this.mirror = mirror;
    }

    public Boolean getPrivateRepository() {
        return privateRepository;
    }

    public void setPrivateRepository(Boolean privateRepository) {
        this.privateRepository = privateRepository;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
