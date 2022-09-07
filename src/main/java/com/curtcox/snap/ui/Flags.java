package com.curtcox.snap.ui;

import com.curtcox.snap.model.Packet.*;

public final class Flags {

    String[] args;
    String title() {
        return after("title");
    }
    String message() {
        return after("message");
    }
    String[] messages() {
        return after("messages").split(",");
    }

    private String after(String key) {
        for (int i=0; i<args.length; i++) {
            if (key.equals(args[i])) {
                return args[i+1];
            }
        }
        throw new IllegalArgumentException("Missing " + key);
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
