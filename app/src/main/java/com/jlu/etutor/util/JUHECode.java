package com.jlu.etutor.util;

import android.os.Handler;

import com.google.gson.Gson;
import com.jlu.etutor.gson.CodeResult;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 程杰 on 2018/3/2.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class JUHECode {
    private static final String DEF_CHATSET = "UTF-8";
    private static final int DEF_CONN_TIMEOUT = 30000;
    private static final int DEF_READ_TIMEOUT = 30000;

    //配置您申请的KEY
    private static final String APPKEY = "ca2eefaf4bae008c9a2188beb5e54bb6";


    //2.发送短信
    public static boolean sendCode(Handler handler,String phone, String code) {
        String result;
        String url = "http://v.juhe.cn/sms/send";//请求接口地址
        Map<String, Object> params = new HashMap<>();//请求参数
        params.put("mobile", phone);//接收短信的手机号码
        params.put("tpl_id", "65267");//短信模板ID，请参考个人中心短信模板设置
        params.put("tpl_value", code);//变量名和变量值对。如果你的变量名或者变量值中带有#&=中的任意一个特殊符号，请先分别进行urlencode编码后再传递，<a href="http://www.juhe.cn/news/index/id/50" target="_blank">详细说明></a>
        params.put("key", APPKEY);//应用APPKEY(应用详细页查询)

        try {
            result = net(url, params);
            CodeResult res = new Gson().fromJson(result, CodeResult.class);
            if (res.getError_code() == 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler.post(new UpdateUITools("验证码发送失败，请稍后再试"));
        return false;
    }

    private static String net(String strUrl, Map<String, Object> params) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuilder sb = new StringBuilder();
            strUrl = strUrl + "?" + urlencode(params);
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params != null && "GET".equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead ;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    private static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
