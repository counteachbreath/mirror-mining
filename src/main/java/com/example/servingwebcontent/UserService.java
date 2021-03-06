package com.example.servingwebcontent;

import com.jcraft.jsch.JSchException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/*der UserService zieht die Daten aus der DB*/
public class UserService {
    private static final List<String> usernames = new ArrayList<>();

    static {
        try {
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initData() throws JSchException, SQLException, IOException {
        // Get users from the DB and add them to a list of users.
        DatabaseConnection db = new DatabaseConnection();
        List<Map<String, Object>> users = db.executeQuery("select id from a_users");
        List<Integer> userList = new ArrayList<>();
        for (Map<String, Object> user : users) {
            userList.add((Integer) user.get("id"));
        }
        Collections.sort(userList);

        for (int i : userList) {
            usernames.add(String.valueOf(i));
        }
    }
    public static List<String> getUsernames() {
        return usernames;
    }
}
