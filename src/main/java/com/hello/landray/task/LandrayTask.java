package com.hello.landray.task;

import com.hello.PurposeEnum;
import com.hello.factory.ExpFactory;
import com.hello.strategy.ExpStrategy;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.concurrent.Task;

import java.util.List;

public class LandrayTask extends Task<Object> {

    public interface IMakeResultText {
        void doSuccess(String v);
        void doFailure(String v);
        void doFinish();
    }
    private IMakeResultText makeResultText;
    public void setClickDelegate(IMakeResultText makeResultText) {
        this.makeResultText = makeResultText;
    }
    /**
     * 线程名称
     */
    private String name;
    /**
     * 漏洞名称
     */
    private String expName;
    /**
     * 检测地址
     */
    private String url;

    private Boolean isAll = false;

    /**
     * 只读对象包装器
     */
    private ReadOnlyObjectWrapper<String> results =
            new ReadOnlyObjectWrapper<>(this, "Results", "");
    public LandrayTask(String url, String expName) {
        this.expName = expName;
        this.url = url;
    }

    @Override
    protected Object call() throws Exception {
        try {
            //全部exp检测
            if (expName.equals(PurposeEnum.ALL.getExpType())){
                List<String> allExp = PurposeEnum.getExpType(PurposeEnum.LandrayOA.getExpType());
                for (String expName : allExp) {
                    ExpStrategy LandrayOA = ExpFactory.getFactApplyStrategy(expName);
                    String result = LandrayOA.checkVul(this.url);
                    //把检测结果添加到文本框
                    setMsg(result);
                }

            }else{//单个exp检测
                ExpStrategy LandrayOA = ExpFactory.getFactApplyStrategy(expName);
                String result = LandrayOA.checkVul(this.url);
                //把检测结果添加到文本框
                setMsg(result);
            }
        } catch (Exception exception) {
            if (isCancelled()) {
                updateMessage("Cancelled");
            }
        }
        updateMessage("Done!");
        makeResultText.doFinish();
        return results.get();
    }

    /**
     * 获取任务名称
     *
     * @return 任务名称
     */
    public String getName() {
        return name;
    }

    /**
     * 把检测结果添加到文本框
     * @param result 文本内容
     */
    private void setMsg(String result){
        if (makeResultText != null){
            if (result.contains("成功")){
                makeResultText.doSuccess(result);
            }else {
                makeResultText.doFailure(result);
            }
        }
    }
    /**
     * 获取只读对象
     * @return 只读对象
     */
    public final String getResults() {
        return results.get();
    }
}
