package com.tencent.utp.utpplugin;

import org.gradle.api.NamedDomainObjectFactory;
import org.gradle.internal.reflect.Instantiator;

/**
 * Created by yoyoqin on 2018/3/25.
 */

public class SmallExtensionFactory implements NamedDomainObjectFactory<FilesDefInTestScripts> {

    private Instantiator mInstantiator;

    public SmallExtensionFactory(Instantiator instantiator) {
        this.mInstantiator = instantiator;
    }

    @Override
    public FilesDefInTestScripts create(String name) {
        return mInstantiator.newInstance(FilesDefInTestScripts.class, name);
    }
}
