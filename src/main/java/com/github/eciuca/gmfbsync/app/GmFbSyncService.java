package com.github.eciuca.gmfbsync.app;

import be.fluid_it.µs.bundle.dropwizard.camel.ManagedCamelContext;
import be.fluid_it.µs.bundle.dropwizard.µService;
import be.fluid_it.µs.bundle.dropwizard.µsBundle;
import be.fluid_it.µs.bundle.dropwizard.µsEnvironment;
import com.github.eciuca.gmfbsync.HelloWorldResource;
import com.github.eciuca.gmfbsync.config.GmFbSyncConfiguration;
import com.github.eciuca.gmfbsync.module.GmFbSyncModule;
import com.github.eciuca.gmfbsync.routes.GmailToFacebookRouteBuilder;
import org.apache.camel.CamelContext;
import org.apache.camel.component.facebook.FacebookComponent;
import org.apache.camel.component.google.mail.GoogleMailComponent;
import org.apache.camel.impl.DefaultCamelContext;

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

        CamelContext camelContext = new DefaultCamelContext();
        µsEnvironment.lifecycle().manage(ManagedCamelContext.of(camelContext));


        FacebookComponent facebookComponent = new FacebookComponent(configuration.getFacebookConfiguration());
        GoogleMailComponent googleMailComponent = new GoogleMailComponent();
        googleMailComponent.setConfiguration(configuration.getGoogleMailConfiguration());

        camelContext.addComponent("facebook", facebookComponent);
        camelContext.addComponent("google-mail", googleMailComponent);

        camelContext.addRoutes(µsEnvironment.guice().injector().getInstance(GmailToFacebookRouteBuilder.class));
//        µsEnvironment.camel().start();
    }

    @Override
    public void initialize(µsBundle.Builder µsBundleBuilder) {
        µsBundleBuilder
                .addModule(new GmFbSyncModule());
    }
}