package org.devefx.serv.net;

import org.devefx.serv.config.HandlerIdentifier;
import org.devefx.serv.config.HandlerRegistry;
import org.devefx.serv.core.MessageDispatcher;
import org.devefx.serv.core.MessageEvent;
import org.devefx.serv.net.tcp.TcpSender;
import org.devefx.serv.net.udp.UdpSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {

	public static final Logger log = LoggerFactory.getLogger(ServerHandler.class);
	
	private MessageDispatcher dispatcher;
	
	private HandlerIdentifier identifier;
	
	public ServerHandler(HandlerRegistry registry, HandlerIdentifier identifier) {
		dispatcher = new MessageDispatcher(registry);
		dispatcher.start();
		this.identifier = identifier;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if (msg instanceof DatagramPacket) {
			DatagramPacket packet = (DatagramPacket) msg;
			ByteBuf buf = (ByteBuf) packet.content();
			Object msgId = identifier.checkId(buf);
			
			Sender sender = new UdpSender(ctx.channel(), packet.sender());
			MessageEvent event = new MessageEvent(msgId, sender, buf);
			dispatcher.push(event);
		} else {
			ByteBuf buf = (ByteBuf) msg;
			Object msgId = identifier.checkId(buf);
			
			Sender sender = new TcpSender(ctx.channel());
			MessageEvent event = new MessageEvent(msgId, sender, buf);
			dispatcher.push(event);
		}
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		ctx.close();
	}
}
