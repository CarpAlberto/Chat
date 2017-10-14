package ro.mta.se.chat.exceptions;

/**
 * Created by Alberto-Daniel on 10/29/2015.
 * This class is responsable for situation when the end of stream packet has come to end
 */
public class EndOfStreamException extends Exception implements java.io.Serializable {
    /**
     * The default constructor
     */
    public EndOfStreamException() {
        super("");
    }

    /**
     * The constructor with one parameter
     *
     * @param message The message of the error
     */
    public EndOfStreamException(String message) {

        super("End of stream :  : " + message + "\n");
    }

    /**
     * The constructor with two parameters
     *
     * @param message The message of the error
     * @param cause   the cause who generated the error
     */
    public EndOfStreamException(String message, Throwable cause) {
        super("End of stream :" + message, cause);
    }

    /**
     * The constructor which takes just the cause as parameter
     *
     * @param cause The cause which generated the exceptiom
     *              Throwable necesary when a nested exception has happened
     */
    public EndOfStreamException(Throwable cause) {
        super(cause);
    }
}
