package ru.phystech.tokenring.Volatile;

import ru.phystech.tokenring.TokenRingTester;
import java.io.IOException;
import java.util.List;

public class VolatileTester extends TokenRingTester {

    public static void main(String[] args) throws IOException {
        warmup(new VolatileProcessor(5, 5));
        generalDependency();
        nodeAmountDependency();
    }

    private static void generalDependency() throws IOException {
        List<Integer> nodeAmounts = List.of(2, 4, 6, 8, 10, 16, 32, 64);
        List<Integer> dataAmounts = List.of(1, 3, 5, 7, 9, 15, 31, 63);
        long[][] resLatencies = new long[nodeAmounts.size()][dataAmounts.size()];
        long[][] resThroughputs = new long[nodeAmounts.size()][dataAmounts.size()];
        for (int i = 0; i < nodeAmounts.size(); i++) {
            for (int j = 0; j < dataAmounts.size() && dataAmounts.get(j) < nodeAmounts.get(i); j++) {
                testTokenRing(new VolatileProcessor(nodeAmounts.get(i), dataAmounts.get(j)),
                        resLatencies, resThroughputs, i, j);
            }
        }
        printArray(resLatencies);
        printArray(resThroughputs);
        createCsvFile(resLatencies, "VolatileLatencies.csv");
        createCsvFile(resThroughputs, "VolatileThroughputs.csv");
    }

    static void nodeAmountDependency() throws IOException {
        List<Integer> nodeAmounts = List.of(2, 4, 8, 16, 32, 64, 128, 256, 512);
        double[][] resLatencies = new double[1][nodeAmounts.size()];
        double[][] resThroughputs = new double[1][nodeAmounts.size()];
        for (int j = 0; j < nodeAmounts.size(); j++) {
            testTokenRing(new VolatileProcessor(nodeAmounts.get(j), nodeAmounts.get(j) - 1),
                    resLatencies, resThroughputs, 0, j);
        }
        createCsvFile(resLatencies, "VolatileLatenciesNodeAmountDependency.csv");
        createCsvFile(resThroughputs, "VolatileThroughputsNodeAmountDependency.csv");
    }

}

