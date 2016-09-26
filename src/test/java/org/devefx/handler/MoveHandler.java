package org.devefx.handler;

import org.devefx.serv.core.MessageHandler;
import org.devefx.serv.net.Sender;

public class MoveHandler implements MessageHandler<Short> {

	@Override
	public Short getId() {
		return 1;
	}

	@Override
	public void onMessage(Sender sender, Object msg) {
		
		System.out.println(msg);
		
		if (sender.isWritable()) {
			String string = "hello register";
			sender.writeAndFlush(string.getBytes());
		}
	}
	
}
