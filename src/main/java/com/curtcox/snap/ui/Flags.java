package com.curtcox.snap.ui;

import com.curtcox.snap.model.Packet.*;

import java.util.ArrayList;
import java.util.List;

public final class Flags {

    public final String[] args;
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

    public static class Builder {

        String topic;
        String title;
        String name;
        String message;
        String messages;
        Integer time;

        public Builder topic(String value) {
            topic = value;
            return this;
        }

        public Builder title(String value) {
            title = value;
            return this;
        }

        public Builder name(String value) {
            name = value;
            return this;
        }

        public Builder message(String value) {
            message = value;
            return this;
        }

        public Builder messages(String value) {
            messages = value;
            return this;
        }

        public Builder time(int value) {
            time = value;
            return this;
        }
        public Flags build() {
            List<String> list = new ArrayList<>();
            if (topic!=null)    add(list,"topic",topic);
            if (title!=null)    add(list,"title",title);
            if (name!=null)     add(list,"name",name);
            if (message!=null)  add(list,"message",message);
            if (messages!=null) add(list,"messages",messages);
            if (time!=null)     add(list,"time",time.toString());
            return new Flags(list.toArray(new String[0]));
        }

        private void add(List<String> list, String name, String value) {
            list.add(name);
            list.add(value);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
