package com.hello;

import java.util.ArrayList;
import java.util.List;

public enum PurposeEnum {
    //全部漏洞
    ALL("ALL","",""),
    //蓝凌OA
    LandrayOA("LandrayOA", "", ""),
    LandrayUnauthorizedRCE("LandrayOA", "LandrayUnauthorizedRCE", "com.yeq.landray.service.LandrayUnauthorizedRCEService"),

    //信息收集
    Info("Info", "", ""),
    TianYan("Info","天眼查","com.hello.info.service.TianYanService"),
    AiQi("Info","爱企查","com.hello.info.service.AiQiService"),
    ;

    private String expType;
    private String expName;
    private String className;

    private PurposeEnum(String expType, String expName, String className) {
        this.expType = expType;
        this.expName = expName;
        this.className = className;
    }

    public String getExpType() {
        return expType;
    }

    public String getExpName() {
        return expName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 根据type类型返回同类型的exp名字
     * @param expType   exp类型
     * @return
     */
    public static List<String> getExpType(String expType){
        List<String> lists = new ArrayList<>();
        for (PurposeEnum be : values()) {
            if (be.getExpType().equals(expType) && !be.getExpName().isEmpty()) {
                lists.add(be.getExpName());
            }
        }
        return lists;
    }

    public static PurposeEnum getEnumObjByExpName(String expName) {
        for (PurposeEnum be : values()) {
            if (be.getExpName().equals(expName)) {
                return be;
            }
        }
        return null;
    }
}
