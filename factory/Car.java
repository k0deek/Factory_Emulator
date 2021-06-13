package factory;

import factory.accessory.Accessory;
import factory.corps.Corps;
import factory.motor.Motor;

public class Car {
    Accessory accessory;
    Corps corps;
    Motor motor;
    Integer ID;

    public Car(Accessory accessory, Corps corps, Motor motor) {
        this.accessory = accessory;
        this.corps = corps;
        this.motor = motor;
        ID = MakerID.getID();
    }

    public Integer getID() {
        return ID;
    }
}
