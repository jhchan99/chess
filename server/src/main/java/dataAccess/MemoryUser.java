package dataAccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryUser implements UserDataAccess{
    private int nextId = 1;

    final private HashMap<Integer, UserData> users = new HashMap<>();

    @Override
    public UserData createUser(UserData user) {
        user = new UserData(nextId++, user.username(), user.password(), user.email());
        users.put(user.id(), user);
        return user;
    }

    @Override
    public Collection<UserData> listUsers() throws DataAccessException {
        return null;
    }

    @Override
    public UserData getUser(UserData user) throws DataAccessException {
        if(users.containsValue(user)) {
            return user;
        }
        createUser(user);
        return null;
    }

    @Override
    public void deleteUser(UserData user) throws DataAccessException {
        if(users.containsValue(user)) {

        }
    }

    @Override
    public void deleteDatabase() throws DataAccessException {
        users.clear();
    }

}
