package ro.mta.se.chat.crypto;

import ro.mta.se.chat.utils.logging.Logger;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;

/**
 * Created by Alberto-Daniel on 11/23/2015.
 * This class is an util class thet provides support for loading the private key
 */
public class PrivateKeyLoader {
    /**
     * This method loads a private key that is stored encrypted in a java key store.
     *
     * @param path          Absolute path to java key store file
     * @param alias         Alias in the java key store where the private key is
     * @param storepassword Password for java key store
     * @return PrivateKey
     */
    public static PrivateKey loadPrivateKey(String path, String alias, String storepassword) {

        PrivateKey privateKey = null;
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(path), storepassword.toCharArray());
            KeyStore.PrivateKeyEntry prvKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, new KeyStore.PasswordProtection(storepassword.toCharArray()));
            privateKey = prvKeyEntry.getPrivateKey();
        } catch (Exception e) {
            Logger.error("Error loading private key");
        } finally {
            return privateKey;
        }
    }


}
