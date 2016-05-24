package com.audio.haillcall.model;

import com.audio.haillcall.app.AppDefault;

/**
 * Created by Time on 16/5/23.
 */
public class MessageModel extends BaseListModel {

    @Override
    public int getLastId() {
        return AppDefault.DEF_ID;
    }
}
