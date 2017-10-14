package ro.mta.se.chat.singleton;

import javafx.util.Pair;
import ro.mta.se.chat.crypto.AESCipher;
import ro.mta.se.chat.crypto.Certificate;
import ro.mta.se.chat.crypto.PrivateKeyLoader;
import ro.mta.se.chat.factory.PacketFactory;
import ro.mta.se.chat.factory.abstracts.AbstractMessage;
import ro.mta.se.chat.models.UserModel;
import ro.mta.se.chat.network.Connection;
import ro.mta.se.chat.network.NetworkConstants;
import ro.mta.se.chat.utils.ConfigurationManager;
import ro.mta.se.chat.utils.Convert;
import ro.mta.se.chat.utils.IPAddress;
import ro.mta.se.chat.utils.logging.Logger;
import ro.mta.se.chat.utils.Utils;


import javax.crypto.BadPaddingException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by Alberto-Daniel on 11/26/2015.
 */
public class NetworkManager {

    /**
     * This is the unique instance of Network manager class
     */
    private static volatile NetworkManager instance = null;

    private HashMap<String, LinkedList<Integer>> connections_found = new HashMap<>();

    /**
     * This map contains al the connections
     */
    private Map<String, Connection> connections = new ConcurrentHashMap<String, Connection>();

    /**
     * This is the receiver thread
     */
    private ConnectionReceiver receiver;

    /**
     * true if the manager started
     */
    private boolean is_started = false;
    /**
     * The connected user
     */
    UserModel connectedUser;

    /**
     * This function get the unique instance of netork manager object
     *
     * @return the instance of the network manager
     */
    public static NetworkManager getInstance() {
        if (instance == null)
            synchronized (NetworkManager.class) {
                instance = new NetworkManager();
            }
        return instance;
    }

    /**
     * This function perform port scanning and fix start th receiver thread
     */
    public void start() {
        String network_start = ConfigurationManager.getInstance().getValue(NetworkConstants.IP_START);
        String network_end = ConfigurationManager.getInstance().getValue(NetworkConstants.IP_END);
        int port_start = Integer.parseInt(ConfigurationManager.getInstance().getValue(NetworkConstants.LISTEN_TCP_PORT_KEY_START));
        int port_end = Integer.parseInt(ConfigurationManager.getInstance().getValue(NetworkConstants.LISTEN_TCP_PORT_KEY_END));
        boolean done = false;
        IPAddress end = new IPAddress(network_end);
        IPAddress iterator = new IPAddress(network_start);
        while (!done) {
            connections_found.put(iterator.toString(), new LinkedList<>());
            for (int i = port_start; i <= port_end; i++) {
                try {
                    Socket socket = new Socket(iterator.toString(), i);
                    connections_found.get(iterator.toString()).add(i);
                } catch (IOException e) {
                }
            }
            if (iterator.equals(end))
                done = true;
            iterator = iterator.next();
        }
        receiver = new ConnectionReceiver();
        receiver.start();
        is_started = true;
    }

    /**
     * This function stop all the connections
     */
    public void stop() {
        is_started = false;
        receiver.stopThread();
        for (Connection connection : connections.values())
            connection.disconnect();
    }

    /**
     * This function set the connected user of the application
     *
     * @param model the user to be setted
     */
    public synchronized void setConnected(UserModel model) {
        this.connectedUser = model;
    }

    /**
     * This function gets the connected user
     *
     * @return the connected user
     */
    public synchronized UserModel getConnnected() {
        return connectedUser;
    }

    /**
     * This function check if that connection already exists
     *
     * @param ip   the ip of the connection
     * @param port the port of connection
     * @return true if has connection false otherwise
     */
    public boolean hasConnection(String ip, int port) {
        return connections.containsKey(ip + NetworkConstants.KEY_SEPARATOR + port);
    }

