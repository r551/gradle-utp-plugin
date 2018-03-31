package com.tencent.utp.utpplugin;

import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.internal.reflect.Instantiator;

import java.io.File;

/**
 * Created by yoyoqin on 2018/3/25.
 */

public class UtpTestExtension {
    private File testScripts;
    private String scriptsCommand;
    private String caseSet;
    private String user;
    private SimpleFilesExtension simpleFilesExtension;
    NamedDomainObjectContainer<FilesDefInTestScripts> filesExtension;

    public UtpTestExtension(Instantiator instantiator, NamedDomainObjectContainer<FilesDefInTestScripts> filesDefInTestScripts) {
        simpleFilesExtension = instantiator.newInstance(SimpleFilesExtension.class);
        this.filesExtension = filesDefInTestScripts;
    }

    public SimpleFilesExtension getSimpleFilesExtension() {
        return simpleFilesExtension;
    }

    public void setSimpleFilesExtension(SimpleFilesExtension simpleFilesExtension) {
        this.simpleFilesExtension = simpleFilesExtension;
    }

    public File getTestScripts() {
        return testScripts;
    }

    public void setTestScripts(File testScripts) {
        this.testScripts = testScripts;
    }

    public String getScriptsCommand() {
        return scriptsCommand;
    }

    public void setScriptsCommand(String scriptsCommand) {
        this.scriptsCommand = scriptsCommand;
    }

    public String getCaseSet() {
        return caseSet;
    }

    public void setCaseSet(String caseSet) {
        this.caseSet = caseSet;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void simpleFiles(Action<SimpleFilesExtension> action) {
        action.execute(simpleFilesExtension);
    }

    public void files(
            Action<? super NamedDomainObjectContainer<FilesDefInTestScripts>> action) {
        action.execute(filesExtension);
    }
}
