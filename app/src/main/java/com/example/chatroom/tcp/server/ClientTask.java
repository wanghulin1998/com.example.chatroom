package com.example.chatroom.tcp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ClientTask extends Thread implements MessagePool.MessageConmingListener {

    private Socket mSocket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public ClientTask(Socket Socket) {

        try {
            mSocket = Socket;
            inputStream = mSocket.getInputStream();
            outputStream = mSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line ;

            while ((line = br.readLine())!=null)
            {
                System.out.println("read:"+line);
                //转发消息给其他客户端
                MessagePool.getMessagePoolInstance().sendMessage("port:"+mSocket.getPort()+"  message:"+line);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageComing(String message) {

        System.out.println(message+"****");
        try {
            outputStream.write(message.getBytes());
            outputStream.write("\n".getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
