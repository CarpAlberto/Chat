package ro.mta.se.chat.views;

import ro.mta.se.chat.crypto.Certificate;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Alberto-Daniel on 11/19/2015.
 */
public class LoginView extends JFrame {

    /**
     * This is the sign-up button
     */
    private javax.swing.JButton jButton1;
    /**
     * This is the login button
     */
    private javax.swing.JButton jButton2;
    /**
     * This is title label
     */
    private javax.swing.JLabel jLabel1;
    /**
     * This is signup label
     */
    private javax.swing.JLabel jLabel2;
    /**
     * This is login label
     */
    private javax.swing.JLabel jLabel3;
    /**
     * This is username label
     */
    private javax.swing.JLabel jLabel4;
    /**
     * This is password label
     */
    private javax.swing.JLabel jLabel5;
    /**
     * This is nickname label
     */
    private javax.swing.JPanel jPanel1;
    /**
     * This is the password field
     */
    private javax.swing.JPasswordField jPasswordField1;
    /**
     * This is the Username text area
     */
    private javax.swing.JTextField jTextField1;
    /**
     * This is the nickname text area
     */
    private javax.swing.JTextField jTextField2;

    /**
     * This function initialize all the components
     */
    private void initComponents() {
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();

        jLabel2.setText("jLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(102, 0, 102));
        setPreferredSize(new java.awt.Dimension(360, 410));
        setResizable(false);
        getContentPane().setLayout(null);
        getContentPane().add(jPanel1);
        jPanel1.setBounds(519, 0, 272, 10);

        jLabel1.setBackground(new java.awt.Color(102, 153, 255));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 255));
        jLabel1.setText("Login");
        jLabel1.setToolTipText("");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(140, 30, 70, 30);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setText("Password");
        jLabel3.setToolTipText("");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(10, 150, 110, 17);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setText("Nickname");
        jLabel4.setToolTipText("");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(10, 200, 110, 17);

        jPasswordField1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jPasswordField1.setText("jPasswordField1");
        getContentPane().add(jPasswordField1);
        jPasswordField1.setBounds(130, 150, 160, 20);

        jButton1.setBackground(new java.awt.Color(51, 51, 255));
        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton1.setText("SignUp");
        jButton1.setToolTipText("");
        jButton1.setBorder(new javax.swing.border.MatteBorder(null));
        getContentPane().add(jButton1);
        jButton1.setBounds(200, 280, 80, 30);

        jButton2.setBackground(new java.awt.Color(51, 51, 255));
        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton2.setText("Login");
        jButton2.setBorder(new javax.swing.border.MatteBorder(null));
        getContentPane().add(jButton2);
        jButton2.setBounds(60, 280, 80, 30);

        jTextField1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jTextField1.setToolTipText("");
        getContentPane().add(jTextField1);
        jTextField1.setBounds(130, 200, 160, 20);

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setText("Username");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(10, 110, 110, 17);

        jTextField2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jTextField2.setToolTipText("");
        getContentPane().add(jTextField2);
        jTextField2.setBounds(130, 110, 160, 20);

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * This is the constructor of the application
     */
    public LoginView() {
        super("Login Autentification");
        this.setVisible(true);
        initComponents();
    }

    /**
     * This function gets the username value
     *
     * @return the username value
     */
    public String getUsername() {
        return this.jTextField2.getText();
    }

    /**
     * This function gets the password value
     *
     * @return the password value
     */
    public String getPassword() {
        return this.jPasswordField1.getText();
    }

    /**
     * This function gets the nickname value
     *
     * @return the nickname value
     */
    public String getNickname() {
        return this.jTextField1.getText();
    }

    /**
     * This function display a message
     *
     * @param message the message to be displayed
     * @param error   if true then message is an error
     */
    public void displayMessage(String message, boolean error) {
        if (!error)
            JOptionPane.showMessageDialog(this, message, "Message", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This function add a login listener
     *
     * @param listener the login listener
     */
    public void addloginListener(ActionListener listener) {
        this.jButton2.addActionListener(listener);
    }

    /**
     * This function add a signup listener
     *
     * @param listener the listener to be added
     */
    public void addSignupListenr(ActionListener listener) {
        this.jButton1.addActionListener(listener);
    }


    /**
     * This function  clear all the view
     */
    public void clearAll() {
        this.jTextField1.setText("");
        this.jPasswordField1.setText("");
    }

    /**
     * This function display the window
     */
    public void displayWindow() {
        this.setVisible(true);
    }

    public void hideWindow() {
        this.setVisible(false);
    }
}
