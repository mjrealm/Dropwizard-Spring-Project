package com.mdeleon.dropwizard.spring.security;

import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import org.eclipse.jetty.server.session.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * User: mdeleon
 * Date: 6/3/13
 * Time: 4:55 PM
 */
public class SpringSecurityBundle<T extends Configuration> implements ConfiguredBundle<T> {
    private static final Logger LOG = LoggerFactory.getLogger(SpringSecurityBundle.class);

    protected static final String SECURED_PATH = "/*";
    protected static final String FILTER_CHAIN_NAME = "springSecurityFilterChain";

    @Override
    public void run(T configuration, Environment environment) throws Exception {
        LOG.info("Using Spring Security bundle...");
        environment.setSessionHandler(new SessionHandler());
        environment.addFilter(DelegatingFilterProxy.class, SECURED_PATH).setName(FILTER_CHAIN_NAME);
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        // do nothing
    }
}
