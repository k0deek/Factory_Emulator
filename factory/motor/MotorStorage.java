package factory.motor;

import java.util.ArrayDeque;
import java.util.Deque;

public class MotorStorage {
    Deque<Motor> storage;
    Integer currentCount = 0;
    Integer maxCount;


    public MotorStorage(Integer maxCount) {
        storage = new ArrayDeque<>();
        this.maxCount = maxCount;
    }

    public void addEngine(Motor motor) {
        synchronized (this) {
            try {
                while (isFilled()) {
                    wait();
                }
            }
            catch (InterruptedException err) {
                err.printStackTrace();
            }
            storage.add(motor);
            currentCount++;
            notifyAll();
        }
    }

    public Integer getEngineCount() {
        synchronized (this) {
            return currentCount;
        }
    }

    public Motor getEngine() {
        synchronized (this) {
            try {
                while (currentCount == 0) {
                    wait();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            Motor motor = storage.pollLast();
            currentCount--;
            notifyAll();
            return motor;
        }
    }
    public boolean isFilled() {
        return (maxCount.equals(currentCount));
    }
}
