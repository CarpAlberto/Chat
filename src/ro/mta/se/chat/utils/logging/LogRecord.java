package ro.mta.se.chat.utils.logging;

import ro.mta.se.chat.utils.Constants;
import ro.mta.se.chat.utils.logging.Level;

import java.sql.Date;

/**
 * Created by Alberto-Daniel on 10/20/2015.
 * Aceasta este clasa care cuprinde o inregistrare de cod care va fi ulterior publicata
 * Ori de cate ori vrem sa scriem un log instantiem un LogRecord
 */
public class LogRecord {
    /**
     * Levelul din inregistrarea de log.Acesta nu poate fi niciodata null
     */
    private Level level;
    /**
     * Clasa sursa care a lansat logul.Daca aceasta este null se va afisa mesajul Undefined.
     */
    private String sourceClass;
    /**
     * Metoda sursa care a lansat logul.Daca aceasta este null se va afisa mesajul Undefined.
     */
    private String sourceMethod;
    /**
     * Stringul raw cu mesajul de eroare
     */
    private String rawMessage;
    /**
     * Timpul in milisecunde al inregisrarii.
     */
    private long miliseconds;

    /**
     * Exceptia care a cauzat logul
     */

    private Exception exception;

    /**
     * Constructorul
     *
     * @param level - Levelul inregistrarii
     * @param msg   - - Mesajul inregistrarii
     */
    public LogRecord(Level level, String msg) {
        this(level, msg, null, null);
    }

    /**
     * Constructorul
     *
     * @param level       - Levelul inregistrarii
     * @param msg         - - Mesajul inregistrarii
     * @param sourceClass -Clasa Sursa
     */
    public LogRecord(Level level, String msg, String sourceClass) {
        this(level, msg, sourceClass, null);
    }

    /**
     * Constructor
     *
     * @param level        - Levelul inregistrarii
     * @param msg          - Mesajul inregistrarii
     * @param sourceClass  - Clasa Sursa
     * @param sourceMethod - Metoda Sursa
     *                     throws NullPointerException daca levelul este null
     */
    public LogRecord(Level level, String msg, String sourceClass, String sourceMethod) {
        this(level, msg, sourceClass, sourceMethod, null);
    }

    /**
     * Constructor
     *
     * @param level        - Levelul inregistrarii
     * @param msg          - Mesajul inregistrarii
     * @param sourceClass  - Clasa Sursa
     * @param SourceMethod - Metoda Sursa
     * @param exc          - Exception thrown
     *                     throws NullPointerException daca levelul este null
     */
    public LogRecord(Level level, String msg, String sourceClass, String SourceMethod, Exception exc) {
        if (level == null)
            throw new NullPointerException("Lavel should not be null");
        if (msg == null)
            throw new NullPointerException("Message should not be null");
        this.level = level;
        this.rawMessage = msg;
        this.sourceClass = sourceClass;
        this.sourceMethod = SourceMethod;
        this.miliseconds = System.currentTimeMillis();
        this.exception = exc;
    }

    /**
     * Returneaza mesajul in format raw
     *
     * @return Mesajul raw
     */
    public String getRawMessage() {
        return this.rawMessage;
    }

    /**
     * Seteaza valoarea mesajului raw
     *
     * @param rawMessage - Seteaza mesajul raw
     */
    public void setMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    /**
     * Suprascrie comportamentul implicit al dunctiei ToString
     * Intoarce formatat cu eroarea
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(level.toString());
        buffer.append(":");
        buffer.append(rawMessage + "\n");
        Date date = new Date(this.miliseconds);
        buffer.append("Data : " + date.toString());

        buffer.append(" Source class : ");
        if (sourceClass != null)
            buffer.append(sourceClass);
        else
            buffer.append("Undefined ");

        buffer.append(" Source method : ");
        if (sourceMethod == null)
            buffer.append("Undefined ");
        else
            buffer.append(sourceMethod);
        buffer.append("Exception : ");
        if (exception != null)
            buffer.append(exception.toString());
        else
            buffer.append("Undefined ");

        buffer.append("\n");
        return buffer.toString();
    }

    /**
     * Verifica daca inregistrarea curenta este valdia                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               e valida.
     * O inregistrare este valida daca este ca si nivel mai mare
     * decat levelul global
     *
     * @return true daca inregistrarea este valida..false altfel
     */
    public boolean isValid() {
        if (level.level > Constants.GLOBAL_LOG)
            return true;
        return false;
    }

    /**
     * Intoarce levelul curent din inregistrare
     *
     * @return Levelul
     */
    public Level level() {
        return this.level;
    }


}
