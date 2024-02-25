package dataAccess;

import java.util.Collection;
import model.UserData;

public interface UserDataAccess {

    UserData createUser(UserData user) throws DataAccessException;

    Collection<UserData> listUsers() throws DataAccessException;

    UserData getUser(UserData user) throws DataAccessException;

    void deleteUser(UserData user) throws DataAccessException;

    void deleteDatabase() throws DataAccessException;


}
