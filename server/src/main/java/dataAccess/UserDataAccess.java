package dataAccess;

import java.sql.SQLException;
import model.UserData;

public interface UserDataAccess {

    UserData registerUser(UserData user) throws SQLException, DataAccessException;

    UserData getUser(UserData user) throws DataAccessException, SQLException;

    void deleteUsers() throws DataAccessException;


}
