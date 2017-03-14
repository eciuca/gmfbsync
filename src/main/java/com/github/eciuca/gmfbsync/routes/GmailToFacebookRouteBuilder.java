package com.github.eciuca.gmfbsync.routes;

import com.github.eciuca.gmfbsync.repo.PostedTopicsRepository;
import com.google.inject.Inject;
import facebook4j.internal.http.BASE64Encoder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.SimpleBuilder;
import org.apache.commons.lang3.StringUtils;

public class GmailToFacebookRouteBuilder extends RouteBuilder {


    public static final String DIRECT_TRIGGER_POST_GROUP_FEED = "direct://triggerPostGroupFeed";

    public static final String DIRECT_READ_GMAIL_MESSAGE_LIST = "direct://readGmailMessageList";
    public static final String DIRECT_READ_GMAIL_MESSAGE = "direct://readGmailMessage";
    private final GoogleMailHelper googleMailHelper;
    private final FacebookHelper facebookHelper;

    @Inject
    public GmailToFacebookRouteBuilder(PostedTopicsRepository postedTopicsRepo, GoogleMailHelper googleMailHelper) {
        this.facebookHelper = new FacebookHelper(postedTopicsRepo);
        this.googleMailHelper = googleMailHelper;
    }

    public void configure() throws Exception {
        configureGMail();
        configureFacebook();
    }

    private void configureGMail() throws Exception {

        //Check for new Messages every 2 seconds
        from("quartz2://checkNewMessages?trigger.repeatInterval=5000&trigger.repeatCount=0")
                .routeId("GMAIL::CheckForNewMessages")
                .process(googleMailHelper.prepareHeadersForListMessagesRequest())

                .log("Checking for new messages...")
                .to(googleMailHelper.listMessagesEndpoint())
                .to(DIRECT_READ_GMAIL_MESSAGE_LIST);

        from(DIRECT_READ_GMAIL_MESSAGE_LIST)
                .routeId("GMAIL::ReadMessagesList")
                .split(listOfMessages())
                .process(googleMailHelper.prepareHeadersForGetMessageRequest())

                .to(googleMailHelper.getMessageEndpoint())
                .to(DIRECT_READ_GMAIL_MESSAGE);

        from(DIRECT_READ_GMAIL_MESSAGE)
                .routeId("GMAIL::ReadMessage")
                .process(new ReadAndTransformMessageToFacebookPost())
                .to(DIRECT_TRIGGER_POST_GROUP_FEED);
    }

    private void configureFacebook() throws Exception {
//        from("quartz2://checkNewMessages?trigger.repeatInterval=2000&trigger.repeatCount=0")
        from(DIRECT_TRIGGER_POST_GROUP_FEED)
                .setHeader(Constants.CONVERSATION_ID, facebookHelper.extractConversationId())
                .choice()
                .when(facebookHelper.topicExists())
                .process(facebookHelper.prepareFacebookComment())
                .process(System.out::println)
                .to(facebookHelper.getPostCommentEndpoint())
                .otherwise()
                .setHeader("CamelFacebook.postUpdate", facebookHelper.prepareFacebookPostUpdate())
                .to(facebookHelper.getPostGroupFeedEndoint())
                .process(facebookHelper.savePostedTopic())
                .end()
                .log("${body}");
    }

    private SimpleBuilder listOfMessages() {
        return simple("${body.messages}");
    }


    public static void main(String[] args) {

        String s = "Răsp.: RE: Răsp.: [Harmony Residence] oferta curatienie";
        System.out.println(BASE64Encoder.encode(StringUtils.stripAccents(s).getBytes()));
        System.out.println(s.substring(s.indexOf("[Harmony Residence]")));
    }
}

