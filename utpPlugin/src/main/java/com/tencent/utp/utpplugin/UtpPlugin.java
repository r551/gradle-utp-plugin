package com.tencent.utp.utpplugin;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.invocation.DefaultGradle;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UtpPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        // create utp extension
        Instantiator instantiator = ((DefaultGradle) project.getGradle()).getServices().get(
                Instantiator.class);
        NamedDomainObjectContainer<FilesDefInTestScripts> smallExtensionsContainer = project.container(
                FilesDefInTestScripts.class, new SmallExtensionFactory(instantiator));
        final UtpTestExtension utpTestExtension = project.getExtensions().create(
                "utpTest", UtpTestExtension.class, instantiator,smallExtensionsContainer);

        // create utp task
        final UtpTask utpTask = project.getTasks().create("testOnUtp", UtpTask.class);
        utpTask.setGroup("utp");

        // create zip test scripts task
        final ZipTestScriptsTask zipTestScripts = project.getTasks().create("zipTestScripts", ZipTestScriptsTask.class);
        zipTestScripts.setGroup("utp");

        List<Task> dependsOn = new ArrayList<>();
        dependsOn.add(zipTestScripts);
        utpTask.setDependsOn(dependsOn);

        // use extension's attributes in task
        project.afterEvaluate(taskProject -> {
            File testScriptFolder = utpTestExtension.getTestScripts();
            if (null != testScriptFolder) {
                zipTestScripts.setFromSrc(testScriptFolder);
            }

            Iterator<FilesDefInTestScripts> iter = null;
            if (null != utpTestExtension.filesExtension)
            {
                iter = utpTestExtension.filesExtension.iterator();
            }
            if (null != iter)
            {
                while (iter.hasNext())
                {
                    FilesDefInTestScripts fileDef = iter.next();
                    System.out.println("UTP:File:" + project.file(fileDef.getPath()).getPath()); // log
                    File file = project.file(fileDef.getPath());
                    // allow file not exist when afterEvaluate
                    utpTask.addFile(fileDef.name, file);
                }
            }

            File testScripts = project.file(zipTestScripts.destinationDir
                    + File.separator + zipTestScripts.getArchiveName());
            System.out.println("UTP:TestScriptZip:" + testScripts.getPath()); // log
            utpTask.setTestScriptZip(testScripts);

            System.out.println("UTP:ScriptsCommand:" + utpTestExtension.getScriptsCommand()); // log
            utpTask.setScriptsCommand(utpTestExtension.getScriptsCommand());

            System.out.println("UTP:CaseSet:" + utpTestExtension.getCaseSet()); // log
            utpTask.setCaseSet(utpTestExtension.getCaseSet());

            if (null != utpTestExtension.getUser())
            {
                System.out.println("UTP:User:" + utpTestExtension.getUser()); // log
                utpTask.setUser(utpTestExtension.getUser());
            }
        });
    }
}
