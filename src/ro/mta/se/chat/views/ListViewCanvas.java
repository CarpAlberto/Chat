package ro.mta.se.chat.views;

import my_libs.ScrollView;

import java.awt.Dimension;
import java.awt.Canvas;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Event;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.FontMetrics;


public class ListViewCanvas extends Canvas {

    /**
     * The two dimensions used internally by this class
     */
    private Dimension offDimension, dimension;
    /**
     * The image
     */
    private Image offImage;
    /**
     * The aditional graphics
     */
    private Graphics offGraphics;
    /**
     * The main view of the application
     */
    private ChatClientView chatclient;

    /**
     * This is the arrayList of the view
     */
    private ArrayList listArray;
    /**
     * This is the internal offsets used during application
     */
    private int xOffset, yOffset;
    /**
     * This is the messageObsect of the view
     */
    private MessageObject messageobject;
    /**
     * This is the scrollView of the application
     */
    private ScrollView scrollview;
    /**
     * This is the font metric of the aplication
     */
    private FontMetrics fontmetrics;
    /**
     * There are internal variables during the view
     */
    private int canvasType, totalWidth, totalHeight;
    /**
     * This is the selected user from view
     */
    private String selectedUser;

    /**
     * This is the the constructor of the view
     *
     * @param parent     the main application view
     * @param canvasType the type of canvas
     */
    public ListViewCanvas(ChatClientView parent, int canvasType) {
        chatclient = parent;
        dimension = size();
        listArray = new ArrayList();
        selectedUser = "";
        this.canvasType = canvasType;
        setFont(chatclient.getFont());
        fontmetrics = chatclient.getFontMetrics(chatclient.getFont());
    }

    /**
     * This function add a new list item to message object
     *
     * @param ListItem the list item to be added
     * @param key      the key of the list itme
     */
    public void addListItemToMessageObject(String ListItem, String key) {
        int m_startY = ViewConstants.DEFAULT_LIST_CANVAS_POSITION;
        if (listArray.size() > 0) {
            messageobject = (MessageObject) listArray.get(listArray.size() - 1);
            m_startY = messageobject.getStartY() + ViewConstants.DEFAULT_LIST_CANVAS_INCREMENT;
        }
        messageobject = new MessageObject();
        messageobject.setMessage(ListItem);
        messageobject.setStartY(m_startY);
        messageobject.setSelected(false);
        messageobject.setOnFlag(false);
        messageobject.setKey(key);
        messageobject.setWidth(fontmetrics.stringWidth(ListItem) + ViewConstants.DEFAULT_LIST_CANVAS_INCREMENT);
        listArray.add(messageobject);
        totalWidth = Math.max(totalWidth, messageobject.getWidth());
        scrollview.setValues(totalWidth, m_startY + ViewConstants.DEFAULT_LIST_CANVAS_HEIGHT);
        scrollview.setScrollPos(1, 1);
        scrollview.setScrollSteps(2, 1, ViewConstants.DEFAULT_SCROLLING_HEIGHT);
        repaint();
    }

    /**
     * This function add a new list item on message object
     *
     * @param ListItem the listitem
     * @param key      the key of the list item
     * @param on       if true then user is set to be on
     */
    public void addListItemToMessageObject(String ListItem, String key, boolean on) {
        int m_startY = ViewConstants.DEFAULT_LIST_CANVAS_POSITION;
        if (listArray.size() > 0) {
            messageobject = (MessageObject) listArray.get(listArray.size() - 1);
            m_startY = messageobject.getStartY() + ViewConstants.DEFAULT_LIST_CANVAS_INCREMENT;
        }
        messageobject = new MessageObject();
        messageobject.setMessage(ListItem);
        messageobject.setStartY(m_startY);
        messageobject.setSelected(false);
        messageobject.setOnFlag(on);
        messageobject.setKey(key);
        messageobject.setWidth(fontmetrics.stringWidth(ListItem) + ViewConstants.DEFAULT_LIST_CANVAS_INCREMENT);
        listArray.add(messageobject);
        totalWidth = Math.max(totalWidth, messageobject.getWidth());
        scrollview.setValues(totalWidth, m_startY + ViewConstants.DEFAULT_LIST_CANVAS_HEIGHT);
        scrollview.setScrollPos(1, 1);
        scrollview.setScrollSteps(2, 1, ViewConstants.DEFAULT_SCROLLING_HEIGHT);
        repaint();
    }

