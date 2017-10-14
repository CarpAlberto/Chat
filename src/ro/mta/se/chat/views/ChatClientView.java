/*************************************************************************
 * Chat Client Application
 * <p>
 * /Chat Client Application
 * <p>
 * /Chat Client Application
 * <p>
 * /*****************Chat Client Application
 * <p>
 * /
 * <p>
 * /
 * <p>
 * /
 *************************************************************************/
package ro.mta.se.chat.views;


import my_libs.ScrollView;
import ro.mta.se.chat.network.NetworkConstants;
import ro.mta.se.chat.singleton.UserManager;
import ro.mta.se.chat.utils.Constants;
import ro.mta.se.chat.utils.logging.Logger;

import javax.swing.*;
import java.awt.Panel;
import java.awt.Label;
import java.awt.Image;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.MediaTracker;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Map;
import java.awt.Toolkit;
import java.awt.MenuBar;
import java.awt.Menu;
import java.awt.MenuItem;

/**
 * Created by Alberto-Daniel on 11/17/2015.
 * This class represents the client chat view
 */
public class ChatClientView extends JFrame {

    /**
     * The selected username from the view
     */
    private String userName;
    /**
     * The selected room from the view
     */
    private String userRoom;
    /**
     * The logo of the chat
     */
    private String chatLogo;
    /**
     * The name of the banner
     */
    private String bannerName;
    /**
     * The logo image
     */
    private Image imgLogo;
    /**
     * The banner image
     */
    private Image imgBanner;
    /**
     * This is the icon count from the view
     */
    int iconCount;
    /**
     * This is the total user count from image
     */
    int totalUserCount;
    /**
     * The internal color map from the view
     */
    private Color[] colorMap;
    /**
     * This is the information label from the main view
     */
    private JLabel informationLabel;
    /**
     * This is the an array of icons from the view
     */
    private Image[] iconArray;
    /**
     * This is the message canvas fromm the view
     */
    private MessageCanvas messagecanvas;
    /**
     * This is the ScrollView from the view
     */
    private ScrollView messageScrollView;
    /**
     * This is the TapPanel fromview
     */
    private TapPanel tappanel;
    /**
     * This is the general TextMessage from view
     */
    private JTextField txtMessage;
    /**
     * This are the two buttons for cmsSend and cmdExit
     */
    private Button cmdSend, cmdExit;
    /**
     * This is the text font of application
     */
    private Font textFont;
    /**
     * This are the list of private chat currently opened
     */
    private PrivateChat[] privateWindow;
    /**
     * This is the count of privateWindowCount
     */
    private int privateWindowCount;
    /**
     * This is the disconnectedMenuItem
     */
    private MenuItem disconnectitem;
    /**
     * This is an SeparatorMenuItem
     */
    private MenuItem seperatoritem;
    /**
     * This si the quitItem
     */
    private MenuItem quititem;
    /**
     * This is the about item
     */
    private MenuItem aboutitem;
    /**
     * This is the connect item
     */
    private MenuItem connectItem;
    /**
     * This is the newnickname menu item
     */
    private MenuItem newNickItem;

    public PrivateChat[] getPrivate() {
        return privateWindow;
    }

