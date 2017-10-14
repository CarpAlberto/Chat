package ro.mta.se.chat.factory;

import ro.mta.se.chat.crypto.Certificate;
import ro.mta.se.chat.network.protocol.ProtocolConstants;
import ro.mta.se.chat.utils.Convert;
import ro.mta.se.chat.utils.Utils;

import javax.rmi.CORBA.Util;
import java.nio.ByteBuffer;

/**
 * Created by Alberto-Daniel on 11/26/2015.
 * This class is responsable for generating packets by different type
 */
public class PacketFactory {
    /**
     * This function generates an hello packet
     *
     * @param nickname the nickname of the sender
     * @param room     the room of the sender
     * @return the Hello Packet
     */
    public static ByteBuffer getHelloPacket(String nickname, String room) {
        String nickName = nickname + "~" + room;
        byte[] nick_name = nickName.getBytes();
        ByteBuffer packet = Utils.getByteBuffer(1 + 4 + 1 + 3 + 1 + nick_name.length);
        packet.put(ProtocolConstants.PACKET_HEADER);
        packet.putInt(1 + 3 + 1 + nick_name.length);
        packet.put(ProtocolConstants.OP_HELLO);
        packet.put(ProtocolConstants.VERSION);
        packet.put(Convert.intToByte(nick_name.length));
        packet.put(nick_name);
        packet.position(0);
        return packet;
    }

    /**
     * This function generates an nickNameChanged packet
     *
     * @param nickName the newNickname
     * @return the NickNameChanged packet
     */
    public static ByteBuffer getChangeNickNamePacket(String nickName) {
        byte[] nick_name = nickName.getBytes();
        ByteBuffer packet = Utils.getByteBuffer(1 + 4 + 1 + 1 + nick_name.length);
        packet.put(ProtocolConstants.PACKET_HEADER);
        packet.putInt(1 + 1 + nick_name.length);
        packet.put(ProtocolConstants.OP_NEW_NICK);
        packet.put(Convert.intToByte(nick_name.length));
        packet.put(nick_name);
        packet.position(0);
        return packet;
    }

    /**
     * This function generates an messagePacket
     *
     * @param message the message of the packet
     * @return the messagePacket
     */
    public static ByteBuffer getMessagePacket(byte[] message) {
        byte[] message_bytes = message;
        ByteBuffer packet = Utils.getByteBuffer(1 + 4 + 1 + 4 + message_bytes.length);
        packet.put(ProtocolConstants.PACKET_HEADER);
        packet.putInt(1 + 4 + message_bytes.length);
        packet.put(ProtocolConstants.OP_MESSAGE);
        packet.putInt(message_bytes.length);
        packet.put(message_bytes);
        packet.position(0);
        return packet;
    }

    /**
     * This function generates a newRoom packet
     *
     * @param room the new room from packet
     * @return the newRoom packet
     */
    public static ByteBuffer getNewRoomPacket(String room) {
        byte[] message_bytes = room.getBytes();
        ByteBuffer packet = Utils.getByteBuffer(1 + 4 + 1 + 4 + message_bytes.length);
        packet.put(ProtocolConstants.PACKET_HEADER);
        packet.putInt(1 + 4 + message_bytes.length);
        packet.put(ProtocolConstants.OP_ROOM);
        packet.putInt(message_bytes.length);
        packet.put(message_bytes);
        packet.position(0);
        return packet;
    }

    /**
     * This function generates a new startHandShake packet
     *
     * @return the startHandShakePacket
     */
    public static ByteBuffer getStartHandShake() {
        byte[] X509CertificateBytes = Certificate.loadFromFile(Utils.getCertificatePath());
        int length = X509CertificateBytes.length;
        ByteBuffer packet = Utils.getByteBuffer(1 + 4 + 1 + 4 + length);
        packet.put(ProtocolConstants.PACKET_HEADER);
        packet.putInt(1 + 4 + length);
        packet.put(ProtocolConstants.OP_HANDSHAKE_START);
        packet.putInt(length);
        packet.put(X509CertificateBytes);
        packet.position(0);
        return packet;
    }

    /**
     * This function generates a new EndHandShake packet
     *
     * @param data the endShakePacket
     * @return the packet
     */
    public static ByteBuffer getEndHandShake(byte[] data) {
        int length = data.length;
        ByteBuffer packet = Utils.getByteBuffer(1 + 4 + 1 + 4 + length);
        packet.put(ProtocolConstants.PACKET_HEADER);
        packet.putInt(1 + 4 + length);
        packet.put(ProtocolConstants.OP_HANDSHAKE_END);
        packet.putInt(length);
        packet.put(data);
        packet.position(0);
        return packet;
    }

    /**
     * This function returns an AudioPacket
     *
     * @param data the aditional audio data
     * @return the audio packet
     */
    public static ByteBuffer getAudioPacket(byte[] data) {
        int length = data.length;
        ByteBuffer packet = Utils.getByteBuffer(1 + 4 + 1 + 4 + length);
        packet.put(ProtocolConstants.PACKET_HEADER);
        packet.putInt(1 + 4 + length);
        packet.put(ProtocolConstants.OP_AUDIO);
        packet.putInt(length);
        packet.put(data);
        packet.position(0);
        return packet;
    }

    /**
     * This function returns an CallPacket
     *
     * @return the buffer containing the call_request packet
     */
    public static ByteBuffer getCallPacket() {
        ByteBuffer packet = Utils.getByteBuffer(1 + 4 + 1);
        packet.put(ProtocolConstants.PACKET_HEADER);
        packet.putInt(1);
        packet.put(ProtocolConstants.OP_CALL_REQUEST);
        packet.position(0);
        return packet;
    }

    /**
     * This function returns a messPacket from data
     *
     * @param data the data pass as argument
     * @return the buffer
     */
    public static ByteBuffer getMessPacket(byte[] data) {
        int length = data.length;
        ByteBuffer packet = Utils.getByteBuffer(1 + 4 + 1 + 4 + length);
        packet.put(ProtocolConstants.PACKET_HEADER);
        packet.putInt(1 + 4 + length);
        packet.put(ProtocolConstants.OP_MESS);
        packet.putInt(length);
        packet.put(data);
        packet.position(0);
        return packet;
    }

}
