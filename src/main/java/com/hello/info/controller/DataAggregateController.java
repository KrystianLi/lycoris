package com.hello.info.controller;

import com.hello.info.model.aggregate.CompanyBeianTableModel;
import com.hello.info.model.aggregate.CompanyBranchTableModel;
import com.hello.info.model.aggregate.CompanyInvestTableModel;
import com.hello.info.model.aggregate.CompanyStockTableModel;
import com.hello.info.model.CompanyTableModel;
import com.hello.info.service.aggregate.QiDianDataService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

public class DataAggregateController {
    private static DataAggregateController instance;
    @FXML
    public TableView<CompanyStockTableModel> shareholderTable;
    private ObservableList<CompanyStockTableModel> companyStockTableModelObservableList = FXCollections.observableArrayList();


    @FXML
    private TableColumn<CompanyStockTableModel, Integer> shareholderId;

    @FXML
    private TableColumn<CompanyStockTableModel, String> shareholderName;

    @FXML
    private TableColumn<CompanyStockTableModel, String> shareholderType;

    @FXML
    private TableColumn<CompanyStockTableModel, String> shareholderMoney;

    @FXML
    private TableColumn<CompanyStockTableModel, String> shareholderProportion;

    @FXML
    private TableColumn<CompanyStockTableModel, String> shareholderDate;

    @FXML
    private TableColumn<CompanyStockTableModel, String> shareholderFloat;

    @FXML
    private TableColumn<CompanyStockTableModel, String> shareholderCompanyID;

    @FXML
    public TextArea logShareholderArea;

    @FXML
    public TableView<CompanyInvestTableModel> investmentTable;
    private ObservableList<CompanyInvestTableModel> companyInvestTableModelObservableList = FXCollections.observableArrayList();


    @FXML
    private TableColumn<CompanyInvestTableModel, Integer> investmentID;

    @FXML
    private TableColumn<CompanyInvestTableModel, String> investmentCompany;

    @FXML
    private TableColumn<CompanyInvestTableModel, String> investmentPerson;

    @FXML
    private TableColumn<CompanyInvestTableModel, String> investmentProportion;

    @FXML
    private TableColumn<CompanyInvestTableModel, String> investmentMoney;

    @FXML
    private TableColumn<CompanyInvestTableModel, String> investmentDate;

    @FXML
    private TableColumn<CompanyInvestTableModel, String> investmentProduct;
    @FXML
    private TableColumn<CompanyInvestTableModel, String> investmentCompanyID;

    @FXML
    public TextArea logInvestmentArea;

    private ObservableList<CompanyBranchTableModel> companyBranchTableModelObservableList = FXCollections.observableArrayList();
    @FXML
    public TableView<CompanyBranchTableModel> branchTable;

    @FXML
    private TableColumn<CompanyBranchTableModel, Integer> branchID;

    @FXML
    private TableColumn<CompanyBranchTableModel, String> branchName;

    @FXML
    private TableColumn<CompanyBranchTableModel, String> branchState;

    @FXML
    private TableColumn<CompanyBranchTableModel, String> branchPerson;

    @FXML
    private TableColumn<CompanyBranchTableModel, String> branchDate;
    @FXML
    private TableColumn<CompanyBranchTableModel, String> branchCompanyID;

    @FXML
    public TextArea logBranchArea;
    @FXML
    public TableView<CompanyBeianTableModel> beianTable;
    private ObservableList<CompanyBeianTableModel> companyBeianTableModelObservableList = FXCollections.observableArrayList();


    @FXML
    private TableColumn<CompanyBeianTableModel, Integer> beianID;

    @FXML
    private TableColumn<CompanyBeianTableModel, String> beianHomeURL;

    @FXML
    private TableColumn<CompanyBeianTableModel, String> beianSiteName;

    @FXML
    private TableColumn<CompanyBeianTableModel, String> beianDomain;

    @FXML
    private TableColumn<CompanyBeianTableModel, String> beianCheckDate;

    @FXML
    private TableColumn<CompanyBeianTableModel, String> beianNumber;