    /**
     * The constructor of application which initialize all the components
     */
    public ChatClientView() {
        int g_lLoop;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        setSize((int) toolkit.getScreenSize().getWidth() / 2, (int) toolkit.getScreenSize().getHeight() - 20);
        setLayout(new BorderLayout());
        setTitle(Constants.PRODUCT_NAME);
        MenuBar menubar = new MenuBar();
        Menu chat_menu = new Menu("Chat");
        connectItem = new MenuItem("Connect");
        newNickItem = new MenuItem("Change Nickname");
        chat_menu.add(connectItem);
        chat_menu.add(newNickItem);

        Menu loginmenu = new Menu("Logout");
        disconnectitem = new MenuItem("Disconnect");
        seperatoritem = new MenuItem("-");
        quititem = new MenuItem("Quit");
        loginmenu.add(disconnectitem);
        loginmenu.add(seperatoritem);
        loginmenu.add(quititem);

        Menu aboutmenu = new Menu("Help ");
        aboutitem = new MenuItem("About " + Constants.PRODUCT_NAME);
        aboutmenu.add(aboutitem);

        menubar.add(chat_menu);
        menubar.add(loginmenu);
        menubar.add(aboutmenu);
        setMenuBar(menubar);

        userName = "";
        userRoom = "";
        iconCount = 23;
        chatLogo = "images/logo.jpg";
        bannerName = "images/banner.jpg";

        colorMap = new Color[ViewConstants.MAX_COLOR];
        /*******Backgorund*********/
        colorMap[0] = new Color(224, 236, 254);
        /*******Information Panel Background*********/
        colorMap[1] = new Color(133, 146, 255);
        /*******Button Foreground*********/
        colorMap[2] = Color.black;
        /*******Button Background**************/
        colorMap[3] = new Color(224, 236, 254);
        /*******sstab button****************/
        colorMap[4] = new Color(116, 190, 255);
        /*******message canvas*********/
        colorMap[5] = Color.black;
        /*******Top Panel Background*********/
        colorMap[6] = Color.BLUE;
        /*******Label Text Colors*********/
        colorMap[7] = Color.white;

        colorMap[8] = Color.RED;

        colorMap[9] = Color.green;

        MediaTracker tracker = new MediaTracker(this);
        int ImageCount = 0;
        imgLogo = toolkit.getImage(chatLogo);
        tracker.addImage(imgLogo, ImageCount);
        ImageCount++;
        imgBanner = toolkit.getImage(bannerName);
        tracker.addImage(imgBanner, ImageCount);
        ImageCount++;

        iconArray = new Image[iconCount];
        for (g_lLoop = 0; g_lLoop < iconCount; g_lLoop++) {
            iconArray[g_lLoop] = toolkit.getImage("icons/photo" + g_lLoop + ".gif");
            tracker.addImage(iconArray[g_lLoop], ImageCount);
            ImageCount++;
        }

        privateWindow = new PrivateChat[ViewConstants.MAX_PRIVATE_WINDOW];
        privateWindowCount = 0;

        try {
            setAppletStatus("Loading Images and Icons.....");
            tracker.waitForAll();
        } catch (InterruptedException e) {
            Logger.warn("Not all images could be loaded");
        }

        setIconImage(toolkit.getImage("images/logo.gif"));
        setAppletStatus("");
        initializeAppletComponents();
    }

    /**
     * This function update the view on changing the view
     *
     * @return true if the change was succesfuly done
     */
    public boolean onChangingRoom() {
        if (tappanel.getRoomCanvas().getSelectedUser().equals("")) {
            messagecanvas.addMessageToMessageObject("Invalid Room Selection!", ViewConstants.MESSAGE_TYPE_ADMIN);
            return false;
        }

        if (tappanel.getRoomCanvas().getSelectedUser().equals(userRoom)) {
            messagecanvas.addMessageToMessageObject("You are already in that ROOM!", ViewConstants.MESSAGE_TYPE_ADMIN);
            return false;
        }
        return true;
    }

    /**
     * This function updates the view on listing new users
     *
     * @param users the users to be listed
     */
    public void onListingNewUsers(Map<String, String> users) {
        tappanel.getUsersCanvas().clearAll();
        updateinformationLabel();
        for (Map.Entry<String, String> entry : users.entrySet()) {
            tappanel.getUsersCanvas().addListItemToMessageObject(entry.getKey(), entry.getValue(), true);
        }
    }

    /**
     * This function update the view on adding new friends
     *
     * @param elms the list of friend to be added
     */
    public void onAddingFriends(LinkedList<String> elms) {
        for (String elm : elms) {
            if (tappanel.getUsersCanvas().getIndexOf(elm) == -1) {
                tappanel.getUsersCanvas().addListItemToMessageObject(elm, elm, false);
            }
        }
    }

    /**
     * This function update the view on clearing all the messages
     */
    public void onClearingAllMessages() {
        messagecanvas.clearAll();
        messagecanvas.addMessageToMessageObject("Welcome To The " + this.tappanel.selectedRoomKey() + " Room!", ViewConstants.MESSAGE_TYPE_JOIN);
    }

