package com.hlr.config.spring.bean;

import com.alibaba.fastjson.JSON;
import com.hlr.config.ProviderConfig;
import com.hlr.domain.LocalServerInfo;
import com.hlr.domain.RpcProviderConfig;
import com.hlr.registry.RedisRegistryCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ProviderBean extends ProviderConfig implements ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger(ProviderBean.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("providerBean nozzle:{},ref:{},alias:{}",getNozzle(),getRef(),getAlias());

        RpcProviderConfig config = new RpcProviderConfig();
        config.setNozzle(this.getNozzle());
        config.setRef(this.getRef());
        config.setAlias(this.getAlias());
        config.setHost(LocalServerInfo.LOCAL_HOST);
        config.setPort(LocalServerInfo.LOCAL_PORT);
        //注册生产者
        long count = RedisRegistryCenter.registryProvider(config.getNozzle(), config.getAlias(), JSON.toJSONString(config));

        logger.info("注册生产者：{} {} {}", config.getNozzle(), config.getAlias(), count);

    }
}
