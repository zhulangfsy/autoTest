package com.api.utils;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil {
    /**
     * @param url    url地址
     * @param params 参数
     * @return 响应结果
     */
    public static String doPost(String url, Map<String, String> params) {
        String result = null;
        HttpPost post = new HttpPost(url);
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        //通过循环将参数保存到list集合
        for (String key : params.keySet()) {
            String value = params.get(key);
            parameters.add(new BasicNameValuePair(key, value));
        }
        try {
            post.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
            //发起请求，获取接口响应信息
            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(post);
            //获取状态码
            int statusCode = response.getStatusLine().getStatusCode();
            //获取响应报文，将json格式转换成字符串
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param url    url地址
     * @param params 参数
     * @return 响应结果
     */
    public static String doGet(String url, Map<String, String> params) {
        String result = null;
        CloseableHttpResponse response;
        //定义标志位，因为第一个参数用?拼接，后面参数用&拼接
        int mark = 1;
        StringBuilder urlBuilder = new StringBuilder(url);
        //拼接参数
        for (String key : params.keySet()) {
            if (mark == 1) {
                urlBuilder.append("?").append(key).append("=").append(params.get(key));
            } else {
                urlBuilder.append("&").append(key).append("=").append(params.get(key));
            }
            mark++;
        }
        HttpGet get = new HttpGet(String.valueOf(urlBuilder));
        //发起请求，获取接口响应信息
        CloseableHttpClient client = HttpClients.createDefault();

        try {
            response = client.execute(get);
            //获取状态码
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println(statusCode);
            //获取响应报文，将json格式转换成字符串
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String doService(String url, String type, Map<String, String> params) {
        String result = null;
        if ("post".equalsIgnoreCase(type)) {
            result = HttpUtil.doPost(url, params);
        } else {
            result = HttpUtil.doGet(url, params);
        }
        return result;
    }
}
