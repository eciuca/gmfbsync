package com.github.eciuca.gmfbsync;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldResource.class);
    private final String defaultName;

    @Inject
    public HelloWorldResource(@Named("defaultName") String defaultName) {
        this.defaultName = defaultName;
    }

    @GET
    @Timed
    public String helloWorld() {
        LOG.info("Hello! received from {}", defaultName);
        return "Hello, " + defaultName + "!!!";
    }

    @GET
    @Timed
    @Path("/{name}")
    public String helloWorld(@PathParam("name") String name) {
        LOG.info("Hello! received from {}", name);
        return "Hello, " + name + "!!!";
    }
}