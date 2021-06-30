package com.yannick.radioo;

public enum SchedulerStatus {

    WAITING("waiting"), DOWNLOADING("downloading"), PENDING("pending"), FINISHED("finished"), ABORTED("aborted");

    private String status;

    SchedulerStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
