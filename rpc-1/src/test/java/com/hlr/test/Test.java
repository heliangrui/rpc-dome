package com.hlr.test;

import com.alibaba.fastjson.JSONObject;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {

    @org.junit.Test
    public void test1() {
        String[] configs = {"rpc.xml"};
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(configs);

        Object consumer_itstack = classPathXmlApplicationContext.getBean("consumer_itstack");
        Object consumer_UserService = classPathXmlApplicationContext.getBean("consumer_UserService");
        Object provider_UserService = classPathXmlApplicationContext.getBean("provider_UserService");

        System.out.println(JSONObject.toJSONString(consumer_itstack));
        System.out.println(JSONObject.toJSONString(consumer_UserService));
        System.out.println(JSONObject.toJSONString(provider_UserService));
    }

}
