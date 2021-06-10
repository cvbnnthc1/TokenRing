package ru.phystech.tokenring.Volatile;

import ru.phystech.tokenring.DataPackage;
import ru.phystech.tokenring.Node;

public class VolatileNode extends Node {
    private volatile DataPackage dataPackage;

    VolatileNode(int nodeId, VolatileProcessor processor) {
        super(nodeId, processor);
        dataPackage = null;
    }

    @Override
    public void receivePackage(DataPackage dataPackage) {
        this.dataPackage = dataPackage;
    }

    @Override
    public void run() {
        workTime = System.currentTimeMillis();
        while (turnOn) {
            if (dataPackage!= null) {
                VolatileNode curNext = (VolatileNode) next;
                while (curNext.dataPackage != null) {
                    if (!turnOn) return;
                    Thread.onSpinWait();
                }
                next.receivePackage(dataPackage);
                addTime(dataPackage.getData());
                counter++;
                dataPackage = null;

            }
        }
        workTime = System.currentTimeMillis() - workTime;
    }
}
