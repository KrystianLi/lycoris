package com.hello.info.model.aggregate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CompanyBeianTableModel {

    private  int id;
    private  String beianHomeURL;
    private  String beianSiteName;
    private  String beianDomain;
    private  String beianCheckDate;
    private  String beianNumber;
    private  String companyID;

    public CompanyBeianTableModel(int id, String beianHomeURL, String beianSiteName, String beianDomain, String beianCheckDate, String beianNumber, String companyID) {
        this.id = id;
        this.beianHomeURL = beianHomeURL;
        this.beianSiteName = beianSiteName;
        this.beianDomain = beianDomain;
        this.beianCheckDate = beianCheckDate;
        this.beianNumber = beianNumber;
        this.companyID = companyID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBeianHomeURL() {
        return beianHomeURL;
    }

    public void setBeianHomeURL(String beianHomeURL) {
        this.beianHomeURL = beianHomeURL;
    }

    public String getBeianSiteName() {
        return beianSiteName;
    }

    public void setBeianSiteName(String beianSiteName) {
        this.beianSiteName = beianSiteName;
    }

    public String getBeianDomain() {
        return beianDomain;
    }

    public void setBeianDomain(String beianDomain) {
        this.beianDomain = beianDomain;
    }

    public String getBeianCheckDate() {
        return beianCheckDate;
    }

    public void setBeianCheckDate(String beianCheckDate) {
        this.beianCheckDate = beianCheckDate;
    }

    public String getBeianNumber() {
        return beianNumber;
    }

    public void setBeianNumber(String beianNumber) {
        this.beianNumber = beianNumber;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public IntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

    public StringProperty beianHomeURLProperty() {
        return new SimpleStringProperty(beianHomeURL);
    }



    public StringProperty beianSiteNameProperty() {
        return new SimpleStringProperty(beianSiteName);
    }

    public StringProperty beianDomainProperty() {
        return new SimpleStringProperty(beianDomain);
    }
    public StringProperty beianCheckDateProperty() {
        return new SimpleStringProperty(beianCheckDate);
    }


    public StringProperty beianNumberProperty() {
        return new SimpleStringProperty(beianNumber);
    }
    public StringProperty companyIDProperty() {
        return new SimpleStringProperty(companyID);
    }
}
