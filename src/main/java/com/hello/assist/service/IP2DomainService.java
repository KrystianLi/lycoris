package com.hello.assist.service;

import com.hello.assist.controller.Controller;
import com.hello.assist.model.IP2DomainTableModel;
import com.hello.info.controller.DataAggregateController;
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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class IP2DomainService extends ExpStrategy {
    private ThreadPoolExecutor executor;
    private CompletableFuture<Void> future;

    private List<IP2DomainTableModel> ip2DomainTableModels = new ArrayList<>();

    private AtomicInteger ip2DomainID = new AtomicInteger(0);



    private final String BASE_URL = "https://www.dnsgrep.cn/ip/";

    @Override
    public void ip2domainSearch(String ipList) {
        executor = MyExecutor.getExecutor();
        List<String> tempList = Arrays.stream(ipList.split("\n")).collect(Collectors.toList());
        List<String> result = tempList.stream()
                .map(s -> BASE_URL + s)
                .collect(Collectors.toList());
        future = CompletableFuture.runAsync(() -> {
            try {
                parseIP2Domain(result);
            } catch (Exception e) {
                Platform.runLater(() -> Controller.getInstance().IP2DomainLogTextArea.appendText("[-]任务已终止\n"));
                throw new RuntimeException(e);
            }
        }, executor);
    }

    private void parseIP2Domain(List<String> ipList) throws IOException, InterruptedException {
        for (String url : ipList) {
            Platform.runLater(() -> Controller.getInstance().IP2DomainLogTextArea.appendText("[+]正在请求: "+url+"\n"));
            Thread.sleep(200);
            HttpClient45.get(url, null, new SuccessListener() {
                @Override
                public void successListener(HttpEntity entity) throws IOException {
                    String string = EntityUtils.toString(entity);
                    Document doc = Jsoup.parse(string);
                    Elements table = doc.select("table");
                    if (!table.isEmpty()){
                        Elements trs = table.select("tr");
                        for (Element tr : trs) {
                            Elements tds = tr.select("td");
                            if (!tds.isEmpty()){
                                String domain = tr.select("td").get(0).getElementsByAttribute("data").text();
                                String ip = tr.select("td").get(1).getElementsByAttribute("data").text();
                                //更新数据
                                IP2DomainTableModel ip2DomainTableModel = new IP2DomainTableModel(ip2DomainID.incrementAndGet(), ip, domain);
                                Platform.runLater(()-> Controller.getInstance().IP2DomainTable.getItems().add(ip2DomainTableModel));
                                ip2DomainTableModels.add(ip2DomainTableModel);
                            }
                        }
                    }
                }
            }, new FailedListener() {
                @Override
                public void failedListener(Exception e) {
                    Platform.runLater(() -> Controller.getInstance().IP2DomainLogTextArea.appendText("[-]请求失败: "+url+"\n"));
                }
            });
        }
    }

    /**
     * 通过domain反查ip确定ip解析到的域名是否正确
     * @param ip
     * @param domain
     * @return  正确即返回，无法解析就返回空
     */
    private String verifyDomain(String ip,String domain){

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
