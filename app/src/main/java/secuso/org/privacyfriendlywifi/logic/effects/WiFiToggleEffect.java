package secuso.org.privacyfriendlywifi.logic.effects;

/**
 * (De-) Activate WiFi.
 */
public class WiFiToggleEffect implements Effect {
    @Override
    public void apply(Object args) {
        if (args instanceof Boolean) {
            boolean status = (Boolean) args;
            // TODO (de-) activate WiFi according to argument

        } else {
            throw new RuntimeException("A boolean value has to be passed.");
        }
    }
}
