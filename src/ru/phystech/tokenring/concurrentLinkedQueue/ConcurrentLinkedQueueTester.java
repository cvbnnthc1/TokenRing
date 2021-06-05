package ru.phystech.tokenring.concurrentLinkedQueue;

import ru.phystech.tokenring.TokenRingTester;

import java.io.IOException;
import java.util.List;

public class ConcurrentLinkedQueueTester extends TokenRingTester {

    public static void main(String[] args) throws IOException {
        warmup(new ConcurrentLinkedQueueProcessor(5, 5));
        generalDepedency();
        dataAmountDependency();
        nodeAmountDependency();
    }

    private static void generalDepedency() throws IOException {
        List<Integer> nodeAmounts = List.of(2, 4, 6, 8, 10);
        List<Integer> dataAmounts = List.of(1, 2, 4, 8, 16, 32);
        long[][] resLatencies = new long[nodeAmounts.size()][dataAmounts.size()];
        long[][] resThroughputs = new long[nodeAmounts.size()][dataAmounts.size()];
        for (int i = 0; i < nodeAmounts.size(); i++) {
            for (int j = 0; j < dataAmounts.size(); j++) {
                testTokenRing(new ConcurrentLinkedQueueProcessor(nodeAmounts.get(i), dataAmounts.get(j)),
                        resLatencies, resThroughputs, i, j);
            }
        }
        printArray(resLatencies);
        printArray(resThroughputs);
        createCsvFile(resLatencies, "ConcurrentLinkedQueueLatencies.csv");
        createCsvFile(resThroughputs, "ConcurrentLinkedQueueThroughputs.csv");
    }

    static void dataAmountDependency() throws IOException {
        int nodeAmount = 8;
        List<Integer> dataAmounts = List.of(2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048);
        long[][] resLatencies = new long[1][dataAmounts.size()];
        long[][] resThroughputs = new long[1][dataAmounts.size()];
        for (int j = 0; j < dataAmounts.size(); j++) {
            testTokenRing(new ConcurrentLinkedQueueProcessor(nodeAmount, dataAmounts.get(j)),
                    resLatencies, resThroughputs, 0, j);
        }
        createCsvFile(resLatencies, "ConcurrentLinkedQueueLatenciesDataAmountDependency.csv");
        createCsvFile(resThroughputs, "ConcurrentLinkedQueueThroughputsDataAmountDependency.csv");
    }

    static void nodeAmountDependency() throws IOException {
        List<Integer> nodeAmounts = List.of(2, 4, 8, 16, 32, 64, 128, 256, 512);
        double[][] resLatencies = new double[1][nodeAmounts.size()];
        double[][] resThroughputs = new double[1][nodeAmounts.size()];
        for (int j = 0; j < nodeAmounts.size(); j++) {
            testTokenRing(new ConcurrentLinkedQueueProcessor(nodeAmounts.get(j), nodeAmounts.get(j) - 1),
                    resLatencies, resThroughputs, 0, j);
        }
        createCsvFile(resLatencies, "ConcurrentLinkedQueueLatenciesNodeAmountDependency.csv");
        createCsvFile(resThroughputs, "ConcurrentLinkedQueueThroughputsNodeAmountDependency.csv");
    }
}

