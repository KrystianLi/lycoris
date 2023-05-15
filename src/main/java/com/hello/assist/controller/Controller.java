package com.hello.assist.controller;

import com.hello.assist.service.AssistService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class Controller {
    private static Controller instance;

    private AssistService assistService;

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
}
