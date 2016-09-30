package org.devefx.serv.core.old;

import org.devefx.serv.net.Sender;

public interface MessageHandler<T> {

	T getId();
	
	void onMessage(Sender sender, Object msg);
	
}
