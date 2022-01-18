package com.htec.shelffunction.util;

public enum FunctionEvents {

    UPLOAD("upload"),
    DOWNLOAD("download"),
    DELETE("delete"),
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    SYNCHRONIZED("synchronized");

    private final String event;

    FunctionEvents(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }
}