    /**
     * This function connect the current user with the given data
     *
     * @param ip       the given ip
     * @param port     the given port
     * @param nickname the given nickname
     */
    public void connect(String ip, int port, String nickname) {
        if (hasConnection(ip, port))
            return;
        Connection connection = new Connection(ip, port, nickname, connectedUser.getRoom());
        connections.put(ip + NetworkConstants.KEY_SEPARATOR + port, connection);
    }

    /**
     * This function sends an HelloPacket
     *
     * @param ip       the destination ip
     * @param port     the destination port
     * @param nickName the source nickname
     * @param room     the source room
     */
    public void sendHELLO(String ip, int port, String nickName, String room) {
        ByteBuffer packet = PacketFactory.getHelloPacket(nickName, room);
        Connection connection = connections.get(ip + NetworkConstants.KEY_SEPARATOR + port);
        connection.sendPacket(packet);
    }

    /**
     * This function sends a new nickname packet
     *
     * @param ip       the destination ip
     * @param port     the destination port
     * @param nickName the destination nickname
     */
    public void sendNewNick(String ip, int port, String nickName) {
        ByteBuffer packet = PacketFactory.getChangeNickNamePacket(nickName);
        Connection connection = connections.get(ip + NetworkConstants.KEY_SEPARATOR + port);
        connection.sendPacket(packet);
    }

    /**
     * This function sends a message
     *
     * @param ip   the ip of the destination
     * @param port the destination port
     * @param msg  the msg
     */
    public void sendMessage(String ip, int port, AbstractMessage msg) {
        ByteBuffer packet = msg.getPacket();
        Connection connection = connections.get(ip + NetworkConstants.KEY_SEPARATOR + port);
        connection.sendPacket(packet);
    }

    /**
     * This function sends a changed Room packet
     *
     * @param newRoom the  newRoom
     */
    public void sendChangeRoom(String newRoom) {
        ByteBuffer packet = PacketFactory.getNewRoomPacket(newRoom);
        for (Connection ct : connections.values()) {
            ct.sendPacket(packet);
        }
    }

    /**
     * This function sends a handshake start packet
     *
     * @param ip   the source ip
     * @param port the source port
     */
    public void sendHandShake(String ip, int port) {
        ByteBuffer packet = PacketFactory.getStartHandShake();
        Connection connection = connections.get(ip + NetworkConstants.KEY_SEPARATOR + port);
        connection.sendPacket(packet);
    }

    /**
     * This function sends a handshake end packet
     *
     * @param ip   the source ip
     * @param port the source port
     * @param data the aes key
     */
    public void sendHandShakeEnd(String ip, int port, byte[] data) {
        ByteBuffer packet = PacketFactory.getEndHandShake(data);
        Connection connection = connections.get(ip + NetworkConstants.KEY_SEPARATOR + port);
        connection.sendPacket(packet);
    }

    /**
     * This function sends a call request
     *
     * @param ip   the source ip
     * @param port the source port
     */
    public void sendCallRrequest(String ip, int port) {
        ByteBuffer packet = PacketFactory.getCallPacket();
        Connection connection = connections.get(ip + NetworkConstants.KEY_SEPARATOR + port);
        connection.sendPacket(packet);
    }

    /**
     * This function rises on new hello
     *
     * @param ip       the source ip
     * @param port     the source port
     * @param version  the source version
     * @param nickname the nickname
     * @param room     the room user
     */
    public void onHello(String ip, int port, byte[] version, String nickname, String room) {

        UserManager.getInstance().onReceiveUserHello(ip, port, nickname, version);
    }

    /**
     * This function rises on change of nickname
     *
     * @param ip       the source ip
     * @param port     the source port
     * @param nickName the nickname
     */
    public void onNewNick(String ip, int port, String nickName) {
        UserManager.getInstance().onReceiveUserChangedNickName(ip, port, nickName);
    }

