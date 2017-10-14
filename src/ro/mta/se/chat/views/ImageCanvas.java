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

public class ImageCanvas extends Canvas {
    /**
     * This is the two dimesnsion objects used internally in the object
     */
    private Dimension offDimension, dimension;
    /**
     * The image from canvas
     */
    private Image offImage;
    /**
     * The aditional graphics
     */
    private Graphics offGraphics;
    /**
     * The parent of the view
     */
    private ChatClientView chatclient;
    /**
     * The icon Array from the view
     */
    private ArrayList iconArray;
    /**
     * The two offsets from the veiw
     */
    private int xOffset, yOffset;
    /**
     * The messageObject ffrom the view
     */
    private MessageObject messageobject;
    /**
     * The scrollView
     */
    private ScrollView scrollview;
    /**
     * The selected image from the view
     */
    private String selectedImage;

    /**
     * This is the constructor of the application
     *
     * @param parent the parent chat
     */
    ImageCanvas(ChatClientView parent) {
        chatclient = parent;
        dimension = this.size();
        iconArray = new ArrayList();
    }

    /**
     * This function add a new message object to the application
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
            if (g_ILoop % 3 == 0) {
                StartX = ViewConstants.IMAGE_CANVAS_START_POSITION;
                StartY += ViewConstants.DEFAULT_ICON_HEIGHT + ViewConstants.DEFAULT_IMAGE_CANVAS_SPACE;
            } else {
                StartX += ViewConstants.DEFAULT_ICON_WIDTH + ViewConstants.DEFAULT_IMAGE_CANVAS_SPACE;
            }
        }

        scrollview.setValues(dimension.width, StartY);
        scrollview.setScrollPos(1, 1);
        scrollview.setScrollSteps(2, 1, ViewConstants.DEFAULT_SCROLLING_HEIGHT);
        repaint();
    }

    /**
     * This function paint the frame
     *
     * @param graphics the graphics object
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
     * This function paint the Image into canvas object
     *
     * @param graphics
     * @param messageObject
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
     * This function handles a new event
     *
     * @param event event to be handled
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
     * This function fires when mouse ENTER the object region
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
        int m_iconListSize = iconArray.size();
        int g_ILoop;
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
     * This function fires when mouse is down the object region
     *
     * @param event the action event
     * @param i     the i index
     * @param j     the j index
     * @return true
     */
    public boolean mouseDown(Event event, int i, int j) {
        int CurrentY = j + yOffset;
        int m_iconListSize = iconArray.size();
        int g_ILoop;
        for (g_ILoop = 0; g_ILoop < m_iconListSize; g_ILoop++) {
            messageobject = (MessageObject) iconArray.get(g_ILoop);
            if ((CurrentY <= messageobject.getStartY() + messageobject.getHeight()) && (i <= messageobject.getStartY() + messageobject.getWidth())) {
                chatclient.addImageToTextField(messageobject.getMessage());
                break;
            }
        }
        return true;
    }

    /**
     * This function updates the frame
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
     * @param graphics the adidional graphics
     */
    public void update(Graphics graphics) {
        paint(graphics);
    }

    /**
     * This function sets the scrollView
     *
     * @param view
     */
    public void setScrollview(ScrollView view) {
        scrollview = view;
    }

}