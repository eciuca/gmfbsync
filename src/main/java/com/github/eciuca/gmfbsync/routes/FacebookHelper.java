package com.github.eciuca.gmfbsync.routes;

import com.github.eciuca.gmfbsync.repo.PostedTopicsRepository;
import facebook4j.PostUpdate;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class FacebookHelper {

    private final PostedTopicsRepository postedTopicsRepo;

    public FacebookHelper(PostedTopicsRepository postedTopicsRepo) {
        this.postedTopicsRepo = postedTopicsRepo;
    }

    public String getPostGroupFeedEndoint() {
        return "facebook://postGroupFeed?groupId="+ Constants.GROUP_ID;
    }

    public String getPostCommentEndpoint() {
        return "facebook://commentPost";
    }

    public Processor prepareFacebookComment() {
        return exchange -> {
            exchange.getIn().setHeader("CamelFacebook.postId", findTopicId());
            exchange.getIn().setHeader("CamelFacebook.message", buildFacebookPostMessage());
        };
    }

    public Expression prepareFacebookPostUpdate() {
        return new Expression() {
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> aClass) {
                Map<String, String> mailData = exchange.getIn().getBody(Map.class);

                PostUpdate postUpdate = createPostUpdateWithMessage(mailData);
                postUpdate.setName(mailData.get(Constants.SUBJECT));
                postUpdate.setDescription(mailData.get(Constants.FROM));
                postUpdate.setCaption("Mesaj postat pe grupul de Yahoo");
                postUpdate.setLink(createYahooURL(mailData.get(Constants.YAHOO_MESSAGE_ID)));

                return (T) postUpdate;
            }
        };
    }

    @NotNull
    private PostUpdate createPostUpdateWithMessage(Map<String, String> mailData) {
        StringBuilder postMessage = new StringBuilder();
        postMessage.append(mailData.get(Constants.MESSAGE_TEXT)).append("\n\n\n\n");
        postMessage.append("Mesaj postat de ").append(mailData.get(Constants.FROM));

        return new PostUpdate(postMessage.toString());
    }

    @NotNull
    private URL createYahooURL(String urlString) {
        try {
            URL myURL = new URL("https://groups.yahoo.com/neo/groups/harmonyresidence/conversations/messages/" + urlString);

            return myURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @NotNull
    Predicate topicExists() {
        return exchange -> postedTopicsRepo.exists(exchange.getIn().getHeader(Constants.CONVERSATION_ID, String.class));
    }

    @NotNull
    Expression extractConversationId() {
        return new Expression() {
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> aClass) {
                Map<String, String> mailData = exchange.getIn().getBody(Map.class);

                return (T) mailData.get(Constants.CONVERSATION_ID);
            }
        };
    }

    @NotNull
    private Expression findTopicId() {
        return new Expression() {
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> aClass) {
                return (T) postedTopicsRepo.find(exchange.getIn().getHeader(Constants.CONVERSATION_ID, String.class));
            }
        };
    }

    @NotNull
    private Expression buildFacebookPostMessage() {
        return new Expression() {
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> aClass) {
                Map<String, String> mailData = exchange.getIn().getBody(Map.class);

                StringBuilder postMessage = new StringBuilder();
                postMessage.append(mailData.get(Constants.MESSAGE_TEXT)).append("\n\n\n\n");
                postMessage.append("Mesaj postat de ").append(mailData.get(Constants.FROM));

                return (T) postMessage.toString();
            }
        };
    }

    Processor savePostedTopic() {
        return exchange -> {
            postedTopicsRepo.save(
                    exchange.getIn().getHeader(Constants.CONVERSATION_ID, String.class),
                    exchange.getIn().getBody(String.class)
            );
            postedTopicsRepo.logRepoContents();
        };
    }
}
