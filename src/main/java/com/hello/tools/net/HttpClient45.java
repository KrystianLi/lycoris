package com.hello.tools.net;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hello.tools.net.listener.FailedListener;
import com.hello.tools.net.listener.SuccessListener;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class HttpClient45 {

    public static boolean isfProxySetting = false;

    public static String ip = "127.0.0.1";
    public static int port = 8080;
    public static String scheme = "http";
    /**
     * GET请求
     * @throws IOException
     * @param url 请求路径
     * @param headers request请求头, 可以为null, 默认值agent
     * @param successListener 成功回调函数
     * @param failedListener 失败回调函数
     */
    public static void get(String url, Map<String,String> headers, SuccessListener successListener, FailedListener failedListener) throws IOException {
        CloseableHttpResponse response = null;
        try {
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            HttpGet httpGet = new HttpGet(url);
            //设置代理
            if (isfProxySetting){
                HttpHost proxy = new HttpHost(ip,port,scheme);
                RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
                httpGet.setConfig(config);
            }


            //设置request头
            if (headers == null){
                headers = new HashMap<>();
                headers.put("Pragma","no-cache");
                headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36 Edg/113.0.1774.42");
                headers.put("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
            }
            setHeader(httpGet,headers);
            //绕过ssl证书检测
            CloseableHttpClient httpClient = httpClientBuilder.setSSLSocketFactory(getSslConnectionSocketFactory()).build();
            //获取response
            response = httpClient.execute(httpGet);
            //HttpEntity.getEntity包含响应头和响应体
            HttpEntity entity = response.getEntity();
            //获取响应状态
            System.out.println(response.getStatusLine());
            if (entity != null){
                successListener.successListener(entity);
            }
            //将会释放所有由httpEntity所持有的资源
            EntityUtils.consume(entity);
        }catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
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
        //获取response
        CloseableHttpResponse response = null;
        try {
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            HttpPost httpPost = new HttpPost(url);

            //设置代理
            if (isfProxySetting){
                HttpHost proxy = new HttpHost(ip,port,scheme);
                RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
                httpPost.setConfig(config);
            }

            //设置request头
            setHeader(httpPost,headers);

            //绕过ssl证书检测
            CloseableHttpClient httpClient = httpClientBuilder.setSSLSocketFactory(getSslConnectionSocketFactory()).build();


            //设置请求体内容
            httpPost.setEntity(new StringEntity(params));
            //发送post请求
            response = httpClient.execute(httpPost);
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
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
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
        CloseableHttpResponse response = null;
        try {
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            HttpPost httpPost = new HttpPost(url);
            //设置代理
            if (isfProxySetting){
                HttpHost proxy = new HttpHost(ip,port,scheme);
                RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
                httpPost.setConfig(config);
            }
            //设置request头
            setHeader(httpPost,headers);
            //绕过ssl证书检测
            CloseableHttpClient httpClient = httpClientBuilder.setSSLSocketFactory(getSslConnectionSocketFactory()).build();

            //设置请求体内容
            StringEntity stringEntity = new StringEntity(JSON.toJSONString(params), "utf-8");
            httpPost.setEntity(stringEntity);
            //发送post请求
            response = httpClient.execute(httpPost);
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
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
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

    /**
     * 支持SSL
     *
     * @return SSLConnectionSocketFactory
     */
    private static SSLConnectionSocketFactory getSslConnectionSocketFactory() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (x509Certificates, s) -> true;
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        return new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
    }

    public static void main(String[] args) throws IOException {
        String url = "https://www.baidu.com";
        Map<String, String> headers = new HashMap<>();
        headers.put("Pragma","no-cache");
        headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36 Edg/113.0.1774.42");
        headers.put("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        get(url, headers, new SuccessListener() {
            @Override
            public void successListener(HttpEntity entity) {
                try {
                    String s = EntityUtils.toString(entity);
                    System.out.println(s);
                } catch (IOException e){
                    System.out.println(e.getMessage());
                }
            }
        }, new FailedListener() {
            @Override
            public void failedListener(Exception e) {

            }
        });

    }

}
