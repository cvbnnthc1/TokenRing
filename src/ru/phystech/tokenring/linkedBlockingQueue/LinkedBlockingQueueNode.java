package ru.phystech.tokenring.linkedBlockingQueue;

import ru.phystech.tokenring.DataPackage;
import ru.phystech.tokenring.Node;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingQueueNode extends Node {
    private final Queue<DataPackage> bufferStack = new LinkedBlockingQueue<>();

    LinkedBlockingQueueNode(int nodeId, LinkedBlockingQueueProcessor processor) {
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
                DataPackage curPackage = bufferStack.remove();
                addTime(curPackage.getData());
                next.receivePackage(curPackage);
                counter++;
            }
        }
        workTime = System.currentTimeMillis() - workTime;
    }
}
