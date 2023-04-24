import com.hello.tools.DateUtil;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
}
