package com.example.chatroom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chatroom.biz.UdpClientBiz;

public class UDPActivity extends AppCompatActivity  {
    private EditText mEditText;
    private Button mButtonSend;
    private TextView mTextViewMessage;

    private UdpClientBiz udpClientBiz = new UdpClientBiz();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("UDP_CHATROOM");

        initView();

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messages = mEditText.getText().toString();
                if (TextUtils.isEmpty(messages))
                {
                    return;
                }
                appendMessage2Content("client:"+messages);
                udpClientBiz.sendMsg(messages, new UdpClientBiz.OnMessageReturnListener() {
                    @Override
                    public void onMessageReturn(String message) {
                        appendMessage2Content("server:"+message);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
                mEditText.setText(" ");

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


}
