package com.tencent.utp.utpplugin;

import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.bundling.Zip;

import java.io.File;

/**
 * Created by yoyoqin on 2018/3/27.
 */

public class ZipTestScriptsTask extends Zip {
    public String destinationDir =
            "build" + File.separator + "outputs" + File.separator + "utpscripts";

    private File fromSrc;

    @InputDirectory
    public File getSrcDir()
    {
        return fromSrc;
    }

    @OutputDirectory
    public File getDestinationDir()
    {
        return getProject().file(destinationDir);
    }

    public void setFromSrc(File fromSrc) {
        this.fromSrc = fromSrc;
        from(fromSrc);
        System.out.println("UTP:testScripts:" + this.fromSrc.getPath());
    }

    public ZipTestScriptsTask()
    {
        super();
        File projectDir = getProject().getProjectDir();
        if (null == fromSrc)
        {
            fromSrc = this.getProject().file("scripts"); // default scripts folder
        }

        setDestinationDir(getProject().file(destinationDir));
        setExtension(ZIP_EXTENSION);
        setArchiveName("scripts" + "." + getExtension());
        include("**/*");
        from(fromSrc);
        into("scripts");
    }
}
