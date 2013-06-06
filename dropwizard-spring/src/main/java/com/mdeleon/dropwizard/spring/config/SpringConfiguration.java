package com.mdeleon.dropwizard.spring.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

/**
 * User: mdeleon
 * Date: 5/29/13
 * Time: 4:25 PM
 */
public class SpringConfiguration extends Configuration {

    @JsonProperty
    private boolean javaConfig = false;

    @JsonProperty
    private String[] contextConfigLocation;

    public boolean isJavaConfig() {
        return this.javaConfig;
    }

    public String[] getContextConfigLocation() {
        return this.contextConfigLocation;
    }
}
