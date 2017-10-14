package ro.mta.se.chat.network.enums;

/**
 * Created by Alberto-Daniel on 12/24/2015.
 * This is the enum representing the connection status of the user
 */
public enum ConnectionStatus {
    /**
     * The user is disconnected
     */
    DISCONNECTED,
    /**
     * The user is trying to connect
     */
    CONNECTING,
    /**
     * The user is connected
     */
    CONNECTED
}
