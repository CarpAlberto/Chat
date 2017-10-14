package ro.mta.se.chat.proxy;

import ro.mta.se.chat.adapters.DataAdapter;
import ro.mta.se.chat.exceptions.ViolationAccesException;
import ro.mta.se.chat.models.HistoryModel;
import ro.mta.se.chat.models.UserModel;
import ro.mta.se.chat.utils.logging.Logger;

import java.util.LinkedList;

/**
 * Created by Alberto-Daniel on 11/16/2015.
 * This class is an proxy for accesing the database storage by an user
 */
public class DataAdapterProxy {
    /**
     * The user who try to acces the database
     */
    UserModel user;
    /**
     * the adapter which acces the database
     */
    DataAdapter adapter;

    /**
     * The constructo of the Proxy Object
     *
     * @param adapter the adapter of the methid
     * @param model   the connected user
     */
    public DataAdapterProxy(DataAdapter adapter, UserModel model) {
        this.adapter = adapter;
        this.user = model;
    }

    /**
     * This function write a history model to the database
     *
     * @param model the model to be written
     * @throws ViolationAccesException
     */
    public void writeHistory(HistoryModel model) throws ViolationAccesException {
        if (user != null)
            adapter.writeHistory(model, DataAdapter.XML_STORAGE);
        else {
            throw new ViolationAccesException("Acces violation updata write history function");
        }
    }

    /**
     * This function updates a given nickname with a newone
     *
     * @param oldNickname the old nickname
     * @param newNickname the new nickname
     * @throws ViolationAccesException
     */
    public void updateNickname(String oldNickname, String newNickname) throws ViolationAccesException {
        if (user != null)
            adapter.updateNickname(oldNickname, newNickname, DataAdapter.XML_STORAGE);
        else {
            throw new ViolationAccesException("Acces violation updata newNickname function");
        }
    }

    /**
     * This function returns all the users from the database
     *
     * @param type the type of database storage
     * @return all the users from database
     * @throws ViolationAccesException
     */
    public LinkedList<UserModel> getUsers(int type) throws ViolationAccesException {
        if (user != null) {
            return adapter.getUsers(type);
        } else {
            throw new ViolationAccesException("Acces violation getUsers method");
        }
    }

    /**
     * This function reads and history from the databse
     *
     * @param nickname the history's nickname
     * @param type     the type of storgae
     * @return a list of histories
     * @throws ViolationAccesException
     */
    public LinkedList<HistoryModel> readHistory(String nickname, int type) throws ViolationAccesException {
        if (user != null) {
            return adapter.readHistory(nickname, type);
        } else {
            throw new ViolationAccesException("Acces violation readUsers method");
        }
    }
}
