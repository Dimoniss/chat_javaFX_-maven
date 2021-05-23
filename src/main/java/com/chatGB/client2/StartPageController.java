package com.chatGB.client2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;


public class StartPageController {

    @FXML
    private TextField tf_name;

    @FXML
    void openChat(ActionEvent event) throws IOException {
        String name = "guest";
        if (!tf_name.getText().trim().equals(""))
            name = tf_name.getText().trim();
        OpenScene.name = name;
        OpenScene.openChat();
    }

    @FXML
    void initialize() {

    }
}
