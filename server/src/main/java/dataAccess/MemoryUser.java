package dataAccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryUser implements UserDataAccess{
    private final int nextId = 1;

    final private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public UserData registerUser(UserData user) {
        user = new UserData(user.username(), user.password(), user.email());
        users.put(user.username(), user);
        return user;
    }

    @Override
    public UserData getUser(UserData user) throws DataAccessException {
        return users.get(user.username());
    }

    @Override
    public void deleteUser(UserData user) throws DataAccessException {
        if(users.containsValue(user)) {
            users.remove(user.username());
        }else {
            throw new DataAccessException("User not found");
        }
    }

    @Override
    public void deleteDatabase() throws DataAccessException {
        users.clear();
    }

}
