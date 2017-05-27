package de.ayesolutions.gogs.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class CreateRepository {

    private String name;

    private String description;

    @JsonProperty("private")
    private Boolean privateRepository;

    @JsonProperty("auto_init")
    private Boolean autoInit;

    private String gitIgnores;

    private String license;

    private String readme = "Default";

    /**
     * default constructor.
     */
    public CreateRepository() {
    }

    /**
     * default constructor.
     *
     * @param name repository name.
     */
    public CreateRepository(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getAutoInit() {
        return autoInit;
    }

    public void setAutoInit(Boolean autoInit) {
        this.autoInit = autoInit;
    }

    public String getGitIgnores() {
        return gitIgnores;
    }

    public void setGitIgnores(String gitIgnores) {
        this.gitIgnores = gitIgnores;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getReadme() {
        return readme;
    }

    public void setReadme(String readme) {
        this.readme = readme;
    }
}
