package com.hello.factory;

import com.hello.PurposeEnum;
import com.hello.strategy.ExpStrategy;

public class ExpFactory {
    //使用策略工厂获取具体策略实现
    public static ExpStrategy getFactApplyStrategy(String expName) {
        try {
            return (ExpStrategy) Class.forName(PurposeEnum.getEnumObjByExpName(expName).getClassName()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
