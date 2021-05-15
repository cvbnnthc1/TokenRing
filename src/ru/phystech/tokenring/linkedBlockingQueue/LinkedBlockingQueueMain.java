package ru.phystech.tokenring.linkedBlockingQueue;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LinkedBlockingQueueMain {

    public static void main(String[] args) {
        warmup();
        long[][] resLatencies = new long[10][10];
        long[][] resThroughputs = new long[10][10];
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= i; j++) {
                testTokenRing(i, j, resLatencies, resThroughputs);
            }
        }
        printArray(resLatencies);
        printArray(resThroughputs);
    }

    private static void printArray(long[][] resLatencies) {
        for (int i = 0; i < resLatencies.length; i++) {
            for (int j = 0; j < resLatencies[0].length; j++) {
                System.out.print(resLatencies[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println("_______________");
    }

    private static void testTokenRing(int nodesAmount, int dataAmount, long[][] resLatencies, long[][] resThroughputs) {
        List<Double> latencies = new ArrayList<>();
        List<Double> throughputs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LinkedBlockingQueueProcessor processor = new LinkedBlockingQueueProcessor(nodesAmount, dataAmount, new File("logPath"));
            processor.startProcessing();
            processor.doSleep(300);
            processor.stop();
            latencies.add(processor.getLatency());
            throughputs.add(processor.getThroughput());
        }
        double avgLatency = latencies.stream().reduce(Double::sum).get() / latencies.size();
        double avgThroughput = throughputs.stream().reduce(Double::sum).get() / latencies.size();
        resLatencies[nodesAmount - 1][dataAmount - 1] = Math.round(avgLatency);
        resThroughputs[nodesAmount - 1][dataAmount - 1] = Math.round(avgThroughput);
        System.out.println("nodes amount " + nodesAmount + " data amount " + dataAmount);
        System.out.println("latency " + avgLatency);
        System.out.println("throughput  " + avgThroughput);
        System.out.println("_________________");
    }

    private static void warmup() {
        LinkedBlockingQueueProcessor processor = new LinkedBlockingQueueProcessor(5, 5, new File("logPath"));
        processor.startProcessing();
        processor.doSleep(50);
        processor.stop();
    }
}

