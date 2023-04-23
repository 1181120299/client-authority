package com.jack.clientauthority.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        applicationContext = ac;
    }

    /**
     * 获取Bean对象
     * @param clazz Bean的Class
     * @return  Bean实例
     */
    public static <T> T getBean(Class<T> clazz) {
        Assert.notNull(applicationContext, "Not get applicationContext yet, you invoke too early. this method may not be useful for you.");
        return applicationContext.getBean(clazz);
    }
}
