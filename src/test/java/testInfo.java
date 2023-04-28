import com.hello.info.controller.Controller;
import com.hello.tools.DateUtil;
import com.hello.tools.MyExecutor;
import javafx.application.Platform;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class testInfo {
    @Test
    public void testDate(){

        List<String> listDate = new ArrayList<>();
        listDate.add("详细内容: https://www.tianyancha.com/company/62628469\n公司名: 宁波兴光燃气集团有限公司\n成立日期: 1981-12-01\n邮箱: nbgas@mail.nbppt.zj.cn\n股东信息: \n");
        listDate.add("详细内容: https://www.tianyancha.com/company/62628469\n公司名: 宁波兴光燃气集团有限公司\n成立日期: 1982-12-01\n邮箱: nbgas@mail.nbppt.zj.cn\n股东信息: \n");
        listDate.add("详细内容: https://www.tianyancha.com/company/62628469\n公司名: 宁波兴光燃气集团有限公司\n成立日期: 1981-12-02\n邮箱: nbgas@mail.nbppt.zj.cn\n股东信息: \n");
        listDate.add("详细内容: https://www.tianyancha.com/company/62628469\n公司名: 宁波兴光燃气集团有限公司\n成立日期: 1981-11-01\n邮箱: nbgas@mail.nbppt.zj.cn\n股东信息: \n");
        listDate.add("详细内容: https://www.tianyancha.com/company/62628469\n公司名: 宁波兴光燃气集团有限公司\n成立日期: 1921-12-01\n邮箱: nbgas@mail.nbppt.zj.cn\n股东信息: \n");
        List<String> listDate1 = new ArrayList<>();
        for (String d : listDate) {
            String 日期 = Arrays.stream(d.split("\n")).filter(s -> s.contains("日期")).findFirst().get().split(":")[1].trim();
            listDate1.add(日期);
        }
        String s = DateUtil.descDate(listDate1).get(0);
        System.out.println(s);
    }

    public void testFor(){
        int i =0;
        while (true){
            System.out.println(i++);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void testTh() throws InterruptedException {
//        ExecutorService executor = Executors.newFixedThreadPool(10);
        ThreadPoolExecutor executor = MyExecutor.getExecutor();
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // 执行一些耗时的操作
            testFor();
        }, executor);
        future.thenRunAsync(() -> {
            // 任务执行完成后更新进度
            System.out.println("任务完成");
        });
        Thread.sleep(5000);
        System.out.println("开始暂停");
//        future.cancel(true); // 取消CompletableFuture
//        executor.shutdown(); // 关闭线程池
//        try {
//            executor.awaitTermination(5, TimeUnit.SECONDS); // 等待线程池中的任务完成
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        executor.shutdownNow(); // 立即关闭线程池
        System.out.println("暂停结束");
        while (true){

        }
    }
}
