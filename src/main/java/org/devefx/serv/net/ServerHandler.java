package org.devefx.serv.net;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.devefx.serv.core.MessageDispatcher;
import org.devefx.serv.core.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {

	public static final Logger log = LoggerFactory.getLogger(ServerHandler.class);
	
	private MessageDispatcher dispatcher;
	
	public ServerHandler() {
		dispatcher = new MessageDispatcher();
		dispatcher.start();
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
		if (msg instanceof DatagramPacket) {
			DatagramPacket packet = (DatagramPacket) msg;
			InetSocketAddress address = packet.sender();
			System.out.println("[UDP]这个人发来了一条消息：" + address);
		} else {
			Channel channel = ctx.channel();
			SocketAddress address = channel.remoteAddress();
			System.out.println("[TCP]这个人发来了一条消息：" + address);
		}
		dispatcher.push(new MessageEvent());
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		log.error("An error occurred:", cause);
	}
}
