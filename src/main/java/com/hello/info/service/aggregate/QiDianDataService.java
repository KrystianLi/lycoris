package com.hello.info.service.aggregate;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.ArrayList;

public class QiDianDataService {

    private final String BASEURL = "https://holmes.taobao.com";
    private final String CompanyDetailCardBatch = "/web/detail/companyDetailCardBatch";
    private final String companyDetailCard = "/web/detail/companyDetailCard";
    private final int pageSize = 10;
    private String onecomp_id;


    public QiDianDataService(String onecomp_id) {
        this.onecomp_id = onecomp_id;
    }

    public void dataAggregate(){


    }

    public void companyDetailCardBatch(){

        JSONObject companyDetailCardBatchBody = getCompanyDetailCardBatchBody();
        System.out.println(companyDetailCardBatchBody.toString());

    }

    public void companyDetailCard(){
        JSONObject companyDetailCardBody = getCompanyDetailCardBody();
        System.out.println(companyDetailCardBody.toString());
    }

    private JSONObject getCompanyDetailCardBatchBody(){
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

    private JSONObject getCompanyDetailCardBody(){
        JSONObject params = new JSONObject();
        params.put("dataModuleIds", 485);
        JSONArray param = new JSONArray();
        JSONObject paramObj = new JSONObject();
        paramObj.put("key","onecomp_id");
        paramObj.put("value",onecomp_id);
        param.add(0,paramObj);
        params.put("params", param);
        params.put("type", "web");
        params.put("pageNo", 1);
        params.put("pageSize", pageSize);
        return params;
    }

}
