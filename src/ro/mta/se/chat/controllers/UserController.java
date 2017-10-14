package ro.mta.se.chat.controllers;

import ro.mta.se.chat.adapters.DataAdapter;
import ro.mta.se.chat.crypto.Certificate;
import ro.mta.se.chat.crypto.PrivateKeyLoader;
import ro.mta.se.chat.exceptions.ViolationAccesException;
import ro.mta.se.chat.models.HistoryModel;
import ro.mta.se.chat.models.UserModel;
import ro.mta.se.chat.network.NetworkConstants;
import ro.mta.se.chat.proxy.DataAdapterProxy;
import ro.mta.se.chat.singleton.NetworkManager;
import ro.mta.se.chat.singleton.ObserverManager;
import ro.mta.se.chat.singleton.UserManager;
import ro.mta.se.chat.utils.Utils;
import ro.mta.se.chat.views.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.PrivateKey;
import java.util.EventListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Alberto-Daniel on 11/19/2015.
 * This class is responsable for controlling i=the flow of users
 */
public class UserController {

    /**
     * The connected user
     */
    UserModel theModel;
    /**
     * The login forma
     */
    LoginView view;
    /**
     * The history view
     */
    HistoryView historyView;
    /**
     * The client chat
     */
    ChatClientView clientChat;
    /**
     * The nickname view
     */
    ChangeNickNameView theNickView;

    /**
     * This is the setup function which initialize the components
     */
    private void setup() {
        view = new LoginView();
        theNickView = new ChangeNickNameView();
    }

    /**
     * This is the constructor which takes as input the main view
     *
     * @param theView the main view
     */
    public UserController(ChatClientView theView) {
        setup();
        this.clientChat = theView;
        historyView = new HistoryView(clientChat);
        this.view.addloginListener(new LoginListener());
        this.view.addSignupListenr(new SignupListener());
        this.clientChat.addNewNickItem(new NewNickListener());
        this.clientChat.getTappanel().addListener(new TapPanelEventListener());
        this.theNickView.addListeners(new NewNickChange());
        view.displayWindow();
    }

    /**
     * This class represents the action response on login click
     */
    class LoginListener implements ActionListener {
        /**
         * The actual action performed
         *
         * @param e the event arguments
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsername();
            String passwrod = view.getPassword();
            String nickname = view.getNickname();
            synchronized (UserModel.class) {
                theModel = NetworkManager.getInstance().getConnnected();
                if (theModel == null) {
                    theModel = new UserModel();
                    NetworkManager.getInstance().setConnected(theModel);
                }
            }

            PrivateKey key = PrivateKeyLoader.loadPrivateKey(Utils.getPrivateKeyPath(), username, passwrod);
            if (key == null) {
                view.displayMessage("Username or password incorrect", true);
                return;
            } else {
                view.displayMessage("User logged succesfully", false);
            }
            theModel.setPassword(passwrod);
            theModel.setNickname(nickname);
            theModel.setUsername(username);
            theModel.setRoom(ro.mta.se.chat.views.ViewConstants.ROOM_1);
            view.hideWindow();
            ObserverManager ref = ObserverManager.getInstance(); //make sure the manager starts;i8
            ref.initialize();
            ref.start();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e1) {

            }
            HashMap<String, LinkedList<Integer>> lists = NetworkManager.getInstance().getPorts();
            NetworkManager.getInstance().setConnected(theModel);
            for (String network : lists.keySet()) {
                for (Integer integer : lists.get(network)) {
                    UserManager.getInstance().connect(network, integer);
                }
            }
            clientChat.setUsername(nickname);
            DataAdapter adapter = new DataAdapter();
            LinkedList<UserModel> users = adapter.getUsers(DataAdapter.XML_STORAGE);
            LinkedList<String> lisrts = new LinkedList<>();
            for (UserModel user : users) {
                lisrts.add(user.getNickname());
            }
            clientChat.onAddingFriends(lisrts);
            clientChat.displayWindow();
        }
    }

    /**
     * This class represents the action response on signup click
     */
    class SignupListener implements ActionListener {

        /**
         * The actual action performed
         *
         * @param e the event arguments
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsername();
            String passwrod = view.getPassword();
            Certificate.generate(username, passwrod);
            view.displayMessage("User added succesfully.You can now login", false);
            view.clearAll();
        }
    }

    /**
     * This class represents the action response on tap pane; buttons  click
     */
    class TapPanelEventListener implements ActionListener {
        /**
         * The actual action performed
         *
         * @param evt the event arguments
         */
        @Override
        public void actionPerformed(ActionEvent evt) {
            TapPanel panelSource = Utils.findParent((Component) evt.getSource(), TapPanel.class);
            if (evt.getSource().equals(panelSource.getCmdChangeRoom())) {
                if (clientChat.onChangingRoom() == true) {
                    String room = panelSource.selectedRoomKey();
                    UserModel model = NetworkManager.getInstance().getConnnected();
                    UserManager.getInstance().onSendChangeRoom(model, room);
                    HashMap<String, String> maps = UserManager.getInstance().getUsersRoom(room);
                    clientChat.onListingNewUsers(maps);
                    clientChat.onClearingAllMessages();
                }
            }

            if (evt.getSource().equals(panelSource.getCmdIgnoreUser())) {
                if (evt.getActionCommand().equals("Ignore User")) {
                    panelSource.viewSetIgnore(true);
                } else {
                    panelSource.viewSetIgnore(false);
                }
            }

            if (evt.getSource().equals(panelSource.getCmdSendDirect())) {
                panelSource.viewSendMsg();
                AppController.getInstance().getMessageController().updateListeners();
            }
            if (evt.getSource().equals(panelSource.getCmdViewHistory())) {
                String nickname = panelSource.selectedUser();
                DataAdapterProxy theProxy = new DataAdapterProxy(new DataAdapter(), NetworkManager.getInstance().getConnnected());
                LinkedList<HistoryModel> data = null;
                try {
                    data = theProxy.readHistory(nickname, DataAdapter.XML_STORAGE);
                    historyView.clearText();
                    for (HistoryModel model : data) {
                        historyView.setTime(model.getTime());
                        for (int i = 0; i < model.size(); i++)
                            historyView.setHistory(model.toString(i));
                    }
                    historyView.displayHistory();
                } catch (ViolationAccesException e) {
                    clientChat.onError(e.getMessage());
                }
            }

        }
    }

    /**
     * This class represents the action response on change nickname menu item
     */
    class NewNickListener implements ActionListener {
        /**
         * The actual action performed
         *
         * @param e the event arguments
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            theNickView.showWindow();
        }
    }

    /**
     * This class represents the action response on change btn
     */
    class NewNickChange implements ActionListener {
        /**
         * The actual action performed
         *
         * @param e the event arguments
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String newNick = theNickView.getNewNick();
            UserManager.getInstance().onSendNickNameChanged(NetworkManager.getInstance().getConnnected(), newNick);
        }
    }

    /**
     * This class gets the login view
     *
     * @return the login view
     */
    public LoginView getLoginView() {
        return view;
    }


}
