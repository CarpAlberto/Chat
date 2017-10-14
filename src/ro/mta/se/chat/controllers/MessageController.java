package ro.mta.se.chat.controllers;

import ro.mta.se.chat.factory.MessageFactory;
import ro.mta.se.chat.factory.abstracts.AbstractMessage;
import ro.mta.se.chat.models.*;
import ro.mta.se.chat.models.UserModel;
import ro.mta.se.chat.singleton.NetworkManager;
import ro.mta.se.chat.singleton.ObserverManager;
import ro.mta.se.chat.singleton.UserManager;
import ro.mta.se.chat.views.ChatClientView;
import ro.mta.se.chat.views.PrivateChat;
import ro.mta.se.chat.utils.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Alberto-Daniel on 11/26/2015.
 * This class is responsable for controlling the message flow
 */
public class MessageController {
    /**
     * The main view of the application
     */
    ChatClientView theClient;
    /**
     * The current private chat
     */
    PrivateChat thePrivateChat;
    /**
     * The user connected to the application
     */
    UserModel theUserModel;

    public MessageController(ChatClientView theClient) {
        this.theClient = theClient;
        this.theUserModel = NetworkManager.getInstance().getConnnected();
        this.theClient.addActionListener(new ChatClientListener());
    }

    /**
     * This function updates listeners depend on new privateChat
     */
    public void updateListeners() {
        PrivateChat[] privates = theClient.getPrivate();
        for (int i = 0; i < privates.length; i++) {
            if (privates[i] != null) {
                privates[i].clearListeners();
            }
        }

        for (int i = 0; i < privates.length; i++) {
            if (privates[i] != null) {
                privates[i].addActionListener(new PrivateChatListener());
            }
        }
    }

    /**
     * This clasa handles the Actions made on a private chat
     */
    class PrivateChatListener implements ActionListener {
        /**
         * The actual callback method
         *
         * @param e the arguments
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            thePrivateChat = Utils.findParent((Component) e.getSource(), PrivateChat.class);
            UserModel target = UserManager.getInstance().getUser(thePrivateChat.getKey());
            UserModel connected = NetworkManager.getInstance().getConnnected();
            if (e.getSource().equals(thePrivateChat.getCmdSend())) {
                if (!(thePrivateChat.getTxtMessage().getText().trim().equals(""))) {
                    String message = thePrivateChat.getTxtMessage().getText();
                    AbstractMessage msg = MessageFactory.getMessage(MessageFactory.SIMPLE_PRIVATE_TEXT_MESSAGE, target, message.getBytes(), null);
                    UserManager.getInstance().onSendMessage(msg);
                    thePrivateChat.sendMessage(connected.getNickname());
                }
            }
            if (e.getSource().equals(thePrivateChat.getCmdClose())) {
                thePrivateChat.exitPrivateWindow();
            }
            if (e.getSource().equals(thePrivateChat.getCmdCall())) {
                if (thePrivateChat.getCmdCall().getLabel().equals("Call")) {
                    UserManager.getInstance().onReceiveCallRequest(target.getIP(), target.getPort());
                    thePrivateChat.getCmdCall().setLabel("Stop Call..");
                } else
                {
                    UserManager.getInstance().setTransferMode(false);
                    thePrivateChat.getCmdCall().setLabel("Call");
                }
            }
            if (e.getSource().equals(thePrivateChat.getCmdIgnore())) {
                if (e.getActionCommand().equals("Ignore User")) {
                    thePrivateChat.updateIgnoreUser();
                } else {
                    thePrivateChat.updateAllowUser();
                }
            }
            if (e.getSource().equals(thePrivateChat.getCmdEmoticons())) {
                if (thePrivateChat.getEmotionFlag()) {
                    thePrivateChat.updateEmoticon();
                } else {
                    thePrivateChat.updateNotEmoticon();
                }
            }
        }
    }

    /**
     * This class is responsable for handleing the events on the main view of the application
     */
    class ChatClientListener implements ActionListener {

        /**
         * The actual method which perform that
         *
         * @param e the action event
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == theClient.getCmdSend()) {
                UserManager.getInstance().onSendMess(theClient.getTextMessage().getText());
                theClient.onNewMess();

            }
            if (e.getSource() == theClient.getCmdExit()) {
                theClient.closeWindow(true);
            }
        }
    }

}
