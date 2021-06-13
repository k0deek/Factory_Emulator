package factory.accessory;

import factory.MakerID;

public class AccessoryDelivery implements Runnable {
    AccessoryStorage storage;
    Integer timeSleep = 1000;

    public AccessoryDelivery(AccessoryStorage storage) {
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
            Accessory accessory = new Accessory(MakerID.getID());
            storage.addAccessories(accessory);
        }
    }
}
