package com.github.nk.klusterfuck.common.dag;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nipunkumar on 20/05/17.
 */
public class Node<T> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private T data;
    private Map<String, Object> uiProps = new HashMap<>();

    public Node() {}

    public Node(String id, T data) {
        this.id = id;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Map<String, Object> getUiProps() {
        return uiProps;
    }

    public void setUiProps(Map<String, Object> uiProps) {
        this.uiProps = uiProps;
    }

    public Node<T> clone() {
        Node<T> n = new Node<T>();
        n.setData(data);
        n.setId(id);
        n.setUiProps(uiProps);
        return n;
    }
}
