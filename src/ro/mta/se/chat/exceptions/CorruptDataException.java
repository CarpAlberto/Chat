package ro.mta.se.chat.exceptions;

/**
 * Created by Alberto-Daniel on 10/29/2015.
 * This is an costum exception class which handles the corrrupts packets
 * during the chat protocol implements java.io.Serializabla
 */
public class CorruptDataException extends Exception implements java.io.Serializable {
    /**
     * The constructor which gets the message error and format it accordingly
     *
     * @param message The message to be logged
     */
    public CorruptDataException(String message) {
        super(message);
    }

    /**
     * The constructor which gets the message error and format it accordingly
     *
     * @param message The message to be logged
     * @param cause   The cause which generated the error
     */
    public CorruptDataException(String message, Throwable cause) {
        super(message);
    }

    /**
     * The constructor which gets the message error and format it accordingly
     *
     * @param cause The cause which generated the error
     */
    public CorruptDataException(Throwable cause) {
        super(cause);
    }
}
