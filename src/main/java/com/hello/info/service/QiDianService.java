package com.hello.info.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.hello.info.controller.Controller;
import com.hello.info.model.CompanyTableModel;
import com.hello.strategy.ExpStrategy;
import com.hello.tools.MyExecutor;
import com.hello.tools.net.HttpClient45;
import com.hello.tools.net.listener.FailedListener;
import com.hello.tools.net.listener.SuccessListener;
import javafx.application.Platform;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

public class QiDianService extends ExpStrategy {

    private final String BASE_NAME_URL = "https://holmes.taobao.com";
    private final String BASE_NAME_DETAIL_URL = "https://dingtalk.com/qidian/company/";
    private final int pageSize = 10;

    private ThreadPoolExecutor executor;
    private CompletableFuture<Void> future;
    private HashMap<String, String> headerMap;


    public QiDianService() {
        headerMap = new HashMap<>();
        headerMap.put("Pragma","no-cache");
        headerMap.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36 Edg/113.0.1774.42");
        headerMap.put("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
    }

    /**
     * 公司名检索
     * @param keyWord   关键词
     * @return 不使用该参数
     */
    @Override
    public String searchCompanyName(String keyWord) {
        executor = MyExecutor.getExecutor();
        future = CompletableFuture.runAsync(() -> {
            try {
                getCompanyName(keyWord);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executor);

        future.thenRunAsync(() -> {
            // 任务执行完成后更新进度
            Platform.runLater(() -> Controller.getInstance().logCompanyNameArea.appendText("[+]企典: 查询公司名称, 搜索完成\n"));
        });
        return "";
    }

    /**
     * 根据关键词获取公司名
     * @param keyWord
     */
    private void getCompanyName(String keyWord){
        try{
            //cookie判断
            String text = Controller.getInstance().companyNameCookie.getText();
            if (!text.isEmpty()){
                headerMap.put("Cookie",text);
            }
            headerMap.put("Content-Type","application/json");
            headerMap.put("Origin","https://dingtalk.com");

            //body
            JSONObject params = new JSONObject();
            params.put("pageNo", 1);
            params.put("pageSize", pageSize);
            params.put("keyword", keyWord);
            params.put("orderByType", 5);

            //uri
            String uri = "/web/corp/customer/searchWithSummary";
            ArrayList<CompanyTableModel> companyTableModels = new ArrayList<>();
            StringBuilder totalSB = new StringBuilder();
            HttpClient45.post(BASE_NAME_URL + uri, headerMap, params, new SuccessListener() {
                @Override
                public void successListener(HttpEntity entity) throws IOException {
                    String s = EntityUtils.toString(entity);
                    JSONObject jsonObject = JSONObject.parseObject(s);
                    totalSB.append(jsonObject.getJSONObject("data").get("total").toString());
                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("data");
                    if (!jsonArray.isEmpty()){
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String companyName = jsonObject1.get("companyName").toString();
                            String registerDate = jsonObject1.get("registerDate").toString();
                            String email = jsonObject1.get("email").toString();
                            String bizStatus = jsonObject1.get("bizStatus").toString();
                            String ocid = jsonObject1.get("ocid").toString();
                            CompanyTableModel companyTableModel = new CompanyTableModel(companyTableModels.size(),companyName,registerDate,email,"",bizStatus,BASE_NAME_DETAIL_URL+ocid);
                            //更新数据表格
                            Platform.runLater(()-> Controller.getInstance().getCompanyTable().getItems().add(companyTableModel));
                            companyTableModels.add(companyTableModel);
                        }

                    }
                }
            }, new FailedListener() {
                @Override
                public void failedListener(Exception e) {
                    //暂时不做处理
                }
            });

            int total = Integer.parseInt(totalSB.toString());
            Platform.runLater(() ->Controller.getInstance().logCompanyNameArea.appendText("[+]企典: 查询公司名称, 共计搜索"+total+"条\n"));

            for (int i = 2; i <= total/pageSize + 1; i++) {
                params.put("pageNo",i);
                int finalI = i;
                Platform.runLater(()->Controller.getInstance().logCompanyNameArea.appendText("[+]天眼查: 正在访问: "+ finalI +"/"+total+"\n"));
                HttpClient45.post(BASE_NAME_URL + uri, headerMap, params, new SuccessListener() {
                    @Override
                    public void successListener(HttpEntity entity) throws IOException {
                        String s = EntityUtils.toString(entity);
                        JSONObject jsonObject = JSONObject.parseObject(s);
                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("data");
                        if (!jsonArray.isEmpty()){
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String companyName = jsonObject1.get("companyName").toString();
                                String registerDate = jsonObject1.get("registerDate").toString();
                                String email = jsonObject1.get("email").toString();
                                String bizStatus = jsonObject1.get("bizStatus").toString();
                                String ocid = jsonObject1.get("ocid").toString();
                                CompanyTableModel companyTableModel = new CompanyTableModel(companyTableModels.size(),companyName,registerDate,email,"",bizStatus,BASE_NAME_DETAIL_URL+ocid);
                                //更新数据表格
                                Platform.runLater(()-> Controller.getInstance().getCompanyTable().getItems().add(companyTableModel));
                                companyTableModels.add(companyTableModel);
                            }
                        }
                    }
                }, new FailedListener() {
                    @Override
                    public void failedListener(Exception e) {
                        //暂时不做处理
                    }
                });
                //加入延迟，一是避免速度过快，二是这里需要用sleep异常来中断整个异步任务
                Thread.sleep(100);
            }

        }catch (InterruptedException | IOException e){
            Platform.runLater(()->Controller.getInstance().logCompanyNameArea.appendText("[-]任务已终止\n"));
        }
    }
    @Override
    public String searchCompanyBeian(String keyWord) {
        Platform.runLater(() -> Controller.getInstance().logCompanyBeianArea.appendText("[+]企典: 没有此功能, 搜索完成\n"));
        return "";
    }

    @Override
    public Boolean stop() {
        if (!future.isDone()){
            executor.shutdownNow();
            return true;
        }else {
            return false;
        }
    }
}
