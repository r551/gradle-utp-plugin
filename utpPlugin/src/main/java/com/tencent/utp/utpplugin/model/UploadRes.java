package com.tencent.utp.utpplugin.model;

import java.util.List;

/**
 * Created by yoyoqin on 2018/3/28.
 */

public class UploadRes {
    public String status;
    public List<String> files;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
