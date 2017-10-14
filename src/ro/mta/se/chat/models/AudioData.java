package ro.mta.se.chat.models;

import ro.mta.se.chat.audio.AudioPlayer;
import ro.mta.se.chat.factory.PacketFactory;
import ro.mta.se.chat.factory.abstracts.AbstractMessage;
import ro.mta.se.chat.views.ChatClientView;

import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 * Created by Alberto-Daniel on 12/2/2015.
 * This class is the audio model responsable for storing
 * audio data
 */
public class AudioData extends AbstractMessage {
    /**
     * The constructor with two parameters
     *
     * @param content  the content of the audio data
     * @param receiver the receiver of the message
     */
    public AudioData(byte[] content, UserModel receiver) {
        super(content, receiver);
    }

    /**
     * The @ovveride toString() method
     *
     * @return a formatted string of audio data
     */
    @Override
    public String toString() {
        AudioPlayer audioPlayback = new AudioPlayer();
        audioPlayback.init();
        audioPlayback.play(this.content);
        return null;
    }

    /**
     * The audio communication is encrypted
     */
    @Override
    public void setEncryptMode() {
        setEncrypted();
    }

    /**
     * The decrypt method from AudioData
     */
    @Override
    public void setDecryptMode() {
        setDecripted();
    }

    /**
     * This function updates the view on receiving new data
     *
     * @param theView to be updated on receiving a new message
     */
    @Override
    public void updateViewOnReceive(ChatClientView theView) {
        toString();
    }

    /**
     * Get an audio packet
     *
     * @return
     */
    @Override
    public ByteBuffer getPacket() {
        return PacketFactory.getAudioPacket(this.content);
    }
}
