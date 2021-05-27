package ru.phystech.tokenring.blockingQueue;

import ru.phystech.tokenring.DataPackage;
import ru.phystech.tokenring.Node;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class ArrayBlockingQueueNode extends Node  {
    private final Queue<DataPackage> bufferStack = new ArrayBlockingQueue<>(100);

    ArrayBlockingQueueNode(int nodeId, ArrayBlockingQueueRingProcessor processor) {
        super(nodeId, processor);
    }

    public void receivePackage(DataPackage dataPackage) {
        dataPackage.time = System.nanoTime();
        bufferStack.add(dataPackage);
    }

    @Override
    public void run() {
        workTime = System.currentTimeMillis();
        while (turnOn) {
            if (!bufferStack.isEmpty()) {
                DataPackage curPackage = bufferStack.poll();
                latencies.add(System.nanoTime() - curPackage.time);
                next.receivePackage(curPackage);
                counter++;
            }
        }
        workTime = System.currentTimeMillis() - workTime;
    }
}
