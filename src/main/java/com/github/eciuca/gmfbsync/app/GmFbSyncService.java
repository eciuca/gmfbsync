package com.github.eciuca.gmfbsync.app;

import be.fluid_it.µs.bundle.dropwizard.µService;
import be.fluid_it.µs.bundle.dropwizard.µsBundle;
import be.fluid_it.µs.bundle.dropwizard.µsEnvironment;
import com.github.eciuca.gmfbsync.HelloWorldResource;
import com.github.eciuca.gmfbsync.config.GmFbSyncConfiguration;
import com.github.eciuca.gmfbsync.module.GmFbSyncModule;
import com.github.eciuca.gmfbsync.routes.GmailToFacebookRouteBuilder;
import org.apache.camel.component.google.mail.GoogleMailComponent;

public class GmFbSyncService extends µService<GmFbSyncConfiguration> {
    static {
        µService.µServiceClass = GmFbSyncService.class;
        µService.relativePathToYmlInIDE = "gm-fb-sync-config.yml";
    }

    public Class configurationClass() {
        return GmFbSyncConfiguration.class;
    }

    protected void run(GmFbSyncConfiguration configuration, µsEnvironment µsEnvironment) throws Exception {
        µsEnvironment.jersey().register(HelloWorldResource.class);

        GoogleMailComponent googleMailComponent = (GoogleMailComponent) µsEnvironment.camel().getComponent("google-mail");
        googleMailComponent.setConfiguration(configuration.getGoogleMailConfiguration());
//        googleMailComponent.setClientFactory(ModifiedBatchGoogleMailClientFactory.proxiedBatchGoogleMailClientFactory());

        µsEnvironment.camel().start();
    }

    @Override
    public void initialize(µsBundle.Builder µsBundleBuilder) {
        µsBundleBuilder
                .addRoutes(GmailToFacebookRouteBuilder.class)
                .addModule(new GmFbSyncModule());
    }
}