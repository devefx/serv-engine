package org.devefx.serv.net.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.net.SocketAddress;

import org.devefx.serv.net.Sender;

public class TcpSender implements Sender {

	private Channel channel;
	
	public TcpSender(Channel channel) {
		this.channel = channel;
	}
	
	@Override
	public boolean isActive() {
		return isActive();
	}
	
	@Override
	public boolean isWritable() {
		return channel.isWritable();
	}

	@Override
	public SocketAddress remoteAddress() {
		return channel.remoteAddress();
	}

	@Override
	public void writeAndFlush(ByteBuf buf) {
		channel.writeAndFlush(buf);
	}
	
	@Override
	public void writeAndFlush(byte[] bytes) {
		writeAndFlush(Unpooled.copiedBuffer(bytes));
	}
	
	@Override
	public void close() {
		channel.close();
	}

}
