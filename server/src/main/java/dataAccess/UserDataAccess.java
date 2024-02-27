package dataAccess;

import java.util.Collection;
import model.UserData;

public interface UserDataAccess {

    UserData registerUser(UserData user);

    UserData getUser(UserData user) throws DataAccessException;

    void deleteUser(UserData user) throws DataAccessException;

    void deleteDatabase() throws DataAccessException;


}
