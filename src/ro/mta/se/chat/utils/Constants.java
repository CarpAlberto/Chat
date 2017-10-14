package ro.mta.se.chat.utils;

import ro.mta.se.chat.utils.logging.Level;

/**
 * Created by Alberto-Daniel on 10/19/2015.
 * Clasa care cuprinde toate constantele din cadrul aplicatiei
 * Acesatea sunt incarcate dintr-un fisier de configurare
 */
public class Constants {
    /**
     * Loggerul global.Acesta este de obicei setat din fisierele de configurare
     */
    public static final int GLOBAL_LOG = Level.DEBUG;
    /**
     * If @LOG_TO_FILE = true then Logger will write records to file
     * Otherwise the records will be written to Console
     */
    public static final boolean LOG_TO_FILE = true;

    /**
     * This is the name of th log file
     */
    public static final String LOG_FILE_NAME = "app.log";

    /**
     * This is the name of configuration file
     */
    public static final String CONFIGURATION_FILE = "config.cfg";

    /**
     * This is the name of product name
     */
    public static final String PRODUCT_NAME = "Just Speack v1.0";

    /**
     * This is the name of company
     */
    public static final String COMPANY_NAME = "Military Technical Academy Bucharest";


}
