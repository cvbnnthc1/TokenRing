package ru.phystech.tokenring.concurrentLinkedQueue;

import ru.phystech.tokenring.DataPackage;
import ru.phystech.tokenring.Node;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentLinkedQueueNode extends Node {
    private ConcurrentLinkedQueue<DataPackage> bufferStack = new ConcurrentLinkedQueue<>();

    ConcurrentLinkedQueueNode(int nodeId, ConcurrentLinkedQueueProcessor processor) {
        super(nodeId, processor);
    }

    @Override
    public void receivePackage(DataPackage dataPackage) {
        bufferStack.add(dataPackage);
    }

    @Override
    public void run() {
        workTime = System.currentTimeMillis();
        while (turnOn) {
            if (!bufferStack.isEmpty()) {
                DataPackage curPackage = bufferStack.poll();
                addTime(curPackage.getData());
                next.receivePackage(curPackage);
                counter++;
            }
        }
        workTime = System.currentTimeMillis() - workTime;
    }
}
