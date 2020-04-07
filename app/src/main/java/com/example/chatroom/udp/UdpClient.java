package com.example.chatroom.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UdpClient {

    private String mServerIp = "192.168.3.8";
    private InetAddress mInetAddress;
    private int mServerPort = 7777;
    private DatagramSocket mSocket;
    private Scanner mScanner;

    public static void main(String[] args) {
        System.out.println("client is started!");
        new UdpClient().start();
    }

    public UdpClient()
    {
        try {
            mSocket = new DatagramSocket();
            mInetAddress = InetAddress.getByName(mServerIp);
            mScanner = new Scanner(System.in);
            mScanner.useDelimiter("\n");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void start()
    {
        while (true){
            try {
            String clientMessage = mScanner.next();

            byte[] clientMessageBytes = clientMessage.getBytes();

            DatagramPacket clientPacket = new DatagramPacket(clientMessageBytes,
                    clientMessageBytes.length,mInetAddress,mServerPort);

                mSocket.send(clientPacket);


                byte[] bytes = new byte[1024];
                DatagramPacket serverMsgPacket = new DatagramPacket(bytes,bytes.length);
                mSocket.receive(serverMsgPacket);



//                InetAddress address = serverMsgPacket.getAddress();
//                int port = serverMsgPacket.getPort();
                byte[] data = serverMsgPacket.getData();
                String serverMessage = new String(data, 0, serverMsgPacket.getLength());

                System.out.println( "serverMessage:" + serverMessage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
