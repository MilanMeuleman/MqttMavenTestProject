import org.eclipse.paho.client.mqttv3.*;

import java.util.concurrent.ThreadLocalRandom;

public class Blackbox extends Device {

    public static final String GET_SPEED_COMMAND_KEY = "GET_SPEED";

    public Blackbox(String name) {
        super(name);
    }

    public String getName() {
        return name;
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {

        // A message has arrived from the MQTT broker
        // The MQTT broker doesn't send back
        // an acknowledgment to the server until
        // this method returns cleanly

        if (!topic.equals(TOPIC)) {
            return;
        }

        String messageText = new String(message.getPayload(), ENCODING);
        System.out.println(String.format("%s received %S: %s", name, topic, messageText));
        String[] keyValue = messageText.split(COMMAND_SEPARATOR);

        if (keyValue.length != 3) {
            return;
        }

        if (keyValue[0].equals(COMMAND_KEY) && keyValue[1].equals(GET_SPEED_COMMAND_KEY) && keyValue[2].equals(name)) {
            int altitudeInFeet = ThreadLocalRandom.current().nextInt(1, 160);
            System.out.println(String.format("%s speed: %d km/h", name, altitudeInFeet));
        }
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        // Delivery for a message has been completed
        // and all acknowledgments have been received
    }
}
