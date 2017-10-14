package ro.mta.se.chat.utils.logging;

import ro.mta.se.chat.utils.Constants;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Alberto-Daniel on 10/19/2015.
 * Clasa principala responsabila cu logarea erorilor
 */
public class Logger {
    /**
     * Constructorul default setat private pentru ca Logger-ul sa nu fie instantiat
     */
    private Logger() {

    }

    /**
     * Functie raw ,care scrie un logRecord in loggerul curent
     *
     * @param record - Inregistrarea care urmeaza sa fie publicata
     */

    private synchronized static void Log(LogRecord record) {
        if (!record.isValid())
            return;
        if (Constants.LOG_TO_FILE == true) {
            try {
                FileWriter writer = new FileWriter(Constants.LOG_FILE_NAME, true);
                writer.write(record.toString());
                writer.flush();
                writer.close();
            } catch (IOException exc) {

            }
        } else {
            System.out.println(record.toString());
        }

    }


    /**
     * Functie care scrie un warning in handlerele logerului curent
     * ClasaSursa = null
     * MetodaSursa = null
     *
     * @param message
     */
    public synchronized static void warn(String message) {
        warn(message, null, null);
    }

    /**
     * Functie care scrie un warning in handlerele logerului curent
     * MetodaSursa = null
     *
     * @param className Clasa sursa care a lansat eroarea
     * @param message
     */
    public synchronized static void warn(String message, String className) {
        warn(message, className, null);
    }

    /**
     * Functie care scrie un warning in handlerele logerului curent
     *
     * @param message
     */
    public synchronized static void warn(String message, String className, String methodName) {
        LogRecord record = new LogRecord(Level.warning(), message, className, methodName);
        Log(record);
    }

    /**
     * @param message   Mesajul ce trebuie scris
     * @param className Clasa sursa care a lansat eroarea
     */
    public synchronized static void error(String message, String className) {
        error(message, className);
    }

    /**
     * @param message    Mesajul ce trebuie scris
     * @param className  Sursa clasa care a lansat exceptia
     * @param methodName Sursa metoda care a lansat exceptia
     */
    public synchronized static void error(String message, String className, String methodName) {
        LogRecord record = new LogRecord(Level.error(), message, className, methodName);
        Log(record);
    }

    /**
     * @param message Mesajul ce trebuie scris
     */
    public synchronized static void error(String message) {
        error(message, null, null);
    }

    /**
     * @param message    Mesajul ce trebuie scris
     * @param className  Sursa clasa care a lansat exceptia
     * @param methodName Sursa metoda care a lansat exceptia
     */
    public synchronized static void debug(String message, String className, String methodName) {
        LogRecord record = new LogRecord(Level.debug(), message, className, methodName);
        Log(record);
    }

    /**
     * @param message Mesajul ce trebuie scris
     */
    public synchronized static void debug(String message) {
        debug(message, null, null);
    }

    /**
     * @param message   Mesajul ce trebuie scris
     * @param className Sursa clasa care a lansat exceptia
     */
    public synchronized static void debug(String message, String className) {
        debug(message, className);
    }

    /**
     * @param message    Mesajul ce trebuie scris
     * @param className  Sursa clasa care a lansat exceptia
     * @param methodName Sursa metoda care a lansat exceptia
     */
    public synchronized static void info(String message, String className, String methodName) {
        LogRecord record = new LogRecord(Level.info(), message, className, methodName);
        Log(record);
    }

    /**
     * @param message Mesajul ce trebuie scris
     */
    public synchronized static void info(String message) {
        info(message, null, null);
    }

    /**
     * @param message   Mesajul ce trebuie scris
     * @param className Sursa clasa care a lansat exceptia
     */
    public synchronized static void info(String message, String className) {
        info(message, className);
    }

    /**
     * @param message    Mesajul ce trebuie scris
     * @param className  Sursa clasa care a lansat exceptia
     * @param methodName Sursa metoda care a lansat exceptia
     */
    public synchronized static void verbose(String message, String className, String methodName) {
        LogRecord record = new LogRecord(Level.verbose(), message, className, methodName);
        Log(record);
    }

    /**
     * @param message Mesajul ce trebuie scris
     */
    public synchronized static void verbose(String message) {
        verbose(message, null, null);
    }

    /**
     * @param message   Mesajul ce trebuie scris
     * @param className Sursa clasa care a lansat exceptia
     */
    public synchronized static void verbose(String message, String className) {
        verbose(message, className);
    }

}
