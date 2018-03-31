package com.tencent.utp.utpplugin.model;

import java.util.List;

/**
 * Created by yoyoqin on 2018/3/29.
 */

public class CheckTestRes {
    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public CheckTestResMsg getMsg() {
        return msg;
    }

    public void setMsg(CheckTestResMsg msg) {
        this.msg = msg;
    }

    int res;
    CheckTestResMsg msg;

    public static class CheckTestResMsg {
        List<CheckTestResMsgStatus> list;
        int duration;
        CheckTestResMsgSummary summary;
        boolean finish;

        public boolean isFinish() {
            return finish;
        }

        public void setFinish(boolean finish) {
            this.finish = finish;
        }

        public List<CheckTestResMsgStatus> getList() {
            return list;
        }

        public void setList(List<CheckTestResMsgStatus> list) {
            this.list = list;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public CheckTestResMsgSummary getSummary() {
            return summary;
        }

        public void setSummary(CheckTestResMsgSummary summary) {
            this.summary = summary;
        }
    }

    public static class CheckTestResMsgStatus {
        String status;
        String progress;
        String token;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getProgress() {
            return progress;
        }

        public void setProgress(String progress) {
            this.progress = progress;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public static class CheckTestResMsgSummary {
        String errorMsg;
        String mailContent;
        String mail_url;
        CheckTestResMsgSummaryResult result;
        int ret;
        boolean finish;
        boolean exception;

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public String getMailContent() {
            return mailContent;
        }

        public void setMailContent(String mailContent) {
            this.mailContent = mailContent;
        }

        public String getMail_url() {
            return mail_url;
        }

        public void setMail_url(String mail_url) {
            this.mail_url = mail_url;
        }

        public CheckTestResMsgSummaryResult getResult() {
            return result;
        }

        public void setResult(CheckTestResMsgSummaryResult result) {
            this.result = result;
        }

        public int getRet() {
            return ret;
        }

        public void setRet(int ret) {
            this.ret = ret;
        }

        public boolean isFinish() {
            return finish;
        }

        public void setFinish(boolean finish) {
            this.finish = finish;
        }

        public boolean isException() {
            return exception;
        }

        public void setException(boolean exception) {
            this.exception = exception;
        }
    }

    public static class CheckTestResMsgSummaryResult {
        int case_count;
        int success;
        int crash;
        int failure;
        int passRate;
        String resultStatus;
        String time;
        int total;

        public int getCase_count() {
            return case_count;
        }

        public void setCase_count(int case_count) {
            this.case_count = case_count;
        }

        public int getSuccess() {
            return success;
        }

        public void setSuccess(int success) {
            this.success = success;
        }

        public int getCrash() {
            return crash;
        }

        public void setCrash(int crash) {
            this.crash = crash;
        }

        public int getFailure() {
            return failure;
        }

        public void setFailure(int failure) {
            this.failure = failure;
        }

        public int getPassRate() {
            return passRate;
        }

        public void setPassRate(int passRate) {
            this.passRate = passRate;
        }

        public String getResultStatus() {
            return resultStatus;
        }

        public void setResultStatus(String resultStatus) {
            this.resultStatus = resultStatus;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        @Override
        public String toString() {
            return "CheckTestResMsgSummaryResult{" +
                    "case_count=" + case_count +
                    ", success=" + success +
                    ", crash=" + crash +
                    ", failure=" + failure +
                    ", passRate=" + passRate +
                    ", resultStatus='" + resultStatus + '\'' +
                    ", time='" + time + '\'' +
                    ", total=" + total +
                    '}';
        }
    }
}
