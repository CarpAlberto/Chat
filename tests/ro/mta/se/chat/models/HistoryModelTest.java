package ro.mta.se.chat.models;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Alberto-Daniel on 1/2/2016.
 */
public class HistoryModelTest {

    @Test(expected = NullPointerException.class)
    public void testStart() throws Exception {
        UserModel userModel = new UserModel("ip",1111);
        HistoryModel model = new HistoryModel(userModel);
        model.start(null);
    }

    @Test
    public void testSetUser() throws Exception {
        UserModel userModel = new UserModel("ip",1111);
        UserModel userModel2 = new UserModel("ip2",1112);
        HistoryModel model = new HistoryModel(userModel);
        model.setUser(userModel2);
        Assert.assertEquals(1112,model.getUser().getPort());
        Assert.assertEquals("ip2",model.getUser().getIP());
    }

    @Test
    public void testGetAesKey() throws Exception {

    }

    @Test
    public void testAddNewMsg() throws Exception {

    }

    @Test
    public void testGetMsg() throws Exception {

    }

    @Test
    public void testSetAesKey() throws Exception {

    }
}