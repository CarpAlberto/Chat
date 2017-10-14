package ro.mta.se.chat.proxy.interfaces;

import ro.mta.se.chat.models.HistoryModel;
import ro.mta.se.chat.models.UserModel;

import java.util.LinkedList;

/**
 * Created by Alberto-Daniel on 11/17/2015.
 * This interface is used to implement the proxy pattern
 * for accesing the DataAdapter objects
 */
public interface IDataAcces {

    /**
     * This function returns all users from database
     *
     * @param type this is the type of storage of database
     * @return the List of all users
     */
    LinkedList<UserModel> getUsers(int type);

    /**
     * This function write a history model into database
     *
     * @param theHistory the history model to write
     * @param type       the type pf database
     */
    void writeHistory(HistoryModel theHistory, int type);

    /**
     * This function read all the history from a given nickname
     *
     * @param nickname the given nickname
     * @param type     the type of storage
     * @return tge list of history
     */
    LinkedList<HistoryModel> readHistory(String nickname, int type);

    /**
     * This function updates a nickname setting new values
     *
     * @param oldNickname the oldValue
     * @param newNickname the newValue
     * @param type        the type of storage of data
     */
    void updateNickname(String oldNickname, String newNickname, int type);
}
