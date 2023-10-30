## rpc

### 概念介绍

远程过程调用协议 RPC（Remote Procedure Call）—远程过程调用，它是一种通过网络从远程计算机程序上请求服务，而不需要了解底层网络技术的协议。RPC协议假定某些传输协议的存在，如TCP或UDP，为通信程序之间携带信息数据。在OSI网络通信模型中，RPC跨越了传输层和应用层。RPC使得开发包括网络分布式多程序在内的应用程序更加容易。 RPC采用客户机/服务器模式。请求程序就是一个客户机，而服务提供程序就是一个服务器。首先，客户机调用进程发送一个有进程参数的调用信息到服务进程，然后等待应答信息。在服务器端，进程保持睡眠状态直到调用信息到达为止。当一个调用信息到达，服务器获得进程参数，计算结果，发送答复信息，然后等待下一个调用信息，最后，客户端调用进程接收答复信息，获得进程结果，然后调用执行继续进行。

#### 准备

1. 自定义配置xml
2. 通信Netty
3. RPC中间件

### 开始

### 1 自定义配置xml

##### 1.1定义注册中心、服务提供者、消费者

- 注册中心

服务ip地址、服务端口

- 服务提供者

接口、映射、组

- 消费者

接口、组

##### 1.2 定义xml

使用NamespaceHandlerSupport进行xml数据加载

定义rpc.xsd (可以从springbean.xml复制)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<xsd:schema xmlns="http://rpc.hlr.org/schema/rpc"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            xmlns:beans="http://www.springframework.org/schema/beans"
            targetNamespace="http://rpc.hlr.org/schema/rpc"
            elementFormDefault="qualified" attributeFormDefault="unqualified">
    <xsd:import namespace="http://www.springframework.org/schema/beans"/>

    <xsd:element name="server">
        <xsd:complexType>
            <xsd:complexContent>
                <xsd:extension base="beans:identifiedType">
                    <xsd:attribute name="host" type="xsd:string">
                        <xsd:annotation>
                            <xsd:documentation><![CDATA[ HOST ]]></xsd:documentation>
                        </xsd:annotation>
                    </xsd:attribute>
                    <xsd:attribute name="port" type="xsd:string">
                        <xsd:annotation>
                            <xsd:documentation><![CDATA[ PORT  ]]></xsd:documentation>
                        </xsd:annotation>
                    </xsd:attribute>
                </xsd:extension>
            </xsd:complexContent>
        </xsd:complexType>
    </xsd:element>

    <xsd:element name="consumer">
        <xsd:complexType>
            <xsd:complexContent>
                <xsd:extension base="beans:identifiedType">
                    <xsd:attribute name="nozzle" type="xsd:string">
                        <xsd:annotation>
                            <xsd:documentation><![CDATA[ 接口名称 ]]></xsd:documentation>
                        </xsd:annotation>
                    </xsd:attribute>
                    <xsd:attribute name="alias" type="xsd:string">
                        <xsd:annotation>
                            <xsd:documentation><![CDATA[ 服务别名分组信息  ]]></xsd:documentation>
                        </xsd:annotation>
                    </xsd:attribute>
                </xsd:extension>
            </xsd:complexContent>
        </xsd:complexType>
    </xsd:element>

    <xsd:element name="provider">
        <xsd:complexType>
            <xsd:complexContent>
                <xsd:extension base="beans:identifiedType">
                    <xsd:attribute name="nozzle" type="xsd:string">
                        <xsd:annotation>
                            <xsd:documentation><![CDATA[ 接口名称 ]]></xsd:documentation>
                        </xsd:annotation>
                    </xsd:attribute>
                    <xsd:attribute name="ref" type="xsd:string">
                        <xsd:annotation>
                            <xsd:documentation><![CDATA[ 接口实现类  ]]></xsd:documentation>
                        </xsd:annotation>
                    </xsd:attribute>
                    <xsd:attribute name="alias" type="xsd:string">
                        <xsd:annotation>
                            <xsd:documentation><![CDATA[ 服务别名分组信息  ]]></xsd:documentation>
                        </xsd:annotation>
                    </xsd:attribute>
                </xsd:extension>
            </xsd:complexContent>
        </xsd:complexType>
    </xsd:element>
</xsd:schema>
```

定义spring.schemas

```
http\://rpc.hlr.org/schema/rpc/rpc.xsd=META-INF/rpc.xsd
```

定义spring.handlers

```
http\://rpc.hlr.org/schema/rpc=com.hlr.config.spring.MyNamespaceHandler
```

定义MyNamespaceHandler xml处理器

```
package com.hlr.config.spring;

import com.hlr.config.spring.bean.ConsumerBean;
import com.hlr.config.spring.bean.ProviderBean;
import com.hlr.config.spring.bean.ServerBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class MyNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("consumer", new MyBeanDefinitionParser(ConsumerBean.class));
        registerBeanDefinitionParser("provider", new MyBeanDefinitionParser(ProviderBean.class));
        registerBeanDefinitionParser("server", new MyBeanDefinitionParser(ServerBean.class));
    }
}
```

### 2 通信Netty

#### 2.1 定义请求与返回格式

请求体

```
import io.netty.channel.Channel;

/**
 */
public class Request {

    private transient Channel channel;

    private String requestId;
    private String methodName;  //方法
    private Class[] paramTypes; //属性
    private Object[] args;      //入参
    private String nozzle; //接口
    private String ref;    //实现类
    private String alias;  //别名

  
}

```

返回体

```
import io.netty.channel.Channel;

public class Response {

    private transient Channel channel;
    private String requestId;
    private Object result;

}

```

#### 2.2 定义通信协议

数据传输格式-数据大小+数据体

#### 2.3 定义服务端

获取请求内容，从springboot容器中获取对应接口
执行具体方法，将参数返回
- 定义服务注册信息

#### 2.4 定义客户端

将参数返回解析

### 3 RPC中间件

#### 3.1 改造ServerBean

- 启动服务注册中心
- 启动服务端netty

#### 3.2 改造CustomerBean

- 获取服务注册中心服务
- 启动客户端netty
- 生成代理对象

#### 3.3 改造ProviderBean

- 将提供的接口放到注册中心
