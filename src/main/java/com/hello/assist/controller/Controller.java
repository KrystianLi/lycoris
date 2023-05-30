package com.hello.assist.controller;

import com.hello.PurposeEnum;
import com.hello.assist.model.IP2DomainTableModel;
import com.hello.assist.service.AssistService;
import com.hello.factory.ExpFactory;
import com.hello.info.model.aggregate.CompanyBranchTableModel;
import com.hello.strategy.ExpStrategy;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.omg.PortableInterceptor.ACTIVE;

import java.io.IOException;

public class Controller {
    private static Controller instance;

    private AssistService assistService;
    private ExpStrategy factApplyStrategy;

    @FXML
    private TextArea primaryTextArea;

    @FXML
    private TextArea secondaryTextArea;
    @FXML
    public TextArea insertResTextArea;
    @FXML
    public TextArea reduceResTextArea;


    //去重tab
    @FXML
    private Button repetitionBtn;

    @FXML
    private TextArea repContentTextArea;
    @FXML
    public TextArea repResultTextArea;

    //IP2DomainTab
    @FXML
    private ComboBox<String> IP2DomainComboBox;
    public TableView<IP2DomainTableModel> IP2DomainTable;
    private ObservableList<IP2DomainTableModel> ip2DomainTableModels = FXCollections.observableArrayList();

    @FXML
    private TableColumn<IP2DomainTableModel, Integer> IP2DomainID;
    @FXML
    private TableColumn<IP2DomainTableModel, String> IP2DomainIP;

    @FXML
    private TableColumn<IP2DomainTableModel, String> IP2DomainDomain;
    @FXML
    public TextArea IP2DomainListTextArea;
    @FXML
    public TextArea IP2DomainLogTextArea;


    public void initialize(){

        //初始化IP2Domain查询数据获取源下拉框
        for (String s : PurposeEnum.getExpType(PurposeEnum.IP2Domain.getExpType())){
            if (!s.isEmpty())
                IP2DomainComboBox.getItems().add(s);
        }
        IP2DomainComboBox.getSelectionModel().selectFirst();
        // 设置列宽百分比
        IP2DomainID.prefWidthProperty().bind(IP2DomainTable.widthProperty().multiply(0.1));
        IP2DomainIP.prefWidthProperty().bind(IP2DomainTable.widthProperty().multiply(0.5));
        IP2DomainDomain.prefWidthProperty().bind(IP2DomainTable.widthProperty().multiply(0.4));

        IP2DomainID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        IP2DomainIP.setCellValueFactory(cellData -> cellData.getValue().ipProperty());
        IP2DomainDomain.setCellValueFactory(cellData -> cellData.getValue().domainProperty());
        IP2DomainTable.setItems(ip2DomainTableModels);

    }
    @FXML
    private void compare(){
        String primaryText = primaryTextArea.getText();
        String secondaryText = secondaryTextArea.getText();
        try {
            assistService.compareToTextArea(primaryText,secondaryText);
        } catch (IOException e) {
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                System.out.println(stackTraceElement);
            }
        }

    }

    @FXML
    private void repetition(){
        String text = repContentTextArea.getText();
        try {
            assistService.repetition(text);
        } catch (Exception e) {
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                System.out.println(stackTraceElement);
            }
        }
    }

    public static Controller getInstance() {
        return instance;
    }


    //初始化
    public Controller() {
        instance = this;
        assistService = new AssistService();
    }

    @FXML
    private void IP2DomainSearch(ActionEvent event){
        String cve = this.IP2DomainComboBox.getValue().toString().trim();
        IP2DomainTable.getItems().clear();
        String ipList = IP2DomainListTextArea.getText();
        factApplyStrategy = ExpFactory.getFactApplyStrategy(cve);
        try {
            factApplyStrategy.ip2domainSearch(ipList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void IP2DomainStop(ActionEvent event){
        try {
            Boolean aBoolean = false;
            if (factApplyStrategy != null){
                aBoolean = factApplyStrategy.stop();
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
