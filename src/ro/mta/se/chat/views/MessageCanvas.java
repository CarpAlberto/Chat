package ro.mta.se.chat.views;

import my_libs.ScrollView;

import java.awt.Dimension;
import java.awt.Canvas;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Event;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.util.StringTokenizer;

/**
 * Created by Alberto-Daniel on 11/17/2015.
 * This class represents the message canvas
 */
public class MessageCanvas extends Canvas {
    /**
     * This is the two dimension object used internally
     */
    private Dimension offDimension, dimension;
    /**
     * This is the offImage of thevuew
     */
    private Image offImage;
    /**
     * This is the graphical paint of the image
     */
    private Graphics offGraphics;
    /**
     * This is the chat client of the image
     */
    private ChatClientView chatclient;
    /**
     * This is the messageArray of the object
     */
    private ArrayList messageArray;
    /**
     * This is the offsets used internally
     */
    private int xOffset, yOffset, horizantalSpace;
    /**
     * This is the messageObject of the view
     */
    private MessageObject messageobject;
    /**
     * This is the scrollview of the application
     */
    private ScrollView scrollview;
    /**
     * This is the fontmetrics of the view
     */
    private FontMetrics fontmetrics;
    /**
     * There are the the int variables used internally
     */
    private int totalWidth, messageCount, totalHeight;
    /**
     * There are the two fonts used internally
     */
    private Font userNameFont, textFont;

    /**
     * This is the constructor of the application
     *
     * @param parent the parent of the view
     */
    public MessageCanvas(ChatClientView parent) {
        chatclient = parent;
        dimension = chatclient.getSize();
        messageArray = new ArrayList();
        messageCount = 0;
        totalWidth = 0;
        horizantalSpace = 2;
        userNameFont = chatclient.getFont();
        textFont = chatclient.getTextFont();
        setFont(chatclient.getFont());
        fontmetrics = chatclient.getFontMetrics(chatclient.getFont());
    }

    /**
     * This function clears all the view
     */
    public void clearAll() {
        messageArray.clear();
        totalWidth = 0;
        totalHeight = 0;
        messageCount = 0;
        scrollview.setValues(totalWidth, totalHeight);
    }

    /**
     * This function add a meesage to message object
     *
     * @param Message     the message of the canvas
     * @param MessageType the type of the message
     */
    public void addMessageToMessageObject(String Message, int MessageType) {
        String m_Message = "";
        StringTokenizer tokenizer = new StringTokenizer(Message, " ",false);
        while (tokenizer.hasMoreTokens()) {
            String tokenString = tokenizer.nextToken();
            if (fontmetrics.stringWidth(m_Message + tokenString) < dimension.width) {
                m_Message = m_Message + tokenString + " ";
            }
            else
            {
                addMessage(m_Message, MessageType);
                m_Message = "";
            }
        }
        addMessage(m_Message, MessageType);
    }

    /**
     * This function add a new message
     *
     * @param Message     the message
     * @param MessageType the message type
     */
    private void addMessage(String Message, int MessageType) {
        int m_startY = ViewConstants.DEFAULT_MESSAGE_CANVAS_POSITION;
        if (messageArray.size() > 0) {
            messageobject = (MessageObject) messageArray.get(messageArray.size() - 1);
            m_startY = messageobject.getStartY() + messageobject.getHeight();
        }

        messageobject = new MessageObject();
        messageobject.setMessage(Message);
        messageobject.setStartY(m_startY);
        messageobject.setMessageType(MessageType);
        if (Message.indexOf("~~") >= 0) {
            messageobject.setImage(true);
            messageobject.setWidth(ViewConstants.DEFAULT_ICON_WIDTH);
            messageobject.setHeight(ViewConstants.DEFAULT_ICON_HEIGHT);
        } else {
            messageobject.setImage(false);
            messageobject.setWidth(fontmetrics.stringWidth(Message));
            messageobject.setHeight(fontmetrics.getHeight() + fontmetrics.getDescent());
        }
        messageArray.add(messageobject);
        messageCount++;
        totalWidth = Math.max(totalWidth, messageobject.getWidth());
        totalHeight = m_startY + messageobject.getHeight();
        scrollview.setValues(totalWidth, totalHeight);

        int m_Height = totalHeight - yOffset;
        if (m_Height > dimension.height) {
            yOffset = totalHeight - dimension.height;
        }
        scrollview.setScrollPos(2, 2);
        scrollview.setScrollSteps(2, 1, ViewConstants.DEFAULT_SCROLLING_HEIGHT);
        repaint();
    }