    /**
     * This function updates the view on new user connects
     *
     * @param username the username of the user
     * @param ip       the ip of the user
     * @param port     the port of the user
     */
    public void onNewUserConnect(String username, String ip, int port) {
        totalUserCount++;
        String key = ip + NetworkConstants.KEY_SEPARATOR + port;
        updateinformationLabel();
        enablePrivateWindow(username, key);
        tappanel.getUsersCanvas().removeListItem(username);
        tappanel.getUsersCanvas().addListItemToMessageObject(username, key, true);
        messagecanvas.addMessageToMessageObject(username + " joins chat...", ViewConstants.MESSAGE_TYPE_JOIN);

    }

    /**
     * This function updates the view on receiving new message
     *
     * @param user    the source user
     * @param key     the source key
     * @param message the source message
     */
    public void onReceiveNewMessage(String user, String key, String message) {
        if (!(tappanel.getUsersCanvas().isIgnoredUser(user))) {
            boolean PrivateFlag = false;
            int g_lLoop;
            for (g_lLoop = 0; g_lLoop < privateWindowCount; g_lLoop++) {

                if (privateWindow[g_lLoop].getUserName().equals(user)) {
                    privateWindow[g_lLoop].addMessageToMessageCanvas(user + ":" + message);
                    privateWindow[g_lLoop].show();
                    privateWindow[g_lLoop].requestFocus();
                    PrivateFlag = true;
                    break;
                }
            }
            if (!(PrivateFlag)) {
                if (privateWindowCount >= ViewConstants.MAX_PRIVATE_WINDOW) {
                    messagecanvas.addMessageToMessageObject("You are Exceeding private window limit! So you may lose some message from your friends!", ViewConstants.MESSAGE_TYPE_ADMIN);
                } else {
                    privateWindow[privateWindowCount++] = new PrivateChat(this, user, key);
                    privateWindow[privateWindowCount - 1].addMessageToMessageCanvas(user + ":" + message);
                    privateWindow[privateWindowCount - 1].show();
                    privateWindow[privateWindowCount - 1].requestFocus();
                }
            }

        }
    }

    /**
     * This function updates the view on receiving a nicknameChanged event
     *
     * @param newNick the newNickname
     * @param oldNick the oldNickname
     */
    public void onReceivingNickNameChanged(String newNick, String oldNick) {
        this.setAppletStatus("Nickname changed " + newNick);
        ListViewCanvas users = this.tappanel.getUsersCanvas();
        users.updateItem(oldNick, newNick);
    }

