package com.tencent.utp.utpplugin;

/**
 * Created by yoyoqin on 2018/3/25.
 */

public class SimpleFilesExtension {
    public String getDebug_apk() {
        return debug_apk;
    }

    public void setDebug_apk(String debug_apk) {
        this.debug_apk = debug_apk;
    }

    public String getTest_apk() {
        return test_apk;
    }

    public void setTest_apk(String test_apk) {
        this.test_apk = test_apk;
    }

    private String debug_apk;
    private String test_apk;
}
