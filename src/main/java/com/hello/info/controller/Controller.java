/**
 * Sample Skeleton for 'Info.fxml' Controller Class
 */

package com.hello.info.controller;

import com.hello.PurposeEnum;
import com.hello.factory.ExpFactory;
import com.hello.info.model.CompanyTableModel;
import com.hello.strategy.ExpStrategy;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller {

    private static Controller instance;

    @FXML
    private TextField companyText;

    @FXML
    private ComboBox<String> searchCompanyNameComboBox;

    @FXML
    private TextField filterText;

    @FXML
    private Button companyNameSearchBtn;

    @FXML
    private TableView<CompanyTableModel> companyTable;



    @FXML
    private TextField beianText;

    @FXML
    private ComboBox<String> searchBeianComboBox;

    @FXML
    private TextField filterBeianText;

    @FXML
    private Button companyBeianSearchBtn;

    @FXML
    private TableView<String> beianTable;

    @FXML
    public TextArea logbeianArea;

    @FXML
    public TextArea logCompanyNameArea;

    @FXML
    private TableColumn<CompanyTableModel, Integer> id;

    @FXML
    private TableColumn<CompanyTableModel, String> name;

    @FXML
    private TableColumn<CompanyTableModel, String> date;

    @FXML
    private TableColumn<CompanyTableModel, String> email;

    @FXML
    private TableColumn<CompanyTableModel, String> shareholder;

    @FXML
    private TableColumn<CompanyTableModel, String> active;

    private ObservableList<CompanyTableModel> companyTableModels = FXCollections.observableArrayList();

    public void initialize(){
        //初始化公司查询数据获取源下拉框
        for (String s : PurposeEnum.getExpType(PurposeEnum.Info.getExpType())){
            if (!s.isEmpty())
                searchCompanyNameComboBox.getItems().add(s);
        }
        searchCompanyNameComboBox.getSelectionModel().selectFirst();

        id.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        date.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        email.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        shareholder.setCellValueFactory(cellData -> cellData.getValue().shareholderProperty());
        active.setCellValueFactory(cellData -> cellData.getValue().activeProperty());
        companyTable.setItems(companyTableModels);


        //初始化备案查询
        for (String s : PurposeEnum.getExpType(PurposeEnum.Info.getExpType())){
            if (!s.isEmpty())
                searchBeianComboBox.getItems().add(s);
        }
        searchBeianComboBox.getSelectionModel().selectFirst();


    }
    @FXML
    void companyBeianSearch(ActionEvent event) {
        String cve = this.searchBeianComboBox.getValue().toString().trim();
        String keyWord = this.beianText.getText();
        String filterWord = this.filterBeianText.getText();
        ExpStrategy Info = ExpFactory.getFactApplyStrategy(cve);
        try {
            String s = Info.searchCompanyBeian(keyWord,filterWord);
            System.out.println(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void companyNameSearch(ActionEvent event){
        String cve = this.searchCompanyNameComboBox.getValue().toString().trim();
        String keyWord = this.companyText.getText();
        String filterWord = this.filterText.getText();
        ExpStrategy Info = ExpFactory.getFactApplyStrategy(cve);
        try {
            String s = Info.searchCompanyName(keyWord,filterWord);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Controller() {
        instance = this;
    }

    public static Controller getInstance() {
        return instance;
    }

    public TableView<CompanyTableModel> getCompanyTable() {
        return companyTable;
    }
}
