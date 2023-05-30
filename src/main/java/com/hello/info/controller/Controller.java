/**
 * Sample Skeleton for 'Info.fxml' Controller Class
 */

package com.hello.info.controller;

import com.hello.PurposeEnum;
import com.hello.factory.ExpFactory;
import com.hello.info.model.CompanyBeianTableModel;
import com.hello.info.model.CompanyTableModel;
import com.hello.strategy.ExpStrategy;
import com.hello.tools.ExportExcel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.omg.PortableInterceptor.ACTIVE;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

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
    private Button companyNameStopBtn;

    @FXML
    public TextField companyNameCookie;
    @FXML
    private TableView<CompanyTableModel> companyTable;



    @FXML
    private TextField beianText;

    @FXML
    private Button companyBeianStopBtn;
    @FXML
    private Button companyBeianSaveBtn;

    @FXML
    public TextField companyBeianCookie;
    @FXML
    private ComboBox<String> searchBeianComboBox;

    @FXML
    private TextField filterBeianText;

    @FXML
    private Button companyBeianSearchBtn;



    @FXML
    public TextArea logCompanyBeianArea;

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
    @FXML
    private TableColumn<CompanyTableModel, String> href;

    private ObservableList<CompanyTableModel> companyTableModels = FXCollections.observableArrayList();

    private ExpStrategy InfoName;
    private ExpStrategy InfoBeian;
    //备案查询表格
    @FXML
    private TableView<CompanyBeianTableModel> beianTable;

    //备案号列名
    @FXML
    private TableColumn<CompanyBeianTableModel, String> beianId;
    @FXML
    private TableColumn<CompanyBeianTableModel, String> beianNo;
    @FXML
    private TableColumn<CompanyBeianTableModel, String> beianCompanyName;
    @FXML
    private TableColumn<CompanyBeianTableModel, String> beianSiteName;
    @FXML
    private TableColumn<CompanyBeianTableModel, String> beianSiteDomain;
    @FXML
    private TableColumn<CompanyBeianTableModel, String> beianDate;
    @FXML
    private TableColumn<CompanyBeianTableModel, String> beianUrl;
    private ObservableList<CompanyBeianTableModel> companyBeianTableModels = FXCollections.observableArrayList();

    @FXML
    private TextField filterCompanyNameText;

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
        href.setCellValueFactory(cellData -> cellData.getValue().hrefProperty());
        companyTable.setItems(companyTableModels);


        //初始化备案查询
        for (String s : PurposeEnum.getExpType(PurposeEnum.Info.getExpType())){
            if (!s.isEmpty())
                searchBeianComboBox.getItems().add(s);
        }
        searchBeianComboBox.getSelectionModel().selectFirst();

        beianId.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        beianNo.setCellValueFactory(cellData -> cellData.getValue().beianNoProperty());
        beianCompanyName.setCellValueFactory(cellData -> cellData.getValue().companyNameProperty());
        beianSiteName.setCellValueFactory(cellData -> cellData.getValue().siteNameProperty());
        beianSiteDomain.setCellValueFactory(cellData -> cellData.getValue().siteDomainProperty());
        beianDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        beianUrl.setCellValueFactory(cellData -> cellData.getValue().beianUrlProperty());
        beianTable.setItems(companyBeianTableModels);

    }
    @FXML
    void companyBeianSearch(ActionEvent event) {
        String cve = this.searchBeianComboBox.getValue().toString().trim();
        beianTable.getItems().clear();
        String keyWord = this.beianText.getText();
        InfoBeian = ExpFactory.getFactApplyStrategy(cve);
        try {
            String s = InfoBeian.searchCompanyBeian(keyWord);
            System.out.println(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void companyNameSearch(ActionEvent event){
        companyTable.getItems().clear();
        String cve = this.searchCompanyNameComboBox.getValue().toString().trim();
        String keyWord = this.companyText.getText();
        InfoName = ExpFactory.getFactApplyStrategy(cve);
        try {
            String s = InfoName.searchCompanyName(keyWord);
        } catch (Exception e) {
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                System.out.println(stackTraceElement.toString());
            }
        }
    }

    @FXML
    void companyStop(ActionEvent event){
        try {
            Boolean aBoolean = false;
            if (InfoName != null){
                aBoolean = InfoName.stop();
            } else if (InfoBeian != null) {
                aBoolean = InfoBeian.stop();
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

    public Controller() {
        instance = this;
    }

    public static Controller getInstance() {
        return instance;
    }

    public TableView<CompanyTableModel> getCompanyTable() {
        return companyTable;
    }

    public TableView<CompanyBeianTableModel> getBeianTable() {
        return beianTable;
    }

    public void companyNameSave(ActionEvent event) {
        String sheetName = "公司查询导出";
        List<CompanyTableModel> companyTableModelList = getCompanyTable().getItems();
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<String, String>();
        titleMap.put("id","id");
        titleMap.put("name","公司名");
        titleMap.put("date","成立日期");
        titleMap.put("email","邮箱");
        titleMap.put("shareholder","股东信息");
        titleMap.put("active","活跃状态");
        titleMap.put("href","链接");
        ExportExcel.excelExport(companyTableModelList, titleMap, sheetName);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText("导出完成");
        alert.showAndWait();
    }

    public void companyBeianSave(ActionEvent event){
        String sheetName = "备案查询导出";
        List<CompanyBeianTableModel> companyBeianTableModelList = getBeianTable().getItems();
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<String, String>();
        titleMap.put("id","id");
        titleMap.put("beianNo","网站备案号/许可证号");
        titleMap.put("companyName","主办单位名称");
        titleMap.put("siteName","网站名称");
        titleMap.put("siteDomain","网站域名");
        titleMap.put("date","审核时间");
        titleMap.put("beianUrl","详情");
        ExportExcel.excelExport(companyBeianTableModelList, titleMap, sheetName);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText("导出完成");
        alert.showAndWait();
    }

    @FXML
    void companyAggregation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/info_dataAggregate.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        Controller.getInstance().getCompanyTable().getItems();
    }

    @FXML
    void filterSearch(ActionEvent event){

    }
}
