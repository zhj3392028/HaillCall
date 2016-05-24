package com.audio.haillcall.socketconn;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.audio.haillcall.utils.Constant;
import com.audio.haillcall.utils.NetUtil;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;

/**
 * Created by hj on 2016/5/23.
 */
public class InternetService extends Service implements Runnable {

    private Socket socket;

    private BufferedReader reader;//

    private PrintWriter writer;//

    private Binder binder;

    private Thread td;// 线程，获取服务器端发送来的消息

    private String workStatus;// 当前工作状况，null表示正在处理，success表示处理成功，failure表示处理失败

    private String currAction; //标记当前请求头信息，在获取服务器端反馈的数据后，进行验证，以免出现反馈信息和当前请求不一致问题。比如现在发送第二个请求，但服务器端此时才响应第一个请求

    /**
     * 向服务器发送请求
     *
     * @param action
     */
    public void sendRequest(String action) {
        try {
            workStatus = null;
            JSONObject json = new JSONObject();
            json.put("action", action);
            currAction = action;
            sendMessage(json);
        } catch (Exception ex) {
            workStatus = Constant.TAG_FAILURE;
            ex.printStackTrace();
        }
    }

    /**
     * 返回当前workStatus的值
     * /
     * public StringgetWorkStatus()
     * {
     * return workStatus ;
     * }
     * <p/>
     * /**
     * 处理服务器端反馈的数据
     *
     * @param json
     */
    private void dealUploadSuperviseTask(JSONObject json) {
        try {
            workStatus = json.getString("result");

            getXmlData();

        } catch (Exception ex) {
            ex.printStackTrace();
            workStatus = Constant.TAG_FAILURE;
        }
    }

    //得到服务器的xml数据
    public LinkedHashMap<String, String> getXmlData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        // 获取xml解析器
        XmlPullParser parser = null;

