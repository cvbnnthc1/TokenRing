package ru.phystech.tokenring;

import ru.phystech.tokenring.Volatile.VolatileTester;
import ru.phystech.tokenring.blockingQueue.ArrayBlockingQueueTester;
import ru.phystech.tokenring.concurrentLinkedQueue.ConcurrentLinkedQueueTester;
import ru.phystech.tokenring.linkedBlockingQueue.LinkedBlockingQueueTester;

import java.io.IOException;

public class Tester {
    public static void main(String[] args) throws IOException {
        VolatileTester.main(args);
        ArrayBlockingQueueTester.main(args);
        LinkedBlockingQueueTester.main(args);
        ConcurrentLinkedQueueTester.main(args);
    }
}
