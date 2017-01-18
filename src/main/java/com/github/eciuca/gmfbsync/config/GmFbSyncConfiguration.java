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
        //graph api
//        fbConfig.setOAuthAccessToken("EAACEdEose0cBAB65QsZByi9BNmR48BhkVhx8GFzRlj7K2nKvhWdCjW3Gj31ENtoFS5eTVW9uRfCqamq8PifjoM1PUnl9Y3WYG5eioFxjt0C2ZBxpbtZCTjHnTpmYDUJOR9qCYbUYxlvTALt5gHZBAta47uiN07yjjbM5F3ssWOZADDFJ9ZC7048ZAZClLDsNVtAZD");
        //the service
        fbConfig.setOAuthAccessToken("EAAUbDCjheZCABAP15LwNY41GcEPSBiGjO9H51523fnZBr9R5CTUFL5JftWvytoFwSUjOW9SMLF5Ndm7VqUQJvs04e1X2iHobkHJ3ZC1Aa2IWUFBe1OElZAb5jxwDT13Mg2wMiNzfioVZAYLKbV6qw3y9LsuEnTQvvsdUR2DSnkTHc3L1C2bBRRDZA4dr9jsa8ZD");

        return fbConfig;
    }

    public GoogleMailConfiguration getGoogleMailConfiguration() {
        GoogleMailConfiguration gmailConfig = new GoogleMailConfiguration();

        gmailConfig.setClientId(clientId);
        gmailConfig.setClientSecret(clientSecret);
        gmailConfig.setAccessToken("ya29.GlvXA36JrpVDkSLm0zBYGRoSKrLajzBSZgm6UBHldgUHDGO3gM1H33H9hwtHMHSjRR4a_XHedzfxLx2ydknPXKSNKVuwcy4PjeBoc3Uz447xw5RfVeCjn3vZymiL");
        gmailConfig.setRefreshToken("1/X7wr7ER2fXbo1NTu0xbb2xZ85r49aAhy2a3rkAyxO2H-VwQgXdOoynfIZ84o85U_");
        return gmailConfig;
    }
}