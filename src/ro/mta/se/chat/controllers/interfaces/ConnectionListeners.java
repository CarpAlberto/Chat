package ro.mta.se.chat.controllers.interfaces;

import ro.mta.se.chat.factory.abstracts.AbstractMessage;
import ro.mta.se.chat.models.UserModel;
import ro.mta.se.chat.singleton.UserManager;

/**
 * Created by Alberto-Daniel on 11/26/2015.
 * This interface contains all the events that need to be notified
 * to graphical interface
 */
public interface ConnectionListeners {
    /**
     * This method raise when a new user is trying to connect to us
     *
     * @param model the user who try to connect to us
     */
    void onIncomeConnection(UserModel model);

    /**
     * This method rise when a new user succeed in connecting to us
     *
     * @param model the user who connected
     */
    void onConnected(UserModel model);

    /**
     * This method rises when an user disconnected from us
     *
     * @param model the user who disconnected
     */
    void onDisconected(UserModel model);

    /**
     * This method rises when an user failed to connect to us
     *
     * @param model the user who failed to connect
     * @param cause the cause of failure
     */
    void onFailedToConnect(UserModel model, String cause);

    /**
     * This method rises when received an hello
     *
     * @param model the user from which just received hello
     */
    void onReceivedHello(UserModel model);

    /**
     * This method rises when the nickname has just changed
     *
     * @param model the user who changed his nickname
     */
    void onNickNameChanged(UserModel model, String oldNick);

    /**
     * This method rises when just receiveing a new message
     *
     * @param theMessage which just received
     */
    void onNewMessage(AbstractMessage theMessage);

    /**
     * This method ruses when changing the room
     *
     * @param model   user which changed his room
     * @param room    the old room
     * @param oldRoom the new room
     */
    void onChangingRoom(UserModel model, String room, String oldRoom);

    /**
     * This method rises when an user leaves his room
     *
     * @param model   the user who leaves his room
     * @param newRoom the new room
     */
    void onLeavingRoom(UserModel model, String newRoom);

    /**
     * This method rises when an user joins a new room
     *
     * @param model the user who joins the room
     * @param room  the new room
     */
    void onJoiningRoom(UserModel model, String room);

    /**
     * This method rises on getting a new call request
     *
     * @param from the user from which we get the call request
     */
    void onCallRequest(UserModel from);

    /**
     * This method rises on getting new audio chunk data
     *
     * @param msg the chunk audio just received
     */
    void onChunkAudioReceived(AbstractMessage msg);

    /**
     * This function rises when an unexpected error has happened
     *
     * @param errorMessage the errorMessage
     */
    void onError(String errorMessage);

}
