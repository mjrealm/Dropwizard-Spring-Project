package com.mdeleon.dropwizard.spring.bean;

import org.springframework.beans.factory.annotation.Required;

/**
 * User: mdeleon
 * Date: 5/30/13
 * Time: 4:57 PM
 */
public class TestBean {

    private String text;

    @Required
    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

