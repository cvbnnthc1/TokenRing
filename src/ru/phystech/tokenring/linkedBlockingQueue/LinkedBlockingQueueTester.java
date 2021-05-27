package ru.phystech.tokenring.linkedBlockingQueue;

import ru.phystech.tokenring.TokenRingTester;

public class LinkedBlockingQueueTester extends TokenRingTester {

    public static void main(String[] args) {
        warmup(new LinkedBlockingQueueProcessor(5, 5));
        long[][] resLatencies = new long[10][10];
        long[][] resThroughputs = new long[10][10];
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= i; j++) {
                testTokenRing(new LinkedBlockingQueueProcessor(i, j), resLatencies, resThroughputs);
            }
        }
        printArray(resLatencies);
        printArray(resThroughputs);
    }
}