    @FXML
    private TableColumn<CompanyBeianTableModel, String> beianCompanyID;

    @FXML
    public TextArea logBeianArea;
    ObservableList<CompanyTableModel> allCompanyModel;
    QiDianDataService qiDianDataService;

    public void initialize(){
        branchID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        branchName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        branchState.setCellValueFactory(cellData -> cellData.getValue().stateProperty());
        branchPerson.setCellValueFactory(cellData -> cellData.getValue().personProperty());
        branchDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        branchCompanyID.setCellValueFactory(cellData -> cellData.getValue().companyIDProperty());
        branchTable.setItems(companyBranchTableModelObservableList);

        shareholderId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        shareholderName.setCellValueFactory(cellData -> cellData.getValue().stockNameProperty());
        shareholderType.setCellValueFactory(cellData -> cellData.getValue().stockTypeProperty());
        shareholderMoney.setCellValueFactory(cellData -> cellData.getValue().stockPercentProperty());
        shareholderProportion.setCellValueFactory(cellData -> cellData.getValue().shouldCapiProperty());
        shareholderDate.setCellValueFactory(cellData -> cellData.getValue().shouldCapiTimeProperty());
        shareholderFloat.setCellValueFactory(cellData -> cellData.getValue().shareholderFloatTimeProperty());
        shareholderCompanyID.setCellValueFactory(cellData -> cellData.getValue().companyIDProperty());
        shareholderTable.setItems(companyStockTableModelObservableList);


        investmentID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        investmentCompany.setCellValueFactory(cellData -> cellData.getValue().investNameProperty());
        investmentPerson.setCellValueFactory(cellData -> cellData.getValue().investOperNameProperty());
        investmentProportion.setCellValueFactory(cellData -> cellData.getValue().stockPercentageProperty());
        investmentMoney.setCellValueFactory(cellData -> cellData.getValue().shouldCapiProperty());
        investmentDate.setCellValueFactory(cellData -> cellData.getValue().investStartDateProperty());
        investmentProduct.setCellValueFactory(cellData -> cellData.getValue().investProductProperty());
        investmentCompanyID.setCellValueFactory(cellData -> cellData.getValue().companyIDProperty());
        investmentTable.setItems(companyInvestTableModelObservableList);


        beianID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        beianHomeURL.setCellValueFactory(cellData -> cellData.getValue().beianHomeURLProperty());
        beianSiteName.setCellValueFactory(cellData -> cellData.getValue().beianSiteNameProperty());
        beianDomain.setCellValueFactory(cellData -> cellData.getValue().beianDomainProperty());
        beianCheckDate.setCellValueFactory(cellData -> cellData.getValue().beianCheckDateProperty());
        beianNumber.setCellValueFactory(cellData -> cellData.getValue().beianNumberProperty());
        beianCompanyID.setCellValueFactory(cellData -> cellData.getValue().companyIDProperty());
        beianTable.setItems(companyBeianTableModelObservableList);

    }

    public DataAggregateController() {
        instance = this;
        getAllInformation();
    }

    public static DataAggregateController getInstance() {
        return instance;
    }

    /**
     * 解析每一个条信息，获取详情
     */
    private void getAllInformation(){
        allCompanyModel = Controller.getInstance().getCompanyTable().getItems();
        qiDianDataService = new QiDianDataService();
        for (CompanyTableModel tableModel : allCompanyModel) {
            String href = tableModel.getHref();
            String companyId = href.substring(href.lastIndexOf("/") + 1);
            qiDianDataService.companyIdList.add(companyId);
        }
        qiDianDataService.dataAggregate();
    }


    @FXML
    void dataAggregate(ActionEvent event) {

    }

    @FXML
    void dataAggregateStop(ActionEvent event){
        try {
            Boolean aBoolean = false;
            if (qiDianDataService != null){
                aBoolean = qiDianDataService.stop();
            }
            if (aBoolean){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示");
                alert.setHeaderText(null);
                alert.setContentText("停止成功");
                alert.showAndWait();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

