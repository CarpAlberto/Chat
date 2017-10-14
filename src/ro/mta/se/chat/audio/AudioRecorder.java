package ro.mta.se.chat.audio;

import ro.mta.se.chat.utils.logging.Logger;

import javax.sound.sampled.*;

/**
 * Created by Alberto-Daniel on 12/2/2015.
 * This function is responsable for recording audio data
 */
public class AudioRecorder {
    /**
     * This is the chunk containg MAX_SIZE
     */
    public int MAX_DATA_SIZE = 1600;

    /**
     * This is AudioFormat object responsable for recording data
     */
    private AudioFormat audioFormat = null;
    /**
     * This is the target data line
     */
    private TargetDataLine micLine = null;
    /**
     * This is the audio data
     */
    private byte[] audioData = null;

    /**
     * The constructor that initialze the audioFormat object
     */
    public AudioRecorder() {
        audioData = null;
        audioFormat = new AudioFormat(8000.0f, 16, 1, true, true);

    }

    /**
     * This is the function that initialze the target line object
     */
    public void init() {
        try {
            micLine = AudioSystem.getTargetDataLine(audioFormat);
            DataLine.Info dlInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            micLine = (TargetDataLine) AudioSystem.getLine(dlInfo);
            micLine.open(audioFormat);
            audioData = new byte[micLine.getBufferSize() / 5];
            micLine.start();

        } catch (Exception e) {
            Logger.error("Initializing the target line");
        }
    }

    /**
     * This function returns the current audio data recorded
     *
     * @return the current audio data
     */
    public byte[] getAudioData() {

        int bred = micLine.read(audioData, 0, MAX_DATA_SIZE);
        return audioData;
    }

    /**
     * This function close the line
     */
    public void close() {
        micLine.close();
    }

}
