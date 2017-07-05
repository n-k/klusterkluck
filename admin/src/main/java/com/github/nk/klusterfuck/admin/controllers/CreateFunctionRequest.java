package com.github.nk.klusterfuck.admin.controllers;

import com.github.nk.klusterfuck.admin.model.FunctionType;

/**
 * Created by nk on 3/6/17.
 */
public class CreateFunctionRequest {

    private String name;
    private FunctionType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FunctionType getType() {
        return type;
    }

    public void setType(FunctionType type) {
        this.type = type;
    }
}
