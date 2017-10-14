package ro.mta.se.chat.crypto;

import ro.mta.se.chat.models.UserModel;
import ro.mta.se.chat.network.NetworkConstants;
import ro.mta.se.chat.singleton.NetworkManager;
import ro.mta.se.chat.singleton.UserManager;
import ro.mta.se.chat.utils.Utils;
import ro.mta.se.chat.utils.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * Created by Alberto-Daniel on 12/19/2015.
 * This is an class that provides asimetric encryption decriptiom
 */
public class RSACipher {
    /**
     * The encrypt function
     *
     * @param data the data to encrypt
     * @return the encrypted data
     */
    public static byte[] encrypt(byte[] data) {
        X509Certificate certificate = Certificate.loadCertificateFromFile(Utils.getCertificatePath());
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            Logger.error("Error no such alogorithm");
        } catch (NoSuchPaddingException e) {
            Logger.error("Error no such padding");
        } catch (IllegalBlockSizeException e) {
            Logger.error("Error ilegal blocking size key");
        } catch (BadPaddingException e) {
            Logger.error("Error bad padding exception");
        } catch (InvalidKeyException e) {
            Logger.error("Invalid key exception");
        }
        return null;
    }

    /**
     * The decrypt function
     *
     * @param data the data to decrypt
     * @return the plain data
     */
    public static byte[] decrypt(byte[] data) {
        UserModel model = NetworkManager.getInstance().getConnnected();
        PrivateKey privateKey = PrivateKeyLoader.loadPrivateKey(Utils.getPrivateKeyPath(), model.getUsername(), model.getPassword());

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            Logger.error("Error no such alogorithm");
        } catch (NoSuchPaddingException e) {
            Logger.error("Error no such padding");
        } catch (IllegalBlockSizeException e) {
            Logger.error("Error ilegal blocking size key");
        } catch (BadPaddingException e) {
            Logger.error("Error bad padding exception");
        } catch (InvalidKeyException e) {
            Logger.error("Invalid key exception");
        }
        return null;
    }

}
