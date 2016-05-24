package com.audio.haillcall.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.audio.haillcall.socketconn.UrlUtils;
import com.audio.haillcall.utils.CollectsUtil;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by hj on 2016/5/22.
 */
public class APP extends Application {
    public static Context context;
    private static APP instance;
    private ArrayList<Activity> activityLst;
    public static int userId = AppDefault.DEF_USER_ID;

    private static Socket socket;

    public static Socket getSocket(){
        if (socket==null){
            try {
                socket = new Socket(UrlUtils.BASE_URL,UrlUtils.PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return socket;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        // socket连接初始化
        startIMService();
    }
    private void startIMService() {
        Intent intent = new Intent();
        intent.setClass(this, IMService.class);
        startService(intent);
    }

    /**
     * 获取Application
     */
    public static APP getInstance() {
        if (instance == null) {
            instance = new APP();
        }
        return instance;
    }

    /**
     * 添加当前Activity 到列表中
     */
    public void addActivity(Activity activity) {
        if (activityLst == null) {
            activityLst = new ArrayList<Activity>();
        }
        activityLst.add(activity);
    }

    /**
     * 清空activity列表
     */
    public void clearActivity() {
        if (CollectsUtil.isNotEmpty(activityLst)) {
            activityLst.clear();
        }
    }


    /**
     * 遍历退出所有Activity
     */
    public void exit() {
        if (CollectsUtil.isNotEmpty(activityLst)) {
            for (Activity activity : activityLst) {
                activity.finish();
            }
            clearActivity();
        }
        System.exit(0);
    }

}
