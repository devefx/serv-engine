package org.devefx.handler;

import org.devefx.serv.core.old.MessageHandler;
import org.devefx.serv.net.Sender;

public class LoginHandler implements MessageHandler<Short> {

	@Override
	public Short getId() {
		return 0;
	}

	@Override
	public void onMessage(Sender sender, Object msg) {
		
		System.out.println(msg);
		
		if (sender.isWritable()) {
			String string = "hello login";
			sender.writeAndFlush(string.getBytes());
		}
	}

}
