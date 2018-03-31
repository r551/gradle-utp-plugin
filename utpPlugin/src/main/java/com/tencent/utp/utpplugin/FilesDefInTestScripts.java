package com.tencent.utp.utpplugin;

/**
 * Created by yoyoqin on 2018/3/25.
 */

public class FilesDefInTestScripts {
    final String name;
    String alias;
    String path;

    public FilesDefInTestScripts(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public String getPath() {
        return path;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
