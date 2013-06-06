package com.mdeleon.dropwizard.spring.listener;

import org.springframework.util.ObjectUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * User: mdeleon
 * Date: 6/3/13
 * Time: 12:11 PM
 */
public class SpringServletContextListener implements ServletContextListener {

    private final ConfigurableWebApplicationContext context;

    public SpringServletContextListener(ConfigurableWebApplicationContext context) {
        this.context = context;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        // Generate default id
        if (servletContext.getMajorVersion() == 2 && servletContext.getMinorVersion() < 5) {
            this.context.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX +
                    ObjectUtils.getDisplayString(servletContext.getServletContextName()));
        } else {
            this.context.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX +
                    ObjectUtils.getDisplayString(servletContext.getContextPath()));
        }

        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);
        this.context.setServletContext(servletContext);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // do nothing
    }
}
