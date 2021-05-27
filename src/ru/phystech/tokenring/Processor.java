package ru.phystech.tokenring;

import java.util.ArrayList;
import java.util.List;

public abstract class Processor {
    protected final int nodesAmount;
    protected final int dataAmount;
    protected List<Node> nodeList;
    protected List<Long> timeList;

    public Processor(int nodesAmount, int dataAmount) {
        this.nodesAmount = nodesAmount;
        this.dataAmount = dataAmount;
        timeList = new ArrayList<>();
        init();
    }

    protected abstract void init();

    public void startProcessing(){
        for (Node node: nodeList) {
            Thread curThread = new Thread(node);
            curThread.start();
        }
    }

    public double getLatency() {
        double result = 0d;
        long sizes = 0L;
        for (Node node: nodeList) {
            sizes += node.getLatencies().size();
        }
        for (Node node: nodeList) {
            result += node.getAvgLatency() / sizes * node.getLatencies().size();
        }
        return result;
    }

    public double getThroughput() {
        double result = 0d;
        long sizes = 0L;
        for (Node node: nodeList) {
            sizes += node.getLatencies().size();
        }
        for (Node node: nodeList) {
            result += node.getAvgThroughput() / sizes * node.getLatencies().size();
        }
        return result;
    }

    private void doJoin(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void doSleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        for (Node node: nodeList) {
            node.off();
        }
        for (Node node: nodeList) {
            doJoin(node);
        }
    }

    public void reload() {
        timeList = new ArrayList<>();
        init();
    }

    public int getNodesAmount() {
        return nodesAmount;
    }

    public int getDataAmount() {
        return dataAmount;
    }
}
