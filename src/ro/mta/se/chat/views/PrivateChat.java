/*************************************************************************
 * Chat Client Private Chat
 * <p>
 * /Chat Client Private Chat
 * <p>
 * /Chat Client Private Chat
 * <p>
 * /*****************Chat Client Private Chat

/*************************************************************************/
/*************************************************************************/
/*************************************************************************/
package ro.mta.se.chat.views;


import my_libs.ScrollView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

/**
 * Created by Alberto-Daniel on 15/17/2015.
 * This class represents the private chat
 */
public class PrivateChat extends JFrame {

    /**
     * This is the client chat of the application
     */
    private ChatClientView chatclient;
    /**
     * This is the username of the Private Chat
     */
    private String userName;
    /**
     * This is the message canvas of the application
     */
    private MessageCanvas messagecanvas;
    /**
     * This is the message scroll of the application
     */
    private ScrollView messageScrollView;
    /**
     * This is the textview of the private chat
     */
    private TextField txtMessage;
    /**
     * There are all the buttons from the Private Chat
     */
    private Button cmdSend, cmdClose, cmdIgnore, cmdCall, cmdEmoticons;
    /**
     * This is the emotioncanvas of the application
     */
    private EmotionCanvas emotioncanvas;
    /**
     * This is the emotion scrollView from application
     */
    private ScrollView emotionScrollView;
    /**
     * This is the emotion flag from application
     */
    private boolean emotionFlag;
    /**
     * This is the emotion Panel of the application
     */
    private Panel emotionPanel;
    /**
     * This is the key of the application
     */
    private String key;

