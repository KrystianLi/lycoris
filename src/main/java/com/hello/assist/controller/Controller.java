package com.hello.assist.controller;

import com.hello.assist.service.AssistService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class Controller {
    private static Controller instance;


    @FXML
    private TextArea primaryTextArea;

    @FXML
    private TextArea secondaryTextArea;
    @FXML
    public TextArea insertResTextArea;
    @FXML
    public TextArea reduceResTextArea;

    @FXML
    private void compare(){
        AssistService assistService = new AssistService();
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

    public static Controller getInstance() {
        return instance;
    }

    public Controller() {
        instance = this;
    }
}
