package com.mdeleon.dropwizard.examples;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mdeleon.dropwizard.spring.config.SpringConfiguration;
import com.yammer.dropwizard.config.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * User: mdeleon
 * Date: 5/29/13
 * Time: 10:55 PM
 */
public class HelloConfig extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private SpringConfiguration springConfiguration = new SpringConfiguration();

    public SpringConfiguration getSpringConfiguration() {
        return springConfiguration;
    }
}
