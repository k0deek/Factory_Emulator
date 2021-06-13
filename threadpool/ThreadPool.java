package threadpool;

import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {
    PoolWorker[] pool;
    LinkedBlockingQueue queueToPool;
    int threadsCount;



    public ThreadPool(int count) {
        threadsCount = count;
        queueToPool = new LinkedBlockingQueue();
        pool = new PoolWorker[count];
        for (int i = 0; i < count; ++i) {
            pool[i] = new PoolWorker();
            pool[i].start();
        }
    }

    public void execute(Runnable task) {
        synchronized (queueToPool) {
            queueToPool.add(task);
            queueToPool.notify();
        }
    }
    public int getQueueSize() {
        return queueToPool.size();
    }

    private class PoolWorker extends Thread {
        @Override
        public void run() {
            Runnable task;
            while (true) {
                synchronized (queueToPool) {
                    while (queueToPool.isEmpty()) {
                        try {
                            queueToPool.wait();
                        } catch (InterruptedException e) {
                            System.out.println("The waiting time has expired: " + e.getMessage());
                        }
                    }
                    task = (Runnable) queueToPool.poll();
                }
                ////
                try {
                    task.run();
                    synchronized (task) {
                        task.notifyAll();
                    }
                } catch (RuntimeException e) {
                    System.out.println("Thread pool interrupted: " + e.getMessage());
                }
            }
        }
    }
}

