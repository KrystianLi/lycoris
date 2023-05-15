package com.hello.assist.service;

import com.hello.assist.controller.Controller;
import javafx.application.Platform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class AssistService {

    public void compareToTextArea(String primaryContent, String secondaryContent) throws IOException {
        List<String> list1 = Arrays.stream(primaryContent.split("\n")).collect(Collectors.toList());
        List<String> list2 =  Arrays.stream(secondaryContent.split("\n")).collect(Collectors.toList());
        Platform.runLater(() -> Controller.getInstance().insertResTextArea.clear());
        Platform.runLater(() -> Controller.getInstance().reduceResTextArea.clear());
        System.out.println("比较primary区域文本和secondary区域文本，以primary区域文本为主");
        List<String> finalList = list1.stream().filter(line ->
                list2.stream().filter(line2 -> line2.equals(line)).count() == 0
        ).collect(Collectors.toList());

        List<String> finalList2 = list2.stream().filter(line ->
                list1.stream().filter(line1 -> line1.equals(line)).count() == 0
        ).collect(Collectors.toList());
        if (finalList.size() != 0) {
            System.out.println("[+]主内容比多数据：");
            finalList.forEach(one -> Platform.runLater(() -> Controller.getInstance().insertResTextArea.appendText(one+"\n")));
        }else {
            Platform.runLater(() -> Controller.getInstance().insertResTextArea.appendText("[-]主内容比多数据无差异\n"));
        }

        if (finalList2.size() != 0) {
            System.out.println("[+]主内容比少数据：");
            finalList2.forEach(one -> Platform.runLater(() -> Controller.getInstance().reduceResTextArea.appendText(one+"\n")));
        }else {
            Platform.runLater(() -> Controller.getInstance().reduceResTextArea.appendText("[-]主内容比少数据无差异\n"));
        }
    }



    public void compareToFile(String primaryPath, String secondaryPath) throws IOException {
        List<String> list1 =  Files.readAllLines(Paths.get(primaryPath));
        List<String> list2 =  Files.readAllLines(Paths.get(secondaryPath));

        System.out.println("比较"+primaryPath+"和 "+secondaryPath+" 两个文件，以 "+primaryPath+" 为主");
        List<String> finalList = list1.stream().filter(line ->
                list2.stream().filter(line2 -> line2.equals(line)).count() == 0
        ).collect(Collectors.toList());

        List<String> finalList2 = list2.stream().filter(line ->
                list1.stream().filter(line1 -> line1.equals(line)).count() == 0
        ).collect(Collectors.toList());
        if (finalList.size() != 0) {
            System.out.println("[+]主文件比多数据：");
            finalList.forEach(one -> System.out.println(one));
        }else {
            System.out.println("[-]主文件比多数据无差异");
        }

        if (finalList2.size() != 0) {
            System.out.println("[+]主文件比少数据：");
            finalList2.forEach(one -> System.out.println(one));
        }else {
            System.out.println("[-]主文件比少数据无差异");
        }
    }


    public void repetition(String text){
        List<String> list = Arrays.stream(text.split("\n")).map(str -> str.trim()).collect(Collectors.toList());

        LinkedHashSet<String> hashSet = new LinkedHashSet<>(list);

        ArrayList<String> listWithoutDuplicates = new ArrayList<>(hashSet);

        listWithoutDuplicates.forEach( str -> {Platform.runLater(() -> Controller.getInstance().repResultTextArea.appendText(str+"\n"));});
    }
}
