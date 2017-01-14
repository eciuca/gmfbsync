package com.github.eciuca.gmfbsync.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by manu on 10/01/2017.
 */
public class GmailToFacebookRouteBuilder extends RouteBuilder {

    public void configure() throws Exception {

        from("quartz2://checkNewMessages?trigger.repeatInterval=2000&trigger.repeatCount=1")
                .setHeader("CamelGoogleMail.q",simple("is:read"))
                .setHeader("CamelGoogleMail.format",simple("raw"))
                .setHeader("CamelGoogleMail.prettyPrint",simple("true"))
                .setHeader("CamelGoogleMail.labelIds", new Expression() {
                    @Override
                    public <T> T evaluate(Exchange exchange, Class<T> type) {
                        return (T) Collections.singletonList("Label_1480833359491726344");
                    }
                })
                .to("google-mail://messages/list?userId=emanuel.ciuca@gmail.com")
                .split(simple("${body.messages}"))
                .setHeader("CamelGoogleMail.id", simple("${body.id}"))
                .setHeader("CamelGoogleMail.prettyPrint", simple("true"))
                .to("google-mail://messages/get?userId=emanuel.ciuca@gmail.com")
                .log("log: ${body}")
//                .log("log: ${body.payload.headers[5]}");
//                .log("log: ${body.payload.parts[0].body.data}")
//                .setBody(simple("${body.payload.parts[0].body.data}"))
                .setBody(simple("${body.raw}"))
                .process(exchange -> System.out.println(Base64.getDecoder().decode((String) exchange.getIn().getBody())));

//                .to("facebook://");
    }
}