    /**
     * This function rises on new message
     *
     * @param ip      the source ip
     * @param port    the source port
     * @param message the message
     */
    public void onNewMessage(String ip, int port, byte[] message) {

        UserManager.getInstance().onReceivedNewMessage(ip, port, message);
    }

    /**
     * This function rises on new change room
     *
     * @param ip      the source ip
     * @param port    the source port
     * @param message the newRoom
     */
    public void onNewRoom(String ip, int port, String message) {
        UserManager.getInstance().onReceiveUserChangedRoom(ip, port, message);
    }

    /**
     * This function rises on the end of handshake
     *
     * @param ip      the source ip
     * @param port    the source port
     * @param message teh aes key
     */
    public void onHandShakeStart(String ip, int port, byte[] message) {
        X509Certificate certificate = Certificate.loadFromByteArray(message);
        byte[] key = AESCipher.generateKey();
        byte[] encryptedKey = null;
        PublicKey pubKey = certificate.getPublicKey();
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            encryptedKey = cipher.doFinal(key);
            UserManager.getInstance().onReceiveHandShake(ip, port, key);
            sendHandShakeEnd(ip, port, encryptedKey);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function rises on end of handshake
     *
     * @param ip   the source ip
     * @param port the source port
     * @param key  the aes key
     */
    public void onHandShakeEnd(String ip, int port, byte[] key) {
        PrivateKey privateKey = PrivateKeyLoader.loadPrivateKey(Utils.getPrivateKeyPath(), this.connectedUser.getUsername(), connectedUser.getPassword());
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            key = cipher.doFinal(key);
            UserManager.getInstance().onReceiveHandShakeEnd(ip, port, key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function rises on a call request
     *
     * @param ip   the source ip
     * @param port the source port
     */
    public void onCallRequest(String ip, int port) {
        UserManager.getInstance().onReceivedCallRequest(ip, port);
    }

    /**
     * This function rises on an Audio data received
     *
     * @param ip   the ip source
     * @param port the port source
     * @param data the audio data received
     */
    public void onAudio(String ip, int port, byte[] data) {
        UserManager.getInstance().onReceiveAudioData(ip, port, data);
    }

    /**
     * This function rises on a new Mess recieved
     *
     * @param ip   the ip of the source
     * @param port the port source
     * @param mess the mess
     */
    public void onMess(String ip, int port, byte[] mess) {
        UserManager.getInstance().onReceiveMessData(ip, port, mess);
    }

    /**
     * This function rises on an TCP connection opened
     *
     * @param channel the communication channel
     */
    public void onTCPConnectionOpened(Connection channel) {
        UserManager.getInstance().onReceiveUserConnected(channel.getIP(), channel.getPort());
    }

    /**
     * This function rises on an Incoming TCP connection
     *
     * @param channel the communication channel
     */
    public void onIncomingTCPConnection(Connection channel) {

        sendHELLO(channel.getIP(), channel.getPort(), connectedUser.getNickname(), connectedUser.getRoom());
        UserManager.getInstance().onReceiveIncomingUserConnection(channel.getIP(), channel.getPort());
        sendHandShake(channel.getIP(), channel.getPort());
    }

    /**
     * This function rises on a TCP connection closed
     *
     * @param channel the communication channel
     */
    public void onTCPConnectionClosed(Connection channel) {
        connections.remove(channel.getIP() + NetworkConstants.KEY_SEPARATOR + channel.getPort());
        UserManager.getInstance().onReceiveUserDisconnected(channel.getIP(), channel.getPort());
    }

    /**
     * This function rises on a TCP connectiion failed
     *
     * @param channel the channel of communication
     * @param cause   the cause of failure
     */
    public void onTCPConnectingFailed(Connection channel, Throwable cause) {
        connections.remove(channel.getIP() + NetworkConstants.KEY_SEPARATOR + channel.getPort());
        UserManager.getInstance().onReceiveUserConnectingFailed(channel.getIP(), channel.getPort(), cause);
    }

    /**
     * This function gets the availabale combination ip port
     *
     * @return one pair containg the cobination ip - port
     * @throws Exception
     */
    private javafx.util.Pair<String, Integer> getAvaiablePorts() throws Exception {
        String port_start_string = ConfigurationManager.getInstance().getValue(NetworkConstants.LISTEN_TCP_PORT_KEY_START);
        String port_end_string = ConfigurationManager.getInstance().getValue(NetworkConstants.LISTEN_TCP_PORT_KEY_END);
        String network_start = ConfigurationManager.getInstance().getValue(NetworkConstants.IP_START);
        String network_end = ConfigurationManager.getInstance().getValue(NetworkConstants.IP_END);
        if (port_start_string == null && port_end_string == null) {
            throw new Exception("Could not connect to any port");
        }
        int port_start = Integer.parseInt(ConfigurationManager.getInstance().getValue(NetworkConstants.LISTEN_TCP_PORT_KEY_START));
        int port_end = Integer.parseInt(ConfigurationManager.getInstance().getValue(NetworkConstants.LISTEN_TCP_PORT_KEY_END));
        boolean done = false;
        IPAddress end = new IPAddress(network_end);
        IPAddress iterator = new IPAddress(network_start);
        while (!done) {
            for (int i = port_start; i < port_end; i++) {
                if (!connections_found.get(iterator.toString()).contains(i)) {
                    return new Pair<>(iterator.toString(), i);
                }
            }
            if (iterator.equals(end))
                done = true;
            iterator = iterator.next();
        }
        return null;
    }

    /**
     * This function returns the all host found opened
     *
     * @return all hosts found opened
     */
    public HashMap<String, LinkedList<Integer>> getPorts() {
        return this.connections_found;
    }

    /**
     * This class extends Thread and is responsable for receiving new
     * connections
     */
    private class ConnectionReceiver extends Thread {
        /**
         * True meaning looping equals true
         */
        private boolean loop = true;
        /**
         * The server socket
         */
        private ServerSocketChannel server_socket = null;

        /**
         * The constructor of the receiver
         */
        public ConnectionReceiver() {
            super("Connection receiver");
        }

        /**
         * This function runs asyncrounuous and handle
         * incoming connections
         */
        public void run() {
            loop = true;
            try {
                server_socket = ServerSocketChannel.open();
                Pair<String, Integer> connections = getAvaiablePorts();
                int port = NetworkConstants.DEFAULT_PORT;
                String network = NetworkConstants.DEFAULT_NETWORK;
                if (connections != null) {
                    port = connections.getValue();
                    network = connections.getKey();
                }
                UserModel theModel = NetworkManager.getInstance().getConnnected();
                if (theModel == null)
                    theModel = new UserModel(network, port);
                else {
                    theModel.setIp(network);
                    theModel.setPort(port);
                }
                NetworkManager.getInstance().setConnected(theModel);
                server_socket.socket().bind(new InetSocketAddress(network, port));
            } catch (IOException e) {
                Logger.error("Error while connecting");
                return;
            } catch (Exception e) {
                Logger.error("Error while connecting");
                return;
            }
            while (loop) {
                try {
                    SocketChannel channel = server_socket.accept();
                    String ip = Convert.ipToString(channel.socket().getInetAddress().getAddress());
                    int port = channel.socket().getPort();
                    Connection connection = new Connection(channel);
                    connections.put(ip + NetworkConstants.KEY_SEPARATOR + port, connection);
                    onIncomingTCPConnection(connection);
                } catch (IOException e) {
                    Logger.error("Error incoming new connection ");
                    if (!server_socket.isOpen())
                        return;
                }
            }
        }

        /**
         * This function stops the current thread
         */
        public void stopThread() {
            if (server_socket != null)
                try {
                    server_socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            loop = false;
            synchronized (this) {
                this.notify();
            }


        }
    }
}
