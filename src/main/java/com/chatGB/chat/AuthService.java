package com.chatGB.chat;

public interface AuthService {
    void start();
    String getEmailByEmailPassword(String email, String password);
    String getLastnameByEmail(String email);
    void stop();
}
