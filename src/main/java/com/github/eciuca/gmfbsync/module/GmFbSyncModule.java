package com.github.eciuca.gmfbsync.module;

import be.fluid_it.µs.bundle.guice.ApplicationModule;
import com.github.eciuca.gmfbsync.config.GmFbSyncConfiguration;
import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.name.Named;

public class GmFbSyncModule implements ApplicationModule {
    public void configure(Binder binder) {

    }

    @Provides
    @Named("defaultName")
    public String providesName(GmFbSyncConfiguration configuration) {
        return configuration.getDefaultName();
    }
}