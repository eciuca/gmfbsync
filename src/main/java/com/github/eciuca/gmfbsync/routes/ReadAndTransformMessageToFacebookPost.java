package com.github.eciuca.gmfbsync.routes;

import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import facebook4j.internal.http.BASE64Encoder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ReadAndTransformMessageToFacebookPost implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Message message = exchange.getIn().getBody(Message.class);

        String messageText = getMessageText(message);
        Map<String, String> messageData = getMessageData(message);
        String subject = extractPostFeedSubject(messageData.get(Constants.SUBJECT));

        messageData.put(Constants.SUBJECT, subject);
        messageData.put(Constants.YAHOO_MESSAGE_ID, extractYahooMessageId(messageData));
        messageData.put(Constants.MESSAGE_TEXT, messageText);
        messageData.put(Constants.CONVERSATION_ID, generateConversationId(subject));

        exchange.getIn().setBody(messageData);
    }

    @NotNull
    private String extractYahooMessageId(Map<String, String> messageData) {
        return messageData.get(Constants.X_YAHOO_NEWMAN_ID).substring(messageData.get(Constants.X_YAHOO_NEWMAN_ID).lastIndexOf("-m") + 2);
    }

    @NotNull
    private String generateConversationId(String subject) {
        return "GMFBSYNC-" + BASE64Encoder.encode(StringUtils.stripAccents(subject).getBytes());
    }

    @NotNull
    private String extractPostFeedSubject(String subject) {
        return subject.substring(subject.lastIndexOf(Constants.HARMONY_RESIDENCE_TAG));
    }

    private Map<String, String> getMessageData(Message message) {
        return message.getPayload().getHeaders().stream()
                .filter(headerNameEquals(Constants.SUBJECT)
                        .or(headerNameEquals(Constants.FROM))
                        .or(headerNameEquals(Constants.X_YAHOO_NEWMAN_ID))
                )
                .collect(Collectors.toMap(MessagePartHeader::getName, MessagePartHeader::getValue));
    }

    @NotNull
    private String getMessageText(Message message) {
        return message.getPayload().getParts().stream()
                .filter((messagePart -> messagePart.getMimeType().equals("text/plain")))
                .map(messagePart -> messagePart.getBody().decodeData())
                .map(String::new)
                .findFirst()
                .orElse("");
    }

    private Predicate<MessagePartHeader> headerNameEquals(String name) {
        return messagePartHeader -> messagePartHeader.getName().equals(name);
    }
}
