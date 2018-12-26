import org.eclipse.paho.client.mqttv3.*;
import java.util.concurrent.ThreadLocalRandom;

public class Thermometer extends Device {

    public static final String GET_TEMPERATURE_COMMAND_KEY = "GET_TEMPERATURE";

    public Thermometer(String name, String topic) {
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

    if (keyValue[0].equals(COMMAND_KEY) && keyValue[1].equals(GET_TEMPERATURE_COMMAND_KEY) && keyValue[2].equals(name)) {
            int temperature = ThreadLocalRandom.current().nextInt(1, 40);
            System.out.println(String.format("%s Current temperature: %d degrees", name, temperature));
        }
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        // Delivery for a message has been completed and all acknowledgments have been received
    }
}