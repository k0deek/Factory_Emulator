package factory;

import factory.accessory.AccessoryDelivery;
import factory.accessory.AccessoryStorage;
import factory.corps.CorpsDelivery;
import factory.corps.CorpsStorage;
import factory.motor.MotorDelivery;
import factory.motor.MotorStorage;
import threadpool.ThreadPool;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Factory {

    Integer workersCount;
    Integer dealerCount;
    Integer AccessoriesDeliveryCount;
    Integer CorpsStorageSize;
    Integer MotorStorageSize;
    Integer AccessoryStorageSize;
    Integer CarStorageSize;
    boolean isLog;
    Controller controller;
    CorpsDelivery corpsDeliveryRunnable;
    MotorDelivery motorDeliveryRunnable;
    CarDealer[] dealers;
    AccessoryDelivery[] accessoryDeliveryRunnable;
    CorpsStorage corpsStorage;
    MotorStorage motorStorage;
    AccessoryStorage accessoryStorage;
    CarStorage carStorage;
    ThreadPool threadPool;

    public Factory() {
        textConfigure();
        Worker[] workers = new Worker[workersCount];
        dealers = new CarDealer[dealerCount];
        threadPool = new ThreadPool(4);
        corpsStorage = new CorpsStorage(CorpsStorageSize);
        motorStorage = new MotorStorage(MotorStorageSize);
        accessoryStorage = new AccessoryStorage(AccessoryStorageSize);
        carStorage = new CarStorage(CarStorageSize);
        controller = new Controller(workers, carStorage, threadPool);
        for (int i = 0; i < workersCount; i++) {
            workers[i] = new Worker(accessoryStorage, motorStorage, corpsStorage, carStorage, i, controller);
        }

        Date date = new Date();
        for (int i = 0; i < dealerCount; i++) {
            dealers[i] = new CarDealer(carStorage, i, isLog, date);
        }

        corpsDeliveryRunnable = new CorpsDelivery(corpsStorage);
        motorDeliveryRunnable = new MotorDelivery(motorStorage);
        accessoryDeliveryRunnable = new AccessoryDelivery[AccessoriesDeliveryCount];
        for (int i = 0; i < AccessoriesDeliveryCount; ++i) {
            accessoryDeliveryRunnable[i] = new AccessoryDelivery(accessoryStorage);
        }
        this.start();

        JFrame frame = new JFrame("BMW Factory");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(250, 250);

        JSlider accessorySupplierSpeed = new JSlider(1, 10000, 7000);
        accessorySupplierSpeed.addChangeListener(e -> {
            int value = ((JSlider) e.getSource()).getValue();
            for (int i = 0; i < AccessoriesDeliveryCount; i++) {
                accessoryDeliveryRunnable[i].setSleepTime(value);
            }
        });
        JSlider engineSupplierSpeed = new JSlider(1, 10000, 3000);
        engineSupplierSpeed.addChangeListener(e -> {
            int value = ((JSlider) e.getSource()).getValue();
            motorDeliveryRunnable.setSleepTime(value);
        });
        JSlider carcaseSupplierSpeed = new JSlider(1, 10000, 5000);
        carcaseSupplierSpeed.addChangeListener(e -> {
            int value = ((JSlider) e.getSource()).getValue();
            corpsDeliveryRunnable.setSleepTime(value);
        });


        JSlider dealersSpeed = new JSlider(1, 10000, 1000);
        dealersSpeed.addChangeListener(e -> {
            int value = ((JSlider) e.getSource()).getValue();
            for (int i = 0; i < this.dealerCount; i++) {
                dealers[i].setSleepTime(value);
            }
        });

        JLabel accessoryCountView = new JLabel("Accessory: " + accessoryStorage.getAccessoryCount());
        JLabel engineCountView = new JLabel("Motor: " + motorStorage.getEngineCount());
        JLabel carcaseCountView = new JLabel("Corps: " + corpsStorage.getCarcaseCount());
        JLabel carCountView = new JLabel();
        JLabel tasksWait = new JLabel();
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(() -> {
            accessoryCountView.setText("Accessory: " + accessoryStorage.getAccessoryCount());
            engineCountView.setText("Motor: " + motorStorage.getEngineCount());
            carcaseCountView.setText("Corps: " + corpsStorage.getCarcaseCount());
            carCountView.setText("Car: " + carStorage.getCarCount());
            tasksWait.setText("Awaiting task in the pool : " + this.threadPool.getQueueSize());
        }, 0, 1, TimeUnit.SECONDS);
        JPanel contents = new JPanel();

        accessorySupplierSpeed.setToolTipText("Скорость поставщика аксессуаров");
        engineSupplierSpeed.setToolTipText("Скорость поставщика двигателей");
        carcaseSupplierSpeed.setToolTipText("Скорость поставщика кузовов");
        dealersSpeed.setToolTipText("Скорость продавцов");

        contents.add(accessorySupplierSpeed);
        contents.add(engineSupplierSpeed);
        contents.add(carcaseSupplierSpeed);
        contents.add(dealersSpeed);
        contents.add(accessoryCountView);
        contents.add(engineCountView);
        contents.add(carcaseCountView);
        contents.add(carCountView);
        contents.add(tasksWait);
        frame.getContentPane().add(contents);
        frame.setVisible(true);
    }
    void textConfigure() {
        try {
            InputStream input = this.getClass().getResourceAsStream("/cfg.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            do {
                String parts[];
                parts = line.split("[\\W]+");

                if (parts[0].equals("StorageBodySize"))
                    CorpsStorageSize = Integer.valueOf(parts[1]);
                if (parts[0].equals("StorageMotorSize"))
                    MotorStorageSize = Integer.valueOf(parts[1]);
                if (parts[0].equals("StorageAutoSize"))
                    CarStorageSize = Integer.valueOf(parts[1]);
                if (parts[0].equals("AccessorySuppliers"))
                    AccessoriesDeliveryCount = Integer.valueOf(parts[1]);
                if (parts[0].equals("StorageAccessorySize"))
                    AccessoryStorageSize = Integer.valueOf(parts[1]);
                if (parts[0].equals("Workers"))
                    workersCount = Integer.valueOf(parts[1]);
                if (parts[0].equals("Dealers"))
                    dealerCount = Integer.valueOf(parts[1]);
                if (parts[0].equals("LogSale")) {
                    isLog = parts[1].equals("true");
                }

                line = bufferedReader.readLine();
            } while (line != null);
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    public void start() {
        Thread carcaseSupplier = new Thread(corpsDeliveryRunnable);
        Thread engineSupplier = new Thread(motorDeliveryRunnable);
        Thread[] accessoriesSuppliers = new Thread[AccessoriesDeliveryCount];
        Thread[] dealersThreads = new Thread[dealerCount];
        for (int i = 0; i < AccessoriesDeliveryCount; ++i) {
            accessoriesSuppliers[i] = new Thread(accessoryDeliveryRunnable[i]);
        }
        Thread controlCars = new Thread(controller);
        for (int i = 0; i < dealerCount; i++) {
            dealersThreads[i] = new Thread(dealers[i]);
        }

        controlCars.start();
        carcaseSupplier.start();
        engineSupplier.start();
        for (int i = 0; i < AccessoriesDeliveryCount; ++i) {
            accessoriesSuppliers[i].start();
        }
        for (int i = 0; i < dealerCount; i++) {
            dealersThreads[i].start();
        }
    }


}