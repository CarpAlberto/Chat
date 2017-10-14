package ro.mta.se.chat.crypto;

import ro.mta.se.chat.crypto.enums.AES_MODE;
import ro.mta.se.chat.utils.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.management.openmbean.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by Alberto-Daniel on 11/23/2015.
 * This class is responsable for encrypting/decrypting data with AES Cipher
 */
public class AESCipher {

    /**
     * The mode of AES operation
     */
    private AES_MODE mode;

    /**
     * The specifications of the key
     */
    private SecretKeySpec key = null;


    /**
     * This is the internal  cipher of the object
     */
    private Cipher cipher = null;

    /**
     * This is the raw_bytes from aes_key
     */
    private byte[] raw_key;

    /**
     * This is the constructor of the AESCipher
     *
     * @param aesKey the key to aes cipher
     */
    public AESCipher(byte[] aesKey) {
        key = new SecretKeySpec(aesKey, "AES");
        raw_key = aesKey;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            Logger.error("No such algorithm for generating AES KEY");
        } catch (NoSuchPaddingException e) {
            Logger.error("Error padding");
        }
    }

    /**
     * This function encrypts a plaintext and returns the encrypted text
     *
     * @param plaintextData the data to encrypt
     * @return the encrypted data
     */
    public byte[] encrypt(byte[] plaintextData) {

        byte[] ciphertext = null;

        try {
            if (mode != AES_MODE.MODE_ENCRYPT) {
                mode = AES_MODE.MODE_ENCRYPT;
                cipher.init(Cipher.ENCRYPT_MODE, key);
            }
            ciphertext = cipher.doFinal(plaintextData);

        } catch (Exception e) {
            Logger.error("Error while encripting message");
        } finally {
            return ciphertext;
        }
    }

    /**
     * This function decrypt a ciphertext and returns the decrypted text
     *
     * @param ciphertextData the ciphertext
     * @return the  plaintext
     * @throws BadPaddingException
     */
    public byte[] decrypt(byte[] ciphertextData) throws BadPaddingException {

        byte[] plaintext = null;


        try {
            if (mode != AES_MODE.MODE_DECRYPT) {
                mode = AES_MODE.MODE_DECRYPT;
                cipher.init(Cipher.DECRYPT_MODE, key);
            }
            plaintext = cipher.doFinal(ciphertextData);
        } catch (InvalidKeyException e) {
            Logger.error("Error key exception");
            ;
        } catch (IllegalBlockSizeException e) {
            Logger.error("Error key exception");
            ;
        } finally {
            return plaintext;
        }


    }

    /**
     * This function generates a 16 bytes AES_KEY
     *
     * @return the AES KEY
     */
    public static byte[] generateKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[16];
        secureRandom.nextBytes(key);
        return key;
    }

    /**
     * This function returns the RAW_AES_KEY
     *
     * @return RAW_AES_KEY
     */
    public byte[] getRaw_key() {
        return this.raw_key;
    }
}
