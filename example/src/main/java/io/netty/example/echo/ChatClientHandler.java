package io.netty.example.echo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 聊天客户端
 * @author: LeoLee
 * @date: 2019/11/8 17:05
 */
public class ChatClientHandler extends SimpleChannelInboundHandler<BaseMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseMsg msg) throws Exception {
        System.out.println("接收到服务端消息:" + msg.toString());
    }

}
