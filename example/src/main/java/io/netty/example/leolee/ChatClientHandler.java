/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.leolee;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.StringUtil;

import java.util.Scanner;

/**
 * Handler implementation for the echo client.  It initiates the ping-pong
 * traffic between the echo client and server by sending the first message to
 * the server.
 */
public class ChatClientHandler extends SimpleChannelInboundHandler<String> {

    public ChatClientHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("channel激活...");
        ctx.writeAndFlush("你好呀！我是客户端A");
        System.out.println("客户端发送消息");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("接收到服务端的消息:"+msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
        System.out.println("继续输入>>>");
        Scanner scanner = new Scanner(System.in);
        String nextMessage = scanner.next();
        ctx.channel().writeAndFlush(nextMessage);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}