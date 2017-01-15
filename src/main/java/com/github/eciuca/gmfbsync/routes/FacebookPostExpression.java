package com.github.eciuca.gmfbsync.routes;

import facebook4j.PostUpdate;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static com.github.eciuca.gmfbsync.routes.GmailToFacebookRouteBuilder.*;

public class FacebookPostExpression implements Expression {

    @Override
    public <T> T evaluate(Exchange exchange, Class<T> aClass) {
        Map<String, String> mailData = exchange.getIn().getBody(Map.class);
        exchange.getIn().setHeader(CONVERSATION_ID, mailData.get(CONVERSATION_ID));

        PostUpdate postUpdate = createPostUpdateWithMessage(mailData);
        postUpdate.setName(mailData.get(SUBJECT));
        postUpdate.setDescription(mailData.get(FROM));
        postUpdate.setCaption("Mesaj postat pe grupul de Yahoo");
        postUpdate.setLink(createURL("https://groups.yahoo.com/neo/groups/harmonyresidence/conversations/messages/" + mailData.get(YAHOO_MESSAGE_ID)));

        return (T) postUpdate;
    }

    private PostUpdate createPostUpdateWithMessage(Map<String, String> mailData) {
        StringBuilder postMessage = new StringBuilder();
        postMessage.append(mailData.get(MESSAGE_TEXT)).append("\n.\n.\n.\n");
        postMessage.append("").append(mailData.get(CONVERSATION_ID));

        return new PostUpdate(postMessage.toString());
    }

    private URL createURL(String urlString) {
        try {
            URL myURL = new URL(urlString);

            return myURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
