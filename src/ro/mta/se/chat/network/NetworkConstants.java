package ro.mta.se.chat.network;

/**
 * Created by Alberto-Daniel on 11/26/2015.
 */
public class NetworkConstants {
    /**
     * This is the timesout connection time
     */
    public static final int CONNECT_TIMEOUT = 5000;
    /**
     * This is the configuration file
     */
    public static final String CONFIGURATION_FILE = "config.cfg";
    /**
     * This is the default port
     */
    public static final int DEFAULT_PORT = 60000;
    /**
     * This is the key separator
     */
    public static final String KEY_SEPARATOR = ":";
    /**
     * This is the Listen_Port_Start
     */
    public static final String LISTEN_TCP_PORT_KEY_START = "ListenTCPPortStart";
    /**
     * This is the Listen_Port_End_key
     */
    public static final String LISTEN_TCP_PORT_KEY_END = "ListenTCPPortEnd";

    /**
     * This is the ip_start_key
     */
    public static final String IP_START = "IPStart";
    /**
     * This is the ip_end_key
     */
    public static final String IP_END = "IPEnd";
    /**
     * This is the default ip
     */
    public static final String DEFAULT_NETWORK = "127.0.0.1";
    /**
     * This is the max_nixkname_length
     */
    public static final int MAX_NICKNAME_LENGTH = 50;
    /**
     * This is the max_message_length
     */
    public static final int MAX_MESSAGE_LENGTH = 30000;

}
