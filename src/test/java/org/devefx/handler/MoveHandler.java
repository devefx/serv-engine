package org.devefx.handler;

import io.netty.buffer.ByteBuf;

import org.devefx.serv.core.MessageHandler;
import org.devefx.serv.net.Sender;

public class MoveHandler implements MessageHandler<Short> {

	@Override
	public Short getId() {
		return 1;
	}

	@Override
	public void onMessage(Sender sender, ByteBuf buf) {
		
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		System.out.println(new String(bytes));
		
		if (sender.isWritable()) {
			String string = "hello register";
			sender.writeAndFlush(string.getBytes());
		}
	}
	
}
