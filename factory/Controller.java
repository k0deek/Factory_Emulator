package factory;

import threadpool.ThreadPool;

public class Controller implements Runnable {
    Worker[] workers;
    CarStorage carStorage;
    ThreadPool threadPool;

    public Controller(Worker[] workers, CarStorage carStorage, ThreadPool threadPool) {
        this.workers = workers;
        this.carStorage = carStorage;
        this.threadPool = threadPool;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (carStorage) {
                if (carStorage.currentCount.equals(carStorage.maxCount)) {
                    try {
                        carStorage.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            Worker worker = null;
            for (Worker workerIter : workers) {
                if (!workerIter.isWorking) {
                    worker = workerIter;
                    break;
                }
            }

            if (worker != null) {
                worker.isWorking = true;
                threadPool.execute(worker);
            } else {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
}
