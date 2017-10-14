package ro.mta.se.chat.factory.abstracts;

import ro.mta.se.chat.factory.KeyFactory;
import ro.mta.se.chat.models.UserModel;
import ro.mta.se.chat.views.ChatClientView;

import java.nio.ByteBuffer;

/**
 * Created by Alberto-Daniel on 11/17/2015.
 * This class is an abstract class over all Messages
 */
public abstract class AbstractMessage {

    /**
     * Timestamp when the message arrived
     */
    protected long timestamp;

    /**
     * The content of the message.
     */
    protected byte[] content;

    /**
     * The aes key with which the content is encrypted
     */
    protected AbstractKey aesKey;

    /**
     * The sender of the message
     */
    protected UserModel sender;

    /**
     * Protected constructor of the AbstractMessage
     *
     * @param content  the content of the message
     * @param receiver the sender
     */
    protected AbstractMessage(byte[] content, UserModel receiver) {
        this.content = content;
        this.sender = receiver;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Encrypt the content of the message
     */
    protected void setEncrypted() {
        content = this.aesKey.encrypt(this.content);
    }

    /**
     * Decrypt the content of the message
     */
    protected void setDecripted() {
        content = this.aesKey.decrypt(this.content);
    }

    /**
     * Return the sender of the message
     *
     * @return the sender
     */
    public UserModel getUser() {
        return sender;
    }

    /**
     * This function return the content of the content
     *
     * @return the content of the message
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * This function set the user of the message
     *
     * @param user The sender of the message
     */
    public void setUser(UserModel user) {
        this.sender = user;
    }

    /**
     * This function set the aes key of the message
     *
     * @param key the key to be set
     */
    public void setAesKey(byte[] key) {
        this.aesKey = KeyFactory.getKey(KeyFactory.SYMETRIC_KEY_WITH_DATA, key);
    }

    /**
     * Returns the string conversion of the message
     *
     * @return string conversion of the message
     */
    public abstract String toString();

    /**
     * This function set the encrypted mode  of message
     */
    public abstract void setEncryptMode();

    /**
     * This function set decrypted mode of message
     */
    public abstract void setDecryptMode();

    /**
     * This function updates the  ( @param theView ) on receiving new message
     *
     * @param theView to be updated on receiving a new message
     */
    public abstract void updateViewOnReceive(ChatClientView theView);

    /**
     * This function returns a Packet representing the type of message
     *
     * @return the pachet to be send
     */
    public abstract ByteBuffer getPacket();
}