    /**
     * This function clears all the view
     */
    public void clearAll() {
        listArray.clear();
        totalWidth = 0;
        totalHeight = 0;
        scrollview.setValues(totalWidth, totalHeight);
    }

    /**
     * This function gets the index of given message
     *
     * @param message the given message
     * @return the index of the image
     */
    public int getIndexOf(String message) {
        int g_ILoop = 0;
        int m_listSize = listArray.size();
        for (g_ILoop = 0; g_ILoop < m_listSize; g_ILoop++) {
            messageobject = (MessageObject) listArray.get(g_ILoop);
            if (messageobject.getMessage().equals(message))
                return g_ILoop;
        }

        return -1;
    }

    /**
     * This function returns the selected key
     *
     * @return
     */
    public String getSelectedkey() {
        int index = getIndexOf(this.selectedUser);
        MessageObject msg = (MessageObject) this.listArray.get(index);
        return msg.getKey();
    }

    /**
     * This function ignore the given name
     * @param isIgnore aditional boolean check
     * @param ignoreUserName the given username
     */
    public void ignoreUser(boolean isIgnore, String ignoreUserName) {
        int m_listIndex = getIndexOf(ignoreUserName);
        if (m_listIndex >= 0) {
            messageobject = (MessageObject) listArray.get(m_listIndex);
            messageobject.setIgnored(isIgnore);
            listArray.set(m_listIndex, messageobject);

            if (isIgnore) {
                chatclient.getTapPanel().getCmdIgnoreUser().setLabel("Allow User");
                chatclient.getMessageCanvas().addMessageToMessageObject(ignoreUserName + " has been ignored!", ViewConstants.MESSAGE_TYPE_LEAVE);
            } else {
                chatclient.getTapPanel().getCmdIgnoreUser().setLabel("Ignore User");
                chatclient.getMessageCanvas().addMessageToMessageObject(ignoreUserName + " has been romoved from ignored list!", ViewConstants.MESSAGE_TYPE_JOIN);
            }
        }
    }

    /**
     * This function ignore the selected user
     *
     * @param isIgnore aditional boolean check
     */
    public void ignoreUser(boolean isIgnore) {
        if (selectedUser.equals("")) {
            chatclient.getMessageCanvas().addMessageToMessageObject("Invalid User Selection!", ViewConstants.MESSAGE_TYPE_ADMIN);
            return;
        }
        if (selectedUser.equals(chatclient.getUsername())) {
            chatclient.getMessageCanvas().addMessageToMessageObject("You can not ignored yourself!", ViewConstants.MESSAGE_TYPE_ADMIN);
            return;
        }

        ignoreUser(isIgnore, selectedUser);

    }

    /**
     * This function executes on sending a new message
     */
    public void sendDirectMessage() {
        if (selectedUser.equals("")) {
            chatclient.getMessageCanvas().addMessageToMessageObject("Invalid User Selection!", ViewConstants.MESSAGE_TYPE_ADMIN);
            return;
        }
        createPrivateWindow();
    }

    /**
     * This function check if an given username is ignored
     *
     * @param userName the username to be ignored
     * @return true if checking returns true
     */
    public boolean isIgnoredUser(String userName) {
        int m_listIndex = getIndexOf(userName);
        if (m_listIndex >= 0) {
            messageobject = (MessageObject) listArray.get(m_listIndex);
            return messageobject.isIgnored();
        }
        return false;

    }

    /**
     * This function removes a listitem
     *
     * @param listItem the listitem to be removed
     */
    public void removeListItem(String listItem) {
        int ListIndex = getIndexOf(listItem);
        if (ListIndex >= 0) {
            messageobject = (MessageObject) listArray.get(ListIndex);
            int m_StartY = messageobject.getStartY();
            listArray.remove(ListIndex);
            int m_listSize = listArray.size();
            int m_nextStartY;
            int g_ILoop;
            for (g_ILoop = ListIndex; g_ILoop < m_listSize; g_ILoop++) {
                messageobject = (MessageObject) listArray.get(g_ILoop);
                m_nextStartY = messageobject.getStartY();
                messageobject.setStartY(m_StartY);
                m_StartY = m_nextStartY;
            }

        }
        repaint();
    }

