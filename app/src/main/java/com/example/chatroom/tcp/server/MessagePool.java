package com.example.chatroom.tcp.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class MessagePool {

    private static MessagePool messagePoolInstance = new MessagePool();
    private LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue();
    private List<MessageConmingListener> msgListeners = new ArrayList<>();


    public MessagePool() {
    }



    public static MessagePool getMessagePoolInstance()
    {
        return messagePoolInstance;
    }

    public void sendMessage(String message)
    {
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start()
    {
        new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        String msg = messageQueue.take();
                        System.out.println(msg+"+++");
                        notifiMessageComing(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
    }

    private void notifiMessageComing(String msg) {
        for (MessageConmingListener listener:msgListeners) {
            listener.onMessageComing(msg);
        }
    }

    public interface MessageConmingListener
    {
        void onMessageComing(String message);
    }

    public void addMessageConmingListener(MessageConmingListener messageConmingListener)
    {
        msgListeners.add(messageConmingListener);
    }
}
