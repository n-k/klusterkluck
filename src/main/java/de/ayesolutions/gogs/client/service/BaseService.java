package de.ayesolutions.gogs.client.service;

import de.ayesolutions.gogs.client.GogsClient;

/**
 * base service class.
 *
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class BaseService {

    private GogsClient client;

    /**
     * default constructor.
     *
     * @param client gogs http client.
     */
    protected BaseService(final GogsClient client) {
        this.client = client;
    }

    public GogsClient getClient() {
        return client;
    }
}
