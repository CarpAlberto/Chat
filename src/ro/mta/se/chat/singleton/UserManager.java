package ro.mta.se.chat.singleton;

import ro.mta.se.chat.adapters.DataAdapter;
import ro.mta.se.chat.audio.AudioRecorder;
import ro.mta.se.chat.exceptions.ViolationAccesException;
import ro.mta.se.chat.factory.MessageFactory;
import ro.mta.se.chat.factory.abstracts.AbstractMessage;
import ro.mta.se.chat.models.HistoryModel;
import ro.mta.se.chat.models.UserModel;
import ro.mta.se.chat.network.NetworkConstants;
import ro.mta.se.chat.proxy.DataAdapterProxy;
import ro.mta.se.chat.utils.Utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Alberto-Daniel on 11/26/2015.
 * This class is responsable for managing users from database
 */
public class UserManager {

    /**
     * The instance of UserManager
     */
    private static volatile UserManager instance = null;

    /**
     * If is true then audio data is transfering
     */
    private volatile boolean isTransferring;

    /**
     * This is the map containing the users and the coresponding key
     */
    private Map<String, UserModel> users_list = new ConcurrentHashMap<String, UserModel>();

    /**
     * This is an proxy for accesing data
     */
    private DataAdapterProxy proxy;

    /**
     * This function returns the instance of UserManager object
     *
     * @return the instance of UserManager
     */
    public static UserManager getInstance() {
        if (instance == null) {
            synchronized (UserManager.class) {
                instance = new UserManager();
            }
        }
        return instance;
    }

    /**
     * This is the private constructor of userManager
     */
    private UserManager() {
        this.proxy = new DataAdapterProxy(new DataAdapter(), NetworkManager.getInstance().getConnnected());
    }

    /**
     * Syncronized method which returns true if audio data is transferring
     *
     * @return true if audio data is transferring
     */
    public synchronized boolean isTransferring() {
        return isTransferring;
    }

    /**
     * Enter in transfer mode
     *
     * @param bool value which tell us if audio data is going to be send
     */
    public synchronized void setTransferMode(boolean bool) {
        this.isTransferring = bool;
    }

    /**
     * This function tell us if an given user exists already into database
     *
     * @param ip   the ip of the user
     * @param port the port of the user
     * @return true if exists false otherwise
     */
    public boolean hasUser(String ip, int port) {
        return (users_list.containsKey(ip + NetworkConstants.KEY_SEPARATOR + port));
    }

    /**
     * This function gets the user with ip and port given
     *
     * @param ip   the ip of the user
     * @param port the port of the user
     * @return the UserModel
     */
    public UserModel getUser(String ip, int port) {
        return users_list.get(ip + NetworkConstants.KEY_SEPARATOR + port);
    }

    /**
     * This function returns the Molde given the key
     *
     * @param key The key of the user
     * @return the UserModel
     */
    public UserModel getUser(String key) {
        return users_list.get(key);
    }

    /**
     * This function creates an user with ip and port given
     *
     * @param ip   of the new user
     * @param port of the new user
     * @return The New User
     */
    public UserModel createUser(String ip, int port) {
        if (hasUser(ip, port)) {
            return getUser(ip, port);
        }
        UserModel userModel = new UserModel(ip, port);
        users_list.put(userModel.getKey(), userModel);
        return userModel;
    }

    /**
     * This function remove the given user
     *
     * @param user user  to be remover
     */
    public void removeUser(UserModel user) {
        users_list.remove(user.getKey());
    }

    /**
     * This function connect the user with the @param theModel user
     *
     * @param theModel the user with who we want to connect
     */
    public void connect(UserModel theModel) {
        if (theModel.getStatus() != UserModel.Status.DISCONNECTED) {
            return;
        }
        theModel.setStatus(UserModel.Status.CONNECTING);
        NetworkManager.getInstance().connect(theModel.getIP(), theModel.getPort(), NetworkManager.getInstance().getConnnected().getNickname());
    }

    /**
     * This function connect the user with another user with ip and port given
     *
     * @param ip   the ip given
     * @param port the port given
     * @return the new UserModel
     */
    public UserModel connect(String ip, int port) {
        if (UserManager.getInstance().hasUser(ip, port))
            return null;
        UserModel user = UserManager.getInstance().createUser(ip, port);
        UserManager.getInstance().connect(user);
        return user;
    }

    /**
     * This function returns all the users fron given roomName
     *
     * @param roomName the roomName
     * @return a Map containg the users an asociated keys
     */
    public HashMap<String, String> getUsersRoom(String roomName) {
        HashMap<String, String> maps = new HashMap<>();
        Collection<UserModel> users = users_list.values();
        for (UserModel model : users) {
            if (model.getRoom().equals(roomName) && model.getNickname() != null) {
                maps.put(model.getNickname(), model.getKey());
            }
        }
        return maps;
    }

