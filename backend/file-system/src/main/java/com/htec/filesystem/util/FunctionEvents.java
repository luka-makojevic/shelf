package com.htec.filesystem.util;

public enum FunctionEvents {

    UPLOAD("upload", 1L),
    DOWNLOAD("download", 2L),
    DELETE("delete", 3L),
    DAILY("daily", 4L),
    WEEKLY("weekly", 5L),
    MONTHLY("monthly", 6L),
    SYNCHRONIZED("synchronized", 7L);

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