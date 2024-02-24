package dataAccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryUser implements UserDataAccess{
    private int nextId = 1;

    final private HashMap<Integer, UserData> users = new HashMap<>();

    @Override
    public UserData addUser(UserData user) {
        user = new UserData(user.username(), user.password(), user.email());
        return user;
    }

    @Override
    public Collection<UserData> listUsers() throws DataAccessException {
        return null;
    }

    @Override
    public UserData getUser(String name) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteUser(Integer id) throws DataAccessException {

    }

    @Override
    public void deleteDatabase() throws DataAccessException {
        users.clear();
        throw new DataAccessException("uh oh");
    }

}
