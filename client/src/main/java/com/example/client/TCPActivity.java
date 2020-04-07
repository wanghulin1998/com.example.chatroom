package com.example.client;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatroom.biz.TCPClientBiz;

public class TCPActivity extends AppCompatActivity  {
    private EditText mEditText;
    private Button mButtonSend;
    private TextView mTextViewMessage;

    private TCPClientBiz tcpClientbiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_c_p);
        tcpClientbiz = new TCPClientBiz();
        tcpClientbiz.setOnMsgReceiveListener(new TCPClientBiz.OnMsgReceiveListener() {
            @Override
            public void onMessageReceived(String msg) {
                appendMessage2Content(msg);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        initView();

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewMessage.setText(" ");
                String messages = mEditText.getText().toString();
                if (TextUtils.isEmpty(messages))
                {
                    return;
                }
                appendMessage2Content("client:"+messages);
                tcpClientbiz.sendMessage(messages);

            }
        });
    }

    private void appendMessage2Content(String message) {
        mTextViewMessage.append(message+"\n");
    }

    private void initView() {
        mEditText = findViewById(R.id.input);
        mButtonSend = findViewById(R.id.send);
        mTextViewMessage = findViewById(R.id.message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tcpClientbiz.onDestory();
    }
}
