package pl.sda.servlets;

import org.codehaus.jackson.map.ObjectMapper;
import pl.sda.entities.User;
import pl.sda.services.UserService;
import pl.sda.serwlets.responses.CreateUserResponse;
import pl.sda.serwlets.responses.DeleteUserResponse;
import pl.sda.serwlets.responses.UpdateUserResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class JsonReceiver extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        StringBuffer json = new StringBuffer();
        String line = null;

        while ((line = reader.readLine()) != null) {
            json.append(line);
        }
        UserService userService = new UserService();
        CreateUserResponse response = userService.addUser(json.toString());
        ObjectMapper mapper = new ObjectMapper();
        resp.getWriter().write(mapper.writeValueAsString(response));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userID = req.getParameter("id");
        UserService userService = new UserService();
        User user = userService.getUserByUUID(userID);

        ObjectMapper mapper = new ObjectMapper();

        resp.getWriter().write(mapper.writeValueAsString(user));
        resp.setContentType("application/json");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userID = req.getParameter("id");
        UserService userService = new UserService();
        DeleteUserResponse result = userService.removeUserByID(userID);
        ObjectMapper mapper = new ObjectMapper();

        resp.getWriter().write(mapper.writeValueAsString(result));
        resp.setContentType("application/json");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        StringBuffer json = new StringBuffer();
        String line = null;

        while ((line = reader.readLine()) != null) {
            json.append(line);
        }
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(json.toString(), User.class);

        UserService userService = new UserService();
        UpdateUserResponse response = userService.updateUser(user);

        resp.getWriter().write(mapper.writeValueAsString(response));
    }
}
