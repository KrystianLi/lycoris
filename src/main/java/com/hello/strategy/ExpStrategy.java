package com.hello.strategy;

public abstract class ExpStrategy{
    public boolean isVul(){
        throw new UnsupportedOperationException();
    }

    /**
     * 命令执行函数
     * @param url 漏洞url
     * @param cmd 待执行的命令
     * @return 命令执行结果
     * @throws Exception
     */
    public String exeCMD(String url, String cmd) {
        throw new UnsupportedOperationException();
    }

    /**
     * 漏洞检测
     * @param url 待检测url
     * @return
     */
    public String checkVul(String url){
        throw new UnsupportedOperationException();
    }

    /**
     * 漏洞基础信息
     * @return 返回漏洞信息
     */
    public String baseInfo(){
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param url 漏洞url
     * @param filename  上传文件名
     * @param fileContent   上传文件内容
     * @return  上传结果
     */
    public String uploadFile(String url, String filename ,String fileContent){ throw new UnsupportedOperationException();}

    public String searchCompanyName(String keyWord,String filterWorld){ throw new UnsupportedOperationException();}
    public String searchCompanyBeian(String keyWord,String filterWorld){ throw new UnsupportedOperationException();}
}
