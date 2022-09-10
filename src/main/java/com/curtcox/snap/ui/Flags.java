package com.curtcox.snap.ui;

import com.curtcox.snap.model.Packet.*;

public final class Flags {

    String[] args;
    String title() {
        return afterOrNull("title");
    }
    String message() {
        return after("message");
    }
    String name() {
        return afterOrNull("name");
    }
    Integer time() {
        String value = afterOrNull("time");
        return value == null ? null : Integer.parseInt(value);
    }
    String[] messages() {
        return after("messages").split(",");
    }

    private String after(String key) {
        String value = afterOrNull(key);
        if (value==null) {
            throw new IllegalArgumentException("Missing " + key);
        }
        return value;
    }

    private String afterOrNull(String key) {
        for (int i=0; i<args.length; i++) {
            if (key.equals(args[i])) {
                return args[i+1];
            }
        }
        return null;
    }

    Topic topic() {
        return new Topic(after("topic"));
    }

    public Flags(String[] args) {
        this.args = args;
    }

    public static Flags from(String[] args) {
        return new Flags(args);
    }
}
