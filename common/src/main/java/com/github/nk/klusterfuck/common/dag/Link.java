package com.github.nk.klusterfuck.common.dag;

import java.io.Serializable;

/**
 * Created by nipunkumar on 20/05/17.
 */
public class Link implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String from;
    private String to;

    public Link() {}

    public Link(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Link clone() {
        Link l = new Link();
        l.setFrom(from);
        l.setTo(to);
        return l;
    }
}
