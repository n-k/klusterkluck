package de.ayesolutions.gogs.client.service;

import de.ayesolutions.gogs.client.GogsClient;
import de.ayesolutions.gogs.client.model.Markdown;

/**
 * service class for miscellaneous things.
 *
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class MiscellaneousService extends BaseService {

    /**
     * default constructor.
     *
     * @param client gogs client.
     */
    public MiscellaneousService(final GogsClient client) {
        super(client);
    }

    /**
     * render markdown content to html.
     * <p>
     * GET /api/v1/markdown
     *
     * @param markdown markdown definition.
     * @return html rendered markdown.
     */
    public String renderMarkdown(Markdown markdown) {
        return getClient().post(String.class, markdown, "markdown");
    }

    /**
     * render markdown content to html.
     * <p>
     * GET /api/v1/markdown/raw
     *
     * @param data text markdown.
     * @return html rendered markdown.
     */
    public String renderMarkdownRaw(String data) {
        return getClient().post(String.class, data, "markdown", "raw");
    }
}
