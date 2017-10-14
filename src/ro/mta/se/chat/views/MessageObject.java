package ro.mta.se.chat.views;

/**
 * Created by Alberto-Daniel on 15/17/2015.
 * This class represents the message object
 */
public class MessageObject {
    /**
     * The text message
     */
    private String message;
    /**
     * The horizontal start of message
     */
    private int startX;
    /**
     * The vertical start of message
     */
    private int startY;
    /**
     * This is the width of the message
     */
    private int width;
    /**
     * This is the height of the image
     */
    private int height;
    /**
     * true if the message is an image
     */
    private boolean isImage;
    /**
     * true if the message is selected
     */
    private boolean isSelected;
    /**
     * true if the message is ignored
     */
    private boolean isIgnored;
    /**
     * this is the type of the message
     */
    private int messageType;
    /**
     * true if the message is an on message
     */
    private boolean onFlag;
    /**
     * This is the key of the image
     */
    private String key;

    /**
     * This is the constructor of the messageObject
     */
    public MessageObject() {
        width = 0;
        height = 0;
        startX = 0;
        startY = 0;
        message = null;
        isImage = false;
        isSelected = false;
        isIgnored = false;
    }

    /**
     * This function gets the message
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * This function sets the message
     *
     * @param message the message to be seted
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * This function gets the StartX
     *
     * @return the startX value
     */
    public int getStartX() {
        return startX;
    }

    /**
     * This function gets the StartY
     *
     * @return the StartY value
     */
    public int getStartY() {
        return startY;
    }

    /**
     * This function setStartX value
     *
     * @param value the startX value
     */
    public void setStartX(int value) {
        this.startX = value;
    }

    /**
     * This function setStartY value
     *
     * @param value the SetStartY value
     */
    public void setStartY(int value) {
        this.startY = value;
    }

    /**
     * This function gets the width
     *
     * @return the width value
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * This function get the height value
     *
     * @return the height value
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * This function sets the Width value
     *
     * @param value the width value
     */
    public void setWidth(int value) {
        this.width = value;
    }

    /**
     * This function set the height value
     *
     * @param value the height value
     */
    public void setHeight(int value) {
        this.height = value;
    }

    /**
     * This function sets the Message Type
     *
     * @param type the type of the message
     */
    public void setMessageType(int type) {
        this.messageType = type;
    }

    /**
     * This function gets the message Type
     *
     * @return the message type
     */
    public int getMessageType() {
        return this.messageType;
    }

    /**
     * This function sets the message image type
     *
     * @param value true if image false otherwise
     */
    public void setImage(boolean value) {
        this.isImage = value;
    }

    /**
     * This function returns the image type value
     *
     * @return true if image false otherwise
     */
    public boolean isImage() {
        return this.isImage;
    }

    /**
     * This function sets the type of message as ignored
     *
     * @param value the value of the set ignored
     */
    public void setIgnored(boolean value) {
        this.isIgnored = value;
    }

    /**
     * This function gets the isIgnored value
     *
     * @return true if isIgnored is true false otherwise
     */
    public boolean isIgnored() {
        return isIgnored;
    }

    /**
     * This function set message as selected
     *
     * @param value if true then selected else non-selected
     */
    public void setSelected(boolean value) {
        this.isSelected = value;
    }

    /**
     * This function returns true if selected equal true
     *
     * @return true if message is selected
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * This function returns true if message is an on message
     *
     * @return true if message is an on message
     */
    public boolean isOn() {
        return this.onFlag == true;
    }

    /**
     * This function sets the on flag
     *
     * @param value the value given
     */
    public void setOnFlag(boolean value) {
        this.onFlag = value;
    }

    /**
     * This function set the key
     *
     * @param key the key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * This function gets the key
     *
     * @return the key
     */
    public String getKey() {
        return this.key;
    }
}