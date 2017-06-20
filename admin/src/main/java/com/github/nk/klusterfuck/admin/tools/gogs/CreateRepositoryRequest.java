//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.nk.klusterfuck.admin.tools.gogs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRepositoryRequest {
    private String name;
    private String description;
    @JsonProperty("private")
    private Boolean privateRepository;
    @JsonProperty("auto_init")
    private Boolean autoInit;
    private String gitIgnores;
    private String license;
    private String readme = "Default";

    public CreateRepositoryRequest() {
    }

    public CreateRepositoryRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getAutoInit() {
        return this.autoInit;
    }

    public void setAutoInit(Boolean autoInit) {
        this.autoInit = autoInit;
    }

    public String getGitIgnores() {
        return this.gitIgnores;
    }

    public void setGitIgnores(String gitIgnores) {
        this.gitIgnores = gitIgnores;
    }

    public String getLicense() {
        return this.license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getReadme() {
        return this.readme;
    }

    public void setReadme(String readme) {
        this.readme = readme;
    }
}
