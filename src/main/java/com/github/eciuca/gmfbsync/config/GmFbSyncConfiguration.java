package com.github.eciuca.gmfbsync.config;

import io.dropwizard.Configuration;
import org.apache.camel.component.facebook.config.FacebookConfiguration;
import org.apache.camel.component.google.mail.GoogleMailConfiguration;
import org.hibernate.validator.constraints.NotEmpty;

public class GmFbSyncConfiguration extends Configuration {

    @NotEmpty
    private String clientId;

    @NotEmpty
    private String clientSecret;

    @NotEmpty
    private String defaultName;

    @NotEmpty
    private String oAuthAppId;

    @NotEmpty
    private String oAuthAppSecret;

//    @NotEmpty
//    private String refreshToken;

//    @NotEmpty
//    private String oAuthAccessToken;


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public String getoAuthAppId() {
        return oAuthAppId;
    }

    public void setoAuthAppId(String oAuthAppId) {
        this.oAuthAppId = oAuthAppId;
    }

    public String getoAuthAppSecret() {
        return oAuthAppSecret;
    }

    public void setoAuthAppSecret(String oAuthAppSecret) {
        this.oAuthAppSecret = oAuthAppSecret;
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
        gmailConfig.setAccessToken("ya29.Ci_TA8vJAUOg7o5yp7sGLHuiSmDacKIDSvuuY5p_hcGxqRVDb9fZHrlhd1ONeKff0w");
        gmailConfig.setRefreshToken("1/bl3GS0KA6KncLLFDLJ1DmNxmhDPkUNEjJzdBBTQcbp8");
        return gmailConfig;
    }
}