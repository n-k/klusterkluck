package de.ayesolutions.gogs.client;

import de.ayesolutions.gogs.client.model.AccessToken;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Map;

/**
 * Gogs HTTP client for Go Git Service.
 *
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class GogsClient {

    private static final Logger LOG = LoggerFactory.getLogger(GogsClient.class);

    public static final int HTTP_OK = 200;

    public static final int HTTP_CREATED = 201;

    public static final int HTTP_NO_CONTENT = 204;

    public static final int HTTP_NOT_FOUND = 404;

    private URI apiUri;

    private Client client;

    private AccessToken accessToken;

    /**
     * default constructor.
     *
     * @param uri uri to your gogs instance. (with /api/v1).
     */
    public GogsClient(final URI uri) {
        this(uri, null);
    }

    /**
     * default constructor.
     *
     * @param uri      uri to your gogs instance. (with /api/v1).
     * @param username http username (fallback for token).
     * @param password http password (fallback for token).
     * @param token    application access token.
     */
    public GogsClient(final URI uri, final String username, final String password, final String token) {
        this(uri, new AccessToken(null, token, username, password));
    }

    /**
     * default constructor.
     *
     * @param uri         uri to your gogs instance. (with /api/v1)
     * @param accessToken access token.
     */
    public GogsClient(final URI uri, final AccessToken accessToken) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig
                .property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true)
                .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);
        this.client = ClientBuilder.newClient(clientConfig);
        this.apiUri = uri;
        this.accessToken = accessToken;
    }

    /**
     * send GET request and deserialize result to type class.
     *
     * @param clazz class for json deserialization.
     * @param path  rest path.
     * @param <T>   type for result.
     * @return result instance or null if not found (404)
     */
    public <T> T get(Class<T> clazz, String... path) {
        return get(clazz, null, path);
    }

    /**
     * send GET request and deserialize result to type class.
     *
     * @param clazz      class for json deserialization.
     * @param path       rest path.
     * @param parameters url parameters.
     * @param <T>        type for result.
     * @return result instance or null if not found (404)
     */
    public <T> T get(Class<T> clazz, Map<String, String> parameters, String... path) {
        return request("GET", clazz, null, parameters, path);
    }

    /**
     * send GET request and deserialize result to type class.
     *
     * @param clazz class for json deserialization.
     * @param path  rest path.
     * @param <T>   type for result.
     * @return result instance or null if not found (404)
     */

    public <T> T get(GenericType<T> clazz, String... path) {
        return get(clazz, null, path);
    }

    /**
     * send GET request and deserialize result to type class.
     *
     * @param clazz      class for json deserialization.
     * @param path       rest path.
     * @param parameters url parameters.
     * @param <T>        type for result.
     * @return result instance or null if not found (404)
     */
    public <T> T get(GenericType<T> clazz, Map<String, String> parameters, String... path) {
        return request("GET", clazz, null, parameters, path);
    }

    /**
     * send PUT request with data and deserialize result to type class.
     *
     * @param clazz class for json deserialization.
     * @param path  rest path.
     * @param data  data to send.
     * @param <T>   type for result.
     * @return result instance or null if not found (404)
     */
    public <T> T put(Class<T> clazz, Object data, String... path) {
        return request("PUT", clazz, data,
                null, path);
    }

    /**
     * send PUT request with data and deserialize result to type class.
     *
     * @param clazz class for json deserialization.
     * @param path  rest path.
     * @param data  data to send.
     * @param <T>   type for result.
     * @return result instance or null if not found (404)
     */
    public <T> T put(GenericType<T> clazz, Object data, String... path) {
        return request("PUT", clazz, data, null, path);
    }

    /**
     * send POST request with data and deserialize result to type class.
     *
     * @param clazz class for json deserialization.
     * @param path  rest path.
     * @param data  data to send.
     * @param <T>   type for result.
     * @return result instance or null if not found (404)
     */
    public <T> T post(GenericType<T> clazz, Object data, String... path) {
        return request("POST", clazz, data, null, path);
    }

    /**
     * send POST request with data and deserialize result to type class.
     *
     * @param clazz class for json deserialization.
     * @param path  rest path.
     * @param data  data to send.
     * @param <T>   type for result.
     * @return result instance or null if not found (404)
     */
    public <T> T post(Class<T> clazz, Object data, String... path) {
        return request("POST", clazz, data, null, path);
    }

    /**
     * send DELETE request.
     *
     * @param path rest path.
     */
    public void delete(String... path) {
        request("DELETE", Void.class, null, null, path);
    }

    /**
     * send DELETE request with data.
     *
     * @param data data to send.
     * @param path rest path.
     */
    public void delete(Object data, String... path) {
        request("DELETE", Void.class, data, null, path);
    }

    /**
     * send PATCH request with data and deserialize result to type class.
     *
     * @param clazz class for json deserialization.
     * @param path  rest path.
     * @param data  data to send.
     * @param <T>   type for result.
     * @return result instance or null if not found (404)
     */
    public <T> T patch(Class<T> clazz, Object data, String... path) {
        return request("PATCH", clazz, data, null, path);
    }

    /**
     * send HTTP request with data and deserialize result to type class.
     *
     * @param method     http method.
     * @param clazz      class for json deserialization.
     * @param path       rest path.
     * @param parameters url parameters.
     * @param data       data to send.
     * @param <T>        type for result.
     * @return result instance or null if not found (404)
     */
    public <T> T request(String method, Class<T> clazz, Object data, Map<String, String> parameters, String... path) {
        Response response = callRequest(method, data, parameters, path);
        if (!handleStatusCode(response, String.join("/", path))) {
            return null;
        }
        return response.readEntity(clazz);
    }

    /**
     * send HTTP request with data and deserialize result to type class.
     *
     * @param method     http method.
     * @param clazz      class for json deserialization.
     * @param path       rest path.
     * @param parameters url parameters.
     * @param data       data to send.
     * @param <T>        type for result.
     * @return result instance or null if not found (404)
     */
    public <T> T request(String method, GenericType<T> clazz, Object data, Map<String, String> parameters,
                         String... path) {
        Response response = callRequest(method, data, parameters, path);
        if (!handleStatusCode(response, String.join("/", path))) {
            return null;
        }
        return response.readEntity(clazz);
    }

    /**
     * build http call with java ws rs.
     *
     * @param method     http method.
     * @param data       data to send.
     * @param parameters url parameters.
     * @param path       rest path.
     * @return response.
     */
    public Response callRequest(String method, Object data, Map<String, String> parameters, String... path) {
        WebTarget webTarget = client.target(apiUri);

        // set rest path
        for (String part : path) {
            webTarget = webTarget.path(part);
        }

        // set http parameters
        if (parameters != null) {
            for (String key : parameters.keySet()) {
                webTarget = webTarget.queryParam(key, parameters.get(key));
            }
        }

        // set authorization token
        Invocation.Builder builder = webTarget.request();
        if (getAccessToken() != null) {
            builder = builder.header("Authorization", getAccessToken().getTokenAuthorization());
        }

        LOG.debug("call service: " + method + " " + apiUri.toString() + "/" + String.join("/", path));

        // handle methods
        Response response;
        switch (method) {
            case "GET":
                response = builder.get();
                break;
            case "POST":
                response = builder.post(Entity.json(data));
                break;
            case "PATCH":
                response = builder.method("PATCH", Entity.json(data));
                break;
            case "DELETE":
                if (data == null) {
                    response = builder.delete();
                } else {
                    response = builder.method("DELETE", Entity.json(data));
                }
                break;
            case "PUT":
                response = builder.put(Entity.json(data));
                break;
            default:
                throw new GogsClientException("unsupported http method");
        }

        return response;
    }

    /**
     * handle http result.
     *
     * @param response response from server.
     * @param endpoint rest path.
     * @return true if http code 2xx otherwise false or exception.
     */
    public boolean handleStatusCode(Response response, String endpoint) {
        // handle http code
        switch (response.getStatus()) {
            case HTTP_OK:
            case HTTP_CREATED:
            case HTTP_NO_CONTENT:
                LOG.debug("call service: " + apiUri.toString() + "/"
                        + endpoint + " success " + response.getStatus());
                break;
            case HTTP_NOT_FOUND:
                return false;
            default:
                String result = response.readEntity(String.class);
                LOG.error("call service: " + apiUri.toString() + "/" + endpoint
                        + " failed " + response.getStatus());
                if (!result.isEmpty()) {
                    LOG.error(result);
                }
                throw new GogsClientException("communication error " + response.getStatus() + System.lineSeparator()
                        + response.readEntity(String.class));
        }

        return true;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public Client getClient() {
        return client;
    }

    public URI getApiUri() {
        return apiUri;
    }
}
