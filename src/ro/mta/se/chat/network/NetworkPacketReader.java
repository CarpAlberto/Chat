package ro.mta.se.chat.network;

import ro.mta.se.chat.exceptions.CorruptDataException;
import ro.mta.se.chat.exceptions.EndOfStreamException;
import ro.mta.se.chat.singleton.NetworkManager;
import ro.mta.se.chat.utils.Convert;
import ro.mta.se.chat.utils.Utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import ro.mta.se.chat.network.protocol.*;

/**
 * Created by Alberto-Daniel on 11/26/2015.
 * This function is responsable for parsing data from channel e get the results
 */
public class NetworkPacketReader {
    /**
     * This function readThe packet from a SocketChannel @param channel
     *
     * @param channel the channel to be written
     * @throws IOException
     * @throws EndOfStreamException
     * @throws CorruptDataException
     */
    public static void readPacket(SocketChannel channel) throws IOException, EndOfStreamException, CorruptDataException {

        int port = channel.socket().getPort();
        String ip = Convert.ipToString(channel.socket()
                .getInetAddress().getAddress());
        byte header = readByte(channel);
        if (header != ProtocolConstants.PACKET_HEADER)
            throw new IOException("Unknown packet header 0x" + Convert.byteToHex(header));
        ByteBuffer packet_length = Utils.getByteBuffer(4);
        readBuffer(channel, packet_length);
        byte opcode = readByte(channel);
        switch (opcode) {
            case ProtocolConstants.OP_HELLO: {
                byte[] version = new byte[3];
                version[0] = readByte(channel);
                version[1] = readByte(channel);
                version[2] = readByte(channel);
                int nick_length = Convert.byteToInt(readByte(channel));
                if (nick_length > NetworkConstants.MAX_NICKNAME_LENGTH)
                    throw new CorruptDataException("Too big nickname " + nick_length);
                ByteBuffer nick_bytes = Utils.getByteBuffer(nick_length);
                readBuffer(channel, nick_bytes);
                String key = new String(nick_bytes.array());
                String nickname = key.substring(0, key.indexOf("~"));
                String room = key.substring(key.indexOf("~") + 1);
                NetworkManager.getInstance().onHello(ip, port, version, nickname, room);
                break;
            }
            case ProtocolConstants.OP_HANDSHAKE_START: {
                ByteBuffer message_length_bytes = Utils.getByteBuffer(4);
                readBuffer(channel, message_length_bytes);
                int message_length = message_length_bytes.getInt(0);
                if (message_length > NetworkConstants.MAX_MESSAGE_LENGTH)
                    throw new CorruptDataException("Received message is too big " + message_length);
                ByteBuffer message_bytes = Utils.getByteBuffer((long) message_length);
                readBuffer(channel, message_bytes);
                message_bytes.position(0);
                byte[] msg = new byte[message_length];
                message_bytes.get(msg);
                message_bytes.clear();
                NetworkManager.getInstance().onHandShakeStart(ip, port, msg);
                break;
            }

            case ProtocolConstants.OP_HANDSHAKE_END: {
                ByteBuffer message_length_bytes = Utils.getByteBuffer(4);
                readBuffer(channel, message_length_bytes);
                int message_length = message_length_bytes.getInt(0);
                if (message_length > NetworkConstants.MAX_MESSAGE_LENGTH)
                    throw new CorruptDataException("Received message is too big " + message_length);
                ByteBuffer message_bytes = Utils.getByteBuffer(Convert.intToLong(message_length));
                readBuffer(channel, message_bytes);
                message_bytes.position(0);
                byte[] msg = new byte[message_length];
                message_bytes.get(msg);
                message_bytes.clear();
                NetworkManager.getInstance().onHandShakeEnd(ip, port, msg);
                break;
            }

            case ProtocolConstants.OP_NEW_NICK: {
                int nick_length = Convert.byteToInt(readByte(channel));
                if (nick_length > NetworkConstants.MAX_NICKNAME_LENGTH)
                    throw new CorruptDataException("Too big nickname " + nick_length);
                ByteBuffer nick_bytes = Utils.getByteBuffer(nick_length);
                readBuffer(channel, nick_bytes);
                String nick_name = new String(nick_bytes.array());
                NetworkManager.getInstance().onNewNick(ip, port, nick_name);
                break;
            }
            case ProtocolConstants.OP_CALL_REQUEST: {
                NetworkManager.getInstance().onCallRequest(ip, port);
                break;
            }

            case ProtocolConstants.OP_MESSAGE: {
                ByteBuffer message_length_bytes = Utils.getByteBuffer(4);
                readBuffer(channel, message_length_bytes);
                int message_length = message_length_bytes.getInt(0);
                if (message_length > NetworkConstants.MAX_MESSAGE_LENGTH)
                    throw new CorruptDataException("Received message is too big " + message_length);
                ByteBuffer message_bytes = Utils.getByteBuffer(Convert.intToLong(message_length));
                readBuffer(channel, message_bytes);
                message_bytes.position(0);
                byte[] msg = new byte[message_length];
                message_bytes.get(msg);
                message_bytes.clear();
                NetworkManager.getInstance().onNewMessage(ip, port, msg);
                break;
            }
            case ProtocolConstants.OP_ROOM: {
                ByteBuffer message_length_bytes = Utils.getByteBuffer(4);
                readBuffer(channel, message_length_bytes);
                int message_length = message_length_bytes.getInt(0);
                if (message_length > NetworkConstants.MAX_MESSAGE_LENGTH)
                    throw new CorruptDataException("Received message is too big " + message_length);
                ByteBuffer message_bytes = Utils.getByteBuffer(Convert.intToLong(message_length));
                readBuffer(channel, message_bytes);
                String message = new String(message_bytes.array());
                message_bytes.clear();
                message_length_bytes.clear();
                NetworkManager.getInstance().onNewRoom(ip, port, message);
                break;
            }

            case ProtocolConstants.OP_AUDIO: {
                ByteBuffer message_length_bytes = Utils.getByteBuffer(4);
                readBuffer(channel, message_length_bytes);
                int message_length = message_length_bytes.getInt(0);
                if (message_length > NetworkConstants.MAX_MESSAGE_LENGTH)
                    throw new CorruptDataException("Received message is too big " + message_length);
                ByteBuffer message_bytes = Utils.getByteBuffer(Convert.intToLong(message_length));
                readBuffer(channel, message_bytes);
                message_bytes.position(0);
                byte[] msg = new byte[message_length];
                message_bytes.get(msg);
                message_bytes.clear();
                NetworkManager.getInstance().onAudio(ip, port, msg);
                break;
            }
            case ProtocolConstants.OP_MESS: {
                ByteBuffer message_length_bytes = Utils.getByteBuffer(4);
                readBuffer(channel, message_length_bytes);
                int message_length = message_length_bytes.getInt(0);
                if (message_length > NetworkConstants.MAX_MESSAGE_LENGTH)
                    throw new CorruptDataException("Received message is too big " + message_length);
                ByteBuffer message_bytes = Utils.getByteBuffer(Convert.intToLong(message_length));
                readBuffer(channel, message_bytes);
                message_bytes.position(0);
                byte[] msg = new byte[message_length];
                message_bytes.get(msg);
                message_bytes.clear();
                NetworkManager.getInstance().onMess(ip, port, msg);
                break;
            }
            default: {
                throw new CorruptDataException("Unknown opcode 0x" + Convert.byteToHex(opcode));
            }
        }
    }

    /**
     * This function readOne Byte from SocketChannel
     *
     * @param channel the channel to be written
     * @return the actual byte
     * @throws IOException
     * @throws EndOfStreamException
     */
    private static byte readByte(SocketChannel channel) throws IOException, EndOfStreamException {
        ByteBuffer buffer = Utils.getByteBuffer(1);
        readBuffer(channel, buffer);
        return buffer.get(0);
    }

    /**
     * This function read a buffer from the channel into @param buffer
     *
     * @param channel the channel to be written
     * @param buffer  the buffer to put the data
     * @throws IOException
     * @throws EndOfStreamException
     */
    private static void readBuffer(SocketChannel channel, ByteBuffer buffer) throws IOException, EndOfStreamException {
        int byte_count;
        byte_count = channel.read(buffer);
        if (byte_count == -1)
            throw new EndOfStreamException();
    }
}
