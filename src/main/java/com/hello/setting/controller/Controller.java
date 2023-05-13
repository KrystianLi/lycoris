package com.hello.setting.controller;

import com.hello.tools.net.HttpClient45;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.util.Optional;

public class Controller {
    @FXML
    private CheckBox isProxyCheckBox;

    @FXML
    private TextField proxyIPTextFiled;

    @FXML
    private TextField proxyPortTextFiled;

    @FXML
    void startProxy(ActionEvent event) {
        String ip = Optional.ofNullable(proxyIPTextFiled.getText()).orElse("");
        String port = Optional.ofNullable(proxyPortTextFiled.getText()).orElse("");

        if (isProxyCheckBox.isSelected()) {
            HttpClient45.isfProxySetting = true;
            HttpClient45.ip = ip;
            HttpClient45.port = Integer.parseInt(port);
        }else {
            HttpClient45.isfProxySetting = false;
        }

    }
}
