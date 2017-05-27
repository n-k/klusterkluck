package de.ayesolutions.gogs.client.service;

import de.ayesolutions.gogs.client.GogsClient;
import de.ayesolutions.gogs.client.model.Organization;
import de.ayesolutions.gogs.client.model.Team;

import javax.ws.rs.core.GenericType;
import java.util.Collections;
import java.util.List;

/**
 * service class for organization management.
 *
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class OrganizationService extends BaseService {

    /**
     * default constructor.
     *
     * @param client gogs client.
     */
    public OrganizationService(final GogsClient client) {
        super(client);
    }

    /**
     * get organization list of signed in user.
     * <p>
     * GET /api/v1/user/orgs
     *
     * @return list of organizations.
     */
    public List<Organization> listOrganisations() {
        List<Organization> list = getClient().get(new GenericType<List<Organization>>() {
        }, "user", "orgs");

        return list != null ? list : Collections.emptyList();
    }

    /**
     * get organization list of specified user.
     * <p>
     * GET /api/v1/users/:username/orgs
     *
     * @param username name of user.
     * @return list of organizations.
     */
    public List<Organization> listOrganisations(String username) {
        List<Organization> list = getClient().get(new GenericType<List<Organization>>() {
        }, "users", username, "orgs");

        return list != null ? list : Collections.emptyList();
    }

    /**
     * get specified organization.
     * <p>
     * GET /api/v1/orgs/:orgname
     *
     * @param organizationName name of organization.
     * @return organization.
     */
    public Organization getOrganization(String organizationName) {
        return getClient().get(Organization.class, "orgs", organizationName);
    }

    /**
     * update organization information.
     * <p>
     * PATCH /api/v1/orgs/:orgname
     *
     * @param organizationName name of organization.
     * @param organization     organization.
     * @return organization.
     */
    public Organization updateOrganization(String organizationName, Organization organization) {
        return getClient().patch(Organization.class, organization, "orgs", organizationName);
    }

    /**
     * get list of teams from organization.
     * <p>
     * GET /api/v1/orgs/:orgname/teams
     *
     * @param organizationName name of organization.
     * @return list of teams.
     */
    public List<Team> listTeams(String organizationName) {
        List<Team> list = getClient().get(new GenericType<List<Team>>() {
        }, "orgs", organizationName, "teams");

        return list != null ? list : Collections.emptyList();
    }
}