    /**
     * This function paints the view
     *
     * @param graphics the aditional graphics
     */
    private void paintFrame(Graphics graphics) {
        int m_listArraySize = listArray.size();
        int g_ILoop;
        for (g_ILoop = 0; g_ILoop < m_listArraySize; g_ILoop++) {
            messageobject = (MessageObject) listArray.get(g_ILoop);
            if ((messageobject.getStartY() + messageobject.getHeight()) >= yOffset) {
                paintListItemIntoCanvas(graphics, messageobject);
            }
        }
    }

    /**
     * This function paints an item into the CanvasObject
     *
     * @param graphics      the graphics object
     * @param messageObject the message object
     */
    private void paintListItemIntoCanvas(Graphics graphics, MessageObject messageObject) {
        int m_StartY = messageObject.getStartY() - yOffset;
        int m_imageIndex = ViewConstants.ROOM_CANVAS_ICON;
        int on_index;
        if (messageobject.isOn()) {
            on_index = ViewConstants.USER_CANVAS_ON_ICON;
        } else {
            on_index = ViewConstants.USER_CANVAS_OFF_ICON;
        }
        switch (canvasType) {
            case ViewConstants.USER_CANVAS: {
                if (messageobject.isIgnored() == true)
                    m_imageIndex = ViewConstants.USER_CANVAS_IGNORE_ICON;
                else
                    m_imageIndex = ViewConstants.USER_CANVAS_NORMAL_ICON;
                break;
            }
        }
        graphics.drawImage(chatclient.getIconArray()[m_imageIndex], 5 - xOffset, m_StartY, ViewConstants.DEFAULT_LIST_CANVAS_HEIGHT, ViewConstants.DEFAULT_LIST_CANVAS_HEIGHT, this);
        if (messageobject.isSelected() == true) {
            graphics.setColor(Color.blue);
            graphics.fillRect(5 - xOffset + ViewConstants.DEFAULT_LIST_CANVAS_HEIGHT, m_StartY, totalWidth, ViewConstants.DEFAULT_LIST_CANVAS_INCREMENT);
            graphics.setColor(Color.white);
            graphics.drawString(messageObject.getMessage(), 5 - xOffset + ViewConstants.DEFAULT_LIST_CANVAS_INCREMENT, m_StartY + ViewConstants.DEFAULT_LIST_CANVAS_HEIGHT);
        } else {
            graphics.setColor(Color.white);
            graphics.fillRect(5 - xOffset + ViewConstants.DEFAULT_LIST_CANVAS_HEIGHT, m_StartY, totalWidth, ViewConstants.DEFAULT_LIST_CANVAS_INCREMENT);
            graphics.setColor(Color.black);
            graphics.drawString(messageObject.getMessage(), 5 - xOffset + ViewConstants.DEFAULT_LIST_CANVAS_INCREMENT, m_StartY + ViewConstants.DEFAULT_LIST_CANVAS_HEIGHT);
        }
        if (this.canvasType == 0)
            graphics.drawImage(chatclient.getIconArray()[on_index], 230 - xOffset, m_StartY, ViewConstants.DEFAULT_LIST_CANVAS_HEIGHT, ViewConstants.DEFAULT_LIST_CANVAS_HEIGHT, this);
    }

    /**
     * This function handles a new event
     *
     * @param event the event
     * @return true
     */
    public boolean handleEvent(Event event) {
        if (event.id == 1001 && event.arg == scrollview) {
            if (event.modifiers == 1)
                xOffset = event.key;
            else
                yOffset = event.key;
            repaint();
            return true;
        } else {
            return super.handleEvent(event);
        }
    }

