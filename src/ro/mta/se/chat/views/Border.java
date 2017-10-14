
package ro.mta.se.chat.views;

import java.awt.*;
import java.awt.Graphics;
import java.awt.Dimension;
import java.util.Vector;

/**
 * Created by Alberto-Daniel on 11/23/2015.
 * This is a class that represents a border and extends Canvas object
 */
public class Border extends Canvas {
    /**
     * The mode of the Border
     */
    int mode;
    /**
     * The width of the border
     */
    int width;
    /**
     * The height of the border
     */
    int height;
    /**
     * The size of the border
     */
    int size;
    /**
     * The bSize of the border
     */
    int bsize;
    /**
     * The dimension of the border
     */
    Dimension dim;
    /**
     * The BorderPanel of the border
     */
    BorderPanel parent;

    /**
     * The TapPanel that is inside border
     */
    TapPanel cframe;
    /**
     * The height of the tab
     */
    int tabHeight;
    /**
     * The client chat
     */
    ChatClientView chatclient;

    /**
     * The font metric object
     */
    FontMetrics fontmetrics;

    /**
     * This is the constructor of the
     *
     * @param tappanel
     * @param app
     * @param borderpanel
     * @param i
     * @param j
     */
    public Border(TapPanel tappanel, ChatClientView app, BorderPanel borderpanel, int i, int j) {
        cframe = tappanel;
        mode = i;
        size = j;
        chatclient = app;
        parent = borderpanel;
        bsize = 4;
        tabHeight = 22;
        if (i == 1) {
            height = 38;
            width = j;
        }
        if (i == 4) {
            height = bsize + 4;
            width = j;
        }
        if (i == 2 || i == 3) {
            width = bsize + 4;
            height = j;
        }
        if (i == 5) {
            width = j;
            height = 8;
        }
        dim = new Dimension(width, height);
        resize(dim);
        validate();
    }

    /**
     * This function creates a custom button
     *
     * @param g the associeted graphics
     */
    public void drawBottom(Graphics g) {
        int i = size().width;
        int j = size().height;
        g.setColor(chatclient.getColorMap()[4]);
        g.fillRect(0, 0, i, j);
        g.setColor(Color.white);
        g.drawLine(1, 0, 1, j - 2);
        int k = i - (bsize + 4);
        g.drawLine(bsize + 2, 1, k, 1);
        g.fillRect(k, 0, 1, 1);
        g.setColor(Color.gray);
        g.drawLine(bsize + 2, 0, bsize + 2, 0);
        g.drawLine(1, bsize + 2, i - 2, bsize + 2);
        g.drawLine(i - 2, 0, i - 2, j - 2);
        g.setColor(chatclient.getColorMap()[4]);
        g.drawLine(1, bsize + 3, i - 1, bsize + 3);
        g.drawLine(i - 1, 0, i - 1, j - 1);
    }

    /**
     * This function paint the border panel
     *
     * @param g the additiona graphics
     */
    public void paint(Graphics g) {
        if (mode == 1) {
            drawTabs(g);
            return;
        }
        if (mode == 2) {
            drawVertical(g);
            return;
        }
        if (mode == 3) {
            drawVertical(g);
            return;
        }
        if (mode == 4) {
            drawBottom(g);
            return;
        } else {
            drawHorizontal(g);
            return;
        }
    }

    /**
     * This function draw a tab
     *
     * @param g    the graphics
     * @param i    the i index
     * @param j    the j index
     * @param k    the k index
     * @param l    the l flag
     * @param flag aditional flag
     * @param s    aditional string
     */
    public void drawTab(Graphics g, int i, int j, int k, int l, boolean flag, String s) {
        g.setColor(chatclient.getColorMap()[4]);
        g.fillRect(i, j, k, l);
        g.setColor(chatclient.getColorMap()[4]);
        g.drawLine(i, j, (i + k) - 2, j);
        g.drawLine(i, j, i, (j + l) - 1);
        g.setColor(Color.gray);
        g.drawLine((i + k) - 2, j, (i + k) - 2, (j + l) - 1);
        g.setColor(chatclient.getColorMap()[0]);
        g.drawLine((i + k) - 1, j + 1, (i + k) - 1, (j + l) - 1);
        g.setColor(chatclient.getColorMap()[7]);
        if (flag) {
            g.drawString(s, i + (((ViewConstants.TAPPANEL_WIDTH / ViewConstants.TAP_COUNT) - fontmetrics.stringWidth(s)) / 2), j + 16);
            return;
        } else {
            g.drawString(s, i + (((ViewConstants.TAPPANEL_WIDTH / ViewConstants.TAP_COUNT) - fontmetrics.stringWidth(s)) / 2), j + 16);
            return;
        }
    }

    /**
     * This function set the tab index
     *
     * @param i the index of the tab
     */
    public void setTab(int i) {
        parent.curTab = i;
        String s = (String) parent.cardNames.elementAt(i);
        parent.cardLayout.show(parent.cardPanel, s);
        repaint();
    }


    /**
     * This function returns the dimension of the tab
     *
     * @return the dimension of the tab
     */
    public Dimension minimumSize() {
        return dim;
    }

