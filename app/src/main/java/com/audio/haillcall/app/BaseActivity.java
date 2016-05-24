package com.audio.haillcall.app;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.audio.haillcall.callback.IDataCallBack;
import com.audio.haillcall.callback.IHandler;
import com.audio.haillcall.callback.IResult;
import com.audio.haillcall.handler.UIHandler;
import com.audio.haillcall.model.LoginModel;
import com.audio.haillcall.socketconn.InternetService;
import com.audio.haillcall.utils.Constant;

import java.util.LinkedHashMap;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {
    InternetService innetService;
    LinkedHashMap<String, String> result;
    protected static UIHandler handler = new UIHandler(Looper.getMainLooper());
    // 数据回调接口，都传递T的子类实体
    protected IDataCallBack dataCallBack;

    public BaseActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHandler();
        bindService(new Intent(BaseActivity.this, InternetService.class),
                internetServiceConnection, Context.BIND_AUTO_CREATE);//BasicActivity是我自己为所有Activity定义的基类，这段代码也是写在BasicActivity里。
    }

    public ServiceConnection internetServiceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName arg0, IBinder service) {
            innetService = ((InternetService.InterBinder) service).getService();
            result = innetService.getXmlData();
        }

        public void onServiceDisconnected(ComponentName arg0) {

            innetService = null;
        }

    };

    private void setHandler() {
        handler.setHandler(new IHandler() {
            public void handleMessage(Message msg) {
                handler(msg);//有消息就提交给子类实现的方法
            }
        });
    }

    //让子类处理消息
    protected abstract void handler(Message msg);

    // 初始化UI，setContentView等
    protected void initContentView(Bundle savedInstanceState) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 你可以添加多个Action捕获
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_NETWORK_CHANGE);
        filter.addAction(Constant.ACTION_PUSH_DATA);
        filter.addAction(Constant.ACTION_NEW_VERSION);
        registerReceiver(receiver, filter); //还可能发送统计数据，比如第三方的SDK 做统计需求
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver); //还可能发送统计数据，比如第三方的SDK 做统计需求
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) { // 处理各种情况
            String action = intent.getAction();
            if (Constant.ACTION_NETWORK_CHANGE.equals(action)) {
                //网络发生变化 // 处理网络问题 }
            } else if (Constant.ACTION_PUSH_DATA.equals(action)) {
                //可能有新数据
                Bundle b = intent.getExtras();
                List mdata = (List) b.get("data");
                if (dataCallBack != null) {
                    //成功回调数据通知
                    dataCallBack.onNewData(mdata);
                }
            } else if (Constant.ACTION_NEW_VERSION.equals(action)) { // 可能发现新版本 // VersionDialog 可能是版本提示是否需要下载的对话框
            }
        }
    };

    //设置结果回调
    public void setDataCallback(IDataCallBack resultCallback) {
        this.dataCallBack = resultCallback;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(internetServiceConnection);
    }

}