    /**
     * This function executes on mouse gets down
     *
     * @param event the event
     * @param i     the i index
     * @param j     the j index
     * @return true
     */
    public boolean mouseDown(Event event, int i, int j) {
        int CurrentY = j + yOffset;
        int m_listArraySize = listArray.size();
        boolean SelectedFlag = false;
        chatclient.getTapPanel().setTextFieldValue("");
        chatclient.getTapPanel().getCmdIgnoreUser().setLabel("Ignore User");
        int g_ILoop;
        for (g_ILoop = 0; g_ILoop < m_listArraySize; g_ILoop++) {
            messageobject = (MessageObject) listArray.get(g_ILoop);
            if ((CurrentY >= messageobject.getStartY()) && (CurrentY <= (messageobject.getStartY() + ViewConstants.DEFAULT_LIST_CANVAS_HEIGHT))) {
                messageobject.setSelected(true);
                selectedUser = messageobject.getMessage();
                SelectedFlag = true;
                if (canvasType == ViewConstants.ROOM_CANVAS) {
                    String users = Integer.toString(chatclient.getRoomUserCount(selectedUser));
                    chatclient.getTapPanel().setTextFieldValue("Total Users in " + selectedUser + " : " + users);
                }
                if (canvasType == ViewConstants.USER_CANVAS) {
                    if (isIgnoredUser(selectedUser))
                        chatclient.getTapPanel().getCmdIgnoreUser().setLabel("Allow User");
                    else
                        chatclient.getTapPanel().getCmdIgnoreUser().setLabel("Ignore User");
                }
            } else {
                messageobject.setSelected(false);
            }
        }
        repaint();
        if ((!SelectedFlag))
            selectedUser = "";

        if ((event.clickCount == 2) && (canvasType == ViewConstants.USER_CANVAS) && (!(selectedUser.equals(""))) && (!(selectedUser.equals(chatclient.getUsername())))) {
            createPrivateWindow();
        }

        return true;
    }

    /**
     * This function creates a private window
     */
    private void createPrivateWindow() {
        if (!(isIgnoredUser(selectedUser))) {
            boolean PrivateFlag = false;
            int g_ILoop;
            for (g_ILoop = 0; g_ILoop < chatclient.getPrivateWindowCount(); g_ILoop++) {
                if (chatclient.getPrivateWindow(g_ILoop).getUserName().equals(selectedUser)) {
                    chatclient.getPrivateWindow(g_ILoop).show();
                    chatclient.getPrivateWindow(g_ILoop).requestFocus();
                    PrivateFlag = true;
                    break;
                }
            }
            if (!(PrivateFlag)) {
                if (chatclient.getPrivateWindowCount() >= ViewConstants.MAX_PRIVATE_WINDOW) {
                    chatclient.getMessageCanvas().addMessageToMessageObject("You are Exceeding private window limit! So you may lose some message from your friends!", ViewConstants.MESSAGE_TYPE_ADMIN);
                } else {
                    PrivateChat chat = new PrivateChat(chatclient, selectedUser, getSelectedkey());
                    chatclient.setPrivateWindow(chatclient.getPrivateWindowCount(), chat);
                    chatclient.incrementPrivateWindowCount();
                    chatclient.getPrivateWindow(chatclient.getPrivateWindowCount() - 1).show();
                    chatclient.getPrivateWindow(chatclient.getPrivateWindowCount() - 1).requestFocus();
                }
            }

        }
    }

    /**
     * This function paint the view
     *
     * @param graphics the aditional graphics
     */
    public void paint(Graphics graphics) {
        dimension = size();
        if ((offGraphics == null) || (dimension.width != offDimension.width) || (dimension.height != offDimension.height)) {
            offDimension = dimension;
            offImage = createImage(dimension.width, dimension.height);
            offGraphics = offImage.getGraphics();
        }
        offGraphics.setColor(Color.white);
        offGraphics.fillRect(0, 0, dimension.width, dimension.height);
        paintFrame(offGraphics);
        graphics.drawImage(offImage, 0, 0, null);
    }

    /**
     * This function updates the oldItem with newOne
     *
     * @param oldItem the oldItem
     * @param newItem the newOne
     */
    public void updateItem(String oldItem, String newItem) {
        int index = getIndexOf(oldItem);
        if (index == -1)
            return;
        MessageObject obj = (MessageObject) (listArray.get(index));
        obj.setMessage(newItem);
        repaint();
    }

    /**
     * This function updates the view
     *
     * @param graphics the aditional graphics
     */
    public void update(Graphics graphics) {
        paint(graphics);
    }

    /**
     * This function get the scroll view
     *
     * @return the scroll view
     */
    public ScrollView getScrollView() {
        return this.scrollview;
    }

    /**
     * This function set the scroll view
     *
     * @param theView the scroll view
     */
    public void setScrollview(ScrollView theView) {
        this.scrollview = theView;
    }

    /**
     * This function returns the selected User
     *
     * @return the selected user
     */
    public String getSelectedUser() {
        return this.selectedUser;
    }

}