package com.hlr.network.codec;

import com.hlr.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * RpcDecoder
 * Description:
 * date: 2023/9/28 10:17
 *
 * @author hlr
 */
public class RpcDecoder extends ByteToMessageDecoder {
    
    private Class<?> genericClass;
    
    public RpcDecoder(Class<?> genericClass){
        this.genericClass = genericClass;
    }
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 数据大小 + 数据
        if (byteBuf.readableBytes() < 4){
         return;   
        }
        byteBuf.markReaderIndex();
        int dataLength = byteBuf.readInt();
        if(byteBuf.readableBytes() < dataLength){
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);
        list.add(SerializationUtil.deserialize(data,genericClass));
    }
}
