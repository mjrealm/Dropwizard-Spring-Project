package com.mdeleon.dropwizard.spring;

import com.mdeleon.dropwizard.spring.config.SpringConfiguration;
import com.mdeleon.dropwizard.spring.config.SpringConfigurationStrategy;
import com.mdeleon.dropwizard.spring.listener.SpringServletContextListener;
import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.lifecycle.Managed;
import com.yammer.dropwizard.tasks.Task;
import com.yammer.metrics.core.HealthCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.Map;

/**
 * User: mdeleon
 * Date: 5/29/13
 * Time: 3:54 PM
 */
public abstract class SpringBundle<T extends Configuration> implements ConfiguredBundle<T>, SpringConfigurationStrategy<T> {
    private static final Logger LOG = LoggerFactory.getLogger(SpringBundle.class);

    private ConfigurableWebApplicationContext context;
    private static final String DEFAULT_CONTEXT_CONFIG = "classpath:applicationContext.xml";

    @Override
    public void run(T configuration, Environment environment) throws Exception {
        LOG.info("Initializing Spring bundle..");
        SpringConfiguration springConfiguration = getSpringConfiguration(configuration);

        // Load Spring context
        this.context = loadContext(springConfiguration);

        // refresh, register and start
        this.context.refresh();
        this.context.registerShutdownHook();
        this.context.start();

        // register root context attribute on servlet context
        environment.addServletListeners(new SpringServletContextListener(this.context));

        LOG.info("Automatically registering Dropwizard entities..");
        registerManaged(environment);
        registerTasks(environment);
        registerHealthChecks(environment);
        registerProviders(environment);
        registerResources(environment);
    }

    private ConfigurableWebApplicationContext loadContext(SpringConfiguration springConfiguration) {
        LOG.info("Determining context type...");

        // use XML by default
        ConfigurableWebApplicationContext context = new XmlWebApplicationContext();
        String[] contextConfigLocations = springConfiguration.getContextConfigLocation();

        if (contextConfigLocations == null) {
            if (springConfiguration.isJavaConfig()) {
                throw new ApplicationContextException("Spring Java configuration not set.");
            } else {
                contextConfigLocations = new String[]{DEFAULT_CONTEXT_CONFIG};
            }
        }

        if (springConfiguration.isJavaConfig()) {
            LOG.info("Using Java context configuration...");
            for (String contextConfigLocation : contextConfigLocations) {
                try {
                    ClassUtils.forName(contextConfigLocation, ClassUtils.getDefaultClassLoader());
                } catch (ClassNotFoundException ex) {
                    throw new ApplicationContextException("Failed to load custom context class [" + contextConfigLocation + "]", ex);
                }
            }
            // use java annotation config
            context = new AnnotationConfigWebApplicationContext();
        }

        context.setConfigLocations(contextConfigLocations);
        return context;
    }

    private void registerManaged(Environment environment) {
        Map<String, Managed> managedMap = this.context.getBeansOfType(Managed.class);
        for (Map.Entry<String, Managed> entry : managedMap.entrySet()) {
            Managed managed = entry.getValue();
            environment.manage(managed);
            LOG.info("Managed Objects added: " + managed.getClass().getName());
        }
    }

    private void registerTasks(Environment environment) {
        Map<String, Task> tasks = this.context.getBeansOfType(Task.class);
        for (Map.Entry<String, Task> entry : tasks.entrySet()) {
            Task task = entry.getValue();
            environment.addTask(task);
            LOG.info("Task added: " + task.getClass().getName());
        }
    }

    private void registerHealthChecks(Environment environment) {
        Map<String, HealthCheck> healthChecks = this.context.getBeansOfType(HealthCheck.class);
        for (Map.Entry<String, HealthCheck> entry : healthChecks.entrySet()) {
            HealthCheck healthCheck = entry.getValue();
            environment.addHealthCheck(healthCheck);
            LOG.info("Health Check added: " + healthCheck.getClass().getName());
        }
    }

    private void registerResources(Environment environment) {
        Map<String, Object> resources = this.context.getBeansWithAnnotation(Path.class);
        for (Map.Entry<String, Object> entry : resources.entrySet()) {
            Object resource = entry.getValue();
            environment.addResource(resource);
            LOG.info("Resources added: " + resource.getClass().getName());
        }
    }

    private void registerProviders(Environment environment) {
        Map<String, Object> resources = this.context.getBeansWithAnnotation(Provider.class);
        for (Map.Entry<String, Object> entry : resources.entrySet()) {
            Object provider = entry.getValue();
            environment.addProvider(provider);
            LOG.info("Provider added: " + provider.getClass().getName());
        }
    }

    @Override
    public final void initialize(Bootstrap<?> bootstrap) {
        // Do nothing
    }

    public ConfigurableWebApplicationContext getContext() {
        return context;
    }
}
