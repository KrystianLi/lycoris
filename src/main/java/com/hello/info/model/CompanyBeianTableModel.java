package com.hello.info.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CompanyBeianTableModel {

    private  String id;
    private  String beianNo;
    private  String beianUrl;
    private  String companyName;
    private  String siteName;
    private  String siteDomain;
    private  String date;

    public CompanyBeianTableModel(String id, String beianNo, String beianUrl, String companyName, String siteName, String siteDomain, String date) {
        this.id = id;
        this.beianNo = beianNo;
        this.beianUrl = beianUrl;
        this.companyName = companyName;
        this.siteName = siteName;
        this.siteDomain = siteDomain;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeianNo() {
        return beianNo;
    }

    public void setBeianNo(String beianNo) {
        this.beianNo = beianNo;
    }

    public String getBeianUrl() {
        return beianUrl;
    }

    public void setBeianUrl(String beianUrl) {
        this.beianUrl = beianUrl;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteDomain() {
        return siteDomain;
    }

    public void setSiteDomain(String siteDomain) {
        this.siteDomain = siteDomain;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public StringProperty idProperty() {
        return new SimpleStringProperty(id);
    }

    public StringProperty companyNameProperty() {
        return new SimpleStringProperty(companyName);
    }
    public StringProperty beianUrlProperty() {
        return new SimpleStringProperty(beianUrl);
    }



    public StringProperty siteNameProperty() {
        return new SimpleStringProperty(siteName);
    }

    public StringProperty siteDomainProperty() {
        return new SimpleStringProperty(siteDomain);
    }
    public StringProperty dateProperty() {
        return new SimpleStringProperty(date);
    }

}
