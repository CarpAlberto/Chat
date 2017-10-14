package ro.mta.se.chat.views;

import java.awt.Button;

/**
 * Created by Alberto-Daniel on 11/23/2015.
 * This class is an extension of standard button
 */
class CustomButton extends Button {

    /**
     * The parent of the button
     */
    ChatClientView chatclient;

    /**
     * Ths constructor of the button
     *
     * @param parent the parent
     * @param label  the text of the button
     */
    public CustomButton(ChatClientView parent, String label) {
        chatclient = parent;
        setLabel(label);
        setBackground(chatclient.getColorMap()[3]);
        setForeground(chatclient.getColorMap()[2]);
    }
}