package ro.mta.se.chat.views;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created by Alberto-Daniel on 12/22/2015.
 */
public class ChangeNickNameView extends JFrame {
    /**
     * The nickname button
     */
    private javax.swing.JButton jButton1;
    /**
     * The change of nickname label
     */
    private javax.swing.JLabel jLabel1;
    /**
     * The Type of nickname value
     */
    private javax.swing.JLabel jLabel2;

    /**
     * The nickname changed area
     */
    private javax.swing.JTextField jTextField1;

    /**
     * The constructor of the class
     */
    public ChangeNickNameView() {
        init();
    }

    /**
     * This function display the window
     */
    public void showWindow() {
        this.setVisible(true);
    }

    /**
     * This funcrtion add a listener to the window
     *
     * @param listener the listener to be added
     */
    public void addListeners(ActionListener listener) {
        this.jButton1.addActionListener(listener);
    }

    /**
     * This function returns the nickname changed value
     *
     * @return the newNickname value
     */
    public String getNewNick() {
        return this.jTextField1.getText();
    }

    /**
     * This function init the graphical area
     */
    private void init() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(430, 228));
        setSize(new java.awt.Dimension(400, 168));
        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel1.setText("Change Nickname");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(130, 20, 130, 30);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 12));
        jLabel2.setText("Type you nickanme");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(50, 80, 110, 30);

        jTextField1.setToolTipText("");
        getContentPane().add(jTextField1);
        jTextField1.setBounds(180, 80, 200, 20);

        jButton1.setText("Update");
        getContentPane().add(jButton1);
        jButton1.setBounds(150, 130, 90, 23);

        pack();
    }
}
