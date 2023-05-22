package com.hello.info.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

public class DataAggregateController {

    @FXML
    private TableView<?> shareholderTable;

    @FXML
    private TableColumn<?, ?> shareholderId;

    @FXML
    private TableColumn<?, ?> shareholderName;

    @FXML
    private TableColumn<?, ?> shareholderType;

    @FXML
    private TableColumn<?, ?> shareholderMoney;

    @FXML
    private TableColumn<?, ?> shareholderProportion;

    @FXML
    private TableColumn<?, ?> shareholderDate;

    @FXML
    private TableColumn<?, ?> shareholderFloat;

    @FXML
    private TableColumn<?, ?> companyID;

    @FXML
    private TextArea logShareholderArea;

    @FXML
    private TableView<?> investmentTable;

    @FXML
    private TableColumn<?, ?> investmentID;

    @FXML
    private TableColumn<?, ?> investmentCompany;

    @FXML
    private TableColumn<?, ?> investmentPerson;

    @FXML
    private TableColumn<?, ?> investmentProportion;

    @FXML
    private TableColumn<?, ?> investmentMoney;

    @FXML
    private TableColumn<?, ?> investmentDate;

    @FXML
    private TableColumn<?, ?> investmentProduct;

    @FXML
    private TextArea logInvestmentArea;

    @FXML
    private TableView<?> branchTable;

    @FXML
    private TableColumn<?, ?> branchID;

    @FXML
    private TableColumn<?, ?> branchName;

    @FXML
    private TableColumn<?, ?> branchState;

    @FXML
    private TableColumn<?, ?> branchPerson;

    @FXML
    private TableColumn<?, ?> branchDate;

    @FXML
    private TextArea logBranchArea;

    @FXML
    void dataAggregate(ActionEvent event) {

    }

}

