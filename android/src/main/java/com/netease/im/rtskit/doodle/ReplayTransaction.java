package com.netease.im.rtskit.doodle;

public class ReplayTransaction {

    private long time;

    private Transaction mTransaction;

    public long getTime() {
        return time;
    }

    public Transaction getTransaction() {
        return mTransaction;
    }

    public ReplayTransaction(long time, Transaction transaction) {
        this.time = time;
        mTransaction = transaction;
    }
}
