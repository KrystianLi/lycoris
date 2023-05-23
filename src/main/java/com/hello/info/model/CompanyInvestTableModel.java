package com.hello.info.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CompanyInvestTableModel {

    private  int id;
    private  String investName;
    private  String investOperName;
    private  String stockPercentage;
    private  String shouldCapi;
    private  String investStartDate;
    private  String investProduct;
    private  String companyID;

    public CompanyInvestTableModel(int id, String investName, String investOperName, String stockPercentage, String shouldCapi, String investStartDate, String investProduct, String companyID) {
        this.id = id;
        this.investName = investName;
        this.investOperName = investOperName;
        this.stockPercentage = stockPercentage;
        this.shouldCapi = shouldCapi;
        this.investStartDate = investStartDate;
        this.investProduct = investProduct;
        this.companyID = companyID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvestName() {
        return investName;
    }

    public void setInvestName(String investName) {
        this.investName = investName;
    }

    public String getInvestOperName() {
        return investOperName;
    }

    public void setInvestOperName(String investOperName) {
        this.investOperName = investOperName;
    }

    public String getStockPercentage() {
        return stockPercentage;
    }

    public void setStockPercentage(String stockPercentage) {
        this.stockPercentage = stockPercentage;
    }

    public String getShouldCapi() {
        return shouldCapi;
    }

    public void setShouldCapi(String shouldCapi) {
        this.shouldCapi = shouldCapi;
    }

    public String getInvestStartDate() {
        return investStartDate;
    }

    public void setInvestStartDate(String investStartDate) {
        this.investStartDate = investStartDate;
    }

    public String getInvestProduct() {
        return investProduct;
    }

    public void setInvestProduct(String investProduct) {
        this.investProduct = investProduct;
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

    public StringProperty investNameProperty() {
        return new SimpleStringProperty(investName);
    }



    public StringProperty investOperNameProperty() {
        return new SimpleStringProperty(investOperName);
    }

    public StringProperty stockPercentageProperty() {
        return new SimpleStringProperty(stockPercentage);
    }
    public StringProperty shouldCapiProperty() {
        return new SimpleStringProperty(shouldCapi);
    }


    public StringProperty investStartDateProperty() {
        return new SimpleStringProperty(investStartDate);
    }
    public StringProperty investProductProperty() {
        return new SimpleStringProperty(investProduct);
    }
    public StringProperty companyIDProperty() {
        return new SimpleStringProperty(companyID);
    }
}
