package ro.mta.se.chat.factory;

import ro.mta.se.chat.factory.abstracts.AbstractMessage;
import ro.mta.se.chat.models.AudioData;
import ro.mta.se.chat.models.MessMessageTextModel;
import ro.mta.se.chat.models.*;
import ro.mta.se.chat.utils.logging.Logger;

/**
 * Created by Alberto-Daniel on 11/17/2015.
 * This class is responsable for generating new Abstract messages
 */
public class MessageFactory {
    /**
     * This is the SIMPLE_PRIVATE_TEXT_MESSAGE flag
     */
    public static final int SIMPLE_PRIVATE_TEXT_MESSAGE = 1;
    /**
     * This is the SIMPLE_MESS_TEXT_MESSAGE flag
     */
    public static final int SIMPLE_MESS_TEXT_MESSAGE = 3;
    /**
     * This is the AUDIO_DATA_MESSAGE flag
     */
    public static final int AUDIO_DATA_MESSAGE = 2;
    /**
     * This is the FILE_DATA_MESSAGE flag
     */
    public static final int FILE_DATA_MESSAGE = 4;

    /**
     * This function returns a new abstract message
     *
     * @param type    the type of message
     * @param from    the sender of the message
     * @param content the content of the message
     * @param room    the room destination of the message (%param  null if it is a private message)
     * @return the abstract message
     */
    public static AbstractMessage getMessage(int type, UserModel from, byte[] content, String room) {
        switch (type) {
            case SIMPLE_PRIVATE_TEXT_MESSAGE:
                return new PrivateTextMessageModel(content, from);
            case SIMPLE_MESS_TEXT_MESSAGE:
                return new MessMessageTextModel(content, from, room);
            case AUDIO_DATA_MESSAGE:
                return new AudioData(content, from);
            case FILE_DATA_MESSAGE:
                return new FileTextModel(content, from);
            default:
                Logger.error("Message type not suported by this version");
        }
        return null;
    }
}
