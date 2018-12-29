package com.netty.zcq.nettylibrary;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyClient {
    private EventLoopGroup workerGroup;
    private MessageListener nettyMessageListener = null;

    public void setNettyMessageListener(MessageListener nettyMessageListener) {
        this.nettyMessageListener = nettyMessageListener;
    }

    /**
     * @param isNCB 是否处理粘包拆包
     * @param host  ip
     * @param port  端口号
     * @param he
     * @throws Exception
     */
    public void connect(final boolean isNCB, final String host, final int port, final NettyClientIntHandler he)
            throws Exception {
        workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            if (isNCB) {  //拆包粘包
                                pipeline
                                        .addLast(new LineBasedFrameDecoder(100 * 1024 * 1024));
                            }

                            pipeline.addLast(new StringDecoder())
                                    .addLast(new StringEncoder())
                                    .addLast(new IdleStateHandler(30, 30, 30))
                                    .addLast(he);
                            //处理网络IO 处理器
                        }
                    });
            // 发起异步连接操作
            ChannelFuture f = b.connect(host, port).sync();
            // 等待客户端链路关闭
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();   //释放NIO线程组
            nettyMessageListener.onMessage(null, 4, "重连");
        }
    }

    public void shutDown() {
        workerGroup.shutdownGracefully();   //释放NIO线程组
    }
}
