package ro.mta.se.chat.exceptions;

/**
 * Created by Alberto-Daniel on 10/29/2015.
 * This class extends Exception class
 * It handles problems caused by violating acces
 */
public class ViolationAccesException extends Exception implements java.io.Serializable {
    /**
     * Ths constructor that format the output message
     *
     * @param message
     */
    public ViolationAccesException(String message) {
        super("Database Exception : " + message + "\n");
    }

    /**
     * Ths constructor that format the output message
     *
     * @param message The message error
     * @param cause   The twrowable cause
     */
    public ViolationAccesException(String message, Throwable cause) {
        super("Database Exception : " + message + "\n", cause);
    }

    /**
     * Ths constructor that format the output message
     *
     * @param cause The twrowable cause
     */
    public ViolationAccesException(Throwable cause) {
        super(cause);
    }

}
