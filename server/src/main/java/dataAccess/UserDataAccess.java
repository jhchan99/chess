package dataAccess;

import java.util.Collection;
import model.UserData;

public interface UserDataAccess {


    UserData addUser(UserData user) throws DataAccessException;

    Collection<UserData> listUsers() throws DataAccessException;

    UserData getUser(String name) throws DataAccessException;

    void deleteUser(Integer id) throws DataAccessException;

    void deleteDatabase() throws DataAccessException;


}
