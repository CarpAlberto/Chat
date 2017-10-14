package ro.mta.se.chat.models;

import ro.mta.se.chat.crypto.AESCipher;
import ro.mta.se.chat.factory.PacketFactory;
import ro.mta.se.chat.factory.abstracts.AbstractMessageTextModel;
import ro.mta.se.chat.views.ChatClientView;

import java.nio.ByteBuffer;

/**
 * Created by Alberto-Daniel on 12/8/2015.
 * This class is responsable for the private message
 */
public class PrivateTextMessageModel extends AbstractMessageTextModel {

    /**
     * The constructor for private text message
     *
     * @param content the content of the message
     * @param from    the sender of the message
     */
    public PrivateTextMessageModel(byte[] content, UserModel from)
    {
        super(content, from);
    }

    /**
     * The encrypt function of the class
     */
    @Override
    public void setEncryptMode() {
        setEncrypted();
    }

    /**
     * The decrypt function of the class
     */
    @Override
    public void setDecryptMode() {
        setDecripted();
    }

    /**
     * This function updates the given view on receiving new message
     *
     * @param theView to be updated on receiving a new message
     * @throws NullPointerException if @param theView is null
     */
    @Override
    public void updateViewOnReceive(ChatClientView theView) {
        if(theView == null)
            throw new NullPointerException();
        String msg = new String(this.getContent());
        theView.onReceiveNewMessage(this.getUser().getNickname(), this.getUser().getKey(), msg);
    }

    /**
     * This function get a packet from object class
     *
     * @return
     */
    @Override
    public ByteBuffer getPacket() {
        return PacketFactory.getMessagePacket(this.getContent());
    }

    /**
     * This function returns null
     *
     * @return null value
     */
    @Override
    public String getRoom() {
        return null;
    }
}
