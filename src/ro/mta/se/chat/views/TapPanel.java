package ro.mta.se.chat.views;

import my_libs.ScrollView;

import java.awt.Panel;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Alberto-Daniel on 11/23/2015.
 * This is a class that represents a border and extends Canvas object
 */
public class TapPanel extends Panel {

    /**
     * This is the chatclient of the application
     */
    private ChatClientView chatclient;
    /**
     * This is the count of users of this view
     */
    private TextField txtUserCount;
    /**
     * There are the scrollView of this view
     */
    private ScrollView imageScrollView, userScrollView, roomScrollView;
    /**
     * This is the image canvas of this view
     */
    private ImageCanvas imagecanvas;
    /**
     * This is the userCanvas of this view
     */
    private ListViewCanvas userCanvas;
    /**
     * This is the roomCanvas of the view
     */
    private ListViewCanvas roomCanvas;
    /**
     * This is the buttons from this view
     */
    private Button cmdChangeRoom, cmdIgnoreUser, cmdSendDirect, cmdViewHistory;

    /**
     * This is the constructor of the view
     *
     * @param parent the main app
     */
    public TapPanel(ChatClientView parent) {
        chatclient = parent;

        Panel Tappanel = new Panel(new BorderLayout());
        CardLayout cardlayout = new CardLayout();
        Panel MainPanel = new Panel(cardlayout);
        Panel UserPanel = new Panel(new BorderLayout());
        userCanvas = new ListViewCanvas(chatclient, ViewConstants.USER_CANVAS);

        userScrollView = new ScrollView(userCanvas, true, true, ViewConstants.TAPPANEL_CANVAS_WIDTH, ViewConstants.TAPPANEL_CANVAS_HEIGHT, ViewConstants.SCROLL_BAR_SIZE);
        userCanvas.setScrollview(userScrollView);
        UserPanel.add("Center", userScrollView);

        Panel UserButtonPanel = new Panel(new BorderLayout());
        cmdSendDirect = new CustomButton(chatclient, "Send Direct Message");
        UserButtonPanel.add("North", cmdSendDirect);
        cmdIgnoreUser = new CustomButton(chatclient, "Ignore User");
        UserButtonPanel.add("Center", cmdIgnoreUser);
        cmdViewHistory = new CustomButton(chatclient, "View History");
        UserButtonPanel.add("South", cmdViewHistory);

        UserPanel.add("South", UserButtonPanel);
        Panel RoomPanel = new Panel(new BorderLayout());
        roomCanvas = new ListViewCanvas(chatclient, ViewConstants.ROOM_CANVAS);

        roomScrollView = new ScrollView(roomCanvas, true, true, ViewConstants.TAPPANEL_CANVAS_WIDTH, ViewConstants.TAPPANEL_CANVAS_HEIGHT, ViewConstants.SCROLL_BAR_SIZE);
        roomCanvas.setScrollview(roomScrollView);
        RoomPanel.add("Center", roomScrollView);

        Panel RoomButtonPanel = new Panel(new BorderLayout());
        Panel RoomCountPanel = new Panel(new BorderLayout());
        Label LblCaption = new Label("ROOM COUNT", 1);
        RoomCountPanel.add("North", LblCaption);
        txtUserCount = new TextField();
        txtUserCount.setEditable(false);
        RoomCountPanel.add("Center", txtUserCount);
        RoomButtonPanel.add("Center", RoomCountPanel);
        cmdChangeRoom = new CustomButton(chatclient, "Change Room");
        RoomButtonPanel.add("South", cmdChangeRoom);
        RoomPanel.add("South", RoomButtonPanel);
        Panel ImagePanel = new Panel(new BorderLayout());

        imagecanvas = new ImageCanvas(chatclient);
        imageScrollView = new ScrollView(imagecanvas, true, true, ViewConstants.TAPPANEL_CANVAS_WIDTH, ViewConstants.TAPPANEL_CANVAS_HEIGHT, ViewConstants.SCROLL_BAR_SIZE);
        imagecanvas.setScrollview(imageScrollView);
        imagecanvas.addIconsToMessageObject();
        ImagePanel.add("Center", imageScrollView);
        MainPanel.add("UserPanel", UserPanel);
        MainPanel.add("RoomPanel", RoomPanel);
        MainPanel.add("ImagePanel", ImagePanel);
        cardlayout.show(MainPanel, "UserPanel");
        BorderPanel borderpanel = new BorderPanel(this, chatclient, cardlayout, MainPanel, ViewConstants.TAPPANEL_WIDTH, ViewConstants.TAPPANEL_HEIGHT);
        borderpanel.addTab("USERS", "UserPanel");
        borderpanel.addTab("ROOMS", "RoomPanel");
        borderpanel.addTab("EMOTICONS", "ImagePanel");
        Tappanel.add(borderpanel);
        add("Center", Tappanel);


    }

    /**
     * This is the update given by send Direct message
     */
    public void viewSendMsg() {
        userCanvas.sendDirectMessage();
    }

    /**
     * This is the update on setIgnore button
     *
     * @param var the aditional check
     */
    public void viewSetIgnore(boolean var) {
        userCanvas.ignoreUser(var);
    }

    /**
     * This function returns the button changeRoom
     *
     * @return the changeRoom
     */
    public Button getCmdChangeRoom() {
        return cmdChangeRoom;
    }

    /**
     * This function returns the button the ignore user
     *
     * @return the ignore user button
     */
    public Button getCmdIgnoreUser() {
        return cmdIgnoreUser;
    }

    /**
     * This function returns the send direct button
     *
     * @return the send direct button
     */
    public Button getCmdSendDirect() {
        return cmdSendDirect;
    }

    /**
     * This function get the view history button
     *
     * @return the view history button
     */
    public Button getCmdViewHistory() {
        return cmdViewHistory;
    }

    /**
     * This function add a new listener to the buttons
     *
     * @param listener listener to be added
     */
    public void addListener(ActionListener listener) {
        cmdChangeRoom.addActionListener(listener);
        cmdIgnoreUser.addActionListener(listener);
        cmdSendDirect.addActionListener(listener);
        cmdViewHistory.addActionListener(listener);
    }

    /**
     * This function returns the selected user
     *
     * @return the selected user
     */
    public String selectedUser() {
        return userCanvas.getSelectedUser();
    }

    /**
     * This function returns the selected room key
     *
     * @return the selected room key
     */
    public String selectedRoomKey() {
        return roomCanvas.getSelectedUser();
    }

    /**
     * This function returns the room key
     *
     * @return the room key
     */
    public ListViewCanvas getRoomCanvas() {
        return this.roomCanvas;
    }

    /**
     * This function returns the user canvas
     *
     * @return the user canvas
     */
    public ListViewCanvas getUsersCanvas() {
        return this.userCanvas;
    }

    /**
     * This function sets the text field value
     *
     * @param value the value to be setted
     */
    public void setTextFieldValue(String value) {
        this.txtUserCount.setText(value);
    }


}