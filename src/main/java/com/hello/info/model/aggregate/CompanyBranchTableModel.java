package com.hello.info.model.aggregate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CompanyBranchTableModel {

    private  int id;
    private  String name;
    private  String state;
    private  String person;
    private  String date;
    private  String companyID;

    public CompanyBranchTableModel(int id, String name, String state, String person, String date, String companyID) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.person = person;
        this.date = date;
        this.companyID = companyID;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public StringProperty nameProperty() {
        return new SimpleStringProperty(name);
    }



    public StringProperty dateProperty() {
        return new SimpleStringProperty(date);
    }

    public StringProperty stateProperty() {
        return new SimpleStringProperty(state);
    }
    public StringProperty personProperty() {
        return new SimpleStringProperty(person);
    }


    public StringProperty companyIDProperty() {
        return new SimpleStringProperty(companyID);
    }
}
