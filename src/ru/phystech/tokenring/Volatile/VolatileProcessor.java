package ru.phystech.tokenring.Volatile;

import ru.phystech.tokenring.DataPackage;
import ru.phystech.tokenring.Processor;
import java.util.ArrayList;

public class VolatileProcessor extends Processor {

    VolatileProcessor(int nodesAmount, int dataAmount){
        super(nodesAmount, dataAmount);
    }

    @Override
    protected void init(){
        nodeList = new ArrayList<>();
        for (int i = 0; i < nodesAmount; i++) {
            nodeList.add(new VolatileNode(i, this));
        }
        for (int i = 1; i < nodesAmount; i++) {
            nodeList.get(i - 1).setNext(nodeList.get(i));
        }
        nodeList.get(nodesAmount - 1).setNext(nodeList.get(0));

        for (int i = 0; i < dataAmount; i++) {
            DataPackage dataPackage = new DataPackage(i + " data");
            nodeList.get(i).receivePackage(dataPackage);
            VolatileNode curNode = (VolatileNode) nodeList.get(i);
        }

    }
}

