package com.hlr.config.spring.bean;

import com.hlr.config.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ConsumerBean extends ConsumerConfig implements ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(ConsumerBean.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("consumerBean nozzle:{}, alias:{}", getNozzle(), getAlias());
    }
}
