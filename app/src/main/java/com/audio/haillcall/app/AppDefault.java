package com.audio.haillcall.app;

import com.foodmall.android.merchant.domain.imservice.event.LoginEvent;

import utils.netstatus.NetUtils;

public class AppDefault {

    public static final String MY_PKG_NAME = "com.cagtc.app";  //包名
    public static int DEF_USER_ID = 0;	// 用户id
    //public static int DEF_USER_ID = 10102;	// 用户id
    public static String DEF_USER_SHOP_ID = "";	// 用户商城id
    //public static String DEF_USER_SHOP_ID = "687163748795813888";	// 用户商城id
    public static final int DEF_ID = 0;
    public static String USER_HEAD = "";

    public static int DEF_SALE_ID = 0;	 // 商家id

    public static int TEST_TARGET_ID = 0;

    public static String ONLINE_STATE = "0"; // web商城登录状态 0:未登录 1:登录
//    public static LoginEvent IMLOGINSTATE = LoginEvent.NONE; // IM登录状态,区分断线和被踢服务端主动断开连接
//    public static NetUtils.NetType IMNETSTATE = NetUtils.NetType.NONE; // 当前网络状态

    public static int AUTO_FLAG = 0;

    public static int CHAT_LIST_COUNT_MAX = 20;  //每次获取聊天记录最大数

    public static String TESTUSERNAME = "";
    public static String TESTPWD = "";

    public static final int MAX_SELECT_IMAGE_COUNT = 6; //图片最多选择数

    public static final String REMOTE_URL_TEST = "http://120.35.35.189:8081/imall_mobile";  //测试商城地址
    public static final String REMOTE_URL = "http://m.foodmall.com/mobile";  //生产商城地址
    public static final String DEF_HEAD ="http://192.168.140.115:8080/group1/M00/00/A3/wKiMdFbqfrWAajjoAAAQo5ZOU9k137.png";//默认头像地址


}
