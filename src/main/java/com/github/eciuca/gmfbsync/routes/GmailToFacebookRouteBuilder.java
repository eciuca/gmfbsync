package com.github.eciuca.gmfbsync.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * Created by manu on 10/01/2017.
 */
public class GmailToFacebookRouteBuilder extends RouteBuilder {
    private static int x = 0;

    public void configure() throws Exception {

        from("google-mail://messages/get?userId=emanuel.ciuca@gmail.com&id=1598a4fb41ab0b48")
                .process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        System.out.println(exchange.getIn().getBody());
                    }
                })
                .log("log: ${body}");
//                .to("facebook://");
    }
}
