package com.github.nk.klusterfuck.common.dag;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
    private Map<String, Object> uiProps = new HashMap<>();

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

	public Map<String, Object> getUiProps() {
		return uiProps;
	}

	public void setUiProps(Map<String, Object> uiProps) {
		this.uiProps = uiProps;
	}

	public Link clone() {
        Link l = new Link();
        l.setFrom(from);
        l.setTo(to);
        l.setUiProps(uiProps);
        return l;
    }
}
