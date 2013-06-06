package com.mdeleon.dropwizard.spring.listener;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * User: mdeleon
 * Date: 6/5/13
 * Time: 4:15 PM
 */
public class SpringServletContextListenerTest {
    private SpringServletContextListener contextListener;
    private final ConfigurableWebApplicationContext context = mock(ConfigurableWebApplicationContext.class);
    private final ServletContextEvent contextEvent = mock(ServletContextEvent.class);
    private final ServletContext servletContext = mock(ServletContext.class);

    @Before
    public void setup() {
        contextListener = new SpringServletContextListener(context);
        when(contextEvent.getServletContext()).thenReturn(servletContext);
    }

    @Test
    public void rootContextSetSuccessfully() throws Exception {
        contextListener.contextInitialized(contextEvent);

        verify(context).setId(any(String.class));
        verify(servletContext).setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
        verify(context).setServletContext(servletContext);
    }
}
