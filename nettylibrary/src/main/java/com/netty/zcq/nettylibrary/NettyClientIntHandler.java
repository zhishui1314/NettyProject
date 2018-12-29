package com.netty.zcq.nettylibrary;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class NettyClientIntHandler extends SimpleChannelInboundHandler<String> {
    private MessageListener onMessageListener;

    public void setOnMessageListener(MessageListener onMessageListener) {
        this.onMessageListener = onMessageListener;
    }
    // 接收server端的消息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if (onMessageListener != null)
            onMessageListener.onMessage(ctx, 2, msg);
    }

    // 连接成功
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (onMessageListener != null) {  //连接成功
            onMessageListener.onMessage(ctx, 1, null);
        }
    }

    //心跳处理
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.ALL_IDLE) {
                if (onMessageListener != null) {
                    onMessageListener.onMessage(ctx, 3, "");
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    // 捕获到连接异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        // TODO Auto-generated method stub
        if (onMessageListener != null)
            onMessageListener.onMessage(ctx, 0, cause.getMessage());
        if (ctx != null)
            ctx.close();
        super.exceptionCaught(ctx, cause);
    }

    //客户端断开
    @Override
    public void channelInactive(io.netty.channel.ChannelHandlerContext ctx) throws Exception {
        if (onMessageListener != null)
            onMessageListener.onMessage(ctx, 0, "客户端断开");
        if (ctx != null)
            ctx.close();
    }

    // 发送消息
    public static void sendMsg(ChannelHandlerContext ctx, String msg) {
        if (ctx != null) {
            ctx.writeAndFlush(msg);
        }

    }

    // 发送消息
    public static void sendMsg(ChannelHandlerContext ctx, String msg, boolean isNCB) {
        if (ctx != null) {
            ctx.writeAndFlush(msg + "\r\n");
        }

    }


}
