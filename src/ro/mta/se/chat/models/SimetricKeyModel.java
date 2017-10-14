package ro.mta.se.chat.models;

import javax.crypto.BadPaddingException;

import ro.mta.se.chat.crypto.AESCipher;
import ro.mta.se.chat.factory.abstracts.AbstractKey;
import ro.mta.se.chat.utils.logging.Logger;

import java.security.SecureRandom;

/**
 * Created by Alberto-Daniel on 11/16/2015.
 * This class extends abstract class AbstractKey
 * and implements and simetric key model
 */
public class SimetricKeyModel extends AbstractKey {
    /**
     * The internal AESCipher
     */
    AESCipher chiper;

    /**
     * The default constructor of the symetric model
     */
    public SimetricKeyModel() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[16];
        secureRandom.nextBytes(key);
        chiper = new AESCipher(key);
    }

    /**
     * The constructor that takes the input key
     *
     * @param key the input key
     * @throw NullPointerException if key is null
     */
    public SimetricKeyModel(byte[] key) {
        if(key == null)
            throw new NullPointerException();
        chiper = new AESCipher(key);
    }


    /**
     * The encrypt function
     *
     * @param data the data to encrypt
     * @return the encrypted data
     * @throws NullPointerException if @param data is null
     *
     */
    @Override
    public byte[] encrypt(byte[] data) {
        if(data == null)
            throw  new NullPointerException();
        return chiper.encrypt(data);
    }

    /**
     * The decrypt function
     *
     * @param data the encrypted data
     * @return the plain data
     * @throws NullPointerException if @param data is null
     */
    @Override
    public byte[] decrypt(byte[] data) {
        if(data == null)
            throw new NullPointerException();
        byte[] plain = null;
        try {
            plain = chiper.decrypt(data);
        } catch (BadPaddingException e)
        {
            Logger.warn("Error decripting aes data");
        }
        return plain;
    }

    /**
     * This function returns the AESKey
     *
     * @return the aesKey
     */
    @Override
    public byte[] getKey() {
        return chiper.getRaw_key();
    }
}
