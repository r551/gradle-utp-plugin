package com.tencent.utp.utpplugin;

import com.google.gson.Gson;
import com.tencent.utp.utpplugin.model.CheckTestRes;
import com.tencent.utp.utpplugin.model.SubmitTestRes;
import com.tencent.utp.utpplugin.model.UploadRes;
import com.tencent.utp.utpplugin.net.HttpPostFile;
import com.tencent.utp.utpplugin.net.HttpPostJson;
import com.tencent.utp.utpplugin.net.UtpSubmitReq;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yoyoqin on 2018/3/25.
 */

public class UtpTask extends DefaultTask {
    private Map<String, File> fileMap = new LinkedHashMap<>();
    private File mTestScriptZip;
    private String mScriptsCommand;
    private String mCaseSet;
    private String mLogFile;
    private String user;

    @InputFile
    public File getTestScriptZip() {
        return mTestScriptZip;
    }

    public void setTestScriptZip(File testScriptZip) {
        this.mTestScriptZip = testScriptZip;
    }

    @Input
    public String getScriptsCommand() {
        return mScriptsCommand;
    }

    public void setScriptsCommand(String scriptsCommand) {
        this.mScriptsCommand = scriptsCommand;
    }

    @Input
    public String getCaseSet() {
        return mCaseSet;
    }

    public void setCaseSet(String caseSet) {
        this.mCaseSet = caseSet;
    }

    @InputFiles
    public Iterable<File> getFiles()
    {
        return fileMap.values();
    }

    public void addFile(String key, File value)
    {
        fileMap.put(key, value);
    }

    @OutputFile
    public File getLogFile(){
        File logFolder = new File(
                getProject().getBuildDir().getPath() + File.separator + "outputs");
        File logFile = new File(logFolder, "utptask.log");
        mLogFile = logFile.getPath();
        return  logFile;
    }

