package ro.mta.se.chat.audio;

import ro.mta.se.chat.utils.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/**
 * Created by Alberto-Daniel on 12/2/2015.
 * This is a class responsable for playing audio data
 */
public class AudioPlayer {

    /**
     * This is the source data line
     */
    private SourceDataLine speakersLine = null;

    /**
     * The data audio format
     */
    private AudioFormat audioFormat = null;

    /**
     * Constructor that initialize variables with default values
     */
    public AudioPlayer() {
        audioFormat = new AudioFormat(8000.0f, 16, 1, true, true);
    }

    /**
     * This is a function that initialize the speker Line
     */
    public void init() {

        try {
            DataLine.Info dlInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            speakersLine = (SourceDataLine) AudioSystem.getLine(dlInfo);
            speakersLine.open(audioFormat);
            speakersLine.start();

        } catch (Exception e) {
            Logger.error("Error setting up speaker line");
        }
    }

    /**
     * This is a function that plays the audio data
     * @param audioData the audio data to be played
     */
    public void play(byte[] audioData) {
        if(audioData == null)
            throw new NullPointerException();
        speakersLine.write(audioData, 0, audioData.length);
    }

    /**
     * This is a function that close the line
     */
    public void close() {
        if(speakersLine != null) {
            speakersLine.drain();
            speakersLine.close();
        }
    }
}
