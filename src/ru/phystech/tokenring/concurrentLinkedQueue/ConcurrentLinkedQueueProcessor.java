package ru.phystech.tokenring.concurrentLinkedQueue;

import ru.phystech.tokenring.DataPackage;
import ru.phystech.tokenring.Processor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ConcurrentLinkedQueueProcessor extends Processor {

    ConcurrentLinkedQueueProcessor(int nodesAmount, int dataAmount){
        super(nodesAmount, dataAmount);
    }

    @Override
    protected void init(){
        nodeList = new ArrayList<>();
        for (int i = 0; i < nodesAmount; i++) {
            nodeList.add(new ConcurrentLinkedQueueNode(i, this));
        }
        for (int i = 1; i < nodesAmount; i++) {
            nodeList.get(i - 1).setNext(nodeList.get(i));
        }
        nodeList.get(nodesAmount - 1).setNext(nodeList.get(0));

        for (int i = 0; i < dataAmount; i++) {
            DataPackage dataPackage = new DataPackage(i + "");
            nodeList.get(0).receivePackage(dataPackage);
        }
    }
}

