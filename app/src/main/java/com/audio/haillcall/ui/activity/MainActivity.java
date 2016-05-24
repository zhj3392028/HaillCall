package com.audio.haillcall.ui.activity;

import android.os.Bundle;
import android.os.Message;

import com.audio.haillcall.R;
import com.audio.haillcall.app.BaseActivity;
import com.audio.haillcall.callback.IDataCallBack;
import com.audio.haillcall.callback.IResult;

import java.util.List;

public class MainActivity extends BaseActivity implements IDataCallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void loadData() {
        setDataCallback(this);//设置回调函数 //我们还可以把这个Callback传给其它获取数据的类，比如HTTP获取数据
        // 比如 HttClient.get(url,this);
    }

    @Override
    protected void handler(Message msg) {

    }

    @Override
    public void onNewData(List data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //更新UI
            }
        });

        //或者
        handler.sendEmptyMessage(0);
    }

    @Override
    public void onError(String msg, int code) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //弹窗错误信息
            }
        });

        //或者
        handler.sendEmptyMessage(1);
    }
}
