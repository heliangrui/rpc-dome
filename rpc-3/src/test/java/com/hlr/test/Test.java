package com.hlr.test;

import com.hlr.test.service.IUserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {


    public static void main(String[] args) {

        String[] configs = {"rpc.xml","applicationContext.xml"};
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(configs);

        IUserService consumer_UserService = (IUserService)classPathXmlApplicationContext.getBean("consumer_UserService");
        IUserService userService = (IUserService)classPathXmlApplicationContext.getBean("userService");


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String userName = consumer_UserService.getUserName("111");

                    System.out.println(userName);
                    String userName1 = userService.getUserName("111");
                    System.out.println(userName1);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

}
