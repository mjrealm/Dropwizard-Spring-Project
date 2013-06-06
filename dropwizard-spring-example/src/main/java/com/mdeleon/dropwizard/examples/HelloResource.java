package com.mdeleon.dropwizard.examples;

import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * User: mdeleon
 * Date: 6/4/13
 * Time: 2:13 PM
 */
@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloResource {

    @GET
    @Timed
    public String sayHello() {
        return "Hello world!";
    }
}
