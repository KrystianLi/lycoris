package com.hello.assist.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class IP2DomainTableModel {
    private int id;

    private  String ip;
    private  String domain;

    public IP2DomainTableModel(int id, String ip, String domain) {
        this.id = id;
        this.ip = ip;
        this.domain = domain;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }


    public IntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

    public StringProperty ipProperty() {
        return new SimpleStringProperty(ip);
    }
    public StringProperty domainProperty() {
        return new SimpleStringProperty(domain);
    }


}
