package com.chatGB.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class ClientHandler {
    private ChatServer server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String lastname = "";
    private String email = "";
    private static final Logger LOG = LogManager.getLogger(ClientHandler.class);


    public ClientHandler(Socket socket) {
        try {
            this.server = ChatServer.getServer();
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    LOG.info("Start new client handler thread");
                    auth();
                    readMsg();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            }).start();

        } catch (IOException e) {
            LOG.error("error while creating client handler !!!", e);
            e.printStackTrace();
        }
    }

    private void auth() throws IOException {
        while (true) {
            String str = in.readUTF();

            // /auth email pass
            //TODO add check for the number of objects
            if (str.startsWith("/auth")) {
                String[] parts = str.split(" ");
                String emailFromServer = parts[1];
                String password = parts[2];
                String lastnameFromServer = server.getAuthService().getLastnameByEmail(emailFromServer);
                LOG.info("new auth " + emailFromServer);

            //TODO verify the password
                if (email != null) {
                    if (!server.isLastnameBusy(lastnameFromServer)) {
                        sendMsg("/auth ok " + lastnameFromServer);
                        sendMsg("to send personal messages, write </p lastname> before the message");
                        lastname = lastnameFromServer;
                        email = emailFromServer;
                        server.broadcastMsg(lastname + " entered in chat.");
                        server.subscribe(this);
                        LOG.info("client auth OK");
                        return;
                    } else {
                        LOG.info("Lastname is busy!");
                        sendMsg("Lastname is busy!");
                    }
                } else {
                    LOG.info("Wrong e-mail/password");
                    sendMsg("Wrong e-mail/password !!!");
                }

            } else {
                LOG.info("Unauthorized user is trying to send a message");
                sendMsg("Login before writing a message </auth e-mail password>");
                sendMsg("Guest login: email: a@gmail.com; password: 123456; lastname: Agent007");
            }
        }
    }


    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void readMsg() throws IOException {
        while (true) {
            String strFromClient = in.readUTF();
            System.out.println("from " + lastname + ": " + strFromClient);

            if (strFromClient.startsWith("/p")) {
                String[] split = strFromClient.split(" ");
                String lastname = split[1];
                System.out.println(Arrays.toString(split));
                if (server.isLastnameBusy(lastname)) {

                    StringBuilder msg = new StringBuilder();
                    for (int i = 2; i < split.length; i++) {
                        msg.append(split[i]).append(" ");
                    }
                    LOG.trace("msg={}", msg);
                    server.privateMsg(lastname, msg.toString());
                } else {

                    sendMsg(lastname + " not in chat!");
                }
            } else if( strFromClient.equals("/end") ) {
                LOG.info("Close connection from clientHandler.readMsg()");
                closeConnection();
                return;
            } else {
                LOG.trace(this.getLastname()+" send msg");
                server.broadcastMsg(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) +
                                " "+ lastname + ": " + strFromClient);
            }


        }
    }

    public void closeConnection() {
        server.unsubscribe(this);
        LOG.info(this.email + " unsubscribe from broadcast");
        server.broadcastMsg(lastname + " left the chat.");

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
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLastname() {
        return lastname;
    }
    public String getEmail() {
        return email;
    }


}
