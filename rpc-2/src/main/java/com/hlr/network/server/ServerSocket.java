package com.hlr.network.server;

import com.hlr.domain.LocalServerInfo;
import com.hlr.network.codec.RpcDecoder;
import com.hlr.network.codec.RpcEncoder;
import com.hlr.network.msg.Request;
import com.hlr.network.msg.Response;
import com.hlr.util.NetUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.context.ApplicationContext;

/**
 * serverSocket
 * Description:
 * date: 2023/9/25 17:29
 *
 * @author hlr
 */
public class ServerSocket implements Runnable {

    private ChannelFuture f;
    private transient ApplicationContext applicationContext;

    public ServerSocket(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public boolean isActiveSocketServer() {
        try {
            if (f != null) {
                return f.channel().isActive();
            }
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    public void run() {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, work).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new RpcEncoder(Response.class),
                                    new RpcDecoder(Request.class),
                                    new ServerHandler(applicationContext)
                            );

                        }
                    });
            int port = 22201;
            while (NetUtil.isPortUsing(port)) {
                port++;
            }
            LocalServerInfo.LOCAL_HOST = NetUtil.getHost();
            LocalServerInfo.LOCAL_PORT = port;

            this.f = b.bind(port).sync();
            this.f.channel().closeFuture().sync();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }


    }
}
