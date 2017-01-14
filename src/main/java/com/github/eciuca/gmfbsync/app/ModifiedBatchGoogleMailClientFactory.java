package com.github.eciuca.gmfbsync.app;

import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import org.apache.camel.component.google.mail.BatchGoogleMailClientFactory;
import org.apache.camel.component.google.mail.GoogleMailClientFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * I need this class only when I am behind an authenticated proxy
 */
public class ModifiedBatchGoogleMailClientFactory /*extends BatchGoogleMailClientFactory*/ {

    private static final Logger LOG = LoggerFactory.getLogger(BatchGoogleMailClientFactory.class);
//    private HttpTransport transport;
//    private JacksonFactory jsonFactory;
/*
    public ModifiedBatchGoogleMailClientFactory(HttpTransport transport) {
        this.transport = transport;
        this.jsonFactory = new JacksonFactory();
    }*/

   /* @Override
    public Gmail makeClient(String clientId, String clientSecret, Collection<String> scopes, String applicationName, String refreshToken, String accessToken) {
        try {
            Credential credential = this.authorize(clientId, clientSecret, scopes);
            if (refreshToken != null && !"".equals(refreshToken)) {
                credential.setRefreshToken(refreshToken);
            }

            if (accessToken != null && !"".equals(accessToken)) {
                credential.setAccessToken(accessToken);
            }

            return (new com.google.api.services.gmail.Gmail.Builder(this.transport, this.jsonFactory, credential)).setApplicationName(applicationName).build();
        } catch (Exception var9) {
            LOG.error("Could not create Google Drive client.", var9);
            return null;
        }
    }

    private Credential authorize(String clientId, String clientSecret, Collection<String> scopes) throws Exception {
        return (new com.google.api.client.googleapis.auth.oauth2.GoogleCredential.Builder()).setJsonFactory(this.jsonFactory).setTransport(this.transport).setClientSecrets(clientId, clientSecret).build();
    }
*/
    /*public static GoogleMailClientFactory proxiedBatchGoogleMailClientFactory() {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setDefaultMaxPerRoute(20);
        connManager.setMaxTotal(200);


        HttpHost proxyHost = new HttpHost(host, 8012);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxyHost);

        RequestConfig config = RequestConfig.custom()
                .setProxy(proxyHost)
                .setRedirectsEnabled(true)
                .setMaxRedirects(5)
                .setConnectTimeout(100 * 1000)
                .setConnectionRequestTimeout(300 * 1000)
                .setSocketTimeout(300 * 1000)
                .build();

        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        Credentials credentials = new UsernamePasswordCredentials(username, password);
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);

        HttpClient client = HttpClients.custom()
                .setConnectionManager(connManager)
                .setRoutePlanner(routePlanner)
                .setDefaultCredentialsProvider(credentialsProvider)
                .setDefaultRequestConfig(config)
                .build();

        final ApacheHttpTransport transport = new ApacheHttpTransport(client, false);

        BatchGoogleMailClientFactory clientFactory = new ModifiedBatchGoogleMailClientFactory(transport);

        return clientFactory;
    }*/
}
