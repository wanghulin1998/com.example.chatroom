package com.example.chatroom.tcp.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {


    private Scanner mScanner;

    public TCPClient() {
        this.mScanner = new Scanner(System.in);
        mScanner.useDelimiter("\n");
    }

    public void start()
    {
        try {
            Socket socket = new Socket("192.168.2.101",9090);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

            new Thread(){
                @Override
                public void run() {
                    String line = null;
                    try {

                        while ((line = br.readLine()) != null) {

                            System.out.println(line);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }.start();


            while (true)
            {
                String msg =  mScanner.next();
                bw.write(msg);
                bw.newLine();
                bw.flush();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TCPClient().start();
    }
}
