package org.devefx.serv.net;

import org.devefx.serv.config.HandlerRegistry;
import org.devefx.serv.core.MessageDecoder;
import org.devefx.serv.core.MessageDispatcher;
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
	
	private MessageDecoder<?> decoder;
	
	public ServerHandler(HandlerRegistry registry, MessageDecoder<?> decoder) {
		dispatcher = new MessageDispatcher(registry);
		dispatcher.start();
		this.decoder = decoder;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if (msg instanceof DatagramPacket) {
			DatagramPacket packet = (DatagramPacket) msg;
			Sender sender = new UdpSender(ctx.channel(), packet.sender());
			dispatcher.push(decoder.decode((ByteBuf) packet.content(), sender));
		} else {
			Sender sender = new TcpSender(ctx.channel());
			dispatcher.push(decoder.decode((ByteBuf) msg, sender));
		}
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		log.error("An error occurred:", cause);
		ctx.close();
	}
}
