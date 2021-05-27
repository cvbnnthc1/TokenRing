package ru.phystech.tokenring;

import ru.phystech.tokenring.blockingQueue.ArrayBlockingQueueRingProcessor;

import java.util.ArrayList;
import java.util.List;

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

    protected static void testTokenRing(Processor processor, long[][] resLatencies, long[][] resThroughputs) {
        List<Double> latencies = new ArrayList<>();
        List<Double> throughputs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            processor.startProcessing();
            processor.doSleep(300);
            processor.stop();
            latencies.add(processor.getLatency());
            throughputs.add(processor.getThroughput());
            processor.reload();
        }
        double avgLatency = latencies.stream().reduce(Double::sum).get() / latencies.size();
        double avgThroughput = throughputs.stream().reduce(Double::sum).get() / latencies.size();
        resLatencies[processor.getNodesAmount() - 1][processor.getDataAmount() - 1] = Math.round(avgLatency);
        resThroughputs[processor.getNodesAmount() - 1][processor.getDataAmount() - 1] = Math.round(avgThroughput);
        System.out.println("nodes amount " + processor.getNodesAmount() + " data amount " + processor.getDataAmount());
        System.out.println("latency " + avgLatency);
        System.out.println("throughput  " + avgThroughput);
        System.out.println("_________________");
    }

    protected static void warmup(Processor processor) {
        processor.startProcessing();
        processor.doSleep(50);
        processor.stop();
    }
}
