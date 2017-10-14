package ro.mta.se.chat.factory.abstracts;

import ro.mta.se.chat.factory.abstracts.AbstractMessage;
import ro.mta.se.chat.models.UserModel;

import javax.jws.soap.SOAPBinding;

/**
 * Created by Alberto-Daniel on 11/10/2015.
 * This function is an abstract class for all text messages
 */
public abstract class AbstractMessageTextModel extends AbstractMessage {

    /**
     * The protected constructor
     *
     * @param content the content of the message
     * @param from    the destination user
     */
    protected AbstractMessageTextModel(byte[] content, UserModel from) {
        super(content, from);
    }

    /**
     * This function returns the room/groop of message
     *
     * @return the string  format from the room
     */
    public abstract String getRoom();

    /**
     * This function returns the String format of data
     *
     * @return the string format form message
     */
    @Override
    public String toString() {
        return new String(content);
    }

}
