package com.netty.zcq.nettylibrary;

import io.netty.channel.ChannelHandlerContext;

/**
 * 创建者   张蔡奇
 * 创建时间 2018/12/28 17:06
 * 创建公司 珠海市安克电子技术有限公司
 */
public interface MessageListener {
    /**
     * @param ctx
     * @param type   0 Netty抛出异常 1  连接成功  2 接收数据处理 3 处理心跳 4 断线重连
     * @param result
     */
    void onMessage(ChannelHandlerContext ctx, int type, String result);
}
