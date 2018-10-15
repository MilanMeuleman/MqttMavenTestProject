import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.concurrent.ThreadLocalRandom;

public class MqttSampleApp {

    public static void main(String[] args) {

        Blackbox blackbox1 = new Blackbox("[Blackbox #1]");
        blackbox1.connect();
        CLA cla1 = new CLA("[CLA #1]");
        cla1.connect();
        Dashcam dashcam1 = new Dashcam("[Dashcam #1]");
        dashcam1.connect();

        Driver driver1 = new Driver("Milan Meuleman");
        driver1.addDevice(blackbox1);
        driver1.addDevice(cla1);
        driver1.addDevice(dashcam1);

        try {
            while (true) {
                try {
                    Thread.sleep(5000);
                    int r = ThreadLocalRandom.current().nextInt(1, 11);

                    if ((r < 5) && blackbox1.isConnected()) {
                        blackbox1.publishCommand(Blackbox.GET_SPEED_COMMAND_KEY, blackbox1.getName());
                    } else {
                        if (cla1.isConnected()) {
                            cla1.publishCommand(CLA.GET_USAGEDATA_COMMAND_KEY, cla1.getName());
                        }

                        if (dashcam1.isConnected()) {
                            dashcam1.publishCommand(Dashcam.GET_IMAGES_COMMAND_KEY, dashcam1.getName());
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (blackbox1.isConnected()) {
                try {
                    blackbox1.client.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            if (cla1.isConnected()) {
                try {
                    cla1.client.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            if (dashcam1.isConnected()) {
                try {
                    dashcam1.client.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
