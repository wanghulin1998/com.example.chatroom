package com.example.chatroom.biz;

import android.os.Handler;
import android.os.Looper;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class UdpClientBiz {

    private String mServerIp = "192.168.2.101";
    private InetAddress mInetAddress;
    private int mServerPort = 7777;
    private DatagramSocket mSocket;

    private Handler mUIHandler = new Handler(Looper.getMainLooper());

    public interface OnMessageReturnListener {
        void onMessageReturn(String message);

        void onError(Exception e);

    }

    public void sendMsg(final String msg, final OnMessageReturnListener onMessageReturnListener) {
        new Thread() {
            @Override
            public void run() {
                try {
                    //sendmessage
                    byte[] clientMessageBytes = msg.getBytes();

                    DatagramPacket clientPacket = new DatagramPacket(clientMessageBytes,
                            clientMessageBytes.length, mInetAddress, mServerPort);
                    mSocket.send(clientPacket);

                    //reciveMessage
                    byte[] bytes = new byte[1024];
                    DatagramPacket serverMsgPacket = new DatagramPacket(bytes, bytes.length);
                    mSocket.receive(serverMsgPacket);
                    final String serverMessage = new String(serverMsgPacket.getData(), 0, serverMsgPacket.getLength());

                    mUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onMessageReturnListener.onMessageReturn(serverMessage);
                        }
                    });


                    System.out.println("clientMessage:" + serverMessage);
                } catch (final IOException e) {
                    e.printStackTrace();
                    mUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onMessageReturnListener.onError(e);
                        }
                    });

                }
            }
        }.start();

    }

    public UdpClientBiz() {

        try {
            mSocket = new DatagramSocket();
            mInetAddress = InetAddress.getByName(mServerIp);
        } catch (Exception e) {
            e.printStackTrace();

        }


    }
}

