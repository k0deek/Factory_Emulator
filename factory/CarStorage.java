package factory;

import java.util.ArrayDeque;
import java.util.Deque;

public class CarStorage {
    Deque<Car> storage;
    Integer carCount = 0;
    Integer currentCount = 0;
    Integer maxCount;

    public CarStorage(Integer maxCount) {
        storage = new ArrayDeque<>();
        this.maxCount = maxCount;
    }

    public synchronized void addCar (Car car) {
        try {
            while (isFilled()) {
                wait();
            }
        }
        catch (InterruptedException err) {
            err.printStackTrace();
        }
        storage.add(car);
        currentCount++;
        carCount++;
        notifyAll();
    }

    public synchronized Integer getCarCount() {
        return currentCount;
    }

    public synchronized Car getCar() {
        try {
            while (currentCount == 0) {
                wait();
            }
        }
        catch (InterruptedException err) {
            err.printStackTrace();
        }

        Car car = storage.pollLast();
        currentCount--;
        notifyAll();
        return car;
    }

    public boolean isFilled() {
        return (maxCount.equals(currentCount));
    }
}
