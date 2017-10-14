package ro.mta.se.chat.utils;

import ro.mta.se.chat.network.NetworkConstants;
import ro.mta.se.chat.utils.logging.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Alberto-Daniel on 11/26/2015.
 * Thiis it the class responsable for loading and writting
 * the configuration file
 */
public class ConfigurationManager {
    /**
     * The configuration instance
     */
    private static volatile ConfigurationManager instance = null;

    /**
     * This function gets the instance of configuration object
     *
     * @return the instance of configuration object
     */
    public static ConfigurationManager getInstance() {
        if (instance == null)
            synchronized (ConfigurationManager.class) {
                instance = new ConfigurationManager();
            }
        return instance;
    }

    /**
     * This variable contains the configuration properties
     */
    private Properties configuration = new Properties();


    /**
     * This function loads the configuration properties
     */
    public void loadConfig() {
        File file = new File(NetworkConstants.CONFIGURATION_FILE);
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e1) {
                Logger.error("Error creating config file");
            }
        try {
            FileInputStream stream = new FileInputStream(Constants.CONFIGURATION_FILE);
            configuration.load(stream);
            stream.close();
        } catch (Throwable e) {
            Logger.error("Error loading config file");
        }
    }

    /**
     * This function stores the configuration properties
     */
    public void storeConfig() {
        try {
            FileOutputStream stream = new FileOutputStream(Constants.CONFIGURATION_FILE);
            configuration.store(stream, "");
            stream.close();
        } catch (Throwable e) {
            Logger.error("Error storing config file");
        }
    }

    /**
     * This function set certain key with a value
     *
     * @param key   the key
     * @param value the value
     */
    public void setValue(String key, String value) {
        configuration.setProperty(key, value);
        storeConfig();
    }

    /**
     * This function gets a value given key
     *
     * @param key the param key
     * @return the value
     */
    public String getValue(String key) {
        return configuration.getProperty(key);
    }
}
