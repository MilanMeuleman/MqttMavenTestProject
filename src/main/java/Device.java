import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.UnsupportedEncodingException;

public abstract class Device implements MqttCallback, IMqttActionListener {

    public static final String COMMAND_KEY = "COMMAND";
    public static final String COMMAND_SEPARATOR = ":";
    public String topic;
    public static final String ENCODING = "UTF-8";
    public static final int QUALITY_OF_SERVICE = 2;

    protected String name;
    protected String clientId;
    protected MqttAsyncClient client;
    protected MemoryPersistence memoryPersistence;
    protected IMqttToken connectToken;
    protected IMqttToken subscribeToken;

    public Device(String name, String topic) {
        this.name = name;
        this.topic = topic;
    }

    public String getName() {
        return name;
    }

    public String getTopic() {
        return topic;
    }

    public void connect() {

        try {
            MqttConnectOptions options = new MqttConnectOptions();

            // options.setUserName(
            //    "replace with your username");
            // options.setPassword(
            //    "replace with your password"
            //    .toCharArray());

            // Replace with ssl:// and work with TLS/SSL
            // best practices in a
            // production environment

            memoryPersistence = new MemoryPersistence();

            String serverURI = "tcp://localhost:1883";
            clientId = MqttAsyncClient.generateClientId();
            client = new MqttAsyncClient(serverURI, clientId, memoryPersistence);

            client.setCallback(this);
            connectToken = client.connect(options, null, this);

        } catch (MqttException e) {

            e.printStackTrace();

        }

    }

    public boolean isConnected() {
        return (client != null) && (client.isConnected());
    }


    public void connectionLost(Throwable cause) {
        cause.printStackTrace();
    }

    public void onSuccess(IMqttToken asyncActionToken) {
        if (asyncActionToken.equals(connectToken)) {
            System.out.println(String.format("%s succesfully connected", name));

            try {
                subscribeToken = client.subscribe(getTopic(), QUALITY_OF_SERVICE, null, this);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else if (asyncActionToken.equals(subscribeToken)) {
            System.out.println(String.format("%s subscribed tot the %s topic", name, getTopic()));
            publishTextMessage(String.format("%s is listening", name));
        }
    }

    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        exception.printStackTrace();
    }

    public MessageActionListener publishCommand(String commandName, String destinationName) {
        String command = String.format("%s%s%s%s%s", COMMAND_KEY, COMMAND_SEPARATOR, commandName, COMMAND_SEPARATOR, destinationName);
        return publishTextMessage(command);
    }

    public MessageActionListener publishTextMessage(String messageText) {
        byte[] bytesMessage;

        try {
            bytesMessage = messageText.getBytes(ENCODING);
            MqttMessage message;
            message = new MqttMessage(bytesMessage);

            String userContext = "ListeningMessage";
            MessageActionListener actionListener = new MessageActionListener(getTopic(), messageText, userContext);
            client.publish(getTopic(), message, userContext, actionListener);
            return actionListener;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (MqttException e) {
            e.printStackTrace();
            return null;
        }
    }
}
