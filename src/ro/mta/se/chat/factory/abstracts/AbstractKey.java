package ro.mta.se.chat.factory.abstracts;

import ro.mta.se.chat.models.ASimetricKeyModel;
import ro.mta.se.chat.models.SimetricKeyModel;

/**
 * Created by Alberto-Daniel on 11/16/2015.
 * This is the base class for all keys from chat system
 */
public abstract class AbstractKey {
    /**
     * Encryot the data and returns the encrypted values
     *
     * @param data the data to encrypt
     * @return the encrypted values
     */
    public abstract byte[] encrypt(byte[] data);

    /**
     * Decrypt the data and returns the plain data
     *
     * @param data the encrypted data
     * @return the plain data
     */
    public abstract byte[] decrypt(byte[] data);

    /**
     * This function returns the actual key
     *
     * @return the raw key
     */
    public abstract byte[] getKey();

}
