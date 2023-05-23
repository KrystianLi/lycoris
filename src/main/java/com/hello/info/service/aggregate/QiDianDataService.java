package com.hello.info.service.aggregate;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.hello.info.controller.Controller;
import com.hello.info.controller.DataAggregateController;
import com.hello.info.model.CompanyBranchTableModel;
import com.hello.info.model.CompanyInvestTableModel;
import com.hello.info.model.CompanyStockTableModel;
import com.hello.tools.MyExecutor;
import com.hello.tools.net.HttpClient45;
import com.hello.tools.net.listener.FailedListener;
import com.hello.tools.net.listener.SuccessListener;
import javafx.application.Platform;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class QiDianDataService {

    private final String BASEURL = "https://holmes.taobao.com";
    private final String CompanyDetailCardBatch = "/web/detail/companyDetailCardBatch";
    private final String companyDetailCard = "/web/detail/companyDetailCard";
    private final String branchID = "485";
    private final String stockID = "484";
    private final String investID = "453";
    private final int pageSize = 10;
    private HashMap<String, String> headerMap;
    private ArrayList<CompanyBranchTableModel> companyBranchTableModel;
    private ArrayList<CompanyStockTableModel> companyStockTableModels;
    private ArrayList<CompanyInvestTableModel> companyInvestTableModels;
    public List<String> companyIdList = new ArrayList<>();
    private ThreadPoolExecutor executor;
    private CompletableFuture<Void> future;
    private AtomicInteger companyBranchId = new AtomicInteger(0);
    private AtomicInteger companyStockId = new AtomicInteger(0);
    private AtomicInteger companyInvestId = new AtomicInteger(0);

    public QiDianDataService() {
        companyBranchTableModel = new ArrayList<>();
        companyStockTableModels = new ArrayList<>();
        companyInvestTableModels = new ArrayList<>();
        headerMap = new HashMap<>();
        headerMap.put("Content-Type","application/json");
        headerMap.put("Origin","https://dingtalk.com");
    }

    public void dataAggregate(){
        executor = MyExecutor.getExecutor();
        future = CompletableFuture.runAsync(() -> {
            try {
                companyDetailCard();
            }
            catch (Exception e) {
                Platform.runLater(()->DataAggregateController.getInstance().logBranchArea.appendText("[-]任务已终止\n"));
                Platform.runLater(()->DataAggregateController.getInstance().logShareholderArea.appendText("[-]任务已终止\n"));
                Platform.runLater(()->DataAggregateController.getInstance().logInvestmentArea.appendText("[-]任务已终止\n"));
                throw new RuntimeException(e);
            }
        },executor);
        future.thenRunAsync(()->{
            Platform.runLater(()-> {
                DataAggregateController.getInstance().logBranchArea.appendText("[+]任务已完成\n");
                DataAggregateController.getInstance().logShareholderArea.appendText("[+]任务已完成\n");
                DataAggregateController.getInstance().logInvestmentArea.appendText("[+]任务已完成\n");
            });
        });

    }

    public void companyDetailCardBatch(){
        for (String onecomp_id : companyIdList) {
            JSONObject companyDetailCardBatchBody = getCompanyDetailCardBatchBody(onecomp_id);
        }


    }

    public void companyDetailCard() throws InterruptedException {
        for (String onecomp_id : companyIdList) {
            parseCompanyBranch(onecomp_id);
            parseCompanyStock(onecomp_id);
            parseCompanyInvest(onecomp_id);
            Thread.sleep(100);
        }

    }

    /**
     * 解析对外投资
     * @param onecompId 公司id
     */
    private void parseCompanyInvest(String onecompId){
        JSONObject companyDetailCardBody = getCompanyDetailCardBody(onecompId,investID);
        try {
            StringBuilder pagesizeSB = new StringBuilder();
            HttpClient45.post(BASEURL + companyDetailCard, headerMap, companyDetailCardBody, new SuccessListener() {
                @Override
                public void successListener(HttpEntity entity) throws IOException {
                    String s = EntityUtils.toString(entity);
                    JSONObject jsonObject = JSONObject.parseObject(s);
                    JSONObject data = jsonObject.getJSONObject("data");
                    if (null == data){
                        return;
                    }
                    String total = data.get("total").toString();
                    int page = (int)Math.floor(Double.parseDouble(total) / pageSize)+1;
                    pagesizeSB.append(page);
                    Platform.runLater(()->DataAggregateController.getInstance().logInvestmentArea.appendText("[+]企典数据聚合: "+onecompId+"——1/"+page+"\n"));

                    JSONArray rows = data.getJSONArray("rows");
                    if (null == rows){
                        return;
                    }
                    for (Object o : rows) {
                        String investName = "";
                        String investOperName = "";
                        String stockPercentage = "";
                        String shouldCapi = "";
                        String investStartDate = "";
                        try{
                            if (o instanceof JSONArray){
                                investName= ((JSONArray) o).getJSONObject(0).get("value") == null ? "" : ((JSONArray) o).getJSONObject(0).get("value").toString();
                                investOperName= ((JSONArray) o).getJSONObject(1).get("value") == null ? "" : ((JSONArray) o).getJSONObject(1).get("value").toString();
                                stockPercentage= ((JSONArray) o).get(4) == null ? "" : ((JSONArray) o).get(4).toString();
                                shouldCapi= ((JSONArray) o).get(5) == null ? "" : ((JSONArray) o).get(5).toString();
                                investStartDate= ((JSONArray) o).get(6) == null ? "" : ((JSONArray) o).get(6).toString();
                            }
                        }catch (Exception e){
                            System.out.println("解析对外投资: 数据类型转换失败");
                        }


                        //存储数据
                        CompanyInvestTableModel companyInvestTableModel = new CompanyInvestTableModel(companyInvestId.incrementAndGet(), investName, investOperName, stockPercentage, shouldCapi, investStartDate, "" ,onecompId);
                        //更新数据表格
                        Platform.runLater(()-> DataAggregateController.getInstance().investmentTable.getItems().add(companyInvestTableModel));
                        companyInvestTableModels.add(companyInvestTableModel);
                    }

                }
            }, new FailedListener() {
                @Override
                public void failedListener(Exception e) {

                }
            });

            int page = pagesizeSB.toString().isEmpty() ? 0 : Integer.parseInt(pagesizeSB.toString());
            for (int i = 2; i <= page; i++) {
                Thread.sleep(100);
                companyDetailCardBody.put("pageNo", i);
                Platform.runLater(() -> DataAggregateController.getInstance().logShareholderArea.appendText("[+]企典数据聚合: " + onecompId + "——" + companyDetailCardBody.get("pageNo") + "/" + page + "\n"));
                HttpClient45.post(BASEURL + companyDetailCard, headerMap, companyDetailCardBody, new SuccessListener() {
                    @Override
                    public void successListener(HttpEntity entity) throws IOException {
                        String s = EntityUtils.toString(entity);
                        JSONObject jsonObject = JSONObject.parseObject(s);
                        JSONObject data = jsonObject.getJSONObject("data");
                        if (null == data){
                            return;
                        }
                        Platform.runLater(()->DataAggregateController.getInstance().logInvestmentArea.appendText("[+]企典数据聚合: "+onecompId+"——1/"+page+"\n"));

                        JSONArray rows = data.getJSONArray("rows");
                        if (null == rows){
                            return;
                        }
                        for (Object o : rows) {
                            String investName = "";
                            String investOperName = "";
                            String stockPercentage = "";
                            String shouldCapi = "";
                            String investStartDate = "";
                            try{
                                if (o instanceof JSONArray){
                                    investName= ((JSONArray) o).getJSONObject(0).get("value") == null ? "" : ((JSONArray) o).getJSONObject(0).get("value").toString();
                                    investOperName= ((JSONArray) o).getJSONObject(1).get("value") == null ? "" : ((JSONArray) o).getJSONObject(1).get("value").toString();
                                    stockPercentage= ((JSONArray) o).get(4) == null ? "" : ((JSONArray) o).get(4).toString();
                                    shouldCapi= ((JSONArray) o).get(5) == null ? "" : ((JSONArray) o).get(5).toString();
                                    investStartDate= ((JSONArray) o).get(6) == null ? "" : ((JSONArray) o).get(6).toString();
                                }
                            }catch (Exception e){
                                System.out.println("解析对外投资: 数据类型转换失败");
                            }


                            //存储数据
                            CompanyInvestTableModel companyInvestTableModel = new CompanyInvestTableModel(companyInvestId.incrementAndGet(), investName, investOperName, stockPercentage, shouldCapi, investStartDate, "" ,onecompId);
                            //更新数据表格
                            Platform.runLater(()-> DataAggregateController.getInstance().investmentTable.getItems().add(companyInvestTableModel));
                            companyInvestTableModels.add(companyInvestTableModel);
                        }

                    }
                }, new FailedListener() {
                    @Override
                    public void failedListener(Exception e) {

                    }
                });

            }

        }catch (Exception e){
            System.out.println("解析对外投资有异常");
        }
    }

    /**
     * 解析公司股东
     * @param onecompId 公司id
     */
    private void parseCompanyStock(String onecompId){
        JSONObject companyDetailCardBody = getCompanyDetailCardBody(onecompId,stockID);
        try {
            StringBuilder pagesizeSB = new StringBuilder();
            HttpClient45.post(BASEURL + companyDetailCard, headerMap, companyDetailCardBody, new SuccessListener() {
                @Override
                public void successListener(HttpEntity entity) throws IOException {
                    String s = EntityUtils.toString(entity);
                    JSONObject jsonObject = JSONObject.parseObject(s);
                    JSONObject data = jsonObject.getJSONObject("data");
                    if (null == data){
                        return;
                    }
                    String total = data.get("total").toString();
                    int page = (int)Math.floor(Double.parseDouble(total) / pageSize)+1;
                    pagesizeSB.append(page);
                    Platform.runLater(()->DataAggregateController.getInstance().logShareholderArea.appendText("[+]企典数据聚合: "+onecompId+"——1/"+page+"\n"));

                    JSONArray rows = data.getJSONArray("rows");
                    if (null == rows){
                        return;
                    }
                    for (Object o : rows) {
                        String stockName = "";
                        String stockType = "";
                        String shouldCapi = "";
                        String stockPercent = "";
                        String shouldCapiTime = "";
                        try{
                            if (o instanceof JSONArray){
                                stockName= ((JSONArray) o).get(0) == null ? "" : ((JSONArray) o).get(0).toString();
                                stockType= ((JSONArray) o).get(1) == null ? "" : ((JSONArray) o).get(1).toString();
                                shouldCapi= ((JSONArray) o).get(2) == null ? "" : ((JSONArray) o).get(2).toString();
                                stockPercent= ((JSONArray) o).get(3) == null ? "" : ((JSONArray) o).get(3).toString();
                                shouldCapiTime= ((JSONArray) o).get(4) == null ? "" : ((JSONArray) o).get(4).toString();
                            }
                        }catch (Exception e){
                            System.out.println("数据类型转换失败");
                        }


                        //存储数据
                        CompanyStockTableModel companyStockTableModel = new CompanyStockTableModel(companyStockId.incrementAndGet(), stockName, stockType, shouldCapi, stockPercent, shouldCapiTime, "" ,onecompId);
                        //更新数据表格
                        Platform.runLater(()-> DataAggregateController.getInstance().shareholderTable.getItems().add(companyStockTableModel));
                        companyStockTableModels.add(companyStockTableModel);
                    }

                }
            }, new FailedListener() {
                @Override
                public void failedListener(Exception e) {

                }
            });
            int page = pagesizeSB.toString().isEmpty() ? 0 : Integer.parseInt(pagesizeSB.toString());
            for (int i = 2; i <= page; i++) {
                Thread.sleep(100);
                companyDetailCardBody.put("pageNo", i);
                Platform.runLater(() -> DataAggregateController.getInstance().logShareholderArea.appendText("[+]企典数据聚合: " + onecompId + "——" + companyDetailCardBody.get("pageNo") + "/" + page + "\n"));
                HttpClient45.post(BASEURL + companyDetailCard, headerMap, companyDetailCardBody, new SuccessListener() {
                    @Override
                    public void successListener(HttpEntity entity) throws IOException {
                        String s = EntityUtils.toString(entity);
                        JSONObject jsonObject = JSONObject.parseObject(s);
                        JSONObject data = jsonObject.getJSONObject("data");
                        if (null == data){
                            return;
                        }
                        Platform.runLater(()->DataAggregateController.getInstance().logShareholderArea.appendText("[+]企典数据聚合: "+onecompId+"——1/"+page+"\n"));
                        JSONArray rows = data.getJSONArray("rows");
                        if (null == rows){
                            return;
                        }
                        for (Object o : rows) {
                            String stockName = "";
                            String stockType = "";
                            String shouldCapi = "";
                            String stockPercent = "";
                            String shouldCapiTime = "";
                            try{
                                if (o instanceof JSONArray){
                                    stockName= ((JSONArray) o).get(0) == null ? "" : ((JSONArray) o).get(0).toString();
                                    stockType= ((JSONArray) o).get(1) == null ? "" : ((JSONArray) o).get(1).toString();
                                    shouldCapi= ((JSONArray) o).get(2) == null ? "" : ((JSONArray) o).get(2).toString();
                                    stockPercent= ((JSONArray) o).get(3) == null ? "" : ((JSONArray) o).get(3).toString();
                                    shouldCapiTime= ((JSONArray) o).get(4) == null ? "" : ((JSONArray) o).get(4).toString();
                                }
                            }catch (Exception e){
                                System.out.println("数据类型转换失败");
                            }


                            //存储数据
                            CompanyStockTableModel companyBranchTableModel1 = new CompanyStockTableModel(companyStockId.incrementAndGet(), stockName, stockType, shouldCapi, stockPercent, shouldCapiTime, "" ,onecompId);
                            //更新数据表格
                            Platform.runLater(()-> DataAggregateController.getInstance().shareholderTable.getItems().add(companyBranchTableModel1));
                            companyStockTableModels.add(companyBranchTableModel1);
                        }

                    }
                }, new FailedListener() {
                    @Override
                    public void failedListener(Exception e) {

                    }
                });
            }
        } catch (Exception e){
            System.out.println("解析公司股东有异常");
        }
    }

    /**
     * 解析分支机构
     * @param onecompId 公司id
     */
    private void parseCompanyBranch(String onecompId){
        JSONObject companyDetailCardBody = getCompanyDetailCardBody(onecompId,branchID);
        try{
            StringBuilder pagesizeSB = new StringBuilder();
            HttpClient45.post(BASEURL + companyDetailCard, headerMap, companyDetailCardBody, new SuccessListener() {
                @Override
                public void successListener(HttpEntity entity) throws IOException {
                    String s = EntityUtils.toString(entity);
                    JSONObject jsonObject = JSONObject.parseObject(s);
                    JSONObject data = jsonObject.getJSONObject("data");
                    if (null == data){
                        return;
                    }
                    String total = data.get("total").toString();
                    int page = (int)Math.floor(Double.parseDouble(total) / pageSize) + 1;
                    pagesizeSB.append(page);
                    Platform.runLater(()->DataAggregateController.getInstance().logBranchArea.appendText("[+]企典数据聚合: "+onecompId+"——1/"+page+"\n"));

                    JSONArray rows = data.getJSONArray("rows");
                    if (null == rows){
                        return;
                    }
                    for (Object o : rows) {
                        String value = "";
                        String status = "";
                        String oper_name = "";
                        String term_start = "";
                        try{
                            if (o instanceof JSONArray){
                                value= ((JSONArray) o).getJSONObject(0).get("value") == null ? "" : ((JSONArray) o).getJSONObject(0).get("value").toString();
                                status= ((JSONArray) o).get(1) == null ? "" : ((JSONArray) o).get(1).toString();
                                oper_name= ((JSONArray) o).get(2) == null ? "" : ((JSONArray) o).get(2).toString();
                                term_start= ((JSONArray) o).get(3) == null ? "" : ((JSONArray) o).get(3).toString();
                            }
                        }catch (Exception e){
                            System.out.println("数据类型转换失败");
                        }


                        //存储数据
                        CompanyBranchTableModel companyBranchTableModel1 = new CompanyBranchTableModel(companyBranchId.incrementAndGet(), value, status, oper_name, term_start, onecompId);
                        //更新数据表格
                        Platform.runLater(()-> DataAggregateController.getInstance().branchTable.getItems().add(companyBranchTableModel1));
                        companyBranchTableModel.add(companyBranchTableModel1);
                    }

                }
            }, new FailedListener() {
                @Override
                public void failedListener(Exception e) {

                }
            });
            int page = pagesizeSB.toString().isEmpty() ? 0 : Integer.parseInt(pagesizeSB.toString());
            for (int i = 2; i <= page; i++) {
                Thread.sleep(100);
                companyDetailCardBody.put("pageNo",i);
                Platform.runLater(()->DataAggregateController.getInstance().logBranchArea.appendText("[+]企典数据聚合: "+onecompId+"——"+companyDetailCardBody.get("pageNo")+"/"+page+"\n"));

                HttpClient45.post(BASEURL + companyDetailCard, headerMap, companyDetailCardBody, new SuccessListener() {
                    @Override
                    public void successListener(HttpEntity entity) throws IOException {
                        String s = EntityUtils.toString(entity);
                        JSONObject jsonObject = JSONObject.parseObject(s);
                        JSONObject data = jsonObject.getJSONObject("data");
                        if (null == data){
                            return;
                        }
                        JSONArray rows = data.getJSONArray("rows");
                        if (null == rows){
                            return;
                        }
                        for (Object o : rows) {
                            String value = "";
                            String status = "";
                            String oper_name = "";
                            String term_start = "";
                            try{
                                if (o instanceof JSONArray){
                                    value= ((JSONArray) o).getJSONObject(0).get("value") == null ? "" : ((JSONArray) o).getJSONObject(0).get("value").toString();
                                    status= ((JSONArray) o).get(1) == null ? "" : ((JSONArray) o).get(1).toString();
                                    oper_name= ((JSONArray) o).get(2) == null ? "" : ((JSONArray) o).get(2).toString();
                                    term_start= ((JSONArray) o).get(3) == null ? "" : ((JSONArray) o).get(3).toString();
                                }
                            }catch (Exception e){
                                System.out.println("数据类型转换失败");
                            }
                            //存储数据
                            CompanyBranchTableModel companyBranchTableModel1 = new CompanyBranchTableModel(companyBranchId.incrementAndGet(), value, status, oper_name, term_start, onecompId);
                            //更新数据表格
                            Platform.runLater(()-> DataAggregateController.getInstance().branchTable.getItems().add(companyBranchTableModel1));
                            companyBranchTableModel.add(companyBranchTableModel1);
                        }

                    }
                }, new FailedListener() {
                    @Override
                    public void failedListener(Exception e) {

                    }
                });
            }

        }catch (Exception e){
            System.out.println("解析分支机构有异常");
        }
    }




    private JSONObject getCompanyDetailCardBatchBody(String onecomp_id){
        JSONObject params = new JSONObject();
        JSONArray dataModuleIds = new JSONArray();
        dataModuleIds.add(0,482);
        dataModuleIds.add(1,483);
        dataModuleIds.add(2,484);
        dataModuleIds.add(3,486);
        dataModuleIds.add(4,453);
        dataModuleIds.add(5,485);
        params.put("dataModuleIds", dataModuleIds);
        params.put("type", "web");
        params.put("pageNo", 1);
        params.put("pageSize", pageSize);
        JSONArray param = new JSONArray();
        JSONObject paramObj = new JSONObject();
        paramObj.put("key","onecomp_id");
        paramObj.put("value",onecomp_id);
        param.add(0,paramObj);
        params.put("params", param);
        return  params;
    }

    private JSONObject getCompanyDetailCardBody(String onecompId,String dataModuleId){
        JSONObject params = new JSONObject();
        params.put("dataModuleId", dataModuleId);
        JSONArray param = new JSONArray();
        JSONObject paramObj = new JSONObject();
        paramObj.put("key","onecomp_id");
        paramObj.put("value",onecompId);
        param.add(0,paramObj);
        params.put("params", param);
        params.put("type", "web");
        params.put("pageNo", 1);
        params.put("pageSize", pageSize);
        return params;
    }

    public Boolean stopCompany() {
        if (!future.isDone()) {
            executor.shutdownNow();
            return true;
        } else {
            return false;
        }
    }
}
