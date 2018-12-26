import org.eclipse.paho.client.mqttv3.*;
import java.util.concurrent.ThreadLocalRandom;

public class ElectricityMeter extends Device {

    public static final String GET_ELECTRICITY_USAGE_COMMAND_KEY = "GET_ELECTRICITY_USAGE";
    public int totalUsage;

    public ElectricityMeter(String name, String topic) {
        super(name, topic);
    }
    public String getName() {
        return name;
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {

        if (!topic.equals(topic)) {
            return;
        }

        String messageText = new String(message.getPayload(), ENCODING);
        System.out.println(String.format("%s received %S: %s", name, topic, messageText));
        String[] keyValue = messageText.split(COMMAND_SEPARATOR);

        if (keyValue.length != 3) {
            return;
        }

        if (keyValue[0].equals(COMMAND_KEY) && keyValue[1].equals(GET_ELECTRICITY_USAGE_COMMAND_KEY) && keyValue[2].equals(name)) {
            int usageInKw = ThreadLocalRandom.current().nextInt(1, 100);
            totalUsage = totalUsage + usageInKw;
            System.out.println(String.format("%s Today's electricity usage: %d Kw", name, totalUsage));
        }
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        // Delivery for a message has been completed and all acknowledgments have been received
    }
}
