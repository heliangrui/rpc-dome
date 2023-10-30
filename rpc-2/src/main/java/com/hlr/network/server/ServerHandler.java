package com.hlr.network.server;

import com.hlr.network.msg.Request;
import com.hlr.network.msg.Response;
import com.hlr.util.ClassLoaderUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

/**
 * ServerHandler
 * Description:
 * date: 2023/9/28 10:59
 *
 * @author hlr
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    
    private ApplicationContext applicationContext;
    
    public ServerHandler(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = (Request) msg;
        // 反射获取class
        Class<?> aClass = ClassLoaderUtils.forName(request.getNozzle());
        // 获取方法
        Method method = aClass.getMethod(request.getMethodName(), request.getParamTypes());
        // 从容器获取对象
        Object bean = applicationContext.getBean(request.getRef());
        // 调用实例方法
        Object result = method.invoke(bean, request.getArgs());

        Response response = new Response();
        response.setRequestId(request.getRequestId());
        response.setResult(result);
        // 写入
        ctx.writeAndFlush(response);
        // 清空缓存
        ReferenceCountUtil.release(request);
        


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
