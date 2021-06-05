package ru.phystech.tokenring;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TokenRingTester {

    protected static void printArray(long[][] resLatencies) {
        for (long[] resLatency : resLatencies) {
            for (int j = 0; j < resLatencies[0].length; j++) {
                System.out.print(resLatency[j] + "\t");
            }
            System.out.println();
        }
        System.out.println("_______________");
    }

    protected static void testTokenRing(Processor processor, long[][] resLatencies, long[][] resThroughputs,
                                        int iIndex, int jIndex) {
        List<Double> latencies = new ArrayList<>();
        List<Double> throughputs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            processor.startProcessing();
            processor.doSleep(5000);
            processor.stop();
            latencies.add(processor.getLatency());
            throughputs.add(processor.getThroughput());
            processor.reload();
        }
        double medianLatency = latencies
                .stream()
                .sorted()
                .collect(Collectors.toList())
                .get(latencies.size() / 2);
        double medianThroughput = throughputs
                .stream()
                .sorted()
                .collect(Collectors.toList())
                .get(throughputs.size() / 2);
        resLatencies[iIndex][jIndex] = Math.round(medianLatency);
        resThroughputs[iIndex][jIndex] = Math.round(medianThroughput);
        System.out.println("nodes amount " + processor.getNodesAmount() + " data amount " + processor.getDataAmount());
        System.out.println("latency " + medianLatency);
        System.out.println("throughput  " + medianThroughput);
        System.out.println("_________________");
    }

    protected static void testTokenRing(Processor processor, double[][] resLatencies, double[][] resThroughputs,
                                        int iIndex, int jIndex) {
        List<Double> latencies = new ArrayList<>();
        List<Double> throughputs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            processor.startProcessing();
            processor.doSleep(5000);
            processor.stop();
            latencies.add(processor.getLatency());
            throughputs.add(processor.getThroughput());
            processor.reload();
        }
        double medianLatency = latencies
                .stream()
                .sorted()
                .collect(Collectors.toList())
                .get(latencies.size() / 2);
        double medianThroughput = throughputs
                .stream()
                .sorted()
                .collect(Collectors.toList())
                .get(throughputs.size() / 2);
        resLatencies[iIndex][jIndex] = medianLatency;
        resThroughputs[iIndex][jIndex] = medianThroughput;
        System.out.println("nodes amount " + processor.getNodesAmount() + " data amount " + processor.getDataAmount());
        System.out.println("latency " + medianLatency);
        System.out.println("throughput  " + medianThroughput);
        System.out.println("_________________");
    }

    protected static void warmup(Processor processor) {
        processor.startProcessing();
        processor.doSleep(50);
        processor.stop();
    }

    protected static String convertToCSV(long[] data) {
        return Arrays.stream(data)
                .mapToObj(Long::toString)
                .collect(Collectors.joining(";"));
    }

    protected static String convertToCSV(double[] data) {
        return Arrays.stream(data)
                .mapToObj(Double::toString)
                .collect(Collectors.joining(";"));
    }

    protected static void createCsvFile(long[][] data, String name) throws IOException {
        File csvOutputFile = new File(name);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            Arrays.stream(data)
                    .map(TokenRingTester::convertToCSV)
                    .forEach(pw::println);
        }
    }

    protected static void createCsvFile(double[][] data, String name) throws IOException {
        File csvOutputFile = new File(name);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            Arrays.stream(data)
                    .map(TokenRingTester::convertToCSV)
                    .forEach(pw::println);
        }
    }
}
