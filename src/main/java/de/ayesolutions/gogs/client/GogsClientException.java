package de.ayesolutions.gogs.client;

/**
 * default gogs service exception.
 *
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class GogsClientException extends RuntimeException {

    /**
     * default constructor.
     *
     * @param message error message.
     */
    public GogsClientException(final String message) {
        super(message);
    }
}
