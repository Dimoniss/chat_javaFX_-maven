package com.chatGB.chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private static ChatServer server;

    private final int SERVER_PORT = 44444;
    private List<ClientHandler> clients;
    private AuthService authService;
    private ExecutorService executorService;


    public ChatServer() {
        server = this;
        try(ServerSocket server = new ServerSocket(SERVER_PORT)) {
            authService = new BaseAuthService();
            authService.start();
            clients = new ArrayList<>();
            executorService = Executors.newCachedThreadPool();

            while (true) {
                System.out.println("Waiting for connection.");

                Socket socket = server.accept();
                System.out.println("Client connected.");

                new ClientHandler(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (authService != null) {
                authService.stop();
            }
        }
    }

    public synchronized void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);

        displayingHistoryOfLastLinesOfMainChat(clientHandler, 100);
    }

    public void displayingHistoryOfLastLinesOfMainChat(ClientHandler clientHandler, int numberOfRows) {

        String stringPath="chatHistory.txt";
        int counter = 0;

        File file = new File(stringPath);
        try (BufferedReader bufferedReader =
                     new BufferedReader(new InputStreamReader(new ReverseLineInputStream(file)))) {
            ArrayList<String> rows = new ArrayList<>();

            while(true) {
                String row = bufferedReader.readLine();
                if (row == null) break;
                if (counter <= numberOfRows){
                    rows.add(row);
                    counter++;
                }
            }
            Collections.reverse(rows);
            for (String row : rows) {
                clientHandler.sendMsg(row);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcastMsg(String msg) {
        // save message to history
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("chatHistory.txt", true)));) {
            bufferedWriter.append(msg).append("\n");
        } catch (IOException e) {

            e.printStackTrace();
        }

        for (ClientHandler clientHandler : clients) {
            clientHandler.sendMsg(msg);
        }
    }

    public synchronized void setHistoryFromChat(int numberOfStrings, String clientEmail) {
        //TODO when client is joined
    }

    public synchronized void privateMsg(String lastname, String msg) {
        for (ClientHandler clientHandler : clients) {
            if (clientHandler.getLastname().equals(lastname)) {
                clientHandler.sendMsg(msg + " (private)");
                System.out.println("message sent to " + clientHandler.getLastname());
            }
        }
    }

    public synchronized boolean isLastnameBusy(String lastname) {
        for (ClientHandler clientHandler : clients) {
            if (clientHandler.getLastname().equals(lastname)) {
                return true;
            }
        }
        return false;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public static ChatServer getServer() {
        return server;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}

