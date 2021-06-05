package ru.phystech.tokenring.blockingQueue;

import ru.phystech.tokenring.TokenRingTester;

import java.io.IOException;
import java.util.List;

public class ArrayBlockingQueueTester extends TokenRingTester {

    public static void main(String[] args) throws IOException {
        warmup(new ArrayBlockingQueueProcessor(5, 5));
        generalDependency();
        dataAmountDependency();
        nodeAmountDependency();
    }

    private static void generalDependency() throws IOException {
        List<Integer> nodeAmounts = List.of(2, 4, 6, 8, 10);
        List<Integer> dataAmounts = List.of(1, 2, 4, 8, 16, 32);
        long[][] resLatencies = new long[nodeAmounts.size()][dataAmounts.size()];
        long[][] resThroughputs = new long[nodeAmounts.size()][dataAmounts.size()];
        for (int i = 0; i < nodeAmounts.size(); i++) {
            for (int j = 0; j < dataAmounts.size(); j++) {
                testTokenRing(new ArrayBlockingQueueProcessor(nodeAmounts.get(i), dataAmounts.get(j)),
                        resLatencies, resThroughputs, i, j);
            }
        }

        printArray(resLatencies);
        printArray(resThroughputs);
        createCsvFile(resLatencies, "ArrayBlockingQueueLatencies.csv");
        createCsvFile(resThroughputs, "ArrayBlockingQueueThroughputs.csv");
    }

    static void dataAmountDependency() throws IOException {
        int nodeAmount = 8;
        List<Integer> dataAmounts = List.of(2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048);
        long[][] resLatencies = new long[1][dataAmounts.size()];
        long[][] resThroughputs = new long[1][dataAmounts.size()];
        for (int j = 0; j < dataAmounts.size(); j++) {
            testTokenRing(new ArrayBlockingQueueProcessor(nodeAmount, dataAmounts.get(j)),
                    resLatencies, resThroughputs, 0, j);
        }
        createCsvFile(resLatencies, "ArrayBlockingQueueLatenciesDataAmountDependency.csv");
        createCsvFile(resThroughputs, "ArrayBlockingQueueThroughputsDataAmountDependency.csv");
    }


    static void nodeAmountDependency() throws IOException {
        List<Integer> nodeAmounts = List.of(2, 4, 8, 16, 32, 64, 128, 256, 512);
        double[][] resLatencies = new double[1][nodeAmounts.size()];
        double[][] resThroughputs = new double[1][nodeAmounts.size()];
        for (int j = 0; j < nodeAmounts.size(); j++) {
            testTokenRing(new ArrayBlockingQueueProcessor(nodeAmounts.get(j), nodeAmounts.get(j) - 1),
                    resLatencies, resThroughputs, 0, j);
        }
        createCsvFile(resLatencies, "ArrayBlockingQueueLatenciesNodeAmountDependency.csv");
        createCsvFile(resThroughputs, "ArrayBlockingQueueThroughputsNodeAmountDependency.csv");
    }

}

