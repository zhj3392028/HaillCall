package com.audio.haillcall.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hj on 2016/5/22.
 */
public class LoginModel implements Serializable{
    private static final long serialVersionUID = 1L;
    public String id;
    public String type;
    public List dataList;//多种类型数据，一般是List集合，比如获取所有员工列表
}
