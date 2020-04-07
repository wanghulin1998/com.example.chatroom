package com.example.chatroom;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatroom.R;
import com.example.chatroom.biz.TCPClientBiz1;

import https.HttpUtils;


public class HTTPActivity extends AppCompatActivity  {
    private EditText mEditText;
    private Button mButtonSend;
    private TextView mTextViewMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mEditText.getText().toString();
                if (TextUtils.isEmpty(url))
                {
                    return;
                }
                HttpUtils.doGet(HTTPActivity.this,url, new HttpUtils.HttpListener() {
                    @Override
                    public void onSucceed(String content) {
                        mTextViewMessage.append(content);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        mTextViewMessage.setText("请求失败！！！！！");
                        e.printStackTrace();
                    }
                });

            }
        });
    }


    private void initView() {
        mEditText = findViewById(R.id.input);
        mButtonSend = findViewById(R.id.send);
        mTextViewMessage = findViewById(R.id.message);
    }


}
