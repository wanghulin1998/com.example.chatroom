package com.example.chatroom.biz;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TCPClientBiz {
    private Socket socket ;
    private InputStream inputStream ;
    private OutputStream outputStream ;

    private Handler mUIHandler = new Handler(Looper.getMainLooper());


    private  OnMsgReceiveListener mListener;

    public void setOnMsgReceiveListener(OnMsgReceiveListener onMsgReceiveListener) {
        mListener = onMsgReceiveListener;
    }

    public TCPClientBiz() {

        new Thread() {
            @Override
            public void run() {
                try {
                    socket= new Socket("192.168.2.101",9090);
                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();

                    readServerMsg();
                }
                catch (final Exception e)
                {
                    mUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener !=null) {
                                mListener.onError(e);
                            }
                        }
                    });
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void sendMessage(final String msg ) {
        new Thread() {
            @Override
            public void run() {
                try {
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
                    bw.write(msg);
                    bw.newLine();
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void readServerMsg() throws IOException {
         BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        while ((line = br.readLine()) != null) {
            final String finalLine = line;
            mUIHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mListener !=null) {
                        mListener.onMessageReceived(finalLine);
                    }
                }
            });

        }
    }

    public interface OnMsgReceiveListener
    {
        void onMessageReceived(String msg);
        void onError(Exception e);
    }

    public void onDestory()
    {

        try {
            if (socket!=null)
            {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (inputStream !=null)
            {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (outputStream != null)
            {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
