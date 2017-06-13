package com.github.nk.klusterfuck.common;

/**
 * Created by nipunkumar on 02/06/17.
 */
public class FunctionConfig {
    private String command;
    private String[] executables;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String[] getExecutables() {
        return executables;
    }

    public void setExecutables(String[] executables) {
        this.executables = executables;
    }
}
