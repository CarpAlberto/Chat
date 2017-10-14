package ro.mta.se.chat.controllers;

import ro.mta.se.chat.adapters.DataAdapter;
import ro.mta.se.chat.controllers.interfaces.ConnectionListeners;
import ro.mta.se.chat.exceptions.ViolationAccesException;
import ro.mta.se.chat.factory.abstracts.AbstractMessage;
import ro.mta.se.chat.models.UserModel;
import ro.mta.se.chat.proxy.DataAdapterProxy;
import ro.mta.se.chat.singleton.NetworkManager;
import ro.mta.se.chat.singleton.ObserverManager;
import ro.mta.se.chat.singleton.UserManager;
import ro.mta.se.chat.views.ChatClientView;
import ro.mta.se.chat.views.ConnectView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Created by Alberto-Daniel on 11/25/2015.
 * This class is responsable for handles connection events
 */
public class ConnectionController {
    /**
     * The main view of the application
     */
    ChatClientView theView;
    /**
     * The connect vuew of the application
     */
    ConnectView myConnectView;
    /**
     * The user model of the application
     */
    UserModel userModel;

    /**
     * The constructor of the Connection Controller
     *
     * @param theView the main view passed as argument
     */
    public ConnectionController(ChatClientView theView) {
        this.theView = theView;
        this.userModel = NetworkManager.getInstance().getConnnected();
        myConnectView = new ConnectView(theView);
        this.theView.addConnectListener(new StartConnectionListener());
        this.myConnectView.addBtnConnectListener(new ConnectionListener());
        ObserverManager.getInstance().addListener(
                new ConnectionListeners() {
                    @Override
                    public void onIncomeConnection(UserModel model) {
                        theView.setAppletStatus("New  connection" + model);
                    }

                    @Override
                    public void onConnected(UserModel model) {
                        theView.setAppletStatus("connected " + model);
                    }

                    @Override
                    public void onDisconected(UserModel model) {
                        UserModel connected = NetworkManager.getInstance().getConnnected();
                        if (connected.getRoom() == model.getRoom())
                            theView.onDisconnectUser(model.getKey(), model.getNickname());
                    }

                    @Override
                    public void onFailedToConnect(UserModel model, String cause) {
                        theView.onError("Error on connecting...");
                    }

                    @Override
                    public void onReceivedHello(UserModel model) {
                        theView.setAppletStatus("receive hello " + model);
                        if (model.getRoom() == NetworkManager.getInstance().getConnnected().getRoom())
                            theView.onNewUserConnect(model.getNickname(), model.getIP(), model.getPort());
                    }

                    @Override
                    public void onNickNameChanged(UserModel model, String oldNick) {

                        theView.onReceivingNickNameChanged(model.getNickname(), oldNick);
                    }

                    @Override
                    public void onNewMessage(AbstractMessage message) {
                        message.updateViewOnReceive(theView);
                        AppController.getInstance().getMessageController().updateListeners();
                    }

                    @Override
                    public void onChangingRoom(UserModel model, String room, String oldRoom) {
                        String myRoom = NetworkManager.getInstance().getConnnected().getRoom();
                        if (myRoom.equals(room)) {
                            onJoiningRoom(model, room);
                        } else {
                            if (oldRoom.equals(myRoom)) {
                                onLeavingRoom(model, oldRoom);
                            }
                        }
                        theView.onListingNewUsers(UserManager.getInstance().getUsersRoom(myRoom));
                        DataAdapterProxy theProxy = new DataAdapterProxy(new DataAdapter(), NetworkManager.getInstance().getConnnected());

                        LinkedList<UserModel> users = null;
                        try {
                            users = theProxy.getUsers(DataAdapter.XML_STORAGE);
                            LinkedList<String> lisrts = new LinkedList<String>();
                            for (UserModel user : users) {
                                lisrts.add(user.getNickname());
                            }
                            theView.onAddingFriends(lisrts);
                            AppController.getInstance().getMessageController().updateListeners();
                        } catch (ViolationAccesException e) {
                            theView.onError(e.getMessage());
                        }

                    }

                    @Override
                    public void onLeavingRoom(UserModel model, String oldRoom) {
                        DataAdapterProxy theProxy = new DataAdapterProxy(new DataAdapter(), NetworkManager.getInstance().getConnnected());
                        LinkedList<UserModel> users = null;
                        try {
                            users = theProxy.getUsers(DataAdapter.XML_STORAGE);
                            LinkedList<String> list = new LinkedList<String>();
                            for (UserModel user : users) {
                                list.add(user.getNickname());
                            }
                            theView.onAddingFriends(list);
                            theView.onLeavingRoom(model.getNickname(), model.getKey(), oldRoom, model.getRoom());
                        } catch (ViolationAccesException e) {
                            theView.onError(e.getMessage());
                        }

                    }

                    @Override
                    public void onJoiningRoom(UserModel model, String room) {
                        theView.onJoiningRoom(model.getNickname(), model.getKey());
                    }

                    @Override
                    public void onCallRequest(UserModel from) {
                        if (theView.displayChooseOption(" Call from : " + from.getNickname() + "   Accept ?  ") == true) {
                            UserManager.getInstance().onSendAudioData(from);
                        }
                    }

                    @Override
                    public void onChunkAudioReceived(AbstractMessage msg) {
                        msg.updateViewOnReceive(theView);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        theView.onError(errorMessage);
                    }
                }
        );
    }

    /**
     * This class is responsable for handleing the display window
     * event
     */
    class StartConnectionListener implements ActionListener {

        /**
         * The only method from this class
         *
         * @param e the arguments of the Action Performed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            myConnectView.showWindow();
        }
    }

    /**
     * This class is responsable for handleing the action
     * from connect button
     */
    class ConnectionListener implements ActionListener {

        /**
         * The actualy method that handles the action
         *
         * @param e the given events
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String ip = myConnectView.getIp();
            int port = Integer.parseInt(myConnectView.port());
            UserManager.getInstance().connect(ip, port);
            myConnectView.hideWindow();
        }
    }

}
