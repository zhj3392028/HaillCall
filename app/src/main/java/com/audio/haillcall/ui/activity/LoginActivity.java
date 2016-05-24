package com.audio.haillcall.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.audio.haillcall.R;
import com.audio.haillcall.app.BaseActivity;
import com.audio.haillcall.socketconn.SocketService;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {



    // UI references.
    private boolean initial;
    private EditText acount;
    private EditText pwd;
    private ProgressBar progressBar;
    private Button signIn;
    private BufferedInputStream bufferedInputStream;
    private BufferedOutputStream bufferedOutputStream;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = new Intent(this, SocketService.class);
        startService(intent);
        initView();
    }

    private void initView() {
        signIn = (Button) findViewById(R.id.sign_in);
        signIn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (initial == false) {        //在这里直接用Service中的socket
                    try {
                        bufferedInputStream = new BufferedInputStream(SocketService.socket.getInputStream());
                        bufferedOutputStream = new BufferedOutputStream(SocketService.socket.getOutputStream());
                        OutputStream out = SocketService.socket.getOutputStream();
                        PrintStream ps = new PrintStream(out);
                        ps.write("good".getBytes());
                        initial=true;
                        //解析过程
                        // TODO: 2016/5/22  发送消息
                        Message msg = handler.obtainMessage();
                        msg.obj = "";
                        // 通过handler对象在主线程中调用handleMessage(Message msg)函数
                        handler.sendMessage(msg);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

