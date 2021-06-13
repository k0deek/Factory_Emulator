package factory.corps;

import java.util.ArrayDeque;
import java.util.Deque;

public class CorpsStorage {
    Deque<Corps> storage;
    Integer currentCount = 0;
    Integer maxCount;


    public CorpsStorage(Integer maxCount) {
        storage = new ArrayDeque<>();
        this.maxCount = maxCount;
    }

    public void addCarcase(Corps corps) {
        synchronized (this) {
            try {
                while (isFilled()) {
                    wait();
                }
            } catch (InterruptedException err) {
                err.printStackTrace();
            }
            storage.add(corps);
            currentCount++;
            notifyAll();
        }
    }

    public Integer getCarcaseCount() {
        synchronized (this) {
            return currentCount;
        }
    }

    public Corps getCarcase() {
        synchronized (this) {
            try {
                while (currentCount == 0) {
                    wait();
                }
            } catch (InterruptedException err) {
                err.printStackTrace();
            }
            Corps corps = storage.pollLast();
            currentCount--;
            notifyAll();
            return corps;
        }
    }

    public boolean isFilled() {
        return (maxCount.equals(currentCount));
    }
}
