package com.mdeleon.dropwizard.examples;

import com.mdeleon.dropwizard.spring.SpringBundle;
import com.mdeleon.dropwizard.spring.config.SpringConfiguration;
import com.mdeleon.dropwizard.spring.security.SpringSecurityBundle;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

/**
 * User: mdeleon
 * Date: 5/29/13
 * Time: 10:57 PM
 */
public class HelloService extends Service<HelloConfig> {
    private final SpringBundle<HelloConfig> springBundle = new SpringBundle<HelloConfig>() {
        @Override
        public SpringConfiguration getSpringConfiguration(HelloConfig configuration) {
            return configuration.getSpringConfiguration();
        }
    };
    private final SpringSecurityBundle springSecurityBundle = new SpringSecurityBundle();

    public static void main(String[] args) throws Exception {
        new HelloService().run(args);
    }

    @Override
    public void initialize(Bootstrap<HelloConfig> bootstrap) {
        bootstrap.addBundle(springBundle);
        bootstrap.addBundle(springSecurityBundle);
    }

    @Override
    public void run(HelloConfig configuration, Environment environment) throws Exception {
    }

}
