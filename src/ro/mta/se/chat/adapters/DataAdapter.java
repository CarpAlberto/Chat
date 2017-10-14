package ro.mta.se.chat.adapters;

import ro.mta.se.chat.models.HistoryModel;
import ro.mta.se.chat.models.UserModel;
import ro.mta.se.chat.proxy.interfaces.IDataAcces;
import ro.mta.se.chat.utils.logging.Logger;

import java.util.LinkedList;

/**
 * Created by Alberto-Daniel on 11/10/2015.
 * This class is an adapter between XMLDataReader and XMLDataWriter
 * For this version only XML_STORAGE is supported.
 * This class implements the IDataAcces interface.This interface provides
 * functionalities for accesing data
 */
public class DataAdapter implements IDataAcces {

    /**
     * Type of storage provided by DataAdapter
     */
    public static final int XML_STORAGE = 1;

    /**
     * Relative path to the database where files are stored
     */
    public static final String History_Path = "database";

    /**
     * Object of type XmlDataReader.Provides functionalities for
     * readings/parsing xml files
     */
    private XmlDataReader dataReader;

    /**
     * Object of type XmlDataWriter.Provides functionalities for
     * writing/storing xml files
     */
    private XmlDataWriter dataWriter;

    /**
     * The constructor of DataAdapter.It initialize
     * dataReader and dataWriter object
     */
    public DataAdapter() {
        dataReader = new XmlDataReader();
        dataWriter = new XmlDataWriter();
    }

    /**
     * This function return all the users whit which the cliet from local
     * database
     *
     * @param type the type of storage to be chosen.For this version only
     *             XML_STORAGE is supported
     * @return a linked list with all the users from the database
     * @null if the type does not match with any type provided by the storage
     */
    @Override
    public LinkedList<UserModel> getUsers(int type) {
        switch (type) {
            case XML_STORAGE:
                return dataReader.getUsers();
            default:
                Logger.error("Storage type not supported for this version");
        }
        return null;
    }

    /**
     * This function write the history model into the database
     *
     * @param theHistory the history to be written into the database
     * @param type       the type of storage where the  model will be stored
     */
    @Override
    public void writeHistory(HistoryModel theHistory, int type) {
        switch (type) {
            case XML_STORAGE:
                dataWriter.writeHistory(theHistory);
                break;
            default:
                Logger.error("Storage type not supported for this version");
        }
    }

    /**
     * This function read all the history from the @nickname value
     *
     * @param nickname the nickname from which we extract the history
     * @param type     the type of storage in which the history is located
     * @return a linked list containing all the history between the connected user and
     * the nickname value
     */
    @Override
    public LinkedList<HistoryModel> readHistory(String nickname, int type) {
        switch (type) {
            case XML_STORAGE:
                return dataReader.readHistory(nickname);
            default:
                Logger.error("Storage type not supported for this version");
        }
        return null;
    }

    /**
     * This function is responsable for updating the target directory for a nickname
     *
     * @param oldNickname the old value
     * @param newNickname the new value
     */
    @Override
    public void updateNickname(String oldNickname, String newNickname, int type) {
        switch (type) {
            case XML_STORAGE:
                dataWriter.updateNickname(oldNickname, newNickname);
                break;
            default:
                Logger.error("Storage type not supported for this version");
        }
    }


}
