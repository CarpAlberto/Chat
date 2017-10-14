package ro.mta.se.chat.models;

import ro.mta.se.chat.crypto.AESCipher;
import ro.mta.se.chat.factory.PacketFactory;
import ro.mta.se.chat.factory.abstracts.AbstractMessageTextModel;
import ro.mta.se.chat.views.ChatClientView;

import javax.crypto.BadPaddingException;
import java.nio.ByteBuffer;

/**
 * Created by Alberto-Daniel on 12/8/2015.
 * This class is repomsable for storing the messMessage data
 */
public class MessMessageTextModel extends AbstractMessageTextModel {
    /**
     * The associated room with the meww
     */
    String room;

    /**
     * The constructor of th messMessage model
     *
     * @param content the content of the message
     * @param from    the source user
     * @param room    the room of the message
     * @throws NullPointerException if @param room is null
     */
    public MessMessageTextModel(byte[] content, UserModel from, String room) {
        super(content, from);
        if(room == null)
            throw new NullPointerException();
        this.room = room;
    }

    /**
     * This function returns the room asociated
     *
     * @return the room of the message
     */
    @Override
    public String getRoom() {
        return room;
    }

    /**
     * This function updates the view on receiving new mess
     *
     * @param theView to be updated on receiving a new message
     * @throws NullPointerException if @param theView is null
     */
    @Override
    public void updateViewOnReceive(ChatClientView theView) {
        if(theView == null)
            throw new NullPointerException();
        theView.onNewMess(this.getUser().getNickname(), this.toString());
    }

    /**
     * This function returns a mess packet
     *
     * @return a mess packet
     */
    @Override
    public ByteBuffer getPacket() {
        return PacketFactory.getMessPacket(this.getContent());
    }

    /**
     * The mess communication is not encrypted
     */
    @Override
    public void setEncryptMode() {

    }

    /**
     * The mess communication is not decrypted
     */
    @Override
    public void setDecryptMode() {

    }
}
