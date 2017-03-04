package pl.sda.services;

import org.codehaus.jackson.map.ObjectMapper;
import pl.sda.entities.User;
import pl.sda.serwlets.responses.CreateUserResponse;
import pl.sda.serwlets.responses.DeleteUserResponse;
import pl.sda.serwlets.responses.UpdateUserResponse;
import pl.sda.storage.Storage;

import java.io.IOException;
import java.util.UUID;

public class UserService {

    public CreateUserResponse addUser(String userJson) {
        ObjectMapper mapper = new ObjectMapper();
        CreateUserResponse response = new CreateUserResponse();

        try {
            User user = mapper.readValue(userJson, User.class);
            UUID id = UUID.randomUUID();
            user.setId(id);
            Storage.addUser(user);
            response.setStatus("OK");
            response.setId(id.toString());
        } catch (IOException e) {
            response.setError(e.getMessage());
        }
        return response;
    }

    public User getUserByUUID(String id) {
        User result = null;
        if (id != null && !id.isEmpty()) {
            UUID uuid = UUID.fromString(id);
            for (User user : Storage.getUsers()) {
                if (uuid.equals(user.getId())) {
                    result = user;
                    break;
                }
            }
        }
        return result;
    }

    public DeleteUserResponse removeUserByID(String id) {
        DeleteUserResponse result = new DeleteUserResponse();
        result.setMessage("USER WITH ID: " + id + " NOT FOUND");
        if (id != null && !id.isEmpty()) {
            User tempUser = null;
            UUID uuid = UUID.fromString(id);
            for (User user : Storage.getUsers()) {
                if (uuid.equals(user.getId())) {
                    tempUser = user;
                    break;
                }
            }
            Storage.removeUser(tempUser);
            result.setStatus("OK");
            result.setMessage("USER WITH ID: " + id + " WAS REMOVED");
        }
        return result;
    }

    public UpdateUserResponse updateUser(User user) {
        UpdateUserResponse response = new UpdateUserResponse();
        Storage.updateUser(user);
        response.setStatus("OK");
        response.setMessage("USER UPDATED");
        return response;
    }
}
