package ro.mta.se.chat.views;

import java.awt.*;
import java.util.Vector;

/**
 * Created by Alberto-Daniel on 11/23/2015.
 * This is a class that represents a border pannel
 */
public class BorderPanel extends Panel {
    /**
     * The name of all tabs
     */
    public Vector tabNames;
    /**
     * The position of all tabs
     */
    public Vector tabPos;
    /**
     * The cardNames of the tabe
     */
    public Vector cardNames;
    /**
     * The cardPanels
     */
    public Panel cardPanel;
    /**
     * The layput
     */
    public CardLayout cardLayout;
    /**
     * The intermal offset
     */
    public int xofs;
    /**
     * The internal font
     */
    public Font textFont;
    /**
     * The current tab index
     */
    public int curTab;
    /**
     * The border tab
     */
    Border tabs;
    /**
     * The tapPannel parent
     */
    TapPanel cframe;
    /**
     * The main view of chat
     */
    ChatClientView chatclient;

    /**
     * The constructor of aclass
     *
     * @param tappanel   the parent class
     * @param app        the main view of app
     * @param cardlayout the layout
     * @param panel      the aditional pannel
     * @param i          the i index
     * @param j          the j index
     */
    public BorderPanel(TapPanel tappanel, ChatClientView app, CardLayout cardlayout, Panel panel, int i, int j) {
        xofs = 0;
        curTab = 0;
        cframe = tappanel;
        cardLayout = cardlayout;
        cardPanel = panel;
        tabNames = new Vector(10, 5);
        tabPos = new Vector(10, 5);
        cardNames = new Vector(10, 5);
        textFont = new Font("Helvetica", 1, 11);
        setBackground(Color.white);
        GridBagLayout gridbaglayout = new GridBagLayout();
        GridBagConstraints gridbagconstraints = new GridBagConstraints();
        setLayout(gridbaglayout);
        gridbagconstraints.weightx = 1.0D;
        gridbagconstraints.fill = 1;
        gridbagconstraints.gridwidth = 0;
        chatclient = app;
        tabs = new Border(cframe, chatclient, this, 1, i);
        gridbaglayout.setConstraints(tabs, gridbagconstraints);
        add(tabs);
        gridbagconstraints.weightx = 0.0D;
        gridbagconstraints.weighty = 1.0D;
        gridbagconstraints.gridwidth = 1;
        Border border = new Border(cframe, chatclient, this, 2, j);
        gridbaglayout.setConstraints(border, gridbagconstraints);
        add(border);
        Panel panel1 = new Panel();
        GridBagLayout gridbaglayout1 = new GridBagLayout();
        panel1.setLayout(gridbaglayout1);
        gridbagconstraints.weightx = 1.0D;
        gridbagconstraints.gridwidth = 0;
        gridbaglayout1.setConstraints(panel, gridbagconstraints);
        panel1.add(panel);
        gridbagconstraints.gridwidth = -1;
        gridbaglayout.setConstraints(panel1, gridbagconstraints);
        add(panel1);
        gridbagconstraints.weightx = 0.0D;
        gridbagconstraints.gridwidth = 0;
        Border border1 = new Border(cframe, chatclient, this, 3, j);
        gridbaglayout.setConstraints(border1, gridbagconstraints);
        add(border1);
        gridbagconstraints.weightx = 1.0D;
        gridbagconstraints.weighty = 0.0D;
        Border border2 = new Border(cframe, chatclient, this, 4, i);
        gridbaglayout.setConstraints(border2, gridbagconstraints);
        add(border2);
        validate();
    }

    /**
     * This function add a new tab to the collection
     *
     * @param s  the name of the tab
     * @param s1 the card name of the tab
     * @return
     */
    public int addTab(String s, String s1) {
        tabNames.addElement(s);
        cardNames.addElement(s1);
        return tabNames.size() - 1;
    }
}
