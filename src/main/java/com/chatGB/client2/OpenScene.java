package com.chatGB.client2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class OpenScene {
    public static String name = "guest";


    public static void openChat() {

        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(ClassLoader.getSystemResource("chat_window.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Chat");
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setScene(new Scene(root, 600, 400));

            stage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent keyEvent) -> {
                if (KeyCode.ESCAPE == keyEvent.getCode()) {
                    stage.close();
                }
            });



            stage.showAndWait();

            // Hide this current window
//            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
