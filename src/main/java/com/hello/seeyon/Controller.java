package com.hello.seeyon;

import com.hello.strategy.ExpStrategy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import com.hello.seeyon.tools.VulInfo;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class Controller {
    @FXML
    private RadioButton mRadiobutton6;
    @FXML
    private TextField id_urlInput;
    @FXML
    private TextField jndi_echo;
    @FXML
    private ToggleGroup codegroup;
    @FXML
    private ComboBox<Integer> id_thread;
    @FXML
    private TableColumn<VulInfo, String> target;
    @FXML
    private TextArea id_checkarea;
    @FXML
    private TableColumn<VulInfo, String> isVul;
    @FXML
    private Label author;
    @FXML
    private Hyperlink id_help;
    @FXML
    private TextArea upload_info;
    @FXML
    private Button proxySetupBtn;
    @FXML
    private Label proxyStatusLabel;
    @FXML
    private TableColumn<VulInfo, String> cvename;
    @FXML
    private Label tool_name;
    @FXML
    private ChoiceBox thread;
    @FXML
    private TableColumn<VulInfo, String> id;
    @FXML
    private TextField jndi_path;
    @FXML
    private TextField mTextArea14;
    @FXML
    private TableView<VulInfo> table_view;
    @FXML
    private TextField upload_path;
    @FXML
    private Text time;
    public static Map<String, Object> settingInfo = new HashMap();
    @FXML
    private TextArea mTextArea15;
    @FXML
    private RadioButton mRadiobutton4;
    @FXML
    private TextArea id_unalivearea;
    @FXML
    private RadioButton mRadiobutton3;
    @FXML
    private RadioButton mRadiobutton5;
    @FXML
    private TextField url;
    @FXML
    private TextField file_path;
    @FXML
    private ChoiceBox encoding;
    @FXML
    private ChoiceBox choice_cve;
    @FXML
    private ChoiceBox platform;
    @FXML
    private ListView<String> id_listview;
    @FXML
    private TextArea upload_msg;
    @FXML
    private TextArea cmd_info;
    @FXML
    private ChoiceBox jndi_platform;
    @FXML
    private TextArea basic_info;
    private boolean isStop = false;
    @FXML
    private Button id_startcheck;
    @FXML
    private TextArea shellhelp_info;
    public static HashSet<String> fofa_result = new HashSet();
    private ExpStrategy ei;
    ExecutorService threadPool;
    @FXML
    private TextArea id_alivearea;
    @FXML
    private TextArea jndi_info;
    @FXML
    private Label id_status;
    @FXML
    private TextField cmd;

    /**
     * 漏洞检测
     */
    @FXML
    public void choice_CVS(){

    }

    /**
     * 漏洞检测
     */
    @FXML
    public void get_url(){
        String url = this.url.getText().trim();
        String cve = this.choice_cve.getValue().toString().trim();



    }

    /**
     * cmd命令执行
     */
    @FXML
    public void get_execute_cmd(){

    }

    /**
     * jndi方式命令执行
     */
    @FXML
    public void jndi_exec() {

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

    @FXML
    public void startCheck(){

    }

    @FXML
    public void stop(){

    }
}
