import java.util.ArrayList;
import java.util.List;

public class Driver {

    private static int counter = 0;

    private int driverId;
    private String driverName;
    private List<Device> deviceCollection = new ArrayList<Device>();

    public Driver(String driverName) {
        driverId = counter++;
        this.driverName = driverName;
    }

    public int getDriverId() {
        return driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public Device getDevice(int index) {
        return deviceCollection.get(index);
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void addDevice(Device newDevice) {
        deviceCollection.add(newDevice);
    }
}