    /**
     * This function updates the window
     *
     * @param g the aditional graphics
     */
    public void update(Graphics g) {
        paint(g);
    }

    /**
     * This function draw vertical line
     *
     * @param g the aditional graphics
     */
    public void drawVertical(Graphics g) {
        int i = size().height;
        g.setColor(chatclient.getColorMap()[4]);
        g.drawLine(0, 0, 0, i);
        g.fillRect(2, 0, bsize, i);
        g.setColor(chatclient.getColorMap()[4]);
        g.drawLine(1, 0, 1, i);
        g.setColor(Color.gray);
        g.drawLine(bsize + 2, 0, bsize + 2, i);
        g.setColor(chatclient.getColorMap()[4]);
        g.drawLine(bsize + 3, 0, bsize + 3, i);
    }

    /**
     * This function rises on mouse enter
     *
     * @param event the event
     * @param i     the horizontal position
     * @param j     the vertical position
     * @return true
     */
    public boolean mouseEnter(Event event, int i, int j) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        return true;
    }

    /**
     * This function rises when mouse exit
     *
     * @param event the action event
     * @param i     the hprizontal positoin
     * @param j     the vertical position
     * @return true
     */
    public boolean mouseExit(Event event, int i, int j) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        return true;
    }

    /**
     * This function rises on mouse moves down
     *
     * @param event the action event
     * @param i     the horizontal position
     * @param j     the vertica positon
     * @return true
     */
    public boolean mouseDown(Event event, int i, int j) {
        if (mode != 1)
            return true;
        for (int k = 0; k < parent.tabPos.size(); k++) {
            Rectangle rectangle = (Rectangle) parent.tabPos.elementAt(k);
            if (rectangle.inside(i, j))
                setTab(k);
        }

        return true;
    }

    /**
     * This function returns the preffered size
     *
     * @return the preffered size
     */
    public Dimension preferredSize() {
        return minimumSize();
    }

    /**
     * This function draw tabs
     *
     * @param g the aditional graphics
     */
    public void drawTabs(Graphics g) {
        int i = size().width;
        int k = size().height;
        int l = k - (bsize + 4);
        g.setColor(chatclient.getColorMap()[0]);
        g.fillRect(0, 0, i, k);
        g.setFont(parent.textFont);
        fontmetrics = g.getFontMetrics();
        fontmetrics.getHeight();
        int i1 = parent.xofs + 1;
        byte byte0 = 7;
        g.setColor(chatclient.getColorMap()[4]);
        g.fillRect(0, l, i, k);
        g.drawLine(0, l, 0, k + 1);
        g.fillRect(2, l + 1, bsize, k);
        g.fillRect(bsize + 2, l + 1, i - bsize, bsize);
        g.setColor(chatclient.getColorMap()[4]);
        g.drawLine(1, l, 1, k + 1);
        g.drawLine(1, l, i - 3, l);
        g.setColor(Color.gray);
        g.drawLine(bsize + 2, l + bsize + 2, i - (bsize + 2), l + bsize + 2);
        g.drawLine(i - 2, l, i - 2, k + 1);
        g.setColor(chatclient.getColorMap()[4]);
        g.drawLine(bsize + 3, l + bsize + 3, i - (bsize + 3), l + bsize + 3);
        g.drawLine(i - 1, l, i - 1, k + 1);
        parent.tabPos.removeAllElements();
        for (int j1 = 0; j1 < parent.tabNames.size(); j1++) {
            String s = (String) parent.tabNames.elementAt(j1);
            Rectangle rectangle1 = new Rectangle();
            int j = fontmetrics.stringWidth(s);
            rectangle1.x = i1;
            rectangle1.y = byte0 + 1;
            rectangle1.width = ViewConstants.TAPPANEL_WIDTH / ViewConstants.TAP_COUNT;
            rectangle1.height = tabHeight;
            parent.tabPos.addElement(rectangle1);
            drawTab(g, rectangle1.x, rectangle1.y, rectangle1.width, rectangle1.height, false, s);
            i1 += rectangle1.width;
        }

        Rectangle rectangle = (Rectangle) parent.tabPos.elementAt(parent.curTab);
        drawTab(g, rectangle.x, rectangle.y - 4, rectangle.width + 2, rectangle.height + 5, true, (String) parent.tabNames.elementAt(parent.curTab));
    }

    /**
     * This function draw horizontal line
     *
     * @param g the aditional graphic
     */
    public void drawHorizontal(Graphics g) {
        g.setColor(chatclient.getColorMap()[4]);
        g.fillRect(0, 0, width, height);
        g.setColor(chatclient.getColorMap()[4]);
        int i = width - 8;
        g.drawLine(0, 1, i, 1);
        g.fillRect(i, 0, 1, 1);
        g.setColor(chatclient.getColorMap()[4]);
        g.drawLine(6, 0, 6, 0);
        g.drawLine(1, 6, width - 2, 6);
        g.drawLine(width - 2, 0, width - 2, height - 2);
        g.setColor(chatclient.getColorMap()[4]);
        g.drawLine(1, 7, width - 1, 7);
        g.drawLine(width - 1, 0, width - 1, height - 1);
    }
}
