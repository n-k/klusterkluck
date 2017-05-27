package de.ayesolutions.gogs.client.service;

import de.ayesolutions.gogs.client.GogsClient;
import de.ayesolutions.gogs.client.model.Organization;
import de.ayesolutions.gogs.client.model.PublicKey;
import de.ayesolutions.gogs.client.model.Team;
import de.ayesolutions.gogs.client.model.User;

/**
 * service class for administration.
 *
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class AdminService extends BaseService {

    /**
     * default constructor.
     *
     * @param client gogs http client.
     */
    public AdminService(final GogsClient client) {
        super(client);
    }

    /**
     * create new user.
     * <p>
     * POST /api/v1/admin/users
     *
     * @param user user.
     * @return created user.
     */
    public User createUser(User user) {
        return getClient().post(User.class, user, "admin", "users");
    }

    /**
     * update user information.
     * <p>
     * PATCH /api/v1/admin/users/:username
     *
     * @param username username.
     * @param user     user
     * @return updated user.
     */
    public User updateUser(String username, User user) {
        return getClient().patch(User.class, user, "admin", "users", username);
    }

    /**
     * delete user.
     * <p>
     * DELETE /api/v1/admin/users/:username
     *
     * @param username username.
     */
    public void deleteUser(String username) {
        getClient().delete("admin", "users", username);
    }

    /**
     * create new organization to specified user.
     * <p>
     * POST /api/v1/admin/users/:username/orgs
     *
     * @param username     name of user.
     * @param organization organization.
     * @return created organization.
     */
    public Organization createOrganization(String username, Organization organization) {
        return getClient().post(Organization.class, organization, "admin", "users", username, "orgs");
    }

    /**
     * add new public key to specified user.
     * <p>
     * POST /api/v1/admin/users/:username/keys
     *
     * @param username  name of user.
     * @param publicKey public key.
     * @return added public key.
     */
    public PublicKey addPublicKey(String username, PublicKey publicKey) {
        return getClient().post(PublicKey.class, publicKey, "admin", "users", username, "keys");
    }

    /**
     * create new team.
     * <p>
     * POST /api/v1/admin/orgs/:orgname/teams
     *
     * @param organizationName organization name.
     * @param team             team.
     * @return created team.
     */
    public Team createTeam(String organizationName, Team team) {
        return getClient().post(Team.class, team, "admin", "orgs", organizationName, "teams");
    }

    /**
     * add team member to team.
     * <p>
     * PUT /api/v1/admin/teams/:teamId/members/:username
     *
     * @param teamId   team id.
     * @param username username.
     */
    public void addTeamMember(String teamId, String username) {
        getClient().put(Void.class, null, "admin", "teams", teamId, "members", username);
    }

    /**
     * delete team member from team.
     * <p>
     * DELETE /api/v1/admin/teams/:teamId/members/:username
     *
     * @param teamId   team id.
     * @param username username.
     */
    public void deleteTeamMember(String teamId, String username) {
        getClient().delete("admin", "teams", teamId, "members", username);
    }

    /**
     * add team to another repository.
     * <p>
     * PUT /api/v1/admin/teams/:teamId/repos/:reponame
     *
     * @param teamId         team id.
     * @param repositoryName repository name.
     */
    public void addTeamRepository(String teamId, String repositoryName) {
        getClient().put(Void.class, "", "admin", "teams", teamId, "repos", repositoryName);
    }

    /**
     * delete team from repository.
     * <p>
     * DELETE /api/v1/admin/teams/:teamId/repos/:reponame
     *
     * @param teamId         team id.
     * @param repositoryName repository name.
     */
    public void deleteTeamRepository(String teamId, String repositoryName) {
        getClient().delete(Void.class, "admin", "teams", teamId, "repos", repositoryName);
    }
}
