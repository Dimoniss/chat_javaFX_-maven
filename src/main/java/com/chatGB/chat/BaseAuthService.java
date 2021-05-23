package com.chatGB.chat;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class BaseAuthService implements AuthService {
    Map<String, User> users;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("no driver!");
            e.printStackTrace();
        }
    }

    @Override
    public void start() {

        users = new HashMap<>();

        try(Connection postgresConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/chat_users", "postgres", "Progreess1122")) {

            Statement ordersStatement = postgresConnection.createStatement();
            ResultSet resultSet = ordersStatement.executeQuery("select * from users");

            while (resultSet.next()) {
                System.out.println(resultSet.getString("email"));
                users.put(resultSet.getString("email"),
                        new User(resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("lastname")));
            }

        } catch (SQLException SQLEx) {
            System.out.println("SQL Exception");
            SQLEx.printStackTrace();
        }

//        users.put("l@gmail.com", new User("l@gmail.com", "123456", "Lee"));
//        users.put("b@gmail.com", new User("b@gmail.com", "123456", "Ben"));
//        users.put("a@gmail.com", new User("a@gmail.com", "123456","Agent007"));
    }

    @Override
    public String getEmailByEmailPassword(String email, String password) {
        User user = users.get(email);
        if (user != null &&  user.getPass().equals(password)) {
            return user.getEmail();
        }
        return null;
    }

    @Override
    public String getLastnameByEmail(String email) {
        User user = users.get(email);
        if (user != null) {
            System.out.println("get last name :" + user.getLastname());
            return user.getLastname();

        }
        return null;
    }

    @Override
    public void stop() {
        System.out.println("service stopped");
    }
}
