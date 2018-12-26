import java.util.ArrayList;
import java.util.List;

public class Building {

    private static int counter = 0;

    private int buildingId;
    private String buildingName;
    private List<Device> deviceCollection = new ArrayList<Device>();

    public Building(String buildingName) {
        buildingId = counter++;
        this.buildingName = buildingName;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public Device getDevice(int index) {
        return deviceCollection.get(index);
    }
    public void setBuildingName(String driverName) {
        this.buildingName = driverName;
    }

    public void addDevice(Device newDevice) {
        deviceCollection.add(newDevice);
    }
}