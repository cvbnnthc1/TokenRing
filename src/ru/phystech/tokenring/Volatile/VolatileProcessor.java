package ru.phystech.tokenring.Volatile;

import ru.phystech.tokenring.DataPackage;
import ru.phystech.tokenring.concurrentLinkedQueue.ConcurrentLinkedQueueNode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class VolatileProcessor {

    private final int nodesAmount;
    private final int dataAmount;

    private final File logs;

    private List<VolatileNode> nodeList;

    private final Logger logger;

    final List<Long> timeList;
    final List<Long> timeInBufferList;

    VolatileProcessor(int nodesAmount, int dataAmount, File logs){
        this.nodesAmount = nodesAmount;

        this.dataAmount = dataAmount;

        this.logs = logs;

        logger = Logger.getLogger("ringLogger");
        timeInBufferList = new ArrayList<>();
        timeList = new ArrayList<>();
        /*try {
            FileHandler fhandler = new FileHandler("logs");
            SimpleFormatter sformatter = new SimpleFormatter();
            fhandler.setFormatter(sformatter);
            logger.addHandler(fhandler);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        init();
    }


    private void init(){
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
            nodeList.get(i).state = false;
        }

    }

    public void startProcessing(){
        for (VolatileNode node: nodeList) {
            node.start();
        }


    }

    public double getLatency() {
        double result = 0d;
        long sizes = 0l;
        for (VolatileNode node: nodeList) {
            sizes += node.getLatencies().size();
        }
        for (VolatileNode node: nodeList) {
            result += node.getAvgLatency() / sizes * node.getLatencies().size();
        }
        return result;
    }

    public double getThroughput() {
        double result = 0d;
        long sizes = 0l;
        for (VolatileNode node: nodeList) {
            sizes += node.getLatencies().size();
        }
        for (VolatileNode node: nodeList) {
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
        for (VolatileNode node: nodeList) {
            node.off();
        }
        for (Thread node: nodeList) {
            doJoin(node);
        }
    }
}

