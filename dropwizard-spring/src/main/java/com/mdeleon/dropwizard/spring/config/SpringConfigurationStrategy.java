package com.mdeleon.dropwizard.spring.config;

/**
 * User: mdeleon
 * Date: 5/29/13
 * Time: 11:08 PM
 */
public interface SpringConfigurationStrategy<T> {
    SpringConfiguration getSpringConfiguration(T configuration);
}