    /**
     * This function paints the current frame
     *
     * @param graphics the aditional graphics
     */
    private void paintFrame(Graphics graphics) {
        if (messageCount < 1) return;
        int m_YPos = yOffset + dimension.height;
        int m_StartPos = 0;
        int g_ILoop;
        int m_listArraySize = messageArray.size();
        for (g_ILoop = 0; g_ILoop < messageCount && m_StartPos < m_YPos; g_ILoop++) {
            if (m_listArraySize < g_ILoop) return;
            messageobject = (MessageObject) messageArray.get(g_ILoop);
            if (messageobject.getStartY() >= yOffset) {
                paintMessageIntoCanvas(graphics, messageobject);
                m_StartPos = messageobject.getStartY();
            }
        }

        if (g_ILoop < messageCount) {
            messageobject = (MessageObject) messageArray.get(g_ILoop);
            paintMessageIntoCanvas(graphics, messageobject);
        }
    }

    /**
     * This message paint the message into canvas
     *
     * @param graphics      the aditional graphics
     * @param messageObject the messageObject
     */
    private void paintMessageIntoCanvas(Graphics graphics, MessageObject messageObject) {
        graphics.setColor(Color.black);
        int m_YPos = messageobject.getStartY() - yOffset;
        int m_XPos = 5 - xOffset;
        int CustomWidth = 0;
        String Message = messageobject.getMessage();
        if (Message.indexOf(":") >= 0) {
            graphics.setFont(userNameFont);
            chatclient.getGraphics().setFont(userNameFont);
            fontmetrics = chatclient.getGraphics().getFontMetrics();
            String m_UserName = Message.substring(0, Message.indexOf(":") + 1);
            graphics.drawString(m_UserName, m_XPos + CustomWidth, m_YPos);
            CustomWidth += fontmetrics.stringWidth(m_UserName) + horizantalSpace;
            Message = Message.substring(Message.indexOf(":") + 1);
        }
        chatclient.getGraphics().setFont(textFont);
        graphics.setFont(textFont);
        fontmetrics = chatclient.getGraphics().getFontMetrics();
        if (messageobject.isImage()) {
            StringTokenizer tokenizer = new StringTokenizer(Message, " ");
            while (tokenizer.hasMoreTokens()) {
                String tokenString = tokenizer.nextToken();
                if (tokenString.indexOf("~~") >= 0) {
                    try {
                        int m_ImageIndex = Integer.parseInt(tokenString.substring(2));
                        if ((m_ImageIndex >= 0) && (m_ImageIndex < chatclient.getIconCount())) {
                            graphics.drawImage(chatclient.getIconArray()[m_ImageIndex], m_XPos + CustomWidth, m_YPos - 15, messageobject.getWidth(), messageobject.getHeight(), this);
                            CustomWidth += messageobject.getWidth() + horizantalSpace;

                        }
                    } catch (Exception _Exc) {

                    }
                } else {
                    graphics.drawString(tokenString, m_XPos + CustomWidth, m_YPos);
                    CustomWidth += fontmetrics.stringWidth(tokenString) + horizantalSpace;
                }
                if (totalWidth < m_XPos + CustomWidth) {
                    totalWidth = m_XPos + CustomWidth;
                    scrollview.setValues(totalWidth, totalHeight);
                }
            }

        } else {

            switch (messageobject.getMessageType()) {
                case ViewConstants.MESSAGE_TYPE_DEFAULT: {
                    graphics.setColor(Color.black);
                    break;
                }
                case ViewConstants.MESSAGE_TYPE_JOIN: {
                    graphics.setColor(Color.blue);
                    break;
                }
                case ViewConstants.MESSAGE_TYPE_LEAVE: {
                    graphics.setColor(Color.red);
                    break;
                }
                case ViewConstants.MESSAGE_TYPE_ADMIN: {
                    graphics.setColor(Color.gray);
                    break;
                }
            }
            graphics.drawString(Message, m_XPos + CustomWidth, m_YPos);
        }

        graphics.setFont(userNameFont);
        chatclient.getGraphics().setFont(userNameFont);
        fontmetrics = chatclient.getGraphics().getFontMetrics();
    }

    /**
     * This function handles events
     *
     * @param event the current event
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
     * This function handle the mouse down event
     *
     * @param event the event
     * @param i     the i index
     * @param j     the j index
     * @return true
     */
    public boolean mouseDown(Event event, int i, int j) {
        return true;
    }

    /**
     * This function paints the current window
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
     * This function updates the current graphics
     *
     * @param graphics the aditional garphics
     */
    public void update(Graphics graphics) {
        paint(graphics);
    }

    /**
     * This function sets the scrollView
     *
     * @param theView the scrollView
     */
    public void setScrollView(ScrollView theView) {
        this.scrollview = theView;
    }


}