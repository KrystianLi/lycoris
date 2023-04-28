package com.hello.info.service;

import com.hello.info.controller.Controller;
import com.hello.info.model.CompanyBeianTableModel;
import com.hello.info.model.CompanyTableModel;
import com.hello.strategy.ExpStrategy;
import com.hello.tools.DateUtil;
import com.hello.tools.MyExecutor;
import javafx.application.Platform;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TianYanService extends ExpStrategy {

    private final int PAGE_SIZE = 20;
    private int id = 0;
    private boolean isFirst = true;
    private boolean isBreak = false;
    private List<CompanyTableModel> companyNameList = new ArrayList<>();
    private List<CompanyBeianTableModel> companyBeianList = new ArrayList<>();
    private final String BASE_NAME_URL = "https://www.tianyancha.com";
    private final String BASE_BEIAN_URL = "https://beian.tianyancha.com";
    private ThreadPoolExecutor executor;
    @Override
    public String searchCompanyName(String keyWord, String filterWorld) {
        executor = MyExecutor.getExecutor();
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                String encodeKeyword = URLEncoder.encode(keyWord, "UTF-8");
                Platform.runLater(() -> Controller.getInstance().getCompanyTable().getItems().clear());
                getCompanyName(encodeKeyword);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        },executor);

        future.thenRunAsync(() -> {
            // 任务执行完成后更新进度
            Platform.runLater(() -> Controller.getInstance().logCompanyNameArea.appendText("[+]天眼查: 查询公司名称, 搜索完成\n"));
        });
        return "";
    }

    private void getCompanyName(String keyWord){
            try{
                //日期格式：2022-01-01
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String endDate = sdf.format(date);
                String startTime = "";

                //获取最早成立的公司和总数，记录下时间为当前日期 &sortType=4
                String uri = "/search?key="+keyWord+"&sortType=4";
                Document doc = Jsoup.parse(new URL(BASE_NAME_URL+uri), 3000);
                List<CompanyTableModel> stringList = parseCompanyName(doc);
                List<String> dateList = stringList.stream()
                        .map(obj -> obj.getDate())
                        .collect(Collectors.toList());
                startTime = DateUtil.ascDate(dateList).get(0);

                System.out.println(startTime);
                Element totalSpan = doc.select("span.index_title-count__lDSjB").first();
                int total = Integer.parseInt(totalSpan.text().replaceAll("\\D+", ""));
                Platform.runLater(() ->Controller.getInstance().logCompanyNameArea.appendText("[+]天眼查: 查询公司名称, 共计搜索"+total+"条\n"));
                if (total<0){
                    return;
                }
                if (isFirst){
                    id = 0;
                    isFirst = false;
                }

                //循环，从当前日期查询一年内的公司，直到当前日期等于实际日期
                while (!DateUtil.DateComparisonAfter(startTime,endDate) && companyNameList.size() < total && !isBreak){
                    for (int i = 1; i <= 5; i++){
    //                    记录5页以内包含第5页的最近日期，替换为当前日期
                        String url = "https://www.tianyancha.com/search?key=" + keyWord + "&estiblishTimeStart="+startTime+"&pageNum="+i+"&sortType=4";
                        Platform.runLater(()->Controller.getInstance().logCompanyNameArea.appendText("[+]天眼查: 正在访问: "+url+"\n"));
                        Document tempDoc = Jsoup.parse(new URL(url), 3000);
                        List<CompanyTableModel> tempList = parseCompanyName(tempDoc);
                        companyNameList = Stream.concat(companyNameList.stream(), tempList.stream())
                                .collect(Collectors.toList());
                        int tempNum = companyNameList.size();
                        Platform.runLater(()->Controller.getInstance().logCompanyNameArea.appendText("[+]天眼查: 已经查询到"+tempNum+"条\n"));
                        //更新数据表格
                        for (CompanyTableModel companyTableModel : tempList) {
                            Platform.runLater(()-> Controller.getInstance().getCompanyTable().getItems().add(companyTableModel));
                        }

                        //如果一年内查询不超过5页，表示我们搜索访问数据检索完成
                        if (tempList.isEmpty()){
                            startTime = DateUtil.addDate(startTime, 1);
                            break;
                        } else if (i==5) {//否则不需要加一年，而是以第五页的最近日期开始检索
                            List<String> tempDateList = tempList.stream().map(u -> u.getDate()).collect(Collectors.toList());
                            startTime = DateUtil.descDate(tempDateList).get(0);
                        } else if (i == 2 && tempList.isEmpty()){
                            //如果数据量较少不满足分页，可以结束查询
                            isBreak = true;
                            break;
                        }


                    }
                }
                int tempNum = companyNameList.size();
                Platform.runLater(()->Controller.getInstance().logCompanyNameArea.appendText("[+]天眼查: 共计查询到"+tempNum+"条\n"));
    //            System.out.println("共计查询到"+companyNameList.size()+"条");

            }catch (Exception e){
                for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                    System.out.println(stackTraceElement);
                }
            }
    }

    private List<CompanyTableModel> parseCompanyName(Document doc){
        List<CompanyTableModel> companyTableModels = new ArrayList<>();
        try {
            //判断是否存在
            Elements select = doc.select("div:contains(抱歉，没有找到相关企业！)");
            if (!select.isEmpty()){
                return companyTableModels;
            }
            //一般来讲每页有20个，按大的div划分，再从每一个中提取对应信息
            Elements divs = doc.select("div.index_search-item-center__Q2ai5");
            for (Element div : divs) {
                StringBuilder stringBuilder = new StringBuilder();
                Elements aTag = div.select("a.index_alink__zcia5.link-click");
                String href = aTag.attr("href");
                String text = aTag.select("span").text();
//                System.out.println("详细内容: "+href);
//                System.out.println("公司名: "+text);
                Optional<Elements> dateTagNull = Optional.ofNullable(div.select("div:containsOwn(成立):containsOwn(日期).index_info-col__UVcZb"));
                Element dateTag = null;
                if (dateTagNull.isPresent()) {
                    dateTag = dateTagNull.get().first();
                }
                String date = dateTag == null ? "" : dateTag.select("span").text();
//                System.out.println("成立日期: "+date);
                Elements emailSpans = doc.select("span.label:contains(邮箱)");
                String emailContent = "";
                if (!emailSpans.isEmpty()) {
                    Element emailSpan = emailSpans.first();
                    Element sibling = emailSpan.nextElementSibling();
                    if (sibling != null) {
                        emailContent = sibling.text();
                    }
                }
//                System.out.println("邮箱: "+ emailContent);
                Elements shareholderSpans = div.select("div.index_match-field-item__RO4Yi:contains(股东信息)");
                String shareholderContent = "";
                if (!shareholderSpans.isEmpty()) {
                    Element shareholderSpan = shareholderSpans.first();
                    if (shareholderSpan != null) {
                        shareholderContent = shareholderSpan.select("span.index_jump-other__ImOm6").text();
                    }
                }

//                System.out.println("股东信息: "+shareholderContent);
                Elements activeDiv= div.select("div.index_tag-common__YTBxL.index_tag-normal-bg__J1uuI");
                String activeContent ="";
                if (activeDiv != null){
                    activeContent = activeDiv.text();
                }
//                System.out.println("活跃状态: " + activeContent+"\n");
                stringBuilder.append("详细内容: "+ href+"\n");
                stringBuilder.append("公司名: "+text+"\n");
                stringBuilder.append("成立日期: "+date+"\n");
                stringBuilder.append("邮箱: "+ emailContent+"\n");
                stringBuilder.append("股东信息: "+shareholderContent+"\n");
                CompanyTableModel companyTableModel = new CompanyTableModel(id++,text,date,emailContent,shareholderContent,activeContent);
                companyTableModels.add(companyTableModel);
            }
        } catch (Exception e) {
            System.out.println("异常类型：" + e.getClass().getName());
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                System.out.println(stackTraceElement);
            }
        }

        return companyTableModels;
    }

    @Override
    public String searchCompanyBeian(String keyWord, String filterWorld) {
        ThreadPoolExecutor executor = MyExecutor.getExecutor();
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                String encodeKeyword = URLEncoder.encode(keyWord, "UTF-8");
                Platform.runLater(() -> Controller.getInstance().getBeianTable().getItems().clear());
                getCompanyBeian(encodeKeyword);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        },executor);

        future.thenRunAsync(() -> {
            // 任务执行完成后更新进度
            Platform.runLater(() -> Controller.getInstance().logCompanyNameArea.appendText("[+]天眼查: 查询备案, 搜索完成\n"));
        });
        return "";
    }

    private void getCompanyBeian(String keyWord){
        try {
            String uri = "/search/"+keyWord;
            Document doc = Jsoup.parse(new URL(BASE_BEIAN_URL+uri), 3000);

            Elements select = doc.select("div:contains(抱歉，没有找到相关结果！)");
            if (!select.isEmpty()){
                return ;
            }
            //解析查询总数
            String total = doc.select("span.beian-name").text();
            //解析总页数
            int pageNum = Integer.parseInt(total) / PAGE_SIZE;
            List<CompanyBeianTableModel> tempList = parseCompanyBeian(doc);
            companyBeianList = Stream.concat(companyBeianList.stream(), tempList.stream())
                    .collect(Collectors.toList());
            if (pageNum != 0){
                for (int i = 1; i < pageNum; i++) {
                    Document tempDoc = Jsoup.parse(new URL(BASE_BEIAN_URL+uri+"p"+i), 3000);
                    tempList = parseCompanyBeian(tempDoc);
                    companyBeianList = Stream.concat(companyBeianList.stream(), tempList.stream())
                            .collect(Collectors.toList());
                }
            }
            //获取实际总条数
            String tempNum = companyBeianList.get(companyBeianList.size()).getId();
            Platform.runLater(()->Controller.getInstance().logCompanyNameArea.appendText("[+]天眼查: 共计查询到"+tempNum+"条\n"));

        }catch (Exception e){
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                System.out.println(stackTraceElement.toString());
            }
        }
    }

    private List<CompanyBeianTableModel> parseCompanyBeian(Document doc){
        List<CompanyBeianTableModel> companyTableModels = new ArrayList<>();
        Element tbody = doc.select("tbody").first();
        Elements rows = tbody.select("tr");
        for (Element row : rows) {
            Elements cells = row.select("td");
            String id = cells.get(0).text();
            String beianNo = cells.get(1).text();
            Elements links = cells.get(1).select("a");
            String href = links.attr("href");
            String companyName = cells.get(2).text();
            String siteName = cells.get(3).text();
            String siteDomain = cells.get(4).text();
            String date = cells.get(5).text();
            CompanyBeianTableModel companyBeianTableModel = new CompanyBeianTableModel(id, beianNo,href, companyName, siteName, siteDomain, date);
            companyTableModels.add(companyBeianTableModel);
        }
        return companyTableModels;
    }
    @Override
    public Boolean stopCompany() {
        return false;
    }
}
