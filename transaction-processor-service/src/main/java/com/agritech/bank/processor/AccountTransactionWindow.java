package com.agritech.bank.processor;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AccountTransactionWindow {

    private TreeSet<Long> timestamps;
    private final ReentrantReadWriteLock lock;

    private static final long WINDOW_SIZE_MS = 5 * 60 * 1000;
    private static final long WINDOW_ADVANCE_MS = 60 * 1000;
    private static final long GRACE_PERIOD_MS = 10 * 60 * 1000;
    private static final int FRAUD_THRESHOLD = 3;

    // No-arg constructor for JSON deserialization
    public AccountTransactionWindow() {
        this.timestamps = new TreeSet<>();
        this.lock = new ReentrantReadWriteLock();
    }

    // Your existing methods (fixed)
    public void addTransaction(long timestamp) {
        lock.writeLock().lock();
        try {
            timestamps.add(timestamp);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean checkFraud(long newTransactionTime) {
        lock.readLock().lock();
        try {
            for (int i = 0; i < 5; i++) {
                long windowEnd = newTransactionTime + i * WINDOW_ADVANCE_MS;
                long windowStart = windowEnd - WINDOW_SIZE_MS;

                long count = timestamps.subSet(windowStart, windowEnd + 1).size();

                if (count + 1 >= FRAUD_THRESHOLD) {
                    return true;
                }
            }
            return false;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void cleanup(long currentTime) {
        long cleanupTime = currentTime - GRACE_PERIOD_MS;  // Fixed!

        lock.writeLock().lock();
        try {
            SortedSet<Long> toRemove = timestamps.headSet(cleanupTime);
            toRemove.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    // JSON serialization support
    public Set<Long> getTimestamps() {
        lock.readLock().lock();
        try {
            return new TreeSet<>(timestamps);  // Return copy for safety
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setTimestamps(Set<Long> timestamps) {
        lock.writeLock().lock();
        try {
            this.timestamps.clear();
            if (timestamps != null) {
                this.timestamps.addAll(timestamps);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}