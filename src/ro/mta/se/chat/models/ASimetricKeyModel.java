package ro.mta.se.chat.models;

import ro.mta.se.chat.crypto.RSACipher;
import ro.mta.se.chat.factory.abstracts.AbstractKey;

import java.security.*;

/**
 * Created by Alberto-Daniel on 11/16/2015.
 * This is an class that extends the AbstractKey and
 * implements asimetric encryption/decryption
 */
public class ASimetricKeyModel extends AbstractKey {

    /**
     * The constructor of the model
     */
    public ASimetricKeyModel() {

    }

    /**
     * The encrypt function of the Asimetric model
     *
     * @param data the data to encrypt
     * @return the encrypted value
     */
    @Override
    public byte[] encrypt(byte[] data) {
        return RSACipher.encrypt(data);
    }

    /**
     * The decrypt function of the ASimetric model
     *
     * @param data the encrypted data
     * @return the decrypted value
     */
    @Override
    public byte[] decrypt(byte[] data) {
        return RSACipher.decrypt(data);
    }

    /**
     * This function doesn't returns actual data and should not pe used
     *
     * @return nothin
     */
    @Override
    public byte[] getKey() {
        return new byte[0];
    }

}
