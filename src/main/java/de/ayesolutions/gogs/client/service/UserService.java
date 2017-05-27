package de.ayesolutions.gogs.client.service;

import de.ayesolutions.gogs.client.GogsClient;
import de.ayesolutions.gogs.client.GogsClientException;
import de.ayesolutions.gogs.client.model.AccessToken;
import de.ayesolutions.gogs.client.model.Email;
import de.ayesolutions.gogs.client.model.EmailList;
import de.ayesolutions.gogs.client.model.PublicKey;
import de.ayesolutions.gogs.client.model.User;
import de.ayesolutions.gogs.client.model.UserSearchResult;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * service class for user management.
 *
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class UserService extends BaseService {

    /**
     * default constructor.
     *
     * @param client gogs client.
     */
    public UserService(final GogsClient client) {
        super(client);
    }

    /**
     * list all user access tokens.
     * <p>
     * GET /api/v1/users/:username/tokens
     *
     * @param username username.
     * @return list of user access tokens.
     */
    public List<AccessToken> listAccessTokens(String username) {
        Response response = getClient().getClient().target(getClient().getApiUri())
                .path("users").path(username).path("tokens")
                .request().header("Authorization", getClient().getAccessToken().getBasicAuthorization())
                .get();

        if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            return Collections.emptyList();
        }

        if (!(response.getStatus() == Response.Status.OK.getStatusCode())) {
            throw new GogsClientException("unknown error");
        }

        return response.readEntity(new GenericType<List<AccessToken>>() {
        });
    }

    /**
     * create new access token for user.
     * <p>
     * POST /api/v1/users/:username/tokens
     *
     * @param username username.
     * @param name     name of access token.
     * @return new generated access token.
     */
    public AccessToken createToken(String username, String name) {
        Response response = getClient().getClient().target(getClient().getApiUri())
                .path("users").path(username).path("tokens")
                .request().header("Authorization", getClient().getAccessToken().getBasicAuthorization())
                .post(Entity.json(new AccessToken(name, null)));

        if (!(response.getStatus() == Response.Status.CREATED.getStatusCode())) {
            throw new GogsClientException("unknown error");
        }

        return response.readEntity(AccessToken.class);
    }

    /**
     * search for users.
     * <p>
     * GET /api/v1/users/search
     *
     * @param query search string.
     * @return list of users.
     */
    public UserSearchResult search(String query) {
        return search(query, 0);
    }

    /**
     * search for users.
     * <p>
     * GET /api/v1/users/search
     *
     * @param query search string.
     * @param limit limit number of search result.
     * @return list of users.
     */
    public UserSearchResult search(String query, int limit) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("q", query);
        parameters.put("limit", String.valueOf(limit));

        return getClient().get(UserSearchResult.class, parameters, "users", "search");
    }

    /**
     * get user info for specified user.
     * <p>
     * GET /api/v1/users/:username
     *
     * @param username name of user.
     * @return user info.
     */
    public User getUser(String username) {
        return getClient().get(User.class, "users", username);
    }

    /**
     * get public key list of specified user.
     * <p>
     * GET /api/v1/users/:username/keys
     *
     * @param username name of user.
     * @return list of public keys.
     */
    public List<PublicKey> listPublicKeys(String username) {
        List<PublicKey> list = getClient().get(new GenericType<List<PublicKey>>() {
        }, "users", username, "keys");

        return list != null ? list : Collections.emptyList();
    }

    /**
     * get user list of users who follows specified user.
     * <p>
     * GET /api/v1/users/:username/followers
     *
     * @param username name of user.
     * @return list of followers.
     */
    public List<User> listFollowers(String username) {
        List<User> list = getClient().get(new GenericType<List<User>>() {
        }, "users", username, "followers");

        return list != null ? list : Collections.emptyList();
    }

    /**
     * get user list of users who follows signed in user.
     * <p>
     * GET /api/v1/user/followers
     *
     * @return list of followers.
     */
    public List<User> listFollowers() {
        List<User> list = getClient().get(new GenericType<List<User>>() {
        }, "user", "followers");

        return list != null ? list : Collections.emptyList();
    }

    /**
     * get user list of specified user that follow others.
     * <p>
     * GET /api/v1/users/following
     *
     * @param username name of user.
     * @return list of users.
     */
    public List<User> listFollowing(String username) {
        List<User> list = getClient().get(new GenericType<List<User>>() {
        }, "users", username, "following");

        return list != null ? list : Collections.emptyList();
    }

    /**
     * get user list of signed in user that follow others.
     * <p>
     * GET /api/v1/user/following
     *
     * @return list of users.
     */
    public List<User> listFollowing() {
        List<User> list = getClient().get(new GenericType<List<User>>() {
        }, "user", "following");

        return list != null ? list : Collections.emptyList();
    }

    /**
     * check if specified user follow another user.
     * <p>
     * GET /api/v1/users/:username/following/:target
     *
     * @param username       name of user to check.
     * @param targetUsername following user to check.
     * @return true if successful.
     */
    public boolean checkFollowing(String username, String targetUsername) {
        return getClient().get(String.class, "users", username, "following", targetUsername) != null;
    }

    /**
     * check if signed in user follow another user.
     * <p>
     * GET /api/v1/user/following/:target
     *
     * @param targetUsername following user to check.
     * @return true if successful.
     */
    public boolean checkFollowing(String targetUsername) {
        return getClient().get(String.class, "user", "following", targetUsername) != null;
    }

    /**
     * get signed in user info.
     * <p>
     * GET /api/v1/user
     *
     * @return user info.
     */
    public User getUser() {
        return getClient().get(User.class, "user");
    }

    /**
     * get registered user emails.
     * <p>
     * GET /api/v1/user/emails
     *
     * @return list of emails.
     */
    public List<Email> listUserEmails() {
        List<Email> list = getClient().get(new GenericType<List<Email>>() {
        }, "user", "emails");

        return list != null ? list : Collections.emptyList();
    }

    /**
     * add new email address and return all registered emails.
     * <p>
     * POST /api/v1/user/emails
     *
     * @param emailList list of emails to add.
     * @return list of emails.
     */
    public List<Email> addEmail(EmailList emailList) {
        List<Email> list = getClient().post(new GenericType<List<Email>>() {
        }, emailList, "user", "emails");

        return list != null ? list : Collections.emptyList();
    }

    /**
     * delete user email.
     * <p>
     * DELETE /api/v1/user/emails
     *
     * @param emailList email list to delete.
     */
    public void deleteEmail(EmailList emailList) {
        getClient().delete(emailList, "user", "emails");
    }

    /**
     * follow specified user.
     * <p>
     * PUT /api/v1/user/following/:target
     *
     * @param username name of user to follow.
     */
    public void follow(String username) {
        getClient().put(Void.class, null, "user", "following", username);
    }

    /**
     * unfollow specified user.
     * <p>
     * DELETE /api/v1/user/following/:target
     *
     * @param username name of user to unfollow.
     */
    public void unfollow(String username) {
        getClient().delete("user", "following", username);
    }

    /**
     * get public key list of signed in user.
     * <p>
     * GET /api/v1/user/keys
     *
     * @return list of public keys.
     */
    public List<PublicKey> listPublicKeys() {
        List<PublicKey> list = getClient().get(new GenericType<List<PublicKey>>() {
        }, "user", "keys");

        return list != null ? list : Collections.emptyList();
    }

    /**
     * add new public key to signed in user.
     * <p>
     * POST /api/v1/user/keys
     *
     * @param publicKey public key.
     * @return added public key.
     */
    public PublicKey addPublicKey(PublicKey publicKey) {
        return getClient().post(PublicKey.class, publicKey, "user", "keys");
    }

    /**
     * get public key of signed in user.
     * <p>
     * GET /api/v1/user/keys/:id
     *
     * @param id public key is.
     * @return public key.
     */
    public PublicKey getPublicKey(String id) {
        return getClient().get(PublicKey.class, "user", "keys", id);
    }

    /**
     * delete public key of signed in user.
     * <p>
     * DELETE /api/v1/user/keys/:id
     *
     * @param publicKeyId public key id for deletion.
     */
    public void deletePublicKey(String publicKeyId) {
        getClient().delete("user", "keys", publicKeyId);
    }
}
