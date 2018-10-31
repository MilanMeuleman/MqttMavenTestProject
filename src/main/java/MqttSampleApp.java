import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.concurrent.ThreadLocalRandom;

public class MqttSampleApp {

    public static void main(String[] args) {

        String topicCLA = "test-mqtt/cla/usagedata";
        String topicBlackbox = "test-mqtt/blackbox/speed";
        String topicDashcam = "test-mqtt/dashcam/images";

        IpCamera ipCamera1 = new IpCamera("[IpCamera #1]", topicBlackbox);
        ipCamera1.connect();
        ElectricityMeter electricityMeter1 = new ElectricityMeter("[ElectricityMeter #1]", topicCLA);
        electricityMeter1.connect();
        Thermometer thermometer1 = new Thermometer("[Thermometer #1]", topicDashcam);
        thermometer1.connect();

        Driver driver1 = new Driver("Milan Meuleman");
        driver1.addDevice(ipCamera1);
        driver1.addDevice(electricityMeter1);
        driver1.addDevice(thermometer1);

        try {
        while (true) {
                try {
                    Thread.sleep(5000);
                    int r = ThreadLocalRandom.current().nextInt(1, 11);

                    if ((r < 5) && ipCamera1.isConnected()) {
                        ipCamera1.publishCommand(IpCamera.GET_DETECTION_INFORMATION_KEY, ipCamera1.getName());
                    } else {
                        if (electricityMeter1.isConnected()) {
                            electricityMeter1.publishCommand(ElectricityMeter.GET_ELECTRICITY_USAGE_COMMAND_KEY, electricityMeter1.getName());
                        }

                        if (thermometer1.isConnected()) {
                            thermometer1.publishCommand(Thermometer.GET_TEMPERATURE_COMMAND_KEY, thermometer1.getName());
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ipCamera1.isConnected()) {
                try {
                    ipCamera1.client.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            if (electricityMeter1.isConnected()) {
                try {
                    electricityMeter1.client.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            if (thermometer1.isConnected()) {
                try {
                    thermometer1.client.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
