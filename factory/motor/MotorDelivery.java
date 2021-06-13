package factory.motor;

import factory.MakerID;

public class MotorDelivery implements Runnable {
    MotorStorage storage;
    Integer sleepTime = 1000;

    public MotorDelivery(MotorStorage storage) {
        this.storage = storage;
    }

    public void setSleepTime(Integer sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Motor motor = new Motor(MakerID.getID());
            storage.addEngine(motor);
        }
    }
}
