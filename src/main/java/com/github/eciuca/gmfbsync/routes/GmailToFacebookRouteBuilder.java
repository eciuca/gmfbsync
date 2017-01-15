package com.github.eciuca.gmfbsync.routes;

import com.github.eciuca.gmfbsync.repo.PostedTopicsRepository;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.inject.Inject;
import facebook4j.internal.http.BASE64Encoder;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang3.StringUtils;
import org.mapdb.DB;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by manu on 10/01/2017.
 */
public class GmailToFacebookRouteBuilder extends RouteBuilder {

    public static final String HARMONY_RESIDENCE_TAG = "[Harmony Residence]";
    public static final String GROUP_ID = "358526654528397"; //TEST
//    public static final String GROUP_ID = "1023963457666500"; //HARMONY - PROD

    public static final String DIRECT_TRIGGER_POST_GROUP_FEED = "direct://triggerPostGroupFeed";

    public static final String SUBJECT = "Subject";
    public static final String CONVERSATION_ID = "ConversationId";
    public static final String FROM = "From";
    public static final String MESSAGE_TEXT = "MessageText";
    public static final String X_YAHOO_NEWMAN_ID = "X-Yahoo-Newman-Id";
    public static final String YAHOO_MESSAGE_ID = "YahooMessageId";

    private final PostedTopicsRepository postedTopicsRepo;

    @Inject
    public GmailToFacebookRouteBuilder(PostedTopicsRepository postedTopicsRepo) {
        this.postedTopicsRepo = postedTopicsRepo;
    }

    public void configure() throws Exception {
        configureFacebook();
        configureGMail();
    }

    public void configureFacebook() throws Exception {
//        from("quartz2://checkNewMessages?trigger.repeatInterval=2000&trigger.repeatCount=0")
        from(DIRECT_TRIGGER_POST_GROUP_FEED)
                .setHeader("CamelFacebook.postUpdate", new FacebookPostExpression())
                .to("facebook://postGroupFeed?groupId=" + GROUP_ID)
                .process(exchange -> {
                    postedTopicsRepo.save(
                            exchange.getIn().getHeader(CONVERSATION_ID, String.class),
                            exchange.getIn().getBody(String.class)
                    );
                    postedTopicsRepo.logRepoContents();
                })
                .log("${body}");
    }

    public void configureGMail() throws Exception {

        from("quartz2://checkNewMessages?trigger.repeatInterval=2000&trigger.repeatCount=0")
                .setHeader("CamelGoogleMail.q",simple("is:read"))
                .setHeader("CamelGoogleMail.prettyPrint", simple("true"))
                .setHeader("CamelGoogleMail.labelIds", labelsToRetrieve())
                .to("google-mail://messages/list?userId=emanuel.ciuca@gmail.com")

                .split(simple("${body.messages}"))
                .setHeader("CamelGoogleMail.id", simple("${body.id}"))
                .setHeader("CamelGoogleMail.prettyPrint", simple("true"))
                .to("google-mail://messages/get?userId=emanuel.ciuca@gmail.com")
                .process(exchange -> {
                    Message message = exchange.getIn().getBody(Message.class);

                    String messageText = message.getPayload().getParts().stream()
                            .filter((messagePart -> messagePart.getMimeType().equals("text/plain")))
                            .map(messagePart -> messagePart.getBody().decodeData())
                            .map(String::new)
                            .findFirst()
                            .orElse("");

                    Map<String, String> messageData = message.getPayload().getHeaders().stream()
                            .filter(headerNameEquals(SUBJECT)
                                    .or(headerNameEquals(FROM))
                                    .or(headerNameEquals(X_YAHOO_NEWMAN_ID))
                            )
                            .collect(Collectors.toMap(MessagePartHeader::getName, MessagePartHeader::getValue));

                    String subject = messageData.get(SUBJECT);
                    subject = subject.substring(subject.indexOf(HARMONY_RESIDENCE_TAG));
                    messageData.put(SUBJECT, subject);

                    String conversationId = "GMFBSYNC-" + BASE64Encoder.encode(StringUtils.stripAccents(subject).getBytes());
                    messageData.put(CONVERSATION_ID, conversationId);

                    messageData.put(MESSAGE_TEXT, messageText);
                    messageData.put(YAHOO_MESSAGE_ID,
                            messageData.get(X_YAHOO_NEWMAN_ID).substring(messageData.get(X_YAHOO_NEWMAN_ID).lastIndexOf("-m") + 2));

                    exchange.getIn().setBody(messageData);
                })
                .log("log: ${body}")
                .to(DIRECT_TRIGGER_POST_GROUP_FEED);
    }

    public static void main(String[] args) {

        String s = "Răsp.: RE: Răsp.: [Harmony Residence] oferta curatienie";
        System.out.println(BASE64Encoder.encode(StringUtils.stripAccents(s).getBytes()));
        System.out.println(s.substring(s.indexOf("[Harmony Residence]")));
    }

    private Predicate<MessagePartHeader> headerNameEquals(String name) {
        return messagePartHeader -> messagePartHeader.getName().equals(name);
    }

    private Expression labelsToRetrieve() {
        return new Expression() {
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                return (T) Collections.singletonList("Label_1480833359491726344");
            }
        };
    }
}
