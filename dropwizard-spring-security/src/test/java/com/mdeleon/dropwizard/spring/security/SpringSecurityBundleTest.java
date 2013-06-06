package com.mdeleon.dropwizard.spring.security;

import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.config.FilterBuilder;
import org.eclipse.jetty.server.session.SessionHandler;
import org.junit.Test;
import org.springframework.web.filter.DelegatingFilterProxy;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * User: mdeleon
 * Date: 6/3/13
 * Time: 5:00 PM
 */
public class SpringSecurityBundleTest {
    private final Configuration configuration = mock(Configuration.class);
    private final Environment environment = mock(Environment.class);
    private final FilterBuilder filterBuilder = mock(FilterBuilder.class);
    private final SpringSecurityBundle securityBundle = new SpringSecurityBundle();


    @Test
    public void filterSuccessfully() throws Exception {
        when(environment.addFilter(DelegatingFilterProxy.class, SpringSecurityBundle.SECURED_PATH)).thenReturn(filterBuilder);
        securityBundle.run(configuration, environment);

        verify(environment).setSessionHandler(any(SessionHandler.class));
        verify(environment).addFilter(DelegatingFilterProxy.class, SpringSecurityBundle.SECURED_PATH);
        verify(filterBuilder).setName(SpringSecurityBundle.FILTER_CHAIN_NAME);
    }

}
