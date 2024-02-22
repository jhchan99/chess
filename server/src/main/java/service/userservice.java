package service;

import dataAccess.UserDataAccess;

public class userservice {

    private final UserDataAccess dataAccess;

    public userservice(UserDataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void deleteAllUsers() {
        dataAccess.deleteAllUsers();
    }
}
