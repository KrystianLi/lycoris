package com.hello.info.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CompanyTableModel {

    private  int id;
    private  String name;
    private  String date;
    private  String email;
    private  String shareholder;
    private  String active;
    private  String href;

    public CompanyTableModel(int id, String name, String date, String email, String shareholder, String active,String href) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.email = email;
        this.shareholder = shareholder;
        this.active = active;
        this.href = href;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShareholder() {
        return shareholder;
    }

    public void setShareholder(String shareholder) {
        this.shareholder = shareholder;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public IntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

    public StringProperty nameProperty() {
        return new SimpleStringProperty(name);
    }



    public StringProperty dateProperty() {
        return new SimpleStringProperty(date);
    }

    public StringProperty emailProperty() {
        return new SimpleStringProperty(email);
    }
    public StringProperty shareholderProperty() {
        return new SimpleStringProperty(shareholder);
    }


    public StringProperty activeProperty() {
        return new SimpleStringProperty(active);
    }
    public StringProperty hrefProperty() {
        return new SimpleStringProperty(href);
    }
}
