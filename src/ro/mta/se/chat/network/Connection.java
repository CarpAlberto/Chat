package ro.mta.se.chat.network;

import ro.mta.se.chat.exceptions.EndOfStreamException;
import ro.mta.se.chat.factory.PacketFactory;
import ro.mta.se.chat.network.enums.ConnectionStatus;
import ro.mta.se.chat.singleton.NetworkManager;
import ro.mta.se.chat.utils.Convert;
import ro.mta.se.chat.utils.logging.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Alberto-Daniel on 11/26/2015.
 * This class is responsable for handle each connection
 */
public class Connection {

    /**
     * The channel of communication
     */
    private SocketChannel connection = null;
    /**
     * The status of conncetion
     */
    private ConnectionStatus status;
    /**
     * The send queue of packets
     */
    private Queue<ByteBuffer> send_queue = new ConcurrentLinkedQueue<ByteBuffer>();
    /**
     * The send thread of data
     */
    private SendPacketsThread send_thread;
    /**
     * The receive thread of data
     */
    private ReceiveThread receive_thread;
    /**
     * The ip of the sender
     */
    private String ip;
    /**
     * The port of the sender
     */
    private int port;

    /**
     * The connection constructor which takes as parameter the channel of communication
     *
     * @param connection the channl of communication
     */
    public Connection(SocketChannel connection) {
        this.connection = connection;
        port = connection.socket().getPort();
        ip = Convert.ipToString(connection.socket().getInetAddress().getAddress());
        try {
            this.connection.configureBlocking(true);
            this.connection.socket().setKeepAlive(true);
            int peerPort = connection.socket().getPort();
            String peerIP = Convert.ipToString(connection.socket().getInetAddress().getAddress());
            receive_thread = new ReceiveThread(this, peerIP + ":" + peerPort);
            receive_thread.start();
            send_thread = new SendPacketsThread(peerIP + ":" + peerPort);
            send_thread.start();
        } catch (IOException e) {
            Logger.error("Error on connecting");
        }
        status = ConnectionStatus.CONNECTED;
    }

    /**
     * The constructor which initialize the communication
     *
     * @param ip       the ip target
     * @param port     the port target
     * @param nickname the nickanme of the source user
     * @param room     the room of the source user
     */
    public Connection(final String ip, final int port, final String nickname, final String room) {
        this.ip = ip;
        this.port = port;
        final Connection con = this;
        new Thread(new Runnable() {
            public void run() {
                status = ConnectionStatus.CONNECTING;
                try {
                    connection = SocketChannel.open();
                    connection.socket().connect(new InetSocketAddress(ip, port), NetworkConstants.CONNECT_TIMEOUT);
                    int peerPort = connection.socket().getPort();
                    String peerIP = Convert.ipToString(connection.socket().getInetAddress().getAddress());
                    status = ConnectionStatus.CONNECTED;
                    NetworkManager.getInstance().onTCPConnectionOpened(con);
                    receive_thread = new ReceiveThread(con, peerIP + ":" + peerPort);
                    receive_thread.start();
                    send_thread = new SendPacketsThread(peerIP + ":" + peerPort);
                    send_thread.start();
                    sendPacket(PacketFactory.getHelloPacket(nickname, room));
                } catch (IOException e) {
                    Logger.warn("User was disconnected");
                    status = ConnectionStatus.DISCONNECTED;
                    NetworkManager.getInstance().onTCPConnectingFailed(con, e);
                    return;
                }
            }
        }).start();
    }

    /**
     * This function disconnect the cuurent connection
     */
    public void disconnect() {
        try {
            connection.close();
        } catch (IOException e) {
            Logger.error("Error while closing socket");
        }
        receive_thread.stopThread();
        send_thread.stopThread();
    }

    /**
     * Ths function sends a new packet into the send queue
     *
     * @param packet the packet to be send
     */
    public void sendPacket(ByteBuffer packet) {
        packet.position(0);
        send_queue.add(packet);
        if (send_thread != null)
            if (send_thread.isSleeping())
                send_thread.wakeUp();
    }

    /**
     * This function gets the ip of the source user
     *
     * @return gets the ip of the source user
     */
    public String getIP() {
        return ip;
    }

    /**
     * This function gets the port of the source user
     *
     * @return the port of the source user
     */
    public int getPort() {
        return port;
    }

    /**
     * This function extends class thread and is responable
     * for sending data across the connection channel
     */
    class SendPacketsThread extends Thread {

        /**
         * true if we are on looping stage
         * false otherwise
         */
        private boolean loop = true;
        /**
         * true if the thread is sleeping
         * false otherwise
         */
        private boolean is_sleeping = false;


        /**
         * The constructor passed as paramaeter
         *
         * @param id the id of thread
         */
        public SendPacketsThread(String id) {
            super("Packet sender for " + id);
        }

        /**
         * Function that executes asyncronuous
         * This function takes one packet if exists at least one packet
         */
        public void run() {
            loop = true;
            while (loop) {
                is_sleeping = false;
                if (!connection.isConnected())
                    break;
                if (send_queue.isEmpty()) {
                    is_sleeping = true;
                    synchronized (this) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            Logger.error("Interuption exception");
                        }
                    }
                    continue;
                }
                ByteBuffer packet = send_queue.poll();
                packet.position(0);
                try {
                    connection.write(packet);
                } catch (Exception e) {
                    Logger.warn("Warning : There was an error on writing packet");
                    if (!connection.isConnected())
                        break;
                }
            }
        }

        /**
         * this function stops the current tread
         */
        public void stopThread() {
            loop = false;
            synchronized (this) {
                this.notify();
            }
        }

        /**
         * This function returns the state of the thread
         *
         * @return true if sleepeing = true  false otherwise
         */
        public boolean isSleeping() {
            return is_sleeping;
        }

        /**
         * This function wakes up the current thread
         */
        public void wakeUp() {
            synchronized (this) {
                this.notify();
            }
        }
    }

    /**
     * This class is responsable for receiving new packets from current channel
     */
    class ReceiveThread extends Thread {

        /**
         * True if we are in the state of lloping
         */
        private boolean loop = true;

        /**
         * The income network connection
         */
        private Connection network_connection;

        /**
         * The constructor with two parameters
         *
         * @param network_connection the network connection
         * @param id                 the id of thread
         */
        public ReceiveThread(Connection network_connection, String id) {
            super("Packet receiver for " + id);
            this.network_connection = network_connection;
        }

        /**
         * The function that executes asyncronuous
         * It reads a new packet and rises the event accordingly
         */
        public void run() {
            loop = true;
            while (loop) {
                try {
                    NetworkPacketReader.readPacket(connection);
                } catch (Throwable cause) {
                    Logger.error(cause.getStackTrace().toString());
                    if (cause instanceof EndOfStreamException) {
                        NetworkManager.getInstance().onTCPConnectionClosed(network_connection);
                        return;
                    }
                    if (!connection.isConnected()) {
                        NetworkManager.getInstance().onTCPConnectionClosed(network_connection);
                        return;
                    }
                }
            }
        }

        /**
         * This function stops the current thread
         */
        public void stopThread() {
            loop = false;
            synchronized (this) {
                this.notify();
            }
        }
    }


}
