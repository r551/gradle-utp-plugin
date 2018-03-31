package com.tencent.utp.utpplugin.net;

import java.util.List;

public class UtpSubmitReq {
    
    public static class FileParam {
        public String fkey;
        public String fpath;
        public String fname;
        public String furl;
    }

    public static class CaseParam {
        public String caseName;
    }

    public static class DeviceFilter {
        public String type;
        public String os;
        public String resol;
    }

    public FileParam caseFile;

    public boolean concurrent = true;
    public List<CaseParam> testCase;

    public List<FileParam> fileList;

    public String testScript;
    public String testScriptResultFolder = "result";

    public DeviceFilter deviceFilter;

    public Long productId;
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        
        sb.append("{");
        if (productId != null) {
            sb.append("\"productId\":").append(productId).append(",");
        }
        
        sb.append("\"caseFile\":{");
        sb.append("\"fname\":\"").append(caseFile.fname).append("\",");
        sb.append("\"furl\":\"").append(caseFile.furl).append("\"");
        sb.append("},");
        
        sb.append("\"fileList\":[");
        if (fileList != null) {
            int idx = 0;
            for (FileParam f: fileList) {
                idx++;
                sb.append("{");
                sb.append("\"fkey\":\"").append(f.fkey).append("\",");
                sb.append("\"fname\":\"").append(f.fname).append("\",");
                sb.append("\"fpath\":\"").append(f.fpath).append("\",");
                sb.append("\"furl\":\"").append(f.furl).append("\"");
                sb.append("}");
                if (idx < fileList.size()) {
                    sb.append(",");
                }
            }            
        }
        sb.append("],");
        
        sb.append("\"concurrent\":").append(concurrent).append(",");
        
        sb.append("\"testCase\":[");
        if (testCase != null) {
            int idx = 0;
            for (CaseParam cs: testCase) {
                idx++;
                sb.append("{");
                sb.append("\"caseName\":\"").append(cs.caseName).append("\"");
                sb.append("}");
                if (idx < testCase.size()) {
                    sb.append(",");
                }
            }            
        }
        sb.append("],");
        
        sb.append("\"testScript\":\"").append(testScript).append("\",");
        sb.append("\"testScriptResultFolder\":\"").append(testScriptResultFolder).append("\",");
        
        sb.append("\"deviceFilter\":{");    
        if (deviceFilter != null) {
            sb.append("\"type\":\"").append(deviceFilter.type).append("\",");
            sb.append("\"resol\":\"").append(deviceFilter.resol).append("\",");
            sb.append("\"os\":\"").append(deviceFilter.os).append("\"");
        }
        sb.append("}");
        
        sb.append("}");
        return sb.toString();
    }

}

