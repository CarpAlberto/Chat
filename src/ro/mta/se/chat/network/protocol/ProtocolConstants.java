package ro.mta.se.chat.network.protocol;

/**
 * Created by Alberto-Daniel on 11/26/2015.
 * This class contains all constants used in protocol comunnication
 * betwenn two hosts
 */
public class ProtocolConstants {

    /**
     * This is the byteArray that contains the version of the application
     */
    public static final byte[] VERSION = new byte[]{0x00, 0x00, 0x02};

    /**
     * This is a byte that identify the header of a packet
     */
    public static final byte PACKET_HEADER = (byte) 0xAA;

    /**
     * This is the flag that identify a packet of type HELLO
     */
    public static final byte OP_HELLO = 0x01;

    /**
     * This is the flag that identify a packet of type NEW_NICK
     */
    public static final byte OP_NEW_NICK = 0x02;

    /**
     * This is the flag that identify a packet of type MESSAGE
     */
    public static final byte OP_MESSAGE = 0x10;

    /**
     * This is the flag that identify a packet of type ROOM
     */
    public static final byte OP_ROOM = 0x11;

    /**
     * This is the flag that identify a packet of type HANDSHAKE_START
     */

    public static final byte OP_HANDSHAKE_START = 0x14;

    /**
     * This is the flag that identify a packet of type HANDSHAKE_END
     */

    public static final byte OP_HANDSHAKE_END = 0x15;

    /**
     * This is the flag that identify a packet of type AUDIO
     */
    public static final byte OP_AUDIO = 0x16;

    /**
     * This is the flag that identify a packet of type CALL_REQUEST
     */

    public static final byte OP_CALL_REQUEST = 0x17;

    /**
     * This is the flag that identify a packet of type MESS
     */

    public static final byte OP_MESS = 0x18;

}
