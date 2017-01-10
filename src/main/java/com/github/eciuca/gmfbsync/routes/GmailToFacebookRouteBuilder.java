package com.github.eciuca.gmfbsync.routes;

import org.apache.camel.builder.RouteBuilder;

/**
 * Created by manu on 10/01/2017.
 */
public class GmailToFacebookRouteBuilder extends RouteBuilder {
    private static int x = 0;

    public void configure() throws Exception {

        from("google-mail://messages/list?userId=emanuel.ciuca@gmail.com")
                .log("log: " + x++);
//                .to("facebook://");
    }
}