    /**
     * This function remove a private window from list
     *
     * @param key
     */
    /**
     * This function update the view on errorEvent
     *
     * @param msg the error message
     */
    public void onError(String msg) {
        JOptionPane.showConfirmDialog(this, msg, "", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This function updates the view on disconnecting user
     *
     * @param key      the user key
     * @param nickname the nickname key
     */
    public void onDisconnectUser(String key, String nickname) {
        tappanel.getUsersCanvas().removeListItem(nickname);
        removeUserFromPrivateChat(key);
        messagecanvas.addMessageToMessageObject(nickname + " has been logged Out from Chat!", ViewConstants.MESSAGE_TYPE_LEAVE);
        totalUserCount--;
        updateinformationLabel();
    }

    /**
     * This function updates the view on joining new user
     *
     * @param username the username of the user
     * @param key      thr key of the user
     */
    public void onJoiningRoom(String username, String key) {
        tappanel.getUsersCanvas().addListItemToMessageObject(username, key);
        totalUserCount++;
        updateinformationLabel();
        messagecanvas.addMessageToMessageObject(username + " joins chat...", ViewConstants.MESSAGE_TYPE_JOIN);
    }

    /**
     * This function updates the view on leaving a new user
     *
     * @param username the username of the user
     * @param key      the key of the user
     * @param oldRoom  the old room of the user
     * @param newRoom  the new room of the user
     */
    public void onLeavingRoom(String username, String key, String oldRoom, String newRoom) {
        tappanel.getUsersCanvas().removeListItem(username);
        messagecanvas.addMessageToMessageObject(username + " has leaves " + oldRoom + " Room and join into " + newRoom + " Room", ViewConstants.MESSAGE_TYPE_ADMIN);
        totalUserCount--;
        updateinformationLabel();
    }

    /**
     * This function updates the view on sending new mess
     */
    public void onNewMess() {
        messagecanvas.addMessageToMessageObject(userName + ": " + txtMessage.getText(), ViewConstants.MESSAGE_TYPE_DEFAULT);
        txtMessage.setText("");
        txtMessage.requestFocus();
    }

    /**
     * This function updates the view on receiving new mess
     *
     * @param username the source username
     * @param msg      the source message
     */
    public void onNewMess(String username, String msg) {
        if (!(tappanel.getUsersCanvas().isIgnoredUser(msg)))
            messagecanvas.addMessageToMessageObject(username + ":" + msg, ViewConstants.MESSAGE_TYPE_DEFAULT);
    }

    /**
     * This function removes a private window from windows
     *
     * @param key the key of the window
     */
    public void removePrivateWindow(String key) {

        int m_UserIndex = 0;
        int g_lLoop;
        for (g_lLoop = 0; g_lLoop < privateWindowCount; g_lLoop++) {
            m_UserIndex++;
            if (privateWindow[g_lLoop].getKey().equals(key)) break;
        }
        for (int m_iLoop = m_UserIndex; m_iLoop < privateWindowCount; m_iLoop++) {
            privateWindow[m_iLoop] = privateWindow[m_iLoop + 1];
        }
        privateWindowCount--;
    }

    /**
     * This function gets the user count
     *
     * @param roomName the room name
     * @return the user count
     */
    public int getRoomUserCount(String roomName) {
        return UserManager.getInstance().usersCount(roomName);
    }

    /**
     * This function add an image to textField
     *
     * @param ImageName
     */
    public void addImageToTextField(String ImageName) {
        if (txtMessage.getText() == null || txtMessage.getText().equals(""))
            txtMessage.setText("~~" + ImageName + " ");
        else
            txtMessage.setText(txtMessage.getText() + " " + "~~" + ImageName + " ");
    }


    /**
     * This function setThe applet status
     *
     * @param Message
     */
    public void setAppletStatus(String Message) {
        if (messagecanvas != null)
            messagecanvas.addMessageToMessageObject(Message, ViewConstants.MESSAGE_TYPE_ADMIN);
    }

    /**
     * This function display help window
     */

    public void displayHelp() {

        JOptionPane.showConfirmDialog(this, Constants.PRODUCT_NAME + "developed by " + Constants.COMPANY_NAME, "", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This function close the window
     *
     * @param forever if true exit the application
     */
    public void closeWindow(boolean forever) {
        if (forever)
            System.exit(0);
        this.setVisible(false);
    }

    /**
     * This function display the current window
     */
    public void showWindow() {
        this.setVisible(true);
    }

    /**
     * This function display a window
     */

    public void displayWindow() {
        tappanel.getRoomCanvas().clearAll();
        tappanel.getRoomCanvas().addListItemToMessageObject(ViewConstants.ROOM_1, ViewConstants.ROOM_1);
        tappanel.getRoomCanvas().addListItemToMessageObject(ViewConstants.ROOM_2, ViewConstants.ROOM_2);
        tappanel.getRoomCanvas().addListItemToMessageObject(ViewConstants.ROOM_3, ViewConstants.ROOM_3);
        tappanel.getRoomCanvas().addListItemToMessageObject(ViewConstants.ROOM_4, ViewConstants.ROOM_4);
        this.setVisible(true);
    }

    /**
     * This function display the choosen option
     *
     * @param msg message to be display
     * @return true if yes was the answer
     */
    public boolean displayChooseOption(String msg) {

        if (JOptionPane.showConfirmDialog(this, msg, "", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            return true;
        } else
            return false;

    }

    /**
     * This function add an help listener
     *
     * @param listener the listener to be added
     */
    public void addHelpListener(ActionListener listener) {
        this.aboutitem.addActionListener(listener);
    }

    /**
     * This function add an listener to the connect button
     *
     * @param listener the listener to be added
     */
    public void addConnectListener(ActionListener listener) {
        this.connectItem.addActionListener(listener);
    }

    /**
     * This function add an listener to the quit itme
     *
     * @param listener the listener to be added
     */
    public void addQuitListener(ActionListener listener) {
        this.quititem.addActionListener(listener);
    }

    /**
     * This function add a listener to the disconnect item
     *
     * @param listener the listener to be added
     */
    public void addDisconnectItem(ActionListener listener) {
        this.disconnectitem.addActionListener(listener);
    }

    /**
     * This function add a listener to the newNickItem
     *
     * @param listener the listener to be added
     */
    public void addNewNickItem(ActionListener listener) {
        this.newNickItem.addActionListener(listener);
    }

    /**
     * This function add new listeners
     *
     * @param listener
     */
    public void addActionListener(ActionListener listener) {
        this.cmdSend.addActionListener(listener);
        this.cmdExit.addActionListener(listener);
    }


    /**
     * This function return the tapPannel
     *
     * @return the tapPannel
     */
    public TapPanel getTappanel() {
        return this.tappanel;
    }

    /**
     * This function gets the value from general text message
     *
     * @return the value from genral text message
     */
    public JTextField getTextMessage() {
        return this.txtMessage;
    }

    /**
     * This function returns the cmdButton
     *
     * @return the cmd Button
     */
    public Button getCmdSend() {
        return this.cmdSend;
    }

    /**
     * This function gets the cmdExit button
     *
     * @return the cmdExit button
     */
    public Button getCmdExit() {
        return this.cmdExit;
    }

    /**
     * This function sets the username
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.userName = username;
    }

    /**
     * This function gets the local username
     *
     * @return the local username
     */
    public String getUsername() {
        return this.userName;
    }

    /**
     * This function gets the icon count from the view
     *
     * @return teh icon count from the view
     */
    public int getIconCount() {
        return this.iconCount;
    }

    /**
     * This function gets the IconArray from the view
     *
     * @return the iconArray from the view
     */
    Image[] getIconArray() {
        return this.iconArray;
    }

    /**
     * This function gets the Font from the view
     *
     * @return the font from the view
     */
    Font getTextFont() {
        return this.textFont;
    }

    /**
     * This function gets the private windows count
     *
     * @return the private windows count
     */
    int getPrivateWindowCount() {
        return this.privateWindowCount;
    }

    /**
     * This function increment the number of users count
     */
    void incrementPrivateWindowCount() {
        this.privateWindowCount++;
    }

    /**
     * This function returns the message canvas
     *
     * @return the message canvas
     */
    MessageCanvas getMessageCanvas() {
        return this.messagecanvas;
    }

    /**
     * This function gets the color map
     *
     * @return the color map
     */
    Color[] getColorMap() {
        return this.colorMap;
    }

    /**
     * This function gets the tap pannel
     *
     * @return get the tap pannel
     */
    TapPanel getTapPanel() {
        return this.tappanel;
    }

    /**
     * This function gets the private window from given index
     *
     * @param index the given index
     * @return the private window
     */
    public PrivateChat getPrivateWindow(int index) {
        return privateWindow[index];
    }

    /**
     * This function sets the private window on given index
     *
     * @param index the given index
     * @param chat  the parrent chat
     */
    public void setPrivateWindow(int index, PrivateChat chat) {
        this.privateWindow[index] = chat;
    }

    /**
     * This function initialize all the components
     */
    private void initializeAppletComponents() {

        setBackground(colorMap[0]);
        Font font = new Font("Dialog", Font.BOLD, 11);
        textFont = new Font("Dialog", 0, 11);
        setFont(font);

        Panel TopPanel = new Panel(new BorderLayout());
        TopPanel.setBackground(colorMap[6]);
        Panel LogoPanel = new ImagePanel(this, imgLogo);
        TopPanel.add("East", LogoPanel);
        Panel BannerPanel = new ImagePanel(this, imgBanner);
        TopPanel.add("West", BannerPanel);
        add("North", TopPanel);

        Panel CenterPanel = new Panel(new BorderLayout());
        Panel InformationPanel = new Panel(new BorderLayout());
        InformationPanel.setBackground(colorMap[1]);
        informationLabel = new JLabel();
        updateinformationLabel();
        informationLabel.setForeground(colorMap[7]);
        InformationPanel.add("Center", informationLabel);
        CenterPanel.add("North", InformationPanel);

        Panel MessagePanel = new Panel(new BorderLayout());
        messagecanvas = new MessageCanvas(this);
        messageScrollView = new ScrollView(messagecanvas, true, true, ViewConstants.TAPPANEL_CANVAS_WIDTH, ViewConstants.TAPPANEL_CANVAS_HEIGHT, ViewConstants.SCROLL_BAR_SIZE);
        messagecanvas.setScrollView(messageScrollView);
        MessagePanel.add("Center", messageScrollView);

        tappanel = new TapPanel(this);

        MessagePanel.add("East", tappanel);
        CenterPanel.add("Center", MessagePanel);
        Panel InputPanel = new Panel(new BorderLayout());
        Panel TextBoxPanel = new Panel(new BorderLayout());
        Label LblGeneral = new Label("General Message!");
        txtMessage = new JTextField();
        txtMessage.setFont(textFont);
        cmdSend = new CustomButton(this, "Send Message!");
        TextBoxPanel.add("West", LblGeneral);
        TextBoxPanel.add("Center", txtMessage);
        TextBoxPanel.add("East", cmdSend);
        InputPanel.add("Center", TextBoxPanel);
        Panel InputButtonPanel = new Panel(new BorderLayout());
        cmdExit = new CustomButton(this, "Exit Chat");
        InputButtonPanel.add("Center", cmdExit);
        InputPanel.add("East", InputButtonPanel);
        Panel EmptyPanel = new Panel();
        InputPanel.add("South", EmptyPanel);
        CenterPanel.add("South", InputPanel);
        add("Center", CenterPanel);
        enableAll();
    }

    private void updateinformationLabel() {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("User Name: ");
        stringbuffer.append(userName);
        stringbuffer.append("       ");
        stringbuffer.append("Room Name: ");
        stringbuffer.append(userRoom);
        stringbuffer.append("       ");
        stringbuffer.append("No. Of Users: ");
        stringbuffer.append(totalUserCount);
        stringbuffer.append("       ");
        informationLabel.setText(stringbuffer.toString());

    }

    /**
     * This function enable a privae chat
     *
     * @param ToUserName username of the private chat
     * @param key        of the username
     */
    private void enablePrivateWindow(String ToUserName, String key) {
        int g_lLoop;
        for (g_lLoop = 0; g_lLoop < privateWindowCount; g_lLoop++) {
            if (privateWindow[g_lLoop].getUserName().equals(ToUserName)) {
                privateWindow[g_lLoop].getMessageCanvas().addMessageToMessageObject(ToUserName + " is Currently Online!", ViewConstants.MESSAGE_TYPE_ADMIN);
                privateWindow[g_lLoop].enableAll();
                return;
            }
        }
    }

    /**
     * This function removes user from private chat
     *
     * @param key the user key
     */
    private void removeUserFromPrivateChat(String key) {
        int g_lLoop;
        for (g_lLoop = 0; g_lLoop < privateWindowCount; g_lLoop++) {
            if (privateWindow[g_lLoop].getKey().equals(key)) {
                privateWindow[g_lLoop].getMessageCanvas().addMessageToMessageObject(privateWindow[g_lLoop].getUserName() + " is Currently Offline!", ViewConstants.MESSAGE_TYPE_ADMIN);
                privateWindow[g_lLoop].disableAll();
                return;
            }
        }
    }

    /**
     * This function disable the components
     */
    private void disableAll() {
        txtMessage.setEnabled(false);
        cmdSend.setEnabled(false);
        tappanel.enable(false);

        userName = "";
        userRoom = "";
        totalUserCount = 0;
    }

    /**
     * This function enable the components
     */
    private void enableAll() {
        txtMessage.setEnabled(true);
        cmdSend.setEnabled(true);
        tappanel.enable(true);
        ;
    }


}
