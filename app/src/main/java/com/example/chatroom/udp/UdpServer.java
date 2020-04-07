package com.example.chatroom.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UdpServer {

    private InetAddress mAddress;//地址
    private int mPort = 7777; //端口号
    private DatagramSocket mSocket;

    private Scanner scanner;


    public static void main(String[] args) {
        System.out.println("server is started!");
        new UdpServer().start();
    }

    public UdpServer()
    {
        try {
            mAddress = InetAddress.getLocalHost();
            mSocket = new DatagramSocket(mPort,mAddress);
            scanner = new Scanner(System.in);
            scanner.useDelimiter("\n");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }


    public void start() {
        while (true) {
            byte[] bytes = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(bytes, bytes.length);
            try {
                mSocket.receive(receivePacket);

                InetAddress address = receivePacket.getAddress();
                int port = receivePacket.getPort();
                byte[] data = receivePacket.getData();
                String clientMessage = new String(data, 0, receivePacket.getLength());

                System.out.println("address:" + address + "," + "port:" + port + "," + "clientMessage:" + clientMessage);

                String returnedMessage = scanner.next();
                byte[] reruened = returnedMessage.getBytes();

                DatagramPacket sentPacket = new DatagramPacket(reruened, reruened.length, receivePacket.getSocketAddress());
                mSocket.send(sentPacket);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
