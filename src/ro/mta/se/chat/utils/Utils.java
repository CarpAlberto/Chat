package ro.mta.se.chat.utils;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Alberto-Daniel on 11/24/2015.
 * This is an utility class that contanis some static methods which are used as
 * utility stuff in application
 */
public class Utils {

    /**
     * This method returns the absolute path to user's home folder.
     *
     * @return Absolute path to home folder of current user
     */
    public static String getLocalUserDirectory() {

        return System.getenv("USERPROFILE");
    }


    /**
     * This method returns the absolute path for certificate.
     *
     * @return Absolute path to certificate.
     */
    public static String getCertificatePath() {
        return getLocalUserDirectory() + "\\SecureChat.cer";

    }

    /**
     * This method returns the absolute path to private key.
     *
     * @return Absolute path to private key.
     */
    public static String getPrivateKeyPath() {
        return getLocalUserDirectory() + "\\securechat_conf.jks";
    }

    /**
     * This function returns the StackTrace from Throwable object
     *
     * @param e Throwable object
     * @return String from staackTrace
     */
    public static String getStackTrace(Throwable e) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(baos));
            e.printStackTrace(pw);
            pw.close();
            return baos.toString();
        } catch (Throwable ignore) {
            return "";
        }
    }

    /**
     * This function returns the ByteBuffer from long value
     *
     * @param bufferSize input long
     * @return ByteBuffer object from input
     */
    public static ByteBuffer getByteBuffer(long bufferSize) {
        ByteBuffer data = ByteBuffer.allocate(8);
        data.order(ByteOrder.LITTLE_ENDIAN);
        data.putLong(bufferSize);
        return ByteBuffer.allocate(data.getInt(0)).order(
                ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * This functiom finds the object of type T in Component comp
     *
     * @param comp  container to find
     * @param clazz the object
     * @param <T>   the type of object
     * @return the object
     */
    public static <T extends Container> T findParent(Component comp, Class<T> clazz) {
        if (comp == null)
            return null;
        if (clazz.isInstance(comp))
            return (clazz.cast(comp));
        else
            return findParent(comp.getParent(), clazz);
    }
}
