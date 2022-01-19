package com.htec.filesystem.util;

public enum FunctionEvents {

    UPLOAD("UPLOAD", 1L),
    DOWNLOAD("DOWNLOAD", 2L),
    DELETE("DELETE", 3L),
    DAILY("DAILY", 4L),
    WEEKLY("WEEKLY", 5L),
    MONTHLY("MONTHLY", 6L),
    SYNCHRONIZED("SYNCHRONIZED", 7L);

    private final String event;
    private final Long value;

    FunctionEvents(String event, Long value) {
        this.event = event;
        this.value = value;
    }

    public String getEvent() {
        return event;
    }

    public Long getValue() {
        return value;
    }
}