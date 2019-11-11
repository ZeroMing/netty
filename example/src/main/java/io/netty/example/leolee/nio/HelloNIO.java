package io.netty.example.leolee.nio;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Hello ， NIO
 *
 * @author: LeoLee
 * @date: 2019/11/11 14:09
 */
public class HelloNIO {

    public static void main(String[] args) {
        try{
            Selector acceptorSelector = SelectorProvider.provider().openSelector();
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);

            InetAddress lh = InetAddress.getLocalHost();
            InetSocketAddress isa = new InetSocketAddress(lh, 8900);
            ssc.socket().bind(isa);

            SelectionKey acceptorKey = ssc.register(acceptorSelector, SelectionKey.OP_ACCEPT);

            int keysAdd = 0;
            while ((keysAdd = acceptorSelector.select()) > 0) {

                Set<SelectionKey> selectionKeys = acceptorSelector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    iterator.remove();
                    ServerSocketChannel channel = (ServerSocketChannel)next.channel();
                    Socket socket = channel.accept().socket();
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    Date now = new Date();
                    out.println(now);
                    out.close();

                }


            }


        }catch (Exception e){

        }





    }
}
