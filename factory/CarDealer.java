package factory;

import java.util.Date;

public class CarDealer implements Runnable {
    CarStorage carStorage;
    Date start;
    Integer dealerID;
    Integer timeSleep = 500;
    boolean LogSale;

    public CarDealer(CarStorage carStorage, Integer dealerID, boolean LogSale, Date start) {
        this.carStorage = carStorage;
        this.dealerID = dealerID;
        this.LogSale = LogSale;
        this.start = start;
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
            Car car = carStorage.getCar();
            Date date = new Date();
            if (LogSale)
                System.out.println(date.getTime() - start.getTime() + ": Dealer:" + dealerID + ": Car" + car.getID() + " (Carcase:" + car.corps.getID()
                        + ", Engine: " + car.motor.getID() + ", Accessory:" + car.accessory.getID() + ")\n");
        }
    }
}