    /**
     * This function returns all the users from given roomName
     *
     * @param roomName the roomName
     * @return a Map containg a map between Users and a key
     */
    public HashMap<String, UserModel> getModelsByRoom(String roomName) {
        HashMap<String, UserModel> maps = new HashMap<>();
        Collection<UserModel> users = users_list.values();
        for (UserModel model : users) {
            if (model.getRoom().equals(roomName) && model.getNickname() != null) {
                maps.put(model.getKey(), model);
            }
        }
        return maps;
    }

    /**
     * This function returns the users Count from a given roomName
     *
     * @param roomName the given roomName
     * @return the userCount
     */
    public int usersCount(String roomName) {
        int count = 0;
        for (UserModel model : users_list.values()) {
            if (model.getRoom().equals(roomName) && model.getNickname() != null)
                count++;
        }
        return count;
    }

    /**
     * This function sends a new message
     *
     * @param msg the message to be sent
     */
    public void onSendMessage(AbstractMessage msg) {
        UserModel connected = NetworkManager.getInstance().getConnnected();
        UserModel model = msg.getUser();
        connected.addNewMessage(msg, true);
        NetworkManager.getInstance().sendMessage(model.getIP(), model.getPort(), msg);
    }

    /**
     * This function send a MESS to all users from local room
     *
     * @param message the Message to be sent
     */
    public void onSendMess(String message) {
        String localRoom = NetworkManager.getInstance().getConnnected().getRoom();
        UserModel connected = NetworkManager.getInstance().getConnnected();
        HashMap<String, UserModel> users = getModelsByRoom(localRoom);
        AbstractMessage simpleMsg = null;
        for (UserModel model : users.values()) {
            if (model.getNickname() != null) {
                simpleMsg = MessageFactory.getMessage(MessageFactory.SIMPLE_MESS_TEXT_MESSAGE, model, message.getBytes(), connected.getRoom());
                onSendMessage(simpleMsg);
            }

        }
    }

    /**
     * This function send a packet of type CHANGE_ROOM when the local user change his room
     *
     * @param model the local user
     * @param room  the new room
     */
    public void onSendChangeRoom(UserModel model, String room) {
        model.setRoom(room);
        NetworkManager.getInstance().sendChangeRoom(room);
    }

    /**
     * This function sends a Packet_NICKNAME_CHANGED when the nickname of local
     * user has changed
     *
     * @param user    the local user
     * @param newNick the newNickname
     */
    public void onSendNickNameChanged(UserModel user, String newNick) {
        user.setNickname(newNick);
        for (UserModel model : users_list.values()) {
            if (model.getKey() != user.getKey()) {
                NetworkManager.getInstance().sendNewNick(model.getIP(), model.getPort(), newNick);
            }
        }
    }

