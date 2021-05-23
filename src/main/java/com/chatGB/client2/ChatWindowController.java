package com.chatGB.client2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class ChatWindowController {

    private final String SERVER_IP = "localhost";
    private final int SERVER_PORT = 44444;

    private static Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;


    @FXML
    private AnchorPane ap_chat_window;
    @FXML
    private TextField tf_enter_message;
    @FXML
    private TextArea ta_display_chat;
    @FXML
    private Button btn_send_message, btn_exit;

    @FXML
    void initialize() throws IOException {
        openConnection();


        btn_send_message.setOnAction(event -> {
            sendMessage();
        });


    }

    public void openConnection() throws IOException {
        clientSocket = new Socket(SERVER_IP, SERVER_PORT);
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        System.out.println("Ready to read.");
                        String strFromServer = in.readUTF();
                        if (strFromServer.equalsIgnoreCase("/end")) {
                            break;
                        }
                        ta_display_chat.appendText("(" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) +
                                ") Server : " + strFromServer + "\n");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void closeConnection() {
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        if (!tf_enter_message.getText().trim().isEmpty()) {
            try {
                out.writeUTF(tf_enter_message.getText());
                tf_enter_message.clear();
                tf_enter_message.requestFocus();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error sending message");
                alert.setHeaderText("Error sending message!!!");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    @FXML
    void exit(ActionEvent event) {
        closeConnection();
        Stage stage = (Stage) btn_exit.getScene().getWindow();
        stage.close();
    }

    @FXML
    void sendMessageEnter(KeyEvent event){
        if (event.getCode() == KeyCode.ENTER) {
            sendMessage();
        }
    }

}
