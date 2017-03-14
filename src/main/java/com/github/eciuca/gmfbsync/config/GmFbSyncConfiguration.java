package com.github.eciuca.gmfbsync.config;

import io.dropwizard.Configuration;
import org.apache.camel.component.facebook.config.FacebookConfiguration;
import org.apache.camel.component.google.mail.GoogleMailConfiguration;
import org.apache.camel.component.google.mail.internal.GoogleMailApiName;
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
        fbConfig.setOAuthAccessToken("EAACEdEose0cBAO7RfD0cMqAJMmqV2dtRPz8QrhyvDzaA7U7LmlUL4Hqt6dhu9fSZBXc8TyHwKuRlaegeNuhbZBh5TvNTcZBTZAnYcrRn6z5W6iqo5T7PyRixDOsRaHIfDEqzHbYT9WLAbW3S8ZAEcOKIj78gPrHAEo0QG1rAFJJbeZA8t3THKyuN0ZC1FORkUq9RgqZBx0gb7AZDZD");

        return fbConfig;
    }

    public GoogleMailConfiguration getGoogleMailConfiguration() {
        GoogleMailConfiguration gmailConfig = new GoogleMailConfiguration();

        gmailConfig.setClientId(clientId);
        gmailConfig.setClientSecret(clientSecret);
        gmailConfig.setApiName(GoogleMailApiName.MESSAGES);
        gmailConfig.setAccessToken("ya29.GlsOBH6we0_C6FFjZenc7R6tZG_-PGdtbGfKRE-XFAqTNxdnsn6gl-G1NEXrXsgkbuLlCCGXEXgQd6PzuDZsvgNQKBjc9-xRQjgd9trh8bYk7cQVEkKeRgB7yoar");
        gmailConfig.setRefreshToken("1/jsomNdu4RbBQlmDhXeHCr8dgQ70dwbSqrQFW_FFISRQv-k7aOumZ3TKVCh6IYasZ");
        return gmailConfig;
    }
}