    /**
     * This function send an audio chunk data to a given Model
     *
     * @param model the target user
     */
    public void onSendAudioData(UserModel model) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AudioRecorder audioRecorder = new AudioRecorder();
                audioRecorder.init();
                setTransferMode(true);
                UserModel connected = NetworkManager.getInstance().getConnnected();
                byte[] data;
                while (true) {
                    if (isTransferring() == true) {
                        data = audioRecorder.getAudioData();
                        AbstractMessage msg = MessageFactory.getMessage(MessageFactory.AUDIO_DATA_MESSAGE, model, data, null);
                        connected.addNewMessage(msg, true);
                        NetworkManager.getInstance().sendMessage(model.getIP(), model.getPort(), msg);
                    } else {
                        audioRecorder.close();
                        return;
                    }
                }
            }
        }).start();
    }

    /**
     * This function fires on receiving new chunk of audio data
     *
     * @param ip   the ip of the sender
     * @param port the port of the sender
     * @param data the data to receive
     */
    public void onReceiveAudioData(String ip, int port, byte[] data) {
        UserModel model = getUser(ip, port);
        UserModel connected = NetworkManager.getInstance().getConnnected();
        AbstractMessage msg = MessageFactory.getMessage(MessageFactory.AUDIO_DATA_MESSAGE, model, data, null);
        connected.addNewMessage(msg, false);
        ObserverManager.getInstance().notifyAudioDataReceived(msg);
    }

    /**
     * This function fires on receiving new mess data
     *
     * @param ip   the ip of the sender
     * @param port the port of the sender
     * @param data the data
     */
    public void onReceiveMessData(String ip, int port, byte[] data) {
        UserModel model = getUser(ip, port);
        UserModel connected = NetworkManager.getInstance().getConnnected();
        AbstractMessage msg = MessageFactory.getMessage(MessageFactory.SIMPLE_MESS_TEXT_MESSAGE, model, data, model.getRoom());
        connected.addNewMessage(msg, false);
        ObserverManager.getInstance().notifyReceivedMessage(msg);
    }

    /**
     * This function fires when receiving a call request
     *
     * @param ip   the ip of the sender
     * @param port the port of the sender
     */
    public void onReceivedCallRequest(String ip, int port) {
        UserModel model = getUser(ip, port);
        ObserverManager.getInstance().notifyCallRequest(model);
    }

    /**
     * This function fires when receiving an hello from an user
     *
     * @param ip       the ip of the user
     * @param port     the port of the user
     * @param nickName the nickname of the user
     * @param version  the version of the application
     */
    public void onReceiveUserHello(String ip, int port, String nickName, byte[] version) {
        UserModel user = getUser(ip, port);
        user.setNickname(nickName);
        user.setVersion(version);
        ObserverManager.getInstance().notifyReceivedHello(user);
    }

    /**
     * This function fires when receiving a nicknameChanged packet
     *
     * @param ip       the ip of the sender
     * @param port     the port of the sender
     * @param nickName the newNickname of the sender
     */
    public void onReceiveUserChangedNickName(String ip, int port, String nickName) {
        UserModel user = getUser(ip, port);
        String oldNick = user.getNickname();
        user.setNickname(nickName);
        try {
            proxy.updateNickname(oldNick, nickName);
            ObserverManager.getInstance().notifyNickNameChanged(user, oldNick);
        } catch (ViolationAccesException e) {
            ObserverManager.getInstance().notifyOnError("Could not change nickname.Permision denied");
        }
    }

    /**
     * This functon fires on receiving a new message
     *
     * @param ip      the ip of the sender
     * @param port    the port of the sender
     * @param message the new message
     */
    public void onReceivedNewMessage(String ip, int port, byte[] message) {
        UserModel user = getUser(ip, port);
        UserModel connected = NetworkManager.getInstance().getConnnected();
        AbstractMessage simpleMsg = MessageFactory.getMessage(MessageFactory.SIMPLE_PRIVATE_TEXT_MESSAGE, user, message, null);
        connected.addNewMessage(simpleMsg, false);
        ObserverManager.getInstance().notifyReceivedMessage(simpleMsg);
    }

    /**
     * This function fires on new user has connected
     *
     * @param ip   the ip of the new user
     * @param port the port of the new user
     */
    public void onReceiveUserConnected(String ip, int port) {
        UserModel user = getUser(ip, port);
        user.setStatus(UserModel.Status.CONNECTED);
        ObserverManager.getInstance().notifyConnected(user);
    }

    /**
     * This function fires on incoming new connection
     *
     * @param ip   the ip of the new user
     * @param port the port of the new user
     */
    public void onReceiveIncomingUserConnection(String ip, int port) {
        if (hasUser(ip, port)) {
            return;
        }
        UserModel user = createUser(ip, port);
        user.setStatus(UserModel.Status.CONNECTED);
        ObserverManager.getInstance().notifyIncomingConnection(user);
    }

    /**
     * This function fires on user disconnected
     *
     * @param ip   the ip of the user
     * @param port the port of the user
     */
    public void onReceiveUserDisconnected(String ip, int port) {
        UserModel user = getUser(ip, port);
        user.setStatus(UserModel.Status.DISCONNECTED);
        HistoryModel model = NetworkManager.getInstance().getConnnected().getHistory(user);
        model.stopSession();
        try {
            this.proxy.writeHistory(model);
            removeUser(user);
            ObserverManager.getInstance().notifyDisconnected(user);
        } catch (ViolationAccesException e) {
            ObserverManager.getInstance().notifyOnError("Could not write history for " + user.getNickname());
        }
    }

    /**
     * This function fires on a new user failed to connect
     *
     * @param ip    the ip of the user
     * @param port  the port of the user
     * @param cause the cause of failing to connect
     */
    public void onReceiveUserConnectingFailed(String ip, int port, Throwable cause) {
        UserModel user = getUser(ip, port);
        user.setStatus(UserModel.Status.DISCONNECTED);
        ObserverManager.getInstance().notifyConnectingFailed(user, "Failed to connect  :  StackTrace = " + Utils.getStackTrace(cause));
    }

    /**
     * This function fires on a new user changed is room
     *
     * @param ip      the ip of the user
     * @param port    the port of the user
     * @param newRoom the new Room
     */
    public void onReceiveUserChangedRoom(String ip, int port, String newRoom) {
        UserModel user = getUser(ip, port);
        String oldRoom = user.getRoom();
        user.setRoom(newRoom);
        ObserverManager.getInstance().notifyChangingRoom(user, newRoom, oldRoom);
    }

    /**
     * This function fires on the end of the handshake
     *
     * @param ip   the ip of the user
     * @param port the port of the user
     * @param key  the aes key
     */
    public void onReceiveHandShakeEnd(String ip, int port, byte[] key) {
        UserModel model = getUser(ip, port);
        UserModel connected = NetworkManager.getInstance().getConnnected();
        connected.addNewHistoryEntry(model, key);
    }

    /**
     * This class is responsable for handleing the end of hand shake
     *
     * @param ip   the ip of the source user
     * @param port the port of the source user
     * @param key  the encrypted aesKey
     */
    public void onReceiveHandShake(String ip, int port, byte[] key) {
        UserModel model = getUser(ip, port);
        UserModel connected = NetworkManager.getInstance().getConnnected();
        connected.addNewHistoryEntry(model, key);
    }

    /**
     * This class is responsable for handleing the call request
     *
     * @param ip   the ip of the source
     * @param port the port of the source
     */
    public void onReceiveCallRequest(String ip, int port) {
        NetworkManager.getInstance().sendCallRrequest(ip, port);
    }

}
