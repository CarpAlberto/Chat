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

public class EmotionCanvas extends Canvas {

    /**
     * This are the dimensions of the screen used locally in the application
     */
    private Dimension offDimension, dimension;
    /**
     * This is the image from the emotion canvas
     */
    private Image offImage;
    /**
     * This is the used graphics from application
     */
    private Graphics offGraphics;
    /**
     * This is the chatclient from application
     */
    private ChatClientView chatclient;
    /**
     * This is the array of icons
     */
    private ArrayList iconArray;
    /**
     * This is the yOffset from aplication
     */
    private int yOffset;
    /**
     * This is the messageObject from application
     */
    private MessageObject messageobject;
    /**
     * This is the scrollview from application
     */
    private ScrollView scrollview;
    /**
     * This is the selectedImage from aplication
     */
    private String selectedImage;
    /**
     * This is the private chat from application
     */
    private PrivateChat privatechat;

    /**
     * This is the constructor that takes two arguments
     *
     * @param parent        the parent of app
     * @param parentPrivate the private chat associated
     */

    public EmotionCanvas(ChatClientView parent, PrivateChat parentPrivate) {
        chatclient = parent;
        privatechat = parentPrivate;
        dimension = size();
        iconArray = new ArrayList();
        setBackground(chatclient.getColorMap()[0]);
        setFont(chatclient.getTextFont());
    }

    /**
     * This function add an icon to messageObject
     */
    public void addIconsToMessageObject() {
        int StartX = ViewConstants.IMAGE_CANVAS_START_POSITION;
        int StartY = ViewConstants.IMAGE_CANVAS_START_POSITION;
        int g_ILoop;
        for (g_ILoop = 1; g_ILoop <= chatclient.getIconCount(); g_ILoop++) {
            messageobject = new MessageObject();
            messageobject.setMessage((g_ILoop - 1) + "");
            messageobject.setStartX(StartX);
            messageobject.setStartY(StartY);
            messageobject.setImage(true);
            messageobject.setWidth(ViewConstants.DEFAULT_ICON_WIDTH);
            messageobject.setHeight(ViewConstants.DEFAULT_ICON_HEIGHT);
            iconArray.add(messageobject);
            if (g_ILoop % 6 == 0) {
                StartX = ViewConstants.IMAGE_CANVAS_START_POSITION;
                StartY += ViewConstants.DEFAULT_ICON_HEIGHT + ViewConstants.DEFAULT_IMAGE_CANVAS_SPACE;
            } else {
                StartX += ViewConstants.DEFAULT_ICON_WIDTH + ViewConstants.DEFAULT_IMAGE_CANVAS_SPACE;
            }
        }

        scrollview.setValues(dimension.width, StartY);
        scrollview.setScrollPos(1, 1);
        scrollview.setScrollSteps(2, 1,
                ViewConstants.DEFAULT_SCROLLING_HEIGHT);
        repaint();
    }

    /**
     * This function paint the frame
     *
     * @param graphics the graphics asociated
     */
    private void paintFrame(Graphics graphics) {
        int m_iconListSize = iconArray.size();
        int g_ILoop;
        for (g_ILoop = 0; g_ILoop < m_iconListSize; g_ILoop++) {
            messageobject = (MessageObject) iconArray.get(g_ILoop);
            if ((messageobject.getStartY() + messageobject.getHeight()) >= yOffset) {
                paintImagesIntoCanvas(graphics, messageobject);
            }
        }
    }

    /**
     * This function draw an image into canvas
     *
     * @param graphics      the asociated graphics
     * @param messageObject the messageObject asociated
     */
    private void paintImagesIntoCanvas(Graphics graphics, MessageObject messageObject) {
        int m_StartY = messageObject.getStartY() - yOffset;
        if (messageobject.getMessage().equals(selectedImage))
            graphics.draw3DRect(messageObject.getStartX() - 2, m_StartY - 2, ViewConstants.DEFAULT_ICON_WIDTH + 2, ViewConstants.DEFAULT_ICON_HEIGHT + 2, true);
        graphics.drawImage(chatclient.getIconArray()[Integer.parseInt(messageObject.getMessage())], messageObject.getStartX(), m_StartY, ViewConstants.DEFAULT_ICON_WIDTH, ViewConstants.DEFAULT_ICON_HEIGHT, this);
        graphics.setColor(Color.black);
        graphics.drawString(ViewConstants.ICON_NAME + messageObject.getMessage(), messageObject.getStartX() - 1, m_StartY + ViewConstants.DEFAULT_ICON_HEIGHT + 10);
    }

    /**
     * This function handles a certain event
     *
     * @param event the event
     * @return true
     */
    public boolean handleEvent(Event event) {
        int xOffset;
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
     * This function fires when mouse enter the object region
     *
     * @param event the action event
     * @param i     the i index
     * @param j     the j index
     * @return true
     */
    public boolean mouseEnter(Event event, int i, int j) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        return true;
    }

    /**
     * This function fires when mouse exit the object region
     *
     * @param event the action event
     * @param i     the i index
     * @param j     the j index
     * @return true
     */
    public boolean mouseExit(Event event, int i, int j) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        return true;
    }

    /**
     * This function fires when mouse move the object region
     *
     * @param event the action event
     * @param i     the i index
     * @param j     the j index
     * @return true
     */
    public boolean mouseMove(Event event, int i, int j) {
        int CurrentY = j + yOffset;
        int g_ILoop;
        int m_iconListSize = iconArray.size();
        for (g_ILoop = 0; g_ILoop < m_iconListSize; g_ILoop++) {
            messageobject = (MessageObject) iconArray.get(g_ILoop);
            if ((CurrentY <= messageobject.getStartY() + messageobject.getHeight()) && (i <= messageobject.getStartY() + messageobject.getWidth())) {
                selectedImage = messageobject.getMessage();
                repaint();
                break;
            }
            selectedImage = null;
        }
        return true;
    }

    /**
     * This function fires when mouse move down the object region
     *
     * @param event the action event
     * @param i     the i index
     * @param j     the j index
     * @return true
     */
    public boolean mouseDown(Event event, int i, int j) {
        int CurrentY = j + yOffset;
        int g_ILoop;
        int m_iconListSize = iconArray.size();
        for (g_ILoop = 0; g_ILoop < m_iconListSize; g_ILoop++) {
            messageobject = (MessageObject) iconArray.get(g_ILoop);
            if ((CurrentY <= messageobject.getStartY() + messageobject.getHeight()) && (i <= messageobject.getStartX() + messageobject.getWidth())) {
                privatechat.addImageToTextField(messageobject.getMessage());
                break;
            }
        }
        return true;
    }

    /**
     * This function paint the region
     *
     * @param graphics the asociated graphics
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
     * This function updates the frame
     *
     * @param graphics the asociated graphics
     */
    public void update(Graphics graphics) {
        paint(graphics);
    }

    /**
     * This function stes the scroll view
     *
     * @param view
     */
    public void setScrollview(ScrollView view) {
        this.scrollview = view;
    }

}