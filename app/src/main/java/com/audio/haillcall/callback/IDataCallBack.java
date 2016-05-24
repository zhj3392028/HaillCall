package com.audio.haillcall.callback;

import java.util.List;

/**
 * Created by Time on 16/5/23.
 */
public interface IDataCallBack {
    public void onNewData(List data);

    public void onError(String msg, int code);
}
