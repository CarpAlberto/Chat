package ro.mta.se.chat.views;

import ro.mta.se.chat.controllers.interfaces.ConnectionListeners;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created by Alberto-Daniel on 11/26/2015.
 * This is the class that is responsable for connecting to
 * other host
 */
public class ConnectView extends JFrame {
    /**
     * This is the button_ok
     */
    private javax.swing.JButton btn_ok;
    /**
     * This is the label title
     */
    private javax.swing.JLabel jLabel1;
    /**
     * This is the ip-label
     */
    private javax.swing.JLabel jLabel2;
    /**
     * This is the port label
     */
    private javax.swing.JLabel jLabel3;
    /**
     * This is the txt_ip Text Field
     */
    private javax.swing.JTextField txt_ip;
    /**
     * This is the tct-port TextField
     */
    private javax.swing.JTextField txt_port;
    /**
     * This is the main chat of applicaton
     */
    ChatClientView theClient;

    /**
     * This function initialize all the components of the view
     */
    void intializeComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_port = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btn_ok = new javax.swing.JButton();
        txt_ip = new javax.swing.JTextField();

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setTitle("Connect");
        setBackground(new java.awt.Color(0, 51, 204));
        setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        setPreferredSize(new java.awt.Dimension(400, 300));
        setResizable(false);
        setSize(new java.awt.Dimension(500, 500));
        setType(java.awt.Window.Type.POPUP);
        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel1.setText("Port");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(50, 150, 60, 30);

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel2.setText("IP");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(50, 90, 70, 30);

        txt_port.setName("txt_port"); // NOI18N
        getContentPane().add(txt_port);
        txt_port.setBounds(170, 150, 220, 30);

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel3.setText("Connect to partener");
        jLabel3.setToolTipText("");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(150, 20, 131, 34);

        btn_ok.setBackground(new java.awt.Color(0, 0, 204));
        btn_ok.setText("Login");
        btn_ok.setName("btn_login"); // NOI18N

        getContentPane().add(btn_ok);
        btn_ok.setBounds(170, 240, 91, 23);

        txt_ip.setName("txt_ip"); // NOI18N
        getContentPane().add(txt_ip);
        txt_ip.setBounds(170, 90, 220, 30);
        setLocationRelativeTo(null);
        pack();
    }

    /**
     * This is constructor off aplication
     *
     * @param theClient the main view of aplication
     */
    public ConnectView(ChatClientView theClient) {
        intializeComponents();
        this.theClient = theClient;

    }

    /**
     * This function display the window
     */
    public void showWindow() {
        this.setVisible(true);
    }

    /**
     * This function hide the window
     */
    public void hideWindow() {
        this.setVisible(false);
    }

    /**
     * This function add button listener to the view
     *
     * @param listener the listener
     */
    public void addBtnConnectListener(ActionListener listener) {
        this.btn_ok.addActionListener(listener);
    }

    /**
     * This function gets the ip value
     *
     * @return the ip value
     */
    public String getIp() {
        return this.txt_ip.getText();
    }

    /**
     * This function gets the port value
     *
     * @return the port value
     */
    public String port() {
        return this.txt_port.getText();
    }

}

