package com.jlu.etutor.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jlu.etutor.InitApplication;
import com.jlu.etutor.activity.LoginActivity;
import com.jlu.etutor.gson.BaseResult;
import com.jlu.etutor.gson.HuanXinTokenBean;
import com.jlu.etutor.gson.LoginResult;
import com.jlu.etutor.gson.TeaInfoListResult;
import com.jlu.etutor.gson.TeacherInfo;
import com.jlu.etutor.gson.UserInfo;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.vondear.rxtools.RxConstTool;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 医我一生 on 2018/1/31.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class Server {
    public static long mills = 0;

    private static String IPV4 = "123.207.236.80";

    private static String HOST = "8080";

    private static String URL = "http://" + IPV4 + ":" + HOST + "/Server/";

    public Server() {
        throw new RuntimeException("util class can not be instance!");
    }

    /**
     * @param handler handler
     * @param info    用户名
     * @param pwd     密码
     * @return userInfo
     */
    public static UserInfo login(final Handler handler, String info, String pwd) {
        if (!isNetworkAvailable()) {
            handler.post(new UpdateUITools("网络无连接，检查您的网络设置！"));
            return null;
        }
        UserInfo userInfo = null;
        TeacherInfo teacherInfo = null;
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
        RequestBody body = new FormBody.Builder().add("info", info).add("pwd", pwd).build();
        Request request = new Request.Builder().url(URL + "login").post(body).build();
        try {
            Response response = client.newCall(request).execute();
            LoginResult result = new Gson().fromJson(response.body().string(), LoginResult.class);
            if (result.getCode() == 0) {
                userInfo = result.getUserInfo();
                teacherInfo = result.getTeaInfo();
                InitApplication.setTeaInfoList(getTeaInfoList(handler));
                EMClient.getInstance().login(userInfo.getPhone(), userInfo.getPwd(), new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        handler.post(new UpdateUITools("登陆环信聊天服务器成功！"));
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {
                        handler.post(new UpdateUITools("登陆环信聊天服务器失败，稍后将会尝试重新连接"));
                    }
                });
            } else {
                handler.post(new UpdateUITools("用户名或密码错误"));
            }
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException)
                handler.post(new UpdateUITools("连接服务器超时"));
            else if (e instanceof ConnectException)
                handler.post(new UpdateUITools("无法连接到服务器"));

        } catch (JsonSyntaxException e) {
            handler.post(new UpdateUITools("服务器发生错误，请联系客服"));
        }
        InitApplication.setUserInfo(userInfo);
        InitApplication.setTeacherInfo(teacherInfo);
        return userInfo;
    }

    public static String getUserInfo(final Handler handler, String phone) {
        if (!isNetworkAvailable()) {
            handler.post(new UpdateUITools("网络无连接，检查您的网络设置！"));
            return null;
        }
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
        RequestBody body = new FormBody.Builder().add("info", phone).build();
        Request request = new Request.Builder().url(URL + "getUserInfo").post(body).build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException)
                handler.post(new UpdateUITools("服务器开小差了，等会再试吧"));
            else if (e instanceof ConnectException)
                handler.post(new UpdateUITools("无法连接到服务器"));

        } catch (JsonSyntaxException e) {
            handler.post(new UpdateUITools("服务器发生错误，请联系客服"));
        }
        return null;
    }

    public static boolean register(final Handler handler, UserInfo info) {

        try {
            EMClient.getInstance().createAccount(info.getPhone(), info.getPwd());
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
            RequestBody body = new FormBody.Builder().add("info.name", info.getName()).add("info.phone", info.getPhone())
                    .add("info.pwd", info.getPwd()).add("info.time", info.getTime()).add("info.type", String.valueOf(info.getType())).build();
            Request request = new Request.Builder().url(URL + "register").post(body).build();
            Response response = client.newCall(request).execute();
            BaseResult baseResult = new Gson().fromJson(response.body().string(), BaseResult.class);
            if (baseResult.getCode() == 0) {
                InitApplication.setUserInfo(info);
                return true;
            }
        } catch (HyphenateException e) {
            handler.post(new UpdateUITools("注册环信账号失败！"));
            e.printStackTrace();
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException)
                handler.post(new UpdateUITools("连接服务器超时"));
            else if (e instanceof ConnectException)
                handler.post(new UpdateUITools("无法连接到服务器"));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            handler.post(new UpdateUITools("服务器发生错误，请联系客服"));
        }

        return false;
    }

    public static boolean checkInfoExist(final Handler handler, String info, boolean option) {
        if (!isNetworkAvailable()) {
            handler.post(new UpdateUITools("网络无连接，检查您的网络设置！"));
            return false;
        }
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
        RequestBody body = new FormBody.Builder().add("info", info).build();
        Request request = new Request.Builder().url(URL + "check").post(body).build();
        try {
            Response response = client.newCall(request).execute();
            BaseResult baseResult = new Gson().fromJson(response.body().string(), BaseResult.class);
            System.out.println(baseResult.getStatus());
            if (baseResult.getCode() == 0)
                return true;
            else {
                if (option)
                    if (isPhoneNum(info))
                        handler.post(new UpdateUITools("电话号码已注册！"));
                    else
                        handler.post(new UpdateUITools("此昵称已被使用！"));

            }
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException)
                handler.post(new UpdateUITools("连接服务器超时"));
            else if (e instanceof ConnectException)
                handler.post(new UpdateUITools("无法连接到服务器"));

        } catch (JsonSyntaxException e) {
            handler.post(new UpdateUITools("服务器发生错误，请联系客服"));
        }
        return false;
    }

    public static void uploadFile(final Handler handler, final String path, final Activity activity, final ImageView imageView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String BOUNDARY = UUID.randomUUID().toString(); //边界标识 随机生成
                String PREFIX = "--", LINE_END = "\r\n";
                String CONTENT_TYPE = "multipart/form-data"; //内容类型
                File file = new File(path);
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(URL + "upLoadFile?filename=" + file.getName()).openConnection();
                    conn.setReadTimeout(3000);
                    conn.setConnectTimeout(3000);
                    conn.setDoInput(true); //允许输入流
                    conn.setDoOutput(true); //允许输出流
                    conn.setUseCaches(false); //不允许使用缓存
                    conn.setRequestMethod("POST"); //请求方式
                    conn.setRequestProperty("Charset", "utf-8");
                    //设置编码
                    conn.setRequestProperty("connection", "keep-alive");
                    conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
                    if (file != null) {
                /* 当文件不为空，把文件包装并且上传 */
                        OutputStream outputSteam = conn.getOutputStream();
                        DataOutputStream dos = new DataOutputStream(outputSteam);
                        String sb = PREFIX +
                                BOUNDARY +
                                LINE_END +
                                "Content-Disposition: form-data; name=\"img\"; filename=\"" + file.getName() + "\"" + LINE_END +
                                "Content-Type: application/octet-stream; charset=" + "utf-8" + LINE_END +
                                LINE_END;
                /*
                 * 这里重点注意：
                 * name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */
                        dos.write(sb.getBytes());
                        InputStream is = new FileInputStream(file);
                        byte[] bytes = new byte[1024];
                        int len;
                        while ((len = is.read(bytes)) != -1) {
                            dos.write(bytes, 0, len);
                        }
                        is.close();
                        dos.write(LINE_END.getBytes());
                        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                        dos.write(end_data);
                        dos.flush();
                /*
                 * 获取响应码 200=成功
                 * 当响应成功，获取响应的流
                 */
                        int res = conn.getResponseCode();
                        if (res != 200)
                            handler.post(new UpdateUITools("上传头像到服务器失败，要不再试一次？"));
                        else
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Glide.with(activity).load(Server.getURL() + "image/" + InitApplication.getUserInfo()
                                            .getPhone()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                                }
                            });

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    handler.post(new UpdateUITools("上传头像到服务器失败，要不再试一次？"));
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.post(new UpdateUITools("上传头像到服务器失败，要不再试一次？"));
                }
            }
        }).start();


    }

    public static void updateUserInfo(final Handler handler, UserInfo info) {
        if (!isNetworkAvailable()) {
            handler.post(new UpdateUITools("网络无连接，检查您的网络设置！"));
            return;
        }
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
        RequestBody body = new FormBody.Builder()
                .add("info.name", info.getName())
                .add("info.phone", info.getPhone())
                .add("info.type", "" + info.getType())
                .add("info.pwd", info.getPwd())
                .add("info.time", info.getTime())
                .add("info.email", info.getEmail())
                .add("info.tag", info.getTag()).build();
        Request request = new Request.Builder().url(URL + "updateUserInfo").post(body).build();
        try {
            Response response = client.newCall(request).execute();
            BaseResult baseResult = new Gson().fromJson(response.body().string(), BaseResult.class);
            if (baseResult.getCode() != 0)
                handler.post(new UpdateUITools("保存资料失败，请稍后再试"));

        } catch (IOException e) {
            if (e instanceof SocketTimeoutException)
                handler.post(new UpdateUITools("连接服务器超时"));
            else if (e instanceof ConnectException)
                handler.post(new UpdateUITools("无法连接到服务器"));

        } catch (JsonSyntaxException e) {
            handler.post(new UpdateUITools("服务器发生错误，请联系客服"));
        }
    }

    public static void updateTeaInfo(final Handler handler, TeacherInfo info) {
        if (!isNetworkAvailable()) {
            handler.post(new UpdateUITools("网络无连接，检查您的网络设置！"));
            return;
        }
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
        RequestBody body = new FormBody.Builder()
                .add("info.phone", info.getPhone())
                .add("info.college", info.getCollege())
                .add("info.major", info.getMajor())
                .add("info.score", "" + info.getScore())
                .add("info.tag", info.getTag())
                .add("info.salary", info.getSalary())
                .add("info.trueName", info.getTrueName())
                .add("info.time", info.getTime())
                .add("info.sex", "" + info.getSex())
                .add("info.introduction", info.getIntroduction())
                .add("info.name", info.getName()).build();
        Request request = new Request.Builder().url(URL + "updateTeaInfo").post(body).build();
        try {
            Response response = client.newCall(request).execute();
            BaseResult baseResult = new Gson().fromJson(response.body().string(), BaseResult.class);
            if (baseResult.getCode() != 0)
                handler.post(new UpdateUITools("保存资料失败，请稍后再试"));

        } catch (IOException e) {
            if (e instanceof SocketTimeoutException)
                handler.post(new UpdateUITools("连接服务器超时"));
            else if (e instanceof ConnectException)
                handler.post(new UpdateUITools("无法连接到服务器"));

        } catch (JsonSyntaxException e) {
            handler.post(new UpdateUITools("服务器发生错误，请联系客服"));
        }
    }


    public static ArrayList<TeacherInfo> getTeaInfoList(Handler handler) {
        if (!isNetworkAvailable()) {
            handler.post(new UpdateUITools("网络无连接，检查您的网络设置！"));
            return null;
        }
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
        RequestBody body = new FormBody.Builder().build();
        Request request = new Request.Builder().url(URL + "getTeaInfoList").post(body).build();
        try {
            Response response = client.newCall(request).execute();
            TeaInfoListResult result = new Gson().fromJson(response.body().string(), TeaInfoListResult.class);
            ArrayList<TeacherInfo> res = result.getResult();
            InitApplication.setTeaInfoList(res);
            return res;

        } catch (IOException e) {
            if (e instanceof SocketTimeoutException)
                handler.post(new UpdateUITools("连接服务器超时"));
            else if (e instanceof ConnectException)
                handler.post(new UpdateUITools("无法连接到服务器"));
        } catch (JsonSyntaxException e) {
            handler.post(new UpdateUITools("服务器发生错误，请联系客服"));
        }
        return null;
    }

    public static void testConnection(final Handler handler) {
        if (!isNetworkAvailable()) {
            handler.post(new UpdateUITools("网络无连接，检查您的网络设置！"));
        }
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
        RequestBody body = new FormBody.Builder().build();
        Request request = new Request.Builder().url(URL + "welcome").post(body).build();
        try {
            Response response = client.newCall(request).execute();
            BaseResult result = new Gson().fromJson(response.body().string(), BaseResult.class);
            if (result.getCode() == 0)
                handler.post(new UpdateUITools("端口测试成功！"));
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException)
                handler.post(new UpdateUITools("连接服务器超时"));
            else if (e instanceof ConnectException)
                handler.post(new UpdateUITools("无法连接到服务器"));
        } catch (JsonSyntaxException e) {
            handler.post(new UpdateUITools("服务器发生错误，请联系客服"));
        }

    }

    public static boolean editPWD(Handler handler, String phone, String pwd) {
        if (!isNetworkAvailable()) {
            handler.post(new UpdateUITools("网络无连接，检查您的网络设置！"));
        }
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
        RequestBody body = new FormBody.Builder().add("phone", phone).add("pwd", pwd).build();
        Request request = new Request.Builder().url(URL + "editPWD").post(body).build();
        try {
            Response response = client.newCall(request).execute();
            BaseResult result = new Gson().fromJson(response.body().string(), BaseResult.class);
            if (result.getCode() == 0) {
                handler.post(new UpdateUITools("密码修改成功！"));
                editEasePWD(handler, phone, pwd);
                return true;
            }
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException)
                handler.post(new UpdateUITools("连接服务器超时"));
            else if (e instanceof ConnectException)
                handler.post(new UpdateUITools("无法连接到服务器"));
        } catch (JsonSyntaxException e) {
            handler.post(new UpdateUITools("服务器发生错误，请联系客服"));
        }
        return false;
    }

    private static void editEasePWD(Handler handler, String user, String pwd) throws IOException {
        HuanXinTokenBean bean = getToken();
        if (bean != null) {
            OkHttpClient client = new OkHttpClient();
            String baseUrl = "https://a1.easemob.com/1141170714178466/myapplicoation/users/" +
                    user + "/password";
            String gson = "{\"newpassword\" : \"" + pwd + "\"}";
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson);
            Request request = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + bean.getAccess_token())
                    .url(baseUrl)
                    .put(requestBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                System.out.print(response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(new UpdateUITools("修改环信聊天账号密码失败，请尽快联系客服进行手动修改，否则无法使用APP内聊天功能"));
            }
        } else {
            handler.post(new UpdateUITools("修改环信聊天账号密码失败，请尽快联系客服进行手动修改，否则无法使用APP内聊天功能"));
        }
    }

    private static HuanXinTokenBean getToken() throws IOException {
        OkHttpClient client = new OkHttpClient();
        String gson = "{\"grant_type\":\"client_credentials\",\"client_id\":\"YXA64KWMIG0mEeeZxw8WqwkByA\",\"client_secret\":\"YXA60qqDhqbmjA1j-JW8yrObYtvKAwY\"}";
        String baseUrl = "https://a1.easemob.com/1141170714178466/myapplicoation/token";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson);
        Request request = new Request.Builder()
                .header("content-type", "application/json")
                .post(requestBody)
                .url(baseUrl)
                .build();
        Response response = client.newCall(request).execute();
        return new Gson().fromJson(response.body().string(), HuanXinTokenBean.class);
    }

    private static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) InitApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo i : info) {
                    if (i.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isPhoneNum(String phone) {
        Pattern p = Pattern.compile(RxConstTool.REGEX_MOBILE_EXACT);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    public static boolean isEmail(String email) {
        Pattern p = Pattern.compile(RxConstTool.REGEX_EMAIL);
        Matcher m = p.matcher(email);
        return m.matches();
    }


    public static void setURL(String IPV4) {
        URL = "http://" + IPV4 + ":" + HOST + "/Server/";
    }

    public static String getURL() {
        return URL;
    }

    public static void logout(final Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("UserInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                activity.startActivity(new Intent(activity, LoginActivity.class));
                activity.finish();
            }

            @Override
            public void onError(int i, String s) {
                activity.finish();

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    public static String getIPV4() {
        return IPV4;
    }

    public static void setMills(Long v) {
        mills = v;
    }
}
