package com.example.chatroom.tcp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    public void start() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9090);
            MessagePool.getMessagePoolInstance().start();
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("ip:" + socket.getInetAddress().getHostAddress()
                        + ", port:" + socket.getPort() + "is online now ...");

                ClientTask clientTask = new ClientTask(socket);
                MessagePool.getMessagePoolInstance().addMessageConmingListener(clientTask);
                clientTask.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TCPServer().start();
    }
}
