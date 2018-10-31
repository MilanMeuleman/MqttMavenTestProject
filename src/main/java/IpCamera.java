import org.eclipse.paho.client.mqttv3.*;

import java.sql.Time;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class IpCamera extends Device {

    public static final String GET_DETECTION_INFORMATION_KEY = "SEND_DETECTION_INFORMATION";

    public IpCamera(String name, String topic) {
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

        if (keyValue[0].equals(COMMAND_KEY) && keyValue[1].equals(GET_DETECTION_INFORMATION_KEY) && keyValue[2].equals(name)) {
            Instant timeStamp = Instant.now();
            String timeStampString = timeStamp.toString();
            System.out.println(String.format("%s Person detected at: %s", name, timeStampString));
        }
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        // Delivery for a message has been completed and all acknowledgments have been received
    }
}
