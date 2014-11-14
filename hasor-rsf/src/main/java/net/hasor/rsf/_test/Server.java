/*
 * Copyright 2008-2009 the original 赵永春(zyc@hasor.net).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hasor.rsf._test;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.hasor.rsf.net.netty.RSFCodec;
import net.hasor.rsf.runtime.server.ServerHandler;
/**
 * 
 * @version : 2014年9月12日
 * @author 赵永春(zyc@hasor.net)
 */
public class Server {
    public void start(String host, int port) throws Exception {
        final EventLoopGroup bossGroup = new NioEventLoopGroup(4);//
        final ServerRsfContext manager = new ServerRsfContext();
        manager.getCallExecute("aa").execute(new Runnable() {
            public void run() {
                manager.print();
            }
        });
        //
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, manager.getLoopGroup()).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(//
                            new RSFCodec(),//
                            new ServerHandler(manager));
                }
            }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            manager.getLoopGroup().shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.start("127.0.0.1", 8000);
    }
}