    @Input
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private void action(FileOutputStream logOutPut) throws Exception {
        if (this.user == null)
        {
            this.user = "user";
        }
        String fileDir = this.user + Util.getCurrentTime();

        // init upload req
        HttpPostFile httpPostFile = new HttpPostFile("http://xxx.com/file/uploadtask");
        httpPostFile.addTextParameter("fileDir", fileDir);

        // build submit test task req
        HttpPostJson httpPostJson = new HttpPostJson(
                "http://xxx.com/trigger/submit");
        UtpSubmitReq utpSubmitReq = httpPostJson.getJsonParams();
        List<UtpSubmitReq.FileParam> fileList = new ArrayList<>();
        utpSubmitReq.fileList = fileList;

        // do upload
        for (Map.Entry<String, File> entry : fileMap.entrySet())
        {
            String key = entry.getKey();
            File file = entry.getValue();
            String name = file.getName();
            write(logOutPut, "UTP:" + key + ":" + file.getPath());
            httpPostFile.addFileParameter("file", file);
            String ret = new String(httpPostFile.send());
            write(logOutPut, "UTP:Upload result:" + ret);
            // parse parameters for submit test
            Gson gson = new Gson();
            UploadRes result = gson.fromJson(ret, UploadRes.class);
            UtpSubmitReq.FileParam fileParam = new UtpSubmitReq.FileParam();
            fileParam.fname = name;
            fileParam.furl = result.getFiles().get(0);
            fileParam.fkey = key;
            fileList.add(fileParam);
        }

        httpPostFile.addFileParameter(
                "file", mTestScriptZip);
        String ret = new String(httpPostFile.send());
        System.out.println("UTP:Upload result:" + ret);
        Gson gson = new Gson();
        UploadRes result = gson.fromJson(ret, UploadRes.class);
        utpSubmitReq.caseFile = new UtpSubmitReq.FileParam();
        utpSubmitReq.caseFile.furl = result.getFiles().get(0);
        utpSubmitReq.caseFile.fname = "scripts.zip";

        // FIXME hard code now
        utpSubmitReq.testScriptResultFolder = "output";

        UtpSubmitReq.DeviceFilter deviceFilter = new UtpSubmitReq.DeviceFilter();
        deviceFilter.os = "6.0";
        deviceFilter.type = "emulator";
        deviceFilter.resol = "";
        utpSubmitReq.deviceFilter = deviceFilter;

        utpSubmitReq.testScript = this.mScriptsCommand;

        List<UtpSubmitReq.CaseParam> testCaseList = new ArrayList<>();
        String[] testCases = this.mCaseSet.split(",");
        for (String testCase : testCases)
        {
            UtpSubmitReq.CaseParam caseParam  = new UtpSubmitReq.CaseParam();
            caseParam.caseName = testCase.trim();
            if (null != caseParam.caseName && !caseParam.caseName.isEmpty())
            {
                testCaseList.add(caseParam);
            }
        }
        utpSubmitReq.testCase = testCaseList;

        // do submit test
        String submitTestRes = new String(httpPostJson.send());
//        System.out.println("UTP:SubmitTestRes:" + submitTestRes);
        SubmitTestRes submitTestResObject = gson.fromJson(submitTestRes, SubmitTestRes.class);
        write(logOutPut, "UTP prediction estimate_time:" + submitTestResObject.getMsg().getEstimate_time());

        // parse task token
        String body = submitTestRes.substring(
                submitTestRes.indexOf("msg") + 6, submitTestRes.indexOf("ret") - 2);
        body = "{" +body + "}";
//        System.out.println(body);

        if (submitTestResObject.getMsg().getRet() == -1)
        {
            write(logOutPut, "exception:");
            write(logOutPut, submitTestResObject.getMsg().toString());
            throw new RuntimeException("submit fail");
        }

        String queryStatusRsp = null;
        int count = 0;
        while (true)
        {
            if (count > 120) {
                write(logOutPut, "Timeout");
                break;
            }
            if (queryStatusRsp != null && queryStatusRsp.contains("\"finish\":true")) {
                write(logOutPut, "finish");
                break;
            }
            if (queryStatusRsp != null && queryStatusRsp.contains("\"ret\":-1")) {
                write(logOutPut, "exception");
                break;
            }
            HttpPostJson getRetReq = new HttpPostJson("http://xxx.com/getTestStats");
            queryStatusRsp = new String(getRetReq.send(body));
//            System.out.println(queryStatusRsp);

            CheckTestRes checkTestResObject = gson.fromJson(queryStatusRsp, CheckTestRes.class);
            List<CheckTestRes.CheckTestResMsgStatus> checkTestResMsgStatusList = checkTestResObject.getMsg().getList();
            StringBuilder sb = new StringBuilder();
            int i = 0;
            sb.append('[');
            for (CheckTestRes.CheckTestResMsgStatus checkTestResMsgStatus : checkTestResMsgStatusList)
            {
                i++;
                sb.append("task");
                sb.append(i);
                sb.append(':');
                sb.append(checkTestResMsgStatus.getProgress());
                sb.append(" ");
            }
            sb.append(']');
            write(logOutPut, "UTP running progress:" + sb.toString());

            if (checkTestResObject.getMsg().isFinish())
            {
                write(logOutPut, "UTP running finish:" + checkTestResObject.getMsg().getSummary().getResult());
                write(logOutPut, "UTP result url:" + checkTestResObject.getMsg().getSummary().getMail_url());
            }
            Thread.sleep(20000);
            count++;
        }
    }

    private void write(FileOutputStream logOutPut, String content) throws Exception {
        String contentLine = content + "\n";
        System.out.print(contentLine);
        logOutPut.write(contentLine.getBytes(Charset.defaultCharset()));
        logOutPut.flush();
    }
    @TaskAction
    public void submitToUtp() throws Exception {
        File logFile = new File(mLogFile);
        System.out.println("UTP:LogFile:" + logFile.getPath());
        logFile.deleteOnExit();
        FileOutputStream fos = null;
        try {
            logFile.createNewFile();
            fos = new FileOutputStream(logFile);
            action(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e)
        {
            throw e;
        }
        finally {
            if (null != fos)
            {
                try {
                    fos.close();
                }
                catch (Exception e) {
                }
            }
        }
    }
}
