package ru.phystech.tokenring.Volatile;

import ru.phystech.tokenring.DataPackage;
import ru.phystech.tokenring.Node;

public class VolatileNode extends Node {
    volatile boolean state;
    private volatile DataPackage dataPackage;

    VolatileNode(int nodeId, VolatileProcessor processor) {
        super(nodeId, processor);
        state = true;
        dataPackage = null;
    }

    @Override
    public void receivePackage(DataPackage dataPackage) {
        dataPackage.time = System.nanoTime();
        this.dataPackage = dataPackage;
    }

    @Override
    public void run() {
        workTime = System.currentTimeMillis();
        while (turnOn) {
            if (dataPackage!= null) {
                VolatileNode curNext = (VolatileNode) next;
                while (!curNext.state) {
                    if (!turnOn) return;
                    Thread.onSpinWait();
                }
                latencies.add(System.nanoTime() - dataPackage.time);
                next.receivePackage(dataPackage);
                counter++;
                dataPackage = null;
                state = true;
            }
        }
        workTime = System.currentTimeMillis() - workTime;
    }
}
