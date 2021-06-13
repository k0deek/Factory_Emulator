package factory;

import factory.accessory.Accessory;
import factory.accessory.AccessoryStorage;
import factory.corps.Corps;
import factory.corps.CorpsStorage;
import factory.motor.Motor;
import factory.motor.MotorStorage;

public class Worker implements Runnable {
    AccessoryStorage accessoryStorage;
    MotorStorage motorStorage;
    CorpsStorage corpsStorage;
    CarStorage carStorage;
    boolean isWorking = false;
    Integer workerID;
    Controller controller;

    public Worker(AccessoryStorage accessoryStorage, MotorStorage motorStorage, CorpsStorage corpsStorage, CarStorage carStorage, Integer workerID, Controller controller) {
        this.accessoryStorage = accessoryStorage;
        this.motorStorage = motorStorage;
        this.corpsStorage = corpsStorage;
        this.carStorage = carStorage;
        this.workerID = workerID;
        this.controller = controller;
    }

    @Override
    public void run() {
        Accessory accessory = accessoryStorage.getAccessory();
        Motor motor = motorStorage.getEngine();
        Corps corps = corpsStorage.getCarcase();
        Car car = new Car(accessory, corps, motor);
        System.out.println("New car ID: " + car.getID() + "\n" + "Corps: " + corps.getID() + " Motor: " + motor.getID() + " Accessory: " + accessory.getID() + "\n");
        carStorage.addCar(car);
        isWorking = false;
        synchronized (controller) {
            controller.notify();
        }
    }

}