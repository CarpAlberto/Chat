package ro.mta.se.chat.utils.logging;

/**
 * Created by Alberto-Daniel on 10/19/2015.
 * Aceasta este clasa care incapsueaza diferitele nivele de log
 */
public class Level {
    /**
     * Toate nivele de log
     *
     * @param OFF Inseamna logul este dezactivat
     */
    public final static int OFF = Integer.MAX_VALUE;
    public final static int ERROR = 100;
    public final static int WARNING = 90;
    public final static int INFO = 80;
    public final static int DEBUG = 70;
    public final static int VERBOSE = 60;

    int level;
    String levelStr;

    /**
     * Testeaza daca un level dat ca parametru este mai mare
     * decat levelul continut de obiectul @Level
     *
     * @param rhs Levelul dat ca parametru
     * @return true daca levelul curent este mai mare decat levelul dat ca parametru
     */
    public boolean greaterOrEqual(final Level rhs) {
        if (rhs == null)
            return true;
        return this.level >= rhs.level;
    }

    /**
     * Suprascrie comportamentul default al functieie toString()
     *
     * @return
     */
    @Override
    public String toString() {
        return levelStr;
    }

    /**
     * Construieste un nou obiect de tip Level
     *
     * @param level    nivelul de log
     * @param levelStr stringul asociat nivelului de log
     */
    private Level(int level, String levelStr) {
        this.level = level;
        this.levelStr = levelStr;
    }

    /**
     * Creeaza un obiect de tip Level cu levelul intern off
     *
     * @return obiectul level
     */
    public static Level off() {
        return new Level(OFF, "Off");
    }

    /**
     * Creeaza un obiect de tip Level cu levelul intern error
     *
     * @return obiectul level
     */
    public static Level error() {
        return new Level(ERROR, "Error");
    }

    /**
     * Creeaza un obiect de tip Level cu levelul intern warning
     *
     * @return obiectul level
     */
    public static Level warning() {
        return new Level(WARNING, "Warning");
    }

    /**
     * Creeaza un obiect de tip Level cu levelul intern info
     *
     * @return obiectul level
     */
    public static Level info() {
        return new Level(INFO, "Info");
    }

    /**
     * Creeaza un obiect de tip Level cu levelul intern debug
     *
     * @return obiectul level
     */
    public static Level debug() {
        return new Level(DEBUG, "Debug");
    }

    /**
     * Creeaza un obiect de tip Level cu levelul intern verbose
     *
     * @return obiectul level
     */
    public static Level verbose() {
        return new Level(VERBOSE, "Verbose");
    }

}
