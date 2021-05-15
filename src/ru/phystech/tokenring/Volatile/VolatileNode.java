package ru.phystech.tokenring.Volatile;

import ru.phystech.tokenring.DataPackage;

import java.util.ArrayList;
import java.util.List;


public class VolatileNode extends Thread {
    private final int nodeId;
    private volatile boolean turnOn;
    private VolatileNode next;
    private final VolatileProcessor processor;
    private int counter;
    private long workTime;
    private List<Long> latencies;
    volatile boolean state;
    private volatile DataPackage dataPackage;

    VolatileNode(int nodeId, VolatileProcessor processor) {
        this.nodeId = nodeId;
        this.processor = processor;
        this.turnOn = true;
        this.latencies = new ArrayList<>();
        state = true;
        dataPackage = null;
    }

    public void setNext(VolatileNode next) {
        this.next = next;
    }


    public long getId() {
        return nodeId;
    }

    public void off() {
        turnOn = false;
        //processor.getLogger().log(Level.INFO, LocalTime.now() + " id: " + nodeId + " off");

    }


    public void receivePackage(DataPackage dataPackage) {
        dataPackage.time = System.nanoTime();
        this.dataPackage = dataPackage;
    }

    @Override
    public void run() {
        workTime = System.currentTimeMillis();
        while (turnOn) {
            if (dataPackage!= null) {
                while (!next.state) {
                    if (!turnOn) return;
                    Thread.onSpinWait();
                }
                latencies.add(System.nanoTime() - dataPackage.time);
                next.receivePackage(dataPackage);
                counter++;
                dataPackage = null;
                state = true;
                //processor.getLogger().log(Level.INFO, LocalTime.now() + " node: " + nodeId + " sent package " + curPackage.getData());
            }
        }
        workTime = System.currentTimeMillis() - workTime;
    }

    public double getAvgLatency() {
        if (turnOn) throw new IllegalStateException("Node must be off");
        if (latencies.size() > 0) {
            double result = 0d;
            for (int i = 0; i < latencies.size(); i++) {
                result += latencies.get(i) / (double) latencies.size();
            }
            return result;
        } else {
            return 0;
        }
    }

    public double getAvgThroughput() {
        if (turnOn) throw new IllegalStateException("Node must be off");
        return counter / (double) workTime * 1000;
    }

    public List<Long> getLatencies() {
        return latencies;
    }
}
