package ro.mta.se.chat.models;


import ro.mta.se.chat.crypto.AESCipher;
import ro.mta.se.chat.factory.abstracts.AbstractMessage;
import ro.mta.se.chat.network.NetworkConstants;
import ro.mta.se.chat.singleton.NetworkManager;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Alberto-Daniel on 11/10/2015.
 * This is the view which stores informations about users
 */
public class UserModel {
    /**
     * The status of the user
     */
    public enum Status {
        CONNECTED, CONNECTING, DISCONNECTED
    }

    /**
     * The inital value of status of the user
     */
    private Status status = Status.DISCONNECTED;

    /**
     * The version of the application
     */
    private byte[] version = new byte[3];

    /**
     * The nickname of the user
     */
    protected String nickname;

    /**
     * The username of the model
     */
    protected String username;

    /**
     * The ip of the model
     */
    protected String ip;

    /**
     * The port of the model
     */
    protected int port;

    /**
     * The password of the model
     */
    protected String password;

    /**
     * The current room of the model
     */
    private String room;

    /**
     * The on value.True if the user is on
     */
    boolean on;

    /**
     * The history of the model
     */
    private HashMap<String, HistoryModel> history = new HashMap<>();

    /**
     * The constructor of the application
     *
     * @param ip   the ip of the user
     * @param port the port of the user
     */
    public UserModel(String ip, int port) {
        this(ip, port, null);
        on = true;
    }

    /**
     * The default constructor of the model
     */
    public UserModel() {
    }

    /**
     * The constructor with 3 params
     *
     * @param ip       the ip of the user
     * @param port     the port of the user
     * @param nickname the nickname of the user
     */
    public UserModel(String ip, int port, String nickname) {
        this.ip = ip;
        this.port = port;
        this.nickname = nickname;
        this.room = ro.mta.se.chat.views.ViewConstants.ROOM_1;

    }

    /**
     * This function sets the ip of the user
     *
     * @param ip the ip of the user
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * This function sets the port value to the user
     *
     * @param port the port value to the user
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * This function sets the nickname of the user
     *
     * @param nickname the nickname of the use
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * This function sets the username of the user
     *
     * @param username the username of the user
     * @throws NullPointerException if the (@param username) is null
     */
    public void setUsername(String username) {
        if(username == null)
            throw new NullPointerException("The username cannot be null");
        this.username = username;
    }

    /**
     * This function returns the username
     *
     * @return returns the username of the user
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * This function get the port of the user
     *
     * @return the port of the user
     */
    public int getPort() {
        return this.port;
    }

    /**
     * This function get the nickname of the user
     *
     * @return the nickname of the user
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * This function returns the ip of the user
     *
     * @return the ip of the user
     */
    public String getIP() {
        return ip;
    }

    /**
     * This function set the password value of the user
     *
     * @param password the given password
     * @throws NullPointerException if the (@param password) is null
     */
    public void setPassword(String password)throws NullPointerException{
        if(password == null)
            throw new NullPointerException("The password cannot be null");
        this.password = password;
    }

    /**
     * This function returns the password of the user
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * This function returns the hashCode of the user
     *
     * @return the hashCode of the user
     */
    public int hashCode() {
        return (ip + port).hashCode();
    }

    /**
     * This function check if an user is equal with anotherone
     *
     * @param userModel the source user
     * @return true if equal
     */
    public boolean equals(Object userModel) {
        if (userModel == null)
            return false;
        return this.hashCode() == userModel.hashCode();
    }

    /**
     * This function represents the UserModel in string format
     *
     * @return the representation of string
     */
    public String toString() {
        return ip + ":" + port + "Status" + status + "Nickname" + nickname + "version " + versionAsString();
    }

    /**
     * This function gets the version as string
     *
     * @return version as string
     */
    private String versionAsString() {
        return version[0] + "." + version[1] + "." + version[2];
    }

    /**
     * This function gets the key of the user
     *
     * @return the key of the user
     */
    public String getKey() {
        return ip + NetworkConstants.KEY_SEPARATOR + port;
    }

    /**
     * This function gets the status of the string
     *
     * @return the status of the string
     */
    public Status getStatus() {
        return status;
    }

    /**
     * This function set status of the string
     *
     * @param status set status of string
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * This function sets the version of the string
     *
     * @param version the version
     * @throws NullPointerException if @param version is null
     */
    public void setVersion(byte[] version)throws NullPointerException{
        if(version == null)
            throw new NullPointerException("Version type could not be null");
        this.version = version;
    }

    /**
     * This function add a new message to the history view
     *
     * @param incomeMsg the income message
     * @param fromMe    true if from me equal true
     * @throws NullPointerException if the (@param incomeMsg) is null
     */
    public void addNewMessage(AbstractMessage incomeMsg, boolean fromMe)throws NullPointerException {
        if(incomeMsg == null)
            throw new NullPointerException("Could not add abstract message with null value");
        Collection<HistoryModel> history = this.history.values();
        HistoryModel temp = null;
        long timestamp = 0;
        for (HistoryModel model : history) {
            if (model.getTimestampStart() > timestamp) {
                if (model.getUser().getKey().equals(incomeMsg.getUser().getKey())) {
                    temp = model;
                    timestamp = model.getTimestampStart();
                }
            }
        }
        if (temp != null) {
            incomeMsg.setAesKey(temp.getAesKey());
            if (fromMe) {
                incomeMsg.setEncryptMode();
            }
            temp.addNewMsg(incomeMsg.getContent(), fromMe);
            if (!fromMe)
                incomeMsg.setDecryptMode();
        }

    }

    /**
     * This function add a new entry to the history
     *
     * @param destination the destination user
     * @param aesKey      the aes key
     * @throws NullPointerException if @param destination equal null or aesKey equal null
     */
    public void addNewHistoryEntry(UserModel destination, byte[] aesKey) {
        if(destination == null || aesKey == null)
            throw new  NullPointerException();
        HistoryModel model = new HistoryModel(destination);
        model.start(aesKey);
        String key = model.getKey();
        this.history.put(key, model);
    }

    /**
     * This function returns the history form a given user
     *
     * @param user the given user
     * @return returns the history
     */
    public HistoryModel getHistory(UserModel user) {
        if(user == null)
            throw new NullPointerException();
        long timestamp = 0;
        HistoryModel temp = null;
        for (HistoryModel model : history.values()) {
            if (model.getTimestampStart() > timestamp) {
                if (model.getUser().getKey().equals(user.getKey())) {
                    temp = model;
                    timestamp = model.getTimestampStart();
                }
            }
        }
        return temp;
    }

    /**
     * This function set the room with given value
     *
     * @param room the room of the model
     * @throws NullPointerException if the room is null
     */
    public void setRoom(String room) {

        if(room == null)
            throw new NullPointerException();
        this.room = room;
    }

    /**
     * This function returns the room of the userModel
     *
     * @return the room
     */
    public String getRoom() {
        return this.room;
    }

}