        try {
            parser = XmlPullParserFactory.newInstance()
                    .newPullParser();
            InputStream instream = socket.getInputStream();
            parser.setInput(instream, "UTF-8");
            int type = parser.getEventType();
            //开始解析xml文件
            while (type != XmlPullParser.END_DOCUMENT) {
                if (type == XmlPullParser.START_TAG) {
                    // 获取开始标签
                    if (parser.getName().equals("书名")) {
                        //获取节点的值
                        map.put("书名", parser.nextText());
                    }
                    if (parser.getName().equals("价格")) {
                        map.put("价格", parser.nextText());
                    }
                    if (parser.getName().equals("作者")) {
                        map.put("作者", parser.nextText());
                    }
                    if (parser.getName().equals("性别")) {
                        map.put("性别", parser.nextText());
                    }
                    if (parser.getName().equals("年龄")) {
                        map.put("年龄", parser.nextText());
                    }
                }
                type = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * 退出程序时，关闭Socket连接
     */
    public void closeConnection() {

        JSONObject json = new JSONObject();// 向服务器端发送断开连接请求
        try {
            json.put("action", "exit");
            sendMessage(json);// 向服务器端发送断开连接请求
            Log.v("qlq", "the request is " + json.toString());
        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    /**
     * 连接服务器
     */
    private void connectService() {
        try {
            socket = new Socket();
            SocketAddress socAddress = new InetSocketAddress(UrlUtils.BASE_URL, UrlUtils.PORT);
            socket.connect(socAddress, 3000);

            reader = new BufferedReader(new InputStreamReader(
                    socket.getInputStream(), "GBK"));

            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream(), "GBK")), true);

        } catch (SocketException ex) {
            Log.v("QLQ", "socketException ");
            ex.printStackTrace();
            workStatus = Constant.TAG_CONNECTFAILURE;// 如果是网络连接出错了，则提示网络连接错误
            return;
        } catch (SocketTimeoutException ex) {
            ex.printStackTrace();
            workStatus = Constant.TAG_CONNECTFAILURE;// 如果是网络连接出错了，则提示网络连接错误
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            workStatus = Constant.TAG_CONNECTFAILURE;// 如果是网络连接出错了，则提示网络连接错误
            return;
        }
    }

    /**
     * 向服务器发送传入的JSON数据信息
     *
     * @param json
     */
    private void sendMessage(JSONObject json) {
        if (!NetUtil.isConn(InternetService.this))// 如果当前网络连接不可用,直接提示网络连接不可用，并退出执行。
        {
            Log.v("QLQ", "workStatus is not connected!111");
            workStatus = Constant.TAG_CONNECTFAILURE;
            return;
        }
        if (socket == null)// 如果未连接到服务器，创建连接
            connectService();
        if (!InternetService.this.td.isAlive())// 如果当前线程没有处于存活状态，重启线程
            (td = new Thread(InternetService.this)).start();
        if (!socket.isConnected() || (socket.isClosed())) // isConnected（）返回的是是否曾经连接过，isClosed()返回是否处于关闭状态，只有当isConnected（）返回true，isClosed（）返回false的时候，网络处于连接状态
        {
            Log.v("QLQ", "workStatus is not connected!111222");
            for (int i = 0; i < 3 && workStatus == null; i++) {// 如果连接处于关闭状态，重试三次，如果连接正常了，跳出循环
                socket = null;
                connectService();
                if (socket.isConnected() && (!socket.isClosed())) {
                    Log.v("QLQ", "workStatus is not connected!11333");
                    break;
                }
            }
            if (!socket.isConnected() || (socket.isClosed()))// 如果此时连接还是不正常，提示错误，并跳出循环
            {
                workStatus = Constant.TAG_CONNECTFAILURE;
                Log.v("QLQ", "workStatus is not connected!111444");
                return;
            }

        }

        if (!socket.isOutputShutdown()) {// 输入输出流是否关闭
            try {
                writer.println(json.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.v("QLQ", "workStatus is not connected!55555666666");
                e.printStackTrace();
                workStatus = Constant.TAG_FAILURE;
            }
        } else {
            workStatus = Constant.TAG_CONNECTFAILURE;
        }
    }

    /**
     * 处理服务器端传来的消息，并通过action头信息判断，传递给相应处理方法
     *
     * @param str
     */
    private void getMessage(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String action = json.getString("action");// 提取JSON的action信息，获取当前JSON响应的是哪个操作。
            if (!currAction.equals(action))
                return;
            if (action.equals("getCategory")) {
                dealUploadSuperviseTask(json);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            workStatus = Constant.TAG_FAILURE;
        }
    }


    public class InterBinder extends Binder {

        public InternetService getService() {
            return InternetService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        binder = new InterBinder();
        td = new Thread(InternetService.this);// 启动线程
        td.start();

        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // connectService();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("QLQ", "Service is on destroy");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v("QLQ", "service on onUnbind");
        return super.onUnbind(intent);
    }

    /**
     * 循环，接收从服务器端传来的数据
     */
    public void run() {
        try {
            while (true) {
                Thread.sleep(500);// 休眠0.5s
                if (socket != null && !socket.isClosed()) {// 如果socket没有被关闭
                    if (socket.isConnected()) {// 判断socket是否连接成功
                        if (!socket.isInputShutdown()) {
                            String content;
                            if ((content = reader.readLine()) != null) {
                                getMessage(content);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {

            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            workStatus = Constant.TAG_CONNECTFAILURE;// 如果出现异常，提示网络连接出现问题。
            ex.printStackTrace();
        }
    }

    //断线重连
    public void resetSocket() {
        while (InternetService.isServerClose(socket)) {
            try {
                socket = new Socket(UrlUtils.BASE_URL, UrlUtils.PORT);
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            } catch (IOException e) {
                System.out.println("正在重连....");
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }

        }
    }

    /**
     * 判断是否断开连接，断开返回true,没有返回false
     *
     * @param socket
     * @return
     */
    public static Boolean isServerClose(Socket socket) {
        try {
            socket.sendUrgentData(0);//发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
            return false;
        } catch (Exception se) {
            return true;
        }
    }

}
