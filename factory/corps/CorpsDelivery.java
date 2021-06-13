package factory.corps;

import factory.MakerID;

public class CorpsDelivery implements Runnable {
    CorpsStorage storage;
    Integer timeSleep = 1000;

    public CorpsDelivery(CorpsStorage storage) {
        this.storage = storage;
    }

    public void setSleepTime(Integer sleepTime) {
        this.timeSleep = sleepTime;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(timeSleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Corps corps = new Corps(MakerID.getID());
            storage.addCarcase(corps);
        }
    }
}
