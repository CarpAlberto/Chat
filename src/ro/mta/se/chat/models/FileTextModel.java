package ro.mta.se.chat.models;

import ro.mta.se.chat.factory.abstracts.AbstractMessage;
import ro.mta.se.chat.views.ChatClientView;

import java.nio.ByteBuffer;

/**
 * Created by Alberto-Daniel on 11/17/2015.
 * This class represents the FileTextModel
 */
public class FileTextModel extends AbstractMessage {
    /**
     * The public constructor
     *
     * @param content the content of file
     * @param from    the destination user
     */
    public FileTextModel(byte[] content, UserModel from) {
        super(content, from);
    }

    /**
     * Override the toString default implementation
     *
     * @return null
     */
    @Override
    public String toString() {
        return null;
    }

    /**
     * Encryption process is realized during transfer
     */
    @Override
    public void setEncryptMode() {
        setEncrypted();
    }

    /**
     * This is the decrypt function
     */
    @Override
    public void setDecryptMode() {
        setDecripted();
    }

    /**
     * This is the view that must mu updated on receiving a new file
     *
     * @param theView to be updated on receiving a new message
     */
    @Override
    public void updateViewOnReceive(ChatClientView theView) {
    }

    /**
     * This function returns a packet containing an FileTextModel packet
     *
     * @return the FileTextModel packet
     */
    @Override
    public ByteBuffer getPacket() {
        return null;
    }

}
