package com.hlr.config.spring.bean;

import com.hlr.config.ServerConfig;
import com.hlr.domain.LocalServerInfo;
import com.hlr.network.server.ServerSocket;
import com.hlr.registry.RedisRegistryCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ServerBean extends ServerConfig implements ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger(ServerBean.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("server host:{},port:{}", getHost(), getPort());

        logger.info("启动注册中心 ...{},{}",getHost(), getPort());
        RedisRegistryCenter.init(getHost(), getPort());
        logger.info("启动注册中心完成");

        logger.info("初始化生产者服务");
        ServerSocket serverSocket = new ServerSocket(applicationContext);
        Thread thread = new Thread(serverSocket);
        thread.setDaemon(true);
        thread.start();

        while(!serverSocket.isActiveSocketServer()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }
        }
        logger.info("初始化生产者服务 {},{}", LocalServerInfo.LOCAL_PORT,LocalServerInfo.LOCAL_HOST);

    }
}
