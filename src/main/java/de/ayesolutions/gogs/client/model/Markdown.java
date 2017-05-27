package de.ayesolutions.gogs.client.model;

/**
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class Markdown {

    private String text;

    private String mode;

    private String context;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
