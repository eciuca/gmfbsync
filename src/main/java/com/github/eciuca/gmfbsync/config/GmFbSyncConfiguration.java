package com.github.eciuca.gmfbsync.config;

import io.dropwizard.Configuration;
import org.apache.camel.component.facebook.config.FacebookConfiguration;
import org.apache.camel.component.google.mail.GoogleMailConfiguration;
import org.hibernate.validator.constraints.NotEmpty;

public class GmFbSyncConfiguration extends Configuration {

    @NotEmpty
    private String defaultName;

    @NotEmpty
    private String oAuthAppId;

    @NotEmpty
    private String oAuthAppSecret;

    @NotEmpty
    private String clientId;

    @NotEmpty
    private String clientSecret;

//    @NotEmpty
//    private String refreshToken;

//    @NotEmpty
//    private String oAuthAccessToken;

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public FacebookConfiguration getFacebookConfiguration() {
        FacebookConfiguration fbConfig = new FacebookConfiguration();

        fbConfig.setOAuthAppId(oAuthAppId);
        fbConfig.setOAuthAppSecret(oAuthAppSecret);
//        fbConfig.setOAuthAccessToken(oAuthAppId);

        return fbConfig;
    }

    public GoogleMailConfiguration getGoogleMailConfiguration() {
        GoogleMailConfiguration gmailConfig = new GoogleMailConfiguration();

        gmailConfig.setClientId(clientId);
        gmailConfig.setClientSecret(clientSecret);
//        gmailConfig.setRefreshToken(refreshToken);
        return gmailConfig;
    }
}