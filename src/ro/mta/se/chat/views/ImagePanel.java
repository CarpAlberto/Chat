package ro.mta.se.chat.views;

import java.awt.Panel;
import java.awt.Image;
import java.awt.Graphics;

/**
 *  Created by Alberto-Daniel on 11/23/2015.
 *  This is the panel containing an image
 */
public class ImagePanel extends Panel {

    /**
     * The parent view of the application
     */
    private ChatClientView parent;
    /**
     * The displayed image from the parent
     */
    private Image displayImage;

    /**
     * This is the constructor which takes the input parameters
     * @param chatclient the client chat
     * @param image the image to be displayed
     */
    public ImagePanel(ChatClientView chatclient, Image image) {
        setLayout(null);
        parent = chatclient;
        displayImage = image;
        int XPos = image.getWidth(this);
        int YPos = image.getHeight(this);
        setBounds(0, 0, XPos + ViewConstants.TOP_PANEL_START_POS, YPos + ViewConstants.TOP_PANEL_START_POS);
    }

    /**
     * This function draw the view with given graphics
     * @param graphics the given graphics
     */
    public void paint(Graphics graphics) {
        graphics.drawImage(displayImage, ViewConstants.TOP_PANEL_START_POS, ViewConstants.TOP_PANEL_START_POS, this);
    }

    /**
     * This function returns the parent chat
     * @return the parent chat
     */
    public ChatClientView getParent() {
        return parent;
    }

}