package com.audio.haillcall.socketconn;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.net.Socket;

/**
 * Created by hj on 2016/5/22.
 */
public class SocketService extends Service {
    private static 	final String TAG = "MyService";
    private boolean isConnect=false;
    public 	static Socket socket; //这里定义了一个静态socket 供其他activity使用

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        new Thread() {
            @Override
            public void run() {
                if(isConnect==false)
                    initSocket();
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }
    //初始化socket
    private void initSocket() {
        try {
            Log.v(TAG, "iniSocket");
            socket = new Socket("192.168.1.90", 8008);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
