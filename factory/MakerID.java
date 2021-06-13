package factory;

public class MakerID {
    static int ID = 0;
    public static synchronized int getID() {
        return ID++;
    }
}
