package io.netty.example.echo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 聊天服务端
 * @author: LeoLee
 * @date: 2019/11/8 17:12
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<BaseMsg>{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseMsg msg) throws Exception {

    }
}
