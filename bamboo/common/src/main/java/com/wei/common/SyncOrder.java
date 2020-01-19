package com.wei.common;

import java.util.concurrent.locks.ReentrantLock;

public class SyncOrder {

    public synchronized void testOne() {
        System.out.println("SyncMethod testOne");
    }

    private void testTwo() {
        synchronized (this) {
            System.out.println("SyncMethod testTwo");
        }
    }

}
