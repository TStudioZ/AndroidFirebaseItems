package com.tstudioz.androidfirebaseitems.data;

public class Status {

    public static final int SUCCESS = 1;
    public static final int ERROR = 2;
    public static final int WORKING = 3;

    public final int status;

    public boolean isSuccess() {
        return status == SUCCESS;
    }

    public boolean isError() {
        return status == ERROR;
    }

    public boolean isWorking() {
        return status == WORKING;
    }

    private Status(int status) {
        this.status = status;
    }

    public static Status success() {
        return new Status(SUCCESS);
    }

    public static Status error() {
        return new Status(ERROR);
    }

    public static Status working() {
        return new Status(WORKING);
    }
}
