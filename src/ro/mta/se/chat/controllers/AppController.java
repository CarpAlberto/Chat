package ro.mta.se.chat.controllers;

import ro.mta.se.chat.singleton.ObserverManager;
import ro.mta.se.chat.views.ChatClientView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Alberto-Daniel on 11/25/2015.
 * This controller contains the entry point of the aplication
 * It is responsable for creating the others controllers and also
 * creates the main view
 */
public class AppController {
    /**
     * This is the main view of the application
     */
    ChatClientView theView;
    /**
     * This is the controller that handle the flow of messages
     */
    MessageController messageController;
    /**
     * This is the controller that handle the connections
     */
    ConnectionController connectionController;
    /*
     * This is the controller that handle the user interaction
     */
    UserController userController;

    /**
     * Reference to the appController aplication
     */

    static AppController appController;

    /**
     * This is the the entry point of the application
     *
     * @param args the arguments of the main function
     */
    public static void main(String[] args) {
        AppController controller = AppController.getInstance();
    }

    /**
     * This is the private constructor of the application.This is responsable
     * for creating the others controllers and adding listeners to the view
     */
    private AppController() {
        theView = new ChatClientView();
        userController = new UserController(theView);
        connectionController = new ConnectionController(theView);
        messageController = new MessageController(theView);
        this.theView.addHelpListener(new AboutMenuListener());
        this.theView.addDisconnectItem(new DisconnectChatListener());
        this.theView.addQuitListener(new CloseChatListener());

    }

    /**
     * This function gets the instance of the appController
     *
     * @return
     */
    public static AppController getInstance() {
        if (appController == null) {
            appController = new AppController();
        }
        return appController;
    }

    /**
     * This function gets the MessageController
     *
     * @return the message Controller
     */
    public MessageController getMessageController() {
        return messageController;
    }

    /**
     * This class is responsable for handles the disconnect action
     * from user
     */
    class DisconnectChatListener implements ActionListener {

        /**
         * The callback function which rises when pressing disconect button
         *
         * @param e the action event
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            ObserverManager.getInstance().stop();
            theView.closeWindow(false);
            userController.getLoginView().displayWindow();

        }
    }

    /**
     * This class is responsable for handleing the close event
     */
    class CloseChatListener implements ActionListener {

        /**
         * The callback function which rises when pressing close button
         *
         * @param e the action event
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            ObserverManager.getInstance().stop();
            theView.closeWindow(true);
        }
    }

    /**
     * This class is responsable for handleing the about event
     */
    class AboutMenuListener implements ActionListener {

        /**
         * The callback function which rises when pressing about button
         *
         * @param e the action event
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            theView.displayHelp();
        }
    }


}
