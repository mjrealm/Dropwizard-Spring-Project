package com.mdeleon.dropwizard.spring;

import com.mdeleon.dropwizard.spring.bean.TestBean;
import com.mdeleon.dropwizard.spring.config.SpringConfiguration;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import org.junit.Test;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletContextListener;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * User: mdeleon
 * Date: 5/29/13
 * Time: 4:34 PM
 */
public class SpringBundleTest {
    private final Configuration configuration = mock(Configuration.class);
    private final Environment environment = mock(Environment.class);
    private final SpringConfiguration springConfiguration = mock(SpringConfiguration.class);
    private final SpringBundle<Configuration> bundle = new SpringBundle<Configuration>() {
        @Override
        public SpringConfiguration getSpringConfiguration(Configuration configuration) {
            return springConfiguration;
        }
    };

    @Test
    public void useJavaConfigSuccessfully() throws Exception {
        when(springConfiguration.isJavaConfig()).thenReturn(true);
        when(springConfiguration.getContextConfigLocation()).thenReturn(new String[]{"com.mdeleon.dropwizard.spring.config.TestJavaConfig"});

        bundle.run(configuration, environment);

        assertThat(bundle.getContext()).isNotNull();
        assertThat(bundle.getContext()).isInstanceOf(AnnotationConfigWebApplicationContext.class);
        verify(environment).addServletListeners(any(ServletContextListener.class));
    }

    @Test(expected = ApplicationContextException.class)
    public void useJavaConfigWithMissingContextConfig() throws Exception {
        when(springConfiguration.isJavaConfig()).thenReturn(true);
        when(springConfiguration.getContextConfigLocation()).thenReturn(null);

        bundle.run(configuration, environment);
    }

    @Test(expected = ApplicationContextException.class)
    public void useJavaConfigWithInvalidConfigPath() throws Exception {
        when(springConfiguration.isJavaConfig()).thenReturn(true);
        when(springConfiguration.getContextConfigLocation()).thenReturn(new String[]{"where is my config"});

        bundle.run(configuration, environment);
    }

    @Test(expected = ApplicationContextException.class)
    public void useJavaConfigWithOneInvalidConfigPath() throws Exception {
        when(springConfiguration.isJavaConfig()).thenReturn(true);
        when(springConfiguration.getContextConfigLocation()).thenReturn(new String[]{"com.mdeleon.dropwizard.spring.config.TestJavaConfig", "invalidClass"});

        bundle.run(configuration, environment);
    }

    @Test(expected = ApplicationContextException.class)
    public void useJavaConfigWithEmptyConfigPath() throws Exception {
        when(springConfiguration.isJavaConfig()).thenReturn(true);
        when(springConfiguration.getContextConfigLocation()).thenReturn(new String[]{""});

        bundle.run(configuration, environment);
    }

    @Test (expected=ApplicationContextException.class)
    public void useJavaConfigWithOneEmptyConfigPath() throws Exception {
        when(springConfiguration.isJavaConfig()).thenReturn(true);
        when(springConfiguration.getContextConfigLocation()).thenReturn(new String[]{"com.mdeleon.dropwizard.spring.config.TestJavaConfig", ""});

        bundle.run(configuration, environment);
    }

    @Test
    public void useXmlConfigSuccessfully() throws Exception {
        when(springConfiguration.isJavaConfig()).thenReturn(false);
        when(springConfiguration.getContextConfigLocation()).thenReturn(new String[]{"classpath:applicationContext.xml"});

        bundle.run(configuration, environment);

        assertThat(bundle.getContext()).isNotNull();
        assertThat(bundle.getContext()).isInstanceOf(XmlWebApplicationContext.class);
        assertThat(bundle.getContext().getBean("testBean")).isInstanceOf(TestBean.class);
        assertThat(((TestBean) bundle.getContext().getBean("testBean")).getText()).isEqualTo("hello");
        verify(environment).addServletListeners(any(ServletContextListener.class));
    }

    @Test
    public void useXmlConfigSuccessfullyUsingDefaultLocation() throws Exception {
        when(springConfiguration.isJavaConfig()).thenReturn(false);
        when(springConfiguration.getContextConfigLocation()).thenReturn(null);

        bundle.run(configuration, environment);

        assertThat(bundle.getContext()).isNotNull();
        assertThat(bundle.getContext()).isInstanceOf(XmlWebApplicationContext.class);
        assertThat(bundle.getContext().getBean("testBean")).isInstanceOf(TestBean.class);
        assertThat(((TestBean) bundle.getContext().getBean("testBean")).getText()).isEqualTo("hello");
    }

    @Test
    public void useMultipleXmlConfigSuccessfully() throws Exception {
        when(springConfiguration.isJavaConfig()).thenReturn(false);
        when(springConfiguration.getContextConfigLocation()).thenReturn(new String[]{"classpath:applicationContext.xml", "classpath:applicationContext2.xml"});

        bundle.run(configuration, environment);

        assertThat(bundle.getContext()).isNotNull();
        assertThat(bundle.getContext()).isInstanceOf(XmlWebApplicationContext.class);

        assertThat(bundle.getContext().getBean("testBean")).isInstanceOf(TestBean.class);
        assertThat(((TestBean) bundle.getContext().getBean("testBean")).getText()).isEqualTo("hello");

        assertThat(bundle.getContext().getBean("anotherBean")).isInstanceOf(TestBean.class);
        assertThat(((TestBean) bundle.getContext().getBean("anotherBean")).getText()).isEqualTo("twin");
    }

    @Test
    public void useMultipleXmlConfigWithWildcardSuccessfully() throws Exception {
        when(springConfiguration.isJavaConfig()).thenReturn(false);
        when(springConfiguration.getContextConfigLocation()).thenReturn(new String[]{"classpath:*.xml"});

        bundle.run(configuration, environment);

        assertThat(bundle.getContext()).isNotNull();
        assertThat(bundle.getContext()).isInstanceOf(XmlWebApplicationContext.class);

        assertThat(bundle.getContext().getBean("testBean")).isInstanceOf(TestBean.class);
        assertThat(((TestBean) bundle.getContext().getBean("testBean")).getText()).isEqualTo("hello");

        assertThat(bundle.getContext().getBean("anotherBean")).isInstanceOf(TestBean.class);
        assertThat(((TestBean) bundle.getContext().getBean("anotherBean")).getText()).isEqualTo("twin");
    }

    @Test(expected = IllegalArgumentException.class)
    public void useMultipleXmlConfigWithOneInvalid() throws Exception {
        when(springConfiguration.isJavaConfig()).thenReturn(false);
        when(springConfiguration.getContextConfigLocation()).thenReturn(new String[]{"classpath:applicationContext.xml", "myInvalid.xml"});

        bundle.run(configuration, environment);
    }
}
