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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * Echoes back any received data from a client.
 */
public final class ChatServer {

    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));

    public static void main(String[] args) throws Exception {
        @SuppressWarnings("重复代码")
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        // Configure the server.
        // (1) 处理I/O操作的事件循环器 (其实是个线程池)。boss组。负责接收已到达的connection
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 工作轮询线程组。worker组。当boss接收到connection并把它注册到worker后，worker就可以处理connection上的数据通信
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        // 自定义回声处理器
        final ChatServerHandler serverHandler = new ChatServerHandler();
        try {
            // (2) ServerBootstrap 是用来搭建 server 的协助类
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
              // (3) 用来初始化一个新的Channel去接收到达的connection。这里面使用了工厂模式，反射
             .channel(NioServerSocketChannel.class)
             // 日志处理器。handler是boss轮询线程组的处理器
             .handler(new LoggingHandler(LogLevel.INFO))
              // (4) ChannelInitializer是一个特殊的 handler，帮助开发者配置Channel，而多数情况下你会配置Channel下的ChannelPipeline，
              // 往 pipeline 添加一些 handler (例如DiscardServerHandler) 从而实现你的应用逻辑。
              // 当你的应用变得复杂，你可能会向 pipeline 添加更多的 handler，并把这里的匿名类抽取出来作为一个单独的类。
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline p = ch.pipeline();
                     if (sslCtx != null) {
                         p.addLast(sslCtx.newHandler(ch.alloc()));
                     }
                     p.addLast(new StringDecoder());
                     p.addLast(new StringEncoder());
                     //p.addLast(new LoggingHandler(LogLevel.INFO));
                     p.addLast(serverHandler);
                 }
             })
              // (5) 你可以给Channel配置特有的参数。这里我们写的是 TCP/IP 服务器，所以可以配置一些 socket 选项，例如 tcpNoDeply 和 keepAlive。请参考ChannelOption和ChannelConfig文档来获取更多可用的 Channel 配置选项
             .option(ChannelOption.SO_BACKLOG, 100)
              // (6) option()用来配置NioServerSocketChannel(负责接收到来的connection)，而childOption()是用来配置被ServerChannel(这里是NioServerSocketChannel) 所接收的Channel
             .childOption(ChannelOption.SO_KEEPALIVE, true);
            // (7) 绑定端口，启动服务。
            ChannelFuture f = b.bind(PORT).sync();
            // 等待直到服务端Socket套接字关闭
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
