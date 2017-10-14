package ro.mta.se.chat.singleton;

import ro.mta.se.chat.controllers.interfaces.ConnectionListeners;
import ro.mta.se.chat.factory.abstracts.AbstractMessage;
import ro.mta.se.chat.models.UserModel;
import ro.mta.se.chat.utils.ConfigurationManager;
import ro.mta.se.chat.utils.logging.Logger;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Alberto-Daniel on 11/26/2015.
 */
public class ObserverManager {
    /**
     * This is a variable that contains the instance of ObserverManager
     */
    private static volatile ObserverManager instance = null;

    /**
     * This function contains the listeners that wants to be updated
     * by changes
     */
    private Collection<ConnectionListeners> core_listeners = new ConcurrentLinkedQueue<ConnectionListeners>();

    /**
     * This function starts the observer and network manager
     */

    public void start() {
        NetworkManager.getInstance().start();
    }

    /**
     * This function stops the observer and network manager
     */
    public void stop() {
        NetworkManager.getInstance().stop();
    }

    /**
     * This is a function that returns the instance of Observer Manager
     *
     * @return the instance object
     */

    public static ObserverManager getInstance() {
        if (instance == null)
            synchronized (ObserverManager.class) {
                instance = new ObserverManager();
            }
        return instance;
    }

    /**
     * This function notify that an user is trying to connect to us
     *
     * @param model the source user
     */
    public void notifyIncomingConnection(final UserModel model) {
        for (final ConnectionListeners listener : core_listeners) {
            try {
                listener.onIncomeConnection(model);
            } catch (Throwable cause) {
                Logger.warn("Notifying income connection");
            }
        }
}

    public void notifyOnError(String cause) {
        for (final ConnectionListeners listener : core_listeners) {
            try {
                listener.onError(cause);
            } catch (Throwable th) {
                Logger.warn("Notifying error");
            }
        }
    }

    /**
     * This function notify that an user has changed the room
     *
     * @param model   the source  user
     * @param newRoom the new room
     * @param oldRoom the old room
     */
    public void notifyChangingRoom(final UserModel model, String newRoom, String oldRoom) {
        for (ConnectionListeners listener : core_listeners) {
            try {
                listener.onChangingRoom(model, newRoom, oldRoom);
            } catch (Throwable cause) {
                Logger.warn("Notifying changing room");
            }
        }
    }

    /**
     * This function notify that an user succeed in coonecting to us
     *
     * @param model the user who succeed in connecting
     */

    public void notifyConnected(final UserModel model) {
        for (ConnectionListeners listener : core_listeners) {
            try {
                listener.onConnected(model);
            } catch (Throwable cause) {
                Logger.warn("Notifying connected new user");
            }
        }
    }

    /**
     * This function notify the listeners that an user has disconnected from us
     *
     * @param model the source user
     */

    public void notifyDisconnected(final UserModel model) {
        for (ConnectionListeners listener : core_listeners) {
            try {
                listener.onDisconected(model);
            } catch (Throwable cause) {
                Logger.warn("Notifying disconnected new user");
            }
        }
    }

    /**
     * This function notify the listeners that an connecting_failed has happened
     *
     * @param model the user that has failed to connect
     * @param error the detailed error message
     */

    public void notifyConnectingFailed(final UserModel model, final String error) {
        for (ConnectionListeners listener : core_listeners) {
            try {
                listener.onFailedToConnect(model, error);
            } catch (Throwable cause) {
                Logger.warn("Notifying failed to connect" + error);
            }
        }
    }

    /**
     * This function notify the listeners that an hello pachet has arrived
     *
     * @param model the source of the packet
     */

    public void notifyReceivedHello(final UserModel model) {
        for (ConnectionListeners listener : core_listeners) {
            try {
                listener.onReceivedHello(model);
            } catch (Throwable cause) {
                Logger.warn("Notifying received hello");
            }
        }
    }

    /**
     * This function notify the listeners that ( @param model ) has changed the nickname
     *
     * @param model the user that has changed the nickname
     */
    public void notifyNickNameChanged(final UserModel model, String oldNick) {
        for (ConnectionListeners listener : core_listeners) {
            try {
                listener.onNickNameChanged(model, oldNick);
            } catch (Throwable cause) {
                Logger.warn("Notifying nickname changed");
            }
        }
    }

    /**
     * This function notify the listeners that a new message has arrived
     *
     * @param message that has arrived
     */
    public void notifyReceivedMessage(final AbstractMessage message) {
        for (ConnectionListeners listener : core_listeners) {
            try {
                listener.onNewMessage(message);
            } catch (Throwable cause) {
                Logger.warn("Notifying on new Message");
            }
        }
    }

    /**
     * This function notify the listeners that a new call request has arrived
     *
     * @param model the source of the call request
     */
    public void notifyCallRequest(final UserModel model) {
        for (ConnectionListeners listener : core_listeners) {
            try {
                listener.onCallRequest(model);
            } catch (Throwable cause) {
                Logger.warn("Notifying call request");
            }
        }
    }

    /**
     * This function notify the listeners that a new Chunk of Audio Data received
     *
     * @param data that received
     */
    public void notifyAudioDataReceived(final AbstractMessage data) {
        for (ConnectionListeners listener : core_listeners) {
            try {
                listener.onChunkAudioReceived(data);
            } catch (Throwable cause) {
                Logger.warn("Notifying chunk data received");
            }
        }
    }

    /**
     * This function add a new listener to the listeners list
     *
     * @param listener to be added
     */
    public void addListener(ConnectionListeners listener) {
        core_listeners.add(listener);
    }

    /**
     * This function load the necessary configuration for starting the Observer Manager
     */
    public void initialize() {
        ConfigurationManager.getInstance().loadConfig();
    }

}
