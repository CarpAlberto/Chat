package ro.mta.se.chat.models;

import ro.mta.se.chat.crypto.AESCipher;
import ro.mta.se.chat.factory.KeyFactory;
import ro.mta.se.chat.factory.abstracts.AbstractKey;

import javax.crypto.BadPaddingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

/**
 * Created by Alberto-Daniel on 12/1/2015.
 * This is the history model of the application
 */
public class HistoryModel {
    /**
     * The destination user of the history
     */
    UserModel target;
    /**
     * The timestamp start of the history
     */
    long timestampStart;
    /**
     * The timestamp end of the application
     */
    long timestampEnd;
    /**
     * The aes key of the application
     */
    AbstractKey aesKey;
    /**
     * The encrypted messages of the application
     */
    LinkedList<byte[]> encriptedMsg = new LinkedList<>();
    /**
     * The booleans list
     */
    LinkedList<Boolean> fromMe = new LinkedList<Boolean>();
    /**
     * true if the sesion has sterted
     */
    boolean startStation;

    /**
     * This is the constructor of the history model
     *
     * @param source the connected user
     * @throws NullPointerException if @param source is null
     */
    public HistoryModel(UserModel source) {
        if (source == null)
            throw new NullPointerException();
        this.target = source;
        this.timestampStart = System.currentTimeMillis();
    }

    /**
     * This function starts the history moodel with the key aes
     *
     * @param key the key aes
     * @throws NullPointerException if @param key is null
     */
    public void start(byte[] key) {
        if (key == null)
            throw new NullPointerException();
        this.aesKey = KeyFactory.getKey(KeyFactory.SYMETRIC_KEY_WITH_DATA, key);
        startStation = true;
    }

    /**
     * This function sets the user model to the asociated history
     *
     * @param model the connected user
     * @throws NullPointerException if @param model is null
     */
    public void setUser(UserModel model) {
        if (model == null)
            throw new NullPointerException();
        this.target = model;
    }

    /**
     * This function stops the session of the history model
     */
    public void stopSession() {
        this.timestampEnd = System.currentTimeMillis();
    }

    /**
     * This function returns the key of the history
     *
     * @return the key of the history
     */
    public String getKey() {
        String buff = Long.toString(timestampStart) + ":" + target.getKey();
        return buff;
    }

    /**
     * This function returns the user
     *
     * @return the user
     */
    public UserModel getUser() {
        return target;
    }

    /**
     * This function returns the aes key
     *
     * @return the aes key
     */
    public byte[] getAesKey() {
        return aesKey.getKey();
    }

    /**
     * This function returns the timestamp start
     *
     * @return the timestamp start
     */
    public long getTimestampStart() {
        return timestampStart;
    }

    /**
     * This function add a new Message
     *
     * @param encData the encrypted message
     * @param fromMe  true if fromMe is true
     * @throws NullPointerException if encData is null
     */
    public void addNewMsg(byte[] encData, boolean fromMe) {
        if (encData == null)
            throw new NullPointerException();
        this.encriptedMsg.add(encData);
        this.fromMe.add(fromMe);
    }

    /**
     * This function close the session of history
     */
    public void close() {
        timestampEnd = System.currentTimeMillis();
    }

    /**
     * This function returns the xml filename fro history
     *
     * @return
     */
    public String getXMLFilename() {
        return String.valueOf(this.timestampStart) + "_" + String.valueOf(this.timestampEnd) + ".xml";
    }

    /**
     * This function returns the message of given index
     *
     * @param index the given inbdex
     * @return the message
     * @throw IndexOutOfBoundsException if index is less than 0
     */
    public byte[] getMsg(int index) {
        if (index < 0)
            throw new IndexOutOfBoundsException();
        return encriptedMsg.get(index);
    }

    /**
     * This function returns the type of the message
     *
     * @param index the index of message
     * @return true if fromMe is  true
     */
    public boolean getType(int index) {
        return fromMe.get(index);
    }

    /**
     * This function returns the count of all messages
     *
     * @return the count of all messages
     */
    public int size() {
        return encriptedMsg.size();
    }

    /**
     * This function sets t he aes key
     *
     * @param key the key value
     * @throws NullPointerException if @param key is null
     */
    public void setAesKey(byte[] key) {

        if (key == null)
            throw new NullPointerException();
        this.aesKey = KeyFactory.getKey(KeyFactory.SYMETRIC_KEY_WITH_DATA, key);
    }

    /**
     * Thisfunction set the timestamp start for the history
     *
     * @param tStart the timestamp value
     */
    public void setTimestampStart(long tStart) {
        this.timestampStart = tStart;
    }

    /**
     * This function set the timestamp end value
     *
     * @param tEnd the end of the timestamp
     */
    public void setTimestampEnd(long tEnd) {
        this.timestampEnd = tEnd;
    }

    /**
     * This function get timestamp as string value
     *
     * @return the string value from timestamp
     */
    public String getStart() {
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date(this.timestampStart));
    }

    /**
     * This function get timestamp end as string value
     *
     * @return the string value from timestam end
     */
    public String getEnd() {
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date(this.timestampEnd));
    }

    /**
     * This is the string fromatted from the history view
     *
     * @return the string format from history view
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Messages :\n");
        for (int i = 0; i < encriptedMsg.size(); i++) {
            byte[] decoded = aesKey.decrypt(encriptedMsg.get(i));
            if (fromMe.get(i) == true) {
                builder.append("\tSend : " + new String(decoded) + " \n ");
            } else {
                builder.append("\tReceive : " + new String(decoded) + " \n ");
            }
        }
        builder.append("\n\n");
        return builder.toString();
    }

    /**
     * This function return the string representation of a message of given index
     *
     * @param index the given index
     * @return the string representation
     */
    public String toString(int index) {
        if (index < 0)
            throw new IndexOutOfBoundsException();
        byte[] decoded = aesKey.decrypt(encriptedMsg.get(index));
        if (fromMe.get(index) == true) {
            return "\tSend : " + new String(decoded) + " \n ";
        } else {
            return "\tReceive : " + new String(decoded) + " \n ";
        }
    }


    /**
     * This function gets timestart-timeend from history
     *
     * @return the time formar
     */
    public String getTime() {
        StringBuilder builder = new StringBuilder();
        builder.append("Start : " + getStart() + " - ");
        builder.append("End : " + getEnd() + "\n");
        return builder.toString();
    }
}
