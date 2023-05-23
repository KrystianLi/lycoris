package com.hello.info.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CompanyStockTableModel {

    private  int id;
    private  String stockName;
    private  String stockType;
    private  String shouldCapi;
    private  String stockPercent;
    private  String shouldCapiTime;
    private  String shareholderFloat;
    private  String companyID;

    public CompanyStockTableModel(int id, String stockName, String stockType, String shouldCapi, String stockPercent, String shouldCapiTime, String shareholderFloat, String companyID) {
        this.id = id;
        this.stockName = stockName;
        this.stockType = stockType;
        this.shouldCapi = shouldCapi;
        this.stockPercent = stockPercent;
        this.shouldCapiTime = shouldCapiTime;
        this.shareholderFloat = shareholderFloat;
        this.companyID = companyID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public String getShouldCapi() {
        return shouldCapi;
    }

    public void setShouldCapi(String shouldCapi) {
        this.shouldCapi = shouldCapi;
    }

    public String getStockPercent() {
        return stockPercent;
    }

    public void setStockPercent(String stockPercent) {
        this.stockPercent = stockPercent;
    }

    public String getShouldCapiTime() {
        return shouldCapiTime;
    }

    public void setShouldCapiTime(String shouldCapiTime) {
        this.shouldCapiTime = shouldCapiTime;
    }

    public String getShareholderFloat() {
        return shareholderFloat;
    }

    public void setShareholderFloat(String shareholderFloat) {
        this.shareholderFloat = shareholderFloat;
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

    public StringProperty stockNameProperty() {
        return new SimpleStringProperty(stockName);
    }



    public StringProperty stockTypeProperty() {
        return new SimpleStringProperty(stockType);
    }

    public StringProperty shouldCapiProperty() {
        return new SimpleStringProperty(shouldCapi);
    }
    public StringProperty stockPercentProperty() {
        return new SimpleStringProperty(stockPercent);
    }


    public StringProperty shouldCapiTimeProperty() {
        return new SimpleStringProperty(shouldCapiTime);
    }
    public StringProperty shareholderFloatTimeProperty() {
        return new SimpleStringProperty(shareholderFloat);
    }
    public StringProperty companyIDProperty() {
        return new SimpleStringProperty(companyID);
    }
}
