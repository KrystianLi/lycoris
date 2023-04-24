package com.hello.info.service;

import com.hello.info.controller.Controller;
import com.hello.strategy.ExpStrategy;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class AiQiService extends ExpStrategy {

    @Override
    public String searchCompanyName(String keyWord, String filterWorld) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                String url = "https://www.example.com";
                Document doc = Jsoup.parse(new URL(url), 3000);


                for (int i = 0; i < 1; i++) {
                    // 模拟任务执行
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // 更新进度
                    int progress = i + 1;
                    Platform.runLater(() -> Controller.getInstance().logCompanyNameArea.appendText("爱企查 start——查询公司名称"+progress+"\n"));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        future.thenRunAsync(() -> {
            // 任务执行完成后更新进度
            Platform.runLater(() -> Controller.getInstance().logCompanyNameArea.appendText("爱企查 start——查询公司名称——完成\n"));
        });

        return "";
    }

    @Override
    public String searchCompanyBeian(String keyWord, String filterWorld) {
        System.out.println("爱企查 start——查询公司备案");
        return "";
    }


}
