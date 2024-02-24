package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryUser;
import dataAccess.UserDataAccess;
import model.UserData;

public class Service {

    private final UserDataAccess dataAccess = new MemoryUser();


    public UserData addUser(UserData user) throws Exception {
        return dataAccess.addUser(user);
    }

    public void deleteDatabase() throws DataAccessException {
        dataAccess.deleteDatabase();
    }


}

