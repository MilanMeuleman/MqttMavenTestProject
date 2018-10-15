import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ThreadLocalRandom;

public class CLA extends Device {

    public static final String GET_USAGEDATA_COMMAND_KEY = "GET_USAGEDATA";

    public CLA(String name) {
        super(name);
    }

    public String getName() {
        return name;
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {

        if (!topic.equals(TOPIC)) {
            return;
        }

        String messageText = new String(message.getPayload(), ENCODING);
        System.out.println(String.format("%s received %S: %s", name, topic, messageText));
        String[] keyValue = messageText.split(COMMAND_SEPARATOR);

        if (keyValue.length != 3) {
            return;
        }

        if (keyValue[0].equals(COMMAND_KEY) && keyValue[1].equals(GET_USAGEDATA_COMMAND_KEY) && keyValue[2].equals(name)) {
            int usageInSeconds = ThreadLocalRandom.current().nextInt(1, 200);
            System.out.println(String.format("%s has been used for : %d seconds", name, usageInSeconds));
        }
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        // Delivery for a message has been completed
        // and all acknowledgments have been received
    }
}
