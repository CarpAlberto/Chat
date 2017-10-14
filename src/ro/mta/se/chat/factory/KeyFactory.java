package ro.mta.se.chat.factory;


import ro.mta.se.chat.factory.abstracts.AbstractKey;
import ro.mta.se.chat.models.ASimetricKeyModel;
import ro.mta.se.chat.models.SimetricKeyModel;
import ro.mta.se.chat.utils.logging.Logger;

/**
 * Created by Alberto-Daniel on 11/16/2015.
 * This class is responsable for generating Simetric and ASimetric
 * keys.The simetric keys uses the AES cipher, the asimetric key uses
 * the RSA Cipher
 */
public abstract class KeyFactory {

    /**
     * The value for simetric key
     */
    public static final int SYMETRIC_KEY = 1;
    /**
     * The value with simetryc key and also key provided
     */
    public static final int SYMETRIC_KEY_WITH_DATA = 4;
    /**
     * The valye for asimetric key
     */
    public static final int ASYMETRIC_KEY = 2;

    /**
     * The main function of this class.It is responsable for generating keys
     *
     * @param type the type of the key
     * @param data the eventual data provided ( null if no need data)
     * @return the Abstract key
     */
    public static AbstractKey getKey(int type, byte[] data) {
        switch (type) {
            case KeyFactory.ASYMETRIC_KEY:
                return new ASimetricKeyModel();
            case KeyFactory.SYMETRIC_KEY:
                return new SimetricKeyModel();
            case KeyFactory.SYMETRIC_KEY_WITH_DATA:
                return new SimetricKeyModel(data);
            default:
                Logger.error("Key type not found ", KeyFactory.class.getSimpleName());
        }
        return null;
    }
}
