package com.github.eciuca.gmfbsync.routes;

import com.google.api.services.gmail.model.Message;
import org.apache.camel.Processor;

import java.util.Collections;

public class GoogleMailHelper {

    private static final String CAMEL_GOOGLE_MAIL_ID = "CamelGoogleMail.id";
    private static final String CAMEL_GOOGLE_MAIL_LABEL_IDS_PARAMETER = "CamelGoogleMail.labelIds";
    private static final String CAMEL_GOOGLE_MAIL_PRETTY_PRINT = "CamelGoogleMail.prettyPrint";
    private static final String CAMEL_GOOGLE_MAIL_QUERY_PARAMETER = "CamelGoogleMail.q";
    private static final String HARMONY_RESIDENCE_GMAIL_LABEL = "Label_1480833359491726344";


    public String listMessagesEndpoint() {
        return "google-mail://messages/list?userId=emanuel.ciuca@gmail.com";
    }

    public String getMessageEndpoint() {
        return "google-mail://messages/get?userId=emanuel.ciuca@gmail.com";
    }

    public Processor prepareHeadersForListMessagesRequest() {
        return exchange -> {
            exchange.getIn().setHeader(CAMEL_GOOGLE_MAIL_QUERY_PARAMETER, "is:read");
            exchange.getIn().setHeader(CAMEL_GOOGLE_MAIL_PRETTY_PRINT, true);
            exchange.getIn().setHeader(CAMEL_GOOGLE_MAIL_LABEL_IDS_PARAMETER, Collections.singletonList(HARMONY_RESIDENCE_GMAIL_LABEL));
        };
    }

    public Processor prepareHeadersForGetMessageRequest() {
        return exchange -> {
            Message body = exchange.getIn().getBody(Message.class);

            exchange.getIn().setHeader(CAMEL_GOOGLE_MAIL_PRETTY_PRINT, true);
            exchange.getIn().setHeader(CAMEL_GOOGLE_MAIL_ID, body.getId());
        };
    }
}