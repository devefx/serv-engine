package org.devefx.handler;

import io.netty.buffer.ByteBuf;

import org.devefx.serv.core.MessageHandler;
import org.devefx.serv.net.Sender;

public class LoginHandler implements MessageHandler<Short> {

	@Override
	public Short getId() {
		return 0;
	}

	@Override
	public void onMessage(Sender sender, ByteBuf buf) {
		
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		System.out.println(new String(bytes));
		
		if (sender.isWritable()) {
			String string = "hello login";
			sender.writeAndFlush(string.getBytes());
		}
	}

}
