package de.ayesolutions.gogs.client.service;

import de.ayesolutions.gogs.client.GogsClient;
import de.ayesolutions.gogs.client.model.Branch;
import de.ayesolutions.gogs.client.model.Collaborator;
import de.ayesolutions.gogs.client.model.CreateRepository;
import de.ayesolutions.gogs.client.model.EditorDefinition;
import de.ayesolutions.gogs.client.model.MigrationRepository;
import de.ayesolutions.gogs.client.model.PublicKey;
import de.ayesolutions.gogs.client.model.Repository;
import de.ayesolutions.gogs.client.model.WebHook;

import javax.ws.rs.core.GenericType;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * service class for repository management.
 *
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class RepositoryService extends BaseService {

    /**
     * default constructor.
     *
     * @param client gogs client.
     */
    public RepositoryService(final GogsClient client) {
        super(client);
    }

    /**
     * list all repository for signed in user.
     * <p>
     * GET /api/v1/user/repos
     *
     * @return list of repositories.
     */
    public List<Repository> listRepositories() {
        List<Repository> list = getClient().get(new GenericType<List<Repository>>() {
        }, "user", "repos");

        return list != null ? list : Collections.emptyList();
    }

    /**
     * create user repository.
     * <p>
     * POST /api/v1/user/repos
     *
     * @param repository repository.
     * @return created repository.
     */
    public Repository createRepository(CreateRepository repository) {
        return getClient().post(Repository.class, repository, "user", "repos");
    }

    /**
     * create organization repository.
     * <p>
     * POST /api/v1/org/:orgname/repos
     *
     * @param organizationName organization name.
     * @param repository       repository.
     * @return created repository.
     */
    public Repository createOrganizationRepository(String organizationName, CreateRepository repository) {
        return getClient().post(Repository.class, repository, "org", organizationName, "repos");
    }

    /**
     * create new repository to specified user.
     * <p>
     * POST /api/v1/admin/users/:username/repos
     *
     * @param username   name of user.
     * @param repository repository.
     * @return created repository.
     */
    public Repository createRepository(String username, Repository repository) {
        return getClient().post(Repository.class, repository, "users", username, "repos");
    }

    /**
     * search for repositories.
     * <p>
     * GET /api/v1/repos/search
     *
     * @param query  query string.
     * @param userId user id. (default for all 0)
     * @param limit  limit value result. (default 10)
     * @return search result of found repositories.
     */
    public List<Repository> search(String query, long userId, int limit) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("q", query);
        parameters.put("uid", String.valueOf(userId));
        parameters.put("limit", String.valueOf(limit));

        List<Repository> list = getClient().get(new GenericType<List<Repository>>() {
        }, parameters, "user", "repos");

        return list != null ? list : Collections.emptyList();
    }

    /**
     * migrate existing repository to gogs account.
     * <p>
     * POST /api/v1/repos/migrate
     *
     * @param repository repository.
     * @return migrated repository.
     */
    public Repository migrate(MigrationRepository repository) {
        return getClient().post(Repository.class, repository, "repos", "migrate");
    }

    /**
     * get repository.
     * <p>
     * GET /api/v1/repos/:username/:reponame
     *
     * @param username       user name.
     * @param repositoryName repository name.
     * @return repository.
     */
    public Repository getRepository(String username, String repositoryName) {
        return getClient().get(Repository.class, "repos", username, repositoryName);
    }

    /**
     * delete repository from user.
     * <p>
     * DELETE /api/v1/repos/:username/:reponame
     *
     * @param username       username.
     * @param repositoryName repository name.
     */
    public void deleteRepository(String username, String repositoryName) {
        getClient().delete(Repository.class, "repos", username, repositoryName);
    }

    /**
     * get list of web hooks from repository.
     * <p>
     * GET /api/v1/repos/:username/:reponame/hooks
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @return list of web hooks.
     */
    public List<WebHook> listWebHooks(String username, String repositoryName) {
        List<WebHook> list = getClient().get(new GenericType<List<WebHook>>() {
        }, "repos", username, repositoryName, "hooks");

        return list != null ? list : Collections.emptyList();
    }

    /**
     * create new web hook for repository.
     * <p>
     * POST /api/v1/repos/:username/:reponame/hooks
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param webHook        web hook.
     * @return created web hook.
     */
    public WebHook createWebHook(String username, String repositoryName, WebHook webHook) {
        return getClient().post(WebHook.class, webHook, "repos", username, repositoryName, "hooks");
    }

    /**
     * update web hook information.
     * <p>
     * PATCH /api/v1/repos/:username/:reponame/hooks/:id
     *
     * @param username       username.
     * @param repositoryName repository name
     * @param webHook        web hook.
     * @return updated web hook.
     */
    public WebHook updateWebHook(String username, String repositoryName, WebHook webHook) {
        return getClient().patch(WebHook.class, webHook, "repos", username, repositoryName, "hooks",
                webHook.getId().toString());
    }

    /**
     * delete web hook from repository.
     * <p>
     * DELETE /api/v1/repos/:username/:reponame/hooks/:id
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param webHookId      web hook id.
     */
    public void deleteWebHook(String username, String repositoryName, long webHookId) {
        getClient().delete(WebHook.class, "repos", username, repositoryName, "hooks", String.valueOf(webHookId));
    }

    /**
     * add new collaborator to repository.
     * <p>
     * PUT /api/v1/repos/:username/:reponame/collaborator/:id
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param collaboratorId collaboration id.
     * @param collaborator   collaborator.
     */
    public void addCollaborator(String username, String repositoryName, String collaboratorId,
                                Collaborator collaborator) {
        getClient().put(Void.class, collaborator, "repos", username, repositoryName, "collaborator",
                collaboratorId);
    }

    /**
     * get raw content of file in repository.
     * <p>
     * GET /api/v1/repos/:username/:reponame/raw/:path
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param path           path to file.
     * @return data byte array.
     */
    public byte[] getRawFile(String username, String repositoryName, String path) {
        throw new UnsupportedOperationException("receive binary data currently not supported");
    }

    /**
     * get repository archive.
     * <p>
     * GET /api/v1/repos/:username/:reponame/archive/:path
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param path           path to archive.
     * @return data byte array.
     */
    public byte[] getArchive(String username, String repositoryName, String path) {
        throw new UnsupportedOperationException("receive binary data currently not supported");
    }

    /**
     * list branches of repository.
     * <p>
     * GET /api/v1/repos/:username/:reponame/branches
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @return list of branches.
     */
    public List<Branch> listBranches(String username, String repositoryName) {
        List<Branch> list = getClient().get(new GenericType<List<Branch>>() {
        }, "repos", username, repositoryName, "branches");

        return list != null ? list : Collections.emptyList();
    }

    /**
     * get specified branch.
     * <p>
     * GET /api/v1/repos/:username/:reponame/branches/:id
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param branchId       branch id.
     * @return repository branch.
     */
    public Branch getBranch(String username, String repositoryName, String branchId) {
        return getClient().get(Branch.class, "repos", username, repositoryName, "branches", branchId);
    }

    /**
     * get list of deployment keys from repository.
     * <p>
     * GET /api/v1/repos/:username/:reponame/keys
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @return list of deployment keys.
     */
    public List<PublicKey> getDeployKeys(String username, String repositoryName) {
        List<PublicKey> list = getClient().get(new GenericType<List<PublicKey>>() {
        }, "repos", username, repositoryName, "keys");

        return list != null ? list : Collections.emptyList();
    }

    /**
     * add new deployment key to repository.
     * <p>
     * POST /api/v1/repos/:username/:reponame/keys
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param publicKey      public key.
     * @return added public key.
     */
    public PublicKey addDeployKey(String username, String repositoryName, PublicKey publicKey) {
        return getClient().post(PublicKey.class, publicKey, "repos", username, repositoryName, "keys");
    }

    /**
     * get specified deployment key.
     * <p>
     * GET /api/v1/repos/:username/:reponame/keys/:id
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param deployKeyId    deployment key id.
     * @return deployment key.
     */
    public PublicKey getDeployKey(String username, String repositoryName, String deployKeyId) {
        return getClient().get(PublicKey.class, "repos", username, repositoryName, "keys", deployKeyId);
    }

    /**
     * delete deployment key.
     * <p>
     * DELETE /api/v1/repos/:username/:reponame/keys/:id
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param deployKeyId    deployment key id.
     */
    public void deleteDeployKey(String username, String repositoryName, String deployKeyId) {
        getClient().delete(PublicKey.class, "repos", username, repositoryName, "keys", deployKeyId);
    }

    /**
     * get editor configuration from repository.
     * <p>
     * GET /api/v1/repos/:username/:reponame/editorconfig/:path
     *
     * @param username       username.
     * @param repositoryName repository.
     * @param path           path to editor configuration.
     * @return data byte array.
     */
    public EditorDefinition getEditorConfig(String username, String repositoryName, String path) {
        return getClient().get(EditorDefinition.class, "repos", username, repositoryName, "editorconfig", path);
    }
}