    /**
     * This is the constructor of the application
     * @param parent the parent chat
     * @param toUserName the receiver of the message
     * @param key the key of the user
     */
    public PrivateChat(ChatClientView parent, String toUserName, String key) {
        this.chatclient = parent;
        this.userName = toUserName;
        this.key = key;
        setTitle("Private Chat With " + userName);
        Image IconImage = Toolkit.getDefaultToolkit().getImage("images/logo.gif");
        setIconImage(IconImage);
        setBackground(chatclient.getColorMap()[0]);
        setFont(chatclient.getFont());
        emotionFlag = false;
        initializeComponents();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                exitPrivateWindow();
            }
        });
    }

    /**
     * This function returns the key of the private chat
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * This function initialize all the components
     */
    private void initializeComponents() {
        setLayout(null);
        Label LblConversation = new Label("Conversation With " + userName);
        LblConversation.setForeground(chatclient.getColorMap()[4]);
        LblConversation.setBounds(5, 30, 400, 20);
        add(LblConversation);

        Panel MessagePanel = new Panel(new BorderLayout());
        MessagePanel.setBackground(Color.green);
        messagecanvas = new MessageCanvas(chatclient);
        messageScrollView = new ScrollView(messagecanvas, true, true, ViewConstants.TAPPANEL_CANVAS_WIDTH, ViewConstants.TAPPANEL_CANVAS_HEIGHT, ViewConstants.SCROLL_BAR_SIZE);
        messagecanvas.setScrollView(messageScrollView);
        MessagePanel.add("Center", messageScrollView);
        MessagePanel.setBounds(5, 50, 400, 200);
        add(MessagePanel);

        txtMessage = new TextField();
        txtMessage.setFont(chatclient.getTextFont());
        txtMessage.setBounds(5, 260, 320, 20);
        add(txtMessage);

        cmdSend = new CustomButton(chatclient, "Send");
        cmdSend.setBounds(335, 260, 70, 20);
        add(cmdSend);

        cmdCall = new CustomButton(chatclient, "Call");
        cmdCall.setBounds(5, 290, 80, 20);

        cmdIgnore = new CustomButton(chatclient, "Ignore User");
        cmdIgnore.setBounds(105, 290, 80, 20);

        cmdClose = new CustomButton(chatclient, "Close");
        cmdClose.setBounds(205, 290, 80, 20);

        cmdEmoticons = new CustomButton(chatclient, "Emoticons");

        cmdEmoticons.setBounds(305, 290, 80, 20);

        add(cmdCall);
        add(cmdIgnore);
        add(cmdClose);
        add(cmdEmoticons);

        emotionPanel = new Panel(new BorderLayout());
        emotioncanvas = new EmotionCanvas(chatclient, this);
        emotionScrollView = new ScrollView(emotioncanvas, true, true, ViewConstants.EMOTION_CANVAS_WIDTH, ViewConstants.EMOTION_CANVAS_HEIGHT, ViewConstants.SCROLL_BAR_SIZE);
        emotioncanvas.setScrollview(emotionScrollView);
        emotioncanvas.addIconsToMessageObject();
        emotionPanel.add("Center", emotionScrollView);
        emotionPanel.setVisible(false);
        emotionPanel.setBounds(5, 320, ViewConstants.EMOTION_CANVAS_WIDTH, ViewConstants.EMOTION_CANVAS_HEIGHT);
        add(emotionPanel);

        setSize(ViewConstants.PRIVATE_WINDOW_WIDTH, ViewConstants.PRIVATE_WINDOW_HEIGHT);
        setResizable(false);
        show();
        this.requestFocus();
    }

    /**
     * This function updates the view changing the ignore user button
     */
    public void updateIgnoreUser() {
        chatclient.getTappanel().getUsersCanvas().ignoreUser(true, userName);
        messagecanvas.addMessageToMessageObject(userName + " has been ignored!", ViewConstants.MESSAGE_TYPE_ADMIN);
        cmdIgnore.setLabel("Allow User");
    }

    /**
     * This function updates the view changing the allow user button
     */
    public void updateAllowUser() {
        messagecanvas.addMessageToMessageObject(userName + " has been removed from ignored list!", ViewConstants.MESSAGE_TYPE_ADMIN);
        chatclient.getTapPanel().getUsersCanvas().ignoreUser(false, userName);
        cmdIgnore.setLabel("Ignore User");
    }

    /**
     * This function updates the view changing the emoticons view
     */
    public void updateEmoticon() {
        emotionFlag = false;
        emotionPanel.setVisible(false);
        setSize(ViewConstants.PRIVATE_WINDOW_WIDTH, ViewConstants.PRIVATE_WINDOW_HEIGHT);
    }

    /**
     * This function updates the view by hiding the emoticons
     */
    public void updateNotEmoticon() {
        emotionFlag = true;
        emotionPanel.setVisible(true);
        setSize(ViewConstants.PRIVATE_WINDOW_WIDTH, ViewConstants.PRIVATE_WINDOW_HEIGHT + ViewConstants.EMOTION_CANVAS_HEIGHT);
    }

    /**
     * This function add listener to the button view
     * @param listener the listener to be added
     */
    public void addActionListener(ActionListener listener) {
        this.cmdCall.addActionListener(listener);
        this.cmdClose.addActionListener(listener);
        this.cmdEmoticons.addActionListener(listener);
        this.cmdIgnore.addActionListener(listener);
        this.cmdSend.addActionListener(listener);
    }

    /**
     * This function clears the listener from the view
     */
    public void clearListeners() {
        for (ActionListener listener : cmdCall.getActionListeners()) {
            cmdCall.removeActionListener(listener);
        }
        for (ActionListener listener : cmdClose.getActionListeners()) {
            cmdClose.removeActionListener(listener);
        }
        for (ActionListener listener : cmdEmoticons.getActionListeners()) {
            cmdEmoticons.removeActionListener(listener);
        }
        for (ActionListener listener : cmdIgnore.getActionListeners()) {
            cmdIgnore.removeActionListener(listener);
        }
        for (ActionListener listener : cmdSend.getActionListeners()) {
            cmdSend.removeActionListener(listener);
        }
    }

    /**
     * This function send a new message
     * @param from the user
     */
    public void sendMessage(String from) {
        messagecanvas.addMessageToMessageObject(from + ": " + txtMessage.getText(), ViewConstants.MESSAGE_TYPE_DEFAULT);
        txtMessage.setText("");
        txtMessage.requestFocus();
    }

    /**
     * This function adds an image to textView
     * @param imageName the image name
     */
    public void addImageToTextField(String imageName) {
        if (txtMessage.getText() == null || txtMessage.getText().equals(""))
            txtMessage.setText("~~" + imageName + " ");
        else
            txtMessage.setText(txtMessage.getText() + " " + "~~" + imageName + " ");
    }

    /**
     * This function add a message to message canvas
     * @param message the message to be added
     */
    protected void addMessageToMessageCanvas(String message) {
        messagecanvas.addMessageToMessageObject(message, ViewConstants.MESSAGE_TYPE_DEFAULT);
    }

    /**
     * This function disable the buttons from view
     */
    public void disableAll() {
        txtMessage.setEnabled(false);
        cmdSend.setEnabled(false);
    }

    /**
     * This function enable the buttons from view
     */
    public void enableAll() {
        txtMessage.setEnabled(true);
        cmdSend.setEnabled(true);
    }

    /**
     * This function clears the message view
     */
    public void clearAll() {
        this.messagecanvas.clearAll();
    }

    /**
     * This function returns the button Send
     * @return the button Send
     */
    public Button getCmdSend() {
        return cmdSend;
    }

    /**
     * This function returns the button CloseC
     * @return the button Close
     */
    public Button getCmdClose() {
        return cmdClose;
    }

    /**
     * This function returns the button Ignore
     * @return the button Ignore
     */
    public Button getCmdIgnore() {
        return cmdIgnore;
    }

    /**
     * This function returns the button Call
     * @return the button Call
     */
    public Button getCmdCall() {
        return cmdCall;
    }

    /**
     * This function returns the value from textMessage
     * @return the value from textMessage
     */
    public TextField getTxtMessage() {
        return txtMessage;
    }

    /**
     * This function gets the emoticons flag
     * @return gets the emoticons flag
     */
    public boolean getEmotionFlag() {
        return this.emotionFlag;
    }

    /**
     * This function returns the button Emoticons
     * @return the button Emoticons
     */
    public Button getCmdEmoticons() {
        return cmdEmoticons;
    }

    /**
     * This function exits private window
     */
    public void exitPrivateWindow() {
        chatclient.removePrivateWindow(userName);
        setVisible(false);
    }

    /**
     * This function returns the username
     * @return the username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This function set the key
     * @param key the key to be setted
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * This function returns the message canvas
     * @return the message canvas
     */
    public MessageCanvas getMessageCanvas() {
        return this.messagecanvas;
    }


}