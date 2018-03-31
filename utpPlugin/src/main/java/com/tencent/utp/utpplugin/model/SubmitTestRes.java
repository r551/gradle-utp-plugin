package com.tencent.utp.utpplugin.model;

import java.util.List;

/**
 * Created by yoyoqin on 2018/3/29.
 */

public class SubmitTestRes {
    int res;
    SubmitTestResMsg msg;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public SubmitTestResMsg getMsg() {
        return msg;
    }

    public void setMsg(SubmitTestResMsg msg) {
        this.msg = msg;
    }

    public static class SubmitTestResMsg {
        int concurrent_count;
        long estimate_time;
        String buildUrl;
        List<String> handlers;
        int ret;

        public int getConcurrent_count() {
            return concurrent_count;
        }

        public void setConcurrent_count(int concurrent_count) {
            this.concurrent_count = concurrent_count;
        }

        public long getEstimate_time() {
            return estimate_time;
        }

        public void setEstimate_time(long estimate_time) {
            this.estimate_time = estimate_time;
        }

        public String getBuildUrl() {
            return buildUrl;
        }

        public void setBuildUrl(String buildUrl) {
            this.buildUrl = buildUrl;
        }

        public List<String> getHandlers() {
            return handlers;
        }

        public void setHandlers(List<String> handlers) {
            this.handlers = handlers;
        }

        public int getRet() {
            return ret;
        }

        public void setRet(int ret) {
            this.ret = ret;
        }

        @Override
        public String toString() {
            return "SubmitTestResMsg{" +
                    "concurrent_count=" + concurrent_count +
                    ", estimate_time=" + estimate_time +
                    ", buildUrl='" + buildUrl + '\'' +
                    ", handlers=" + handlers +
                    ", ret=" + ret +
                    '}';
        }
    }
}
