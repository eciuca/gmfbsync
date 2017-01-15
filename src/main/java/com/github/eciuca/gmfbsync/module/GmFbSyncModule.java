package com.github.eciuca.gmfbsync.module;

import be.fluid_it.µs.bundle.guice.ApplicationModule;
import com.github.eciuca.gmfbsync.config.GmFbSyncConfiguration;
import com.github.eciuca.gmfbsync.repo.PostedTopicsRepository;
import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.util.concurrent.ConcurrentMap;

public class GmFbSyncModule implements ApplicationModule {
    public void configure(Binder binder) {

    }

    @Provides
    @Named("defaultName")
    public String providesName(GmFbSyncConfiguration configuration) {
        return configuration.getDefaultName();
    }

    @Provides
    @Singleton
    public DB database() {
        DB database = DBMaker
                .fileDB("gmfbsync.db")
                .fileMmapEnableIfSupported()
                .transactionEnable()
                .make();
        return database;
    }

    @Provides
    @Singleton
    public PostedTopicsRepository providesPostedTopicsRepository(DB db) {
        return new PostedTopicsRepository(db);
    }
}