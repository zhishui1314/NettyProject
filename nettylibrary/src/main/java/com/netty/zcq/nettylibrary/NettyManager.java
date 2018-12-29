package com.netty.zcq.nettylibrary;

import io.netty.channel.ChannelHandlerContext;

/**
 * 创建者   张蔡奇
 * 创建时间 2018/12/28 17:05
 * 创建公司 珠海市安克电子技术有限公司
 */
public class NettyManager {
    public static ChannelHandlerContext ctx = null;

    /**
     * 连接tcp
     *
     * @param isNCB                是否粘包拆包
     * @param ip                   ip地址
     * @param port                 端口号
     * @param nettyMessageListener
     */
    public static void connetTCP(final boolean isNCB, final String ip, final int port, final NettyMessageListener nettyMessageListener) {
        new Thread() {
            @Override
            public void run() {
                dealWithTCP(isNCB, ip, port, nettyMessageListener);
            }
        }.start();
    }

    /**
     * 连接tcp 没有粘拆包
     *
     * @param ip                   ip地址
     * @param port                 端口号
     * @param nettyMessageListener
     */
    public static void connetTCP(final String ip, final int port, final NettyMessageListener nettyMessageListener) {
        new Thread() {
            @Override
            public void run() {
                dealWithTCP(ip, port, nettyMessageListener);
            }
        }.start();
    }

    /**
     * @param isNCB                是否粘包拆包
     * @param ip                   ip地址
     * @param port                 端口号
     * @param nettyMessageListener
     */
    public static void dealWithTCP(boolean isNCB, final String ip, int port, final NettyMessageListener nettyMessageListener) {
        try {
            NettyClientIntHandler nettyClientIntHandler = new NettyClientIntHandler();
            nettyClientIntHandler.setOnMessageListener(new NettyMessageListener() {
                @Override
                public void onMessage(ChannelHandlerContext type, int sign, String result) {
                    ctx = type;
                    nettyMessageListener.onMessage(type, sign, result);
                }
            });
            NettyClient nettyClient = new NettyClient();
            nettyClient.setNettyMessageListener(new NettyMessageListener() {
                @Override
                public void onMessage(ChannelHandlerContext type, int sign, String result) {
                    ctx = type;
                    nettyMessageListener.onMessage(type, sign, result);
                }
            });
            nettyClient.connect(isNCB, ip, port, nettyClientIntHandler);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ctx = null;
        }

    }

    /**
     * @param ip                   ip地址
     * @param port                 端口号
     * @param nettyMessageListener
     */
    public static void dealWithTCP(final String ip, int port, final NettyMessageListener nettyMessageListener) {
        try {
            NettyClientIntHandler nettyClientIntHandler = new NettyClientIntHandler();
            nettyClientIntHandler.setOnMessageListener(new NettyMessageListener() {
                @Override
                public void onMessage(ChannelHandlerContext type, int sign, String result) {
                    ctx = type;
                    nettyMessageListener.onMessage(type, sign, result);
                }
            });
            NettyClient nettyClient = new NettyClient();
            nettyClient.setNettyMessageListener(new NettyMessageListener() {
                @Override
                public void onMessage(ChannelHandlerContext type, int sign, String result) {
                    ctx = type;
                    nettyMessageListener.onMessage(type, sign, result);
                }
            });
            nettyClient.connect(ip, port, nettyClientIntHandler);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ctx = null;
        }

    }

    /**
     * 发送数据
     * @param result
     */
    public static void sendMsg(String result) {
        NettyClientIntHandler.sendMsg(ctx, result);
    }

    /**
     * 发送数据处理粘包拆包
     * @param result
     * @param isNCB
     */
    public static void sendMsg(String result, boolean isNCB) {
        NettyClientIntHandler.sendMsg(ctx, result, isNCB);
    }
}
