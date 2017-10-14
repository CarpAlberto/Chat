package ro.mta.se.chat.views;

import my_libs.ScrollView;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Alberto-Daniel on 12/20/2015.
 * This view is responsable for the displaying the hisatory of the app
 */
public class HistoryView extends JFrame {
    /**
     * This is the message scroll of the application
     */
    private ScrollView messageScrollView;

    /**
     * This is the client chat of the application
     */
    private ChatClientView chatclient;

    /**
     * This is the emotioncanvas of the application
     */
    private MessageCanvas messagecanvas;

    /**
     * This function initialize the components
     */

    private void init() {
        setLayout(null);
        Label LblConversation = new Label("History");
        LblConversation.setForeground(chatclient.getColorMap()[4]);
        LblConversation.setBounds(5, 30, 400, 20);
        add(LblConversation);
        Panel MessagePanel = new Panel(new BorderLayout());
        MessagePanel.setBackground(Color.green);
        messagecanvas = new MessageCanvas(chatclient);
        messageScrollView = new ScrollView(messagecanvas, true, true, ViewConstants.PRIVATE_WINDOW_WIDTH, ViewConstants.PRIVATE_WINDOW_HEIGHT, ViewConstants.SCROLL_BAR_SIZE);
        messagecanvas.setScrollView(messageScrollView);
        MessagePanel.add("Center", messageScrollView);
        MessagePanel.setBounds(5, 50, 400, 200);
        add(MessagePanel);
        setSize(ViewConstants.PRIVATE_WINDOW_WIDTH, ViewConstants.PRIVATE_WINDOW_HEIGHT);
        setResizable(false);
        this.requestFocus();
    }

    /**
     * This is the constructor of the view
     */
    public HistoryView(ChatClientView theView) {
        this.chatclient = theView;
        Image IconImage = Toolkit.getDefaultToolkit().getImage("images/logo.gif");
        setIconImage(IconImage);
        setBackground(chatclient.getColorMap()[0]);
        setFont(chatclient.getFont());
        init();
        setVisible(false);

    }

    /**
     * This function sets the history text
     *
     * @param text the text value
     */
    public void setHistory(String text) {
        this.messagecanvas.addMessageToMessageObject(text, ViewConstants.MESSAGE_TYPE_DEFAULT);
    }

    /**
     * This function set just the time
     *
     * @param time the text value
     */
    public void setTime(String time) {
        this.messagecanvas.addMessageToMessageObject(time, ViewConstants.MESSAGE_TYPE_LEAVE);
    }

    /**
     * This function display the history of the
     */
    public void displayHistory() {
        setVisible(true);
    }

    /**
     * This function clear text from textArea
     */
    public void clearText() {
        this.messagecanvas.clearAll();
    }

}
