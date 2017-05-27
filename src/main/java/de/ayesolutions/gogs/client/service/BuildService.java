package de.ayesolutions.gogs.client.service;

import de.ayesolutions.gogs.client.GogsClient;
import de.ayesolutions.gogs.client.model.Status;

import javax.ws.rs.core.GenericType;
import java.util.Collections;
import java.util.List;

/**
 * gogs build service service call class.
 *
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class BuildService extends BaseService {

    /**
     * default constructor.
     *
     * @param client gogs http client.
     */
    public BuildService(final GogsClient client) {
        super(client);
    }

    /**
     * get list of build states of repository.
     * <p>
     * GET /api/v1/repos/:username/:reponame/statuses
     * Response 200, 404, 500
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @return list of states.
     */
    public List<Status> listStatuses(String username, String repositoryName) {
        List<Status> list = getClient().get(new GenericType<List<Status>>() {
        }, "repos", username, repositoryName, "statuses");

        return list != null ? list : Collections.emptyList();
    }

    /**
     * create new build status for repository.
     * <p>
     * POST /api/v1/repos/:username/:reponame/statuses/:sha
     * Response 201, 404, 500
     *
     * @param username       username.
     * @param repositoryName repository name.
     * @param sha            commit sha
     * @param status         status.
     * @return created status.
     */
    public Status createStatus(String username, String repositoryName, String sha, Status status) {
        return getClient().post(Status.class, status, "repos", username, repositoryName, "statuses", sha);
    }
}
