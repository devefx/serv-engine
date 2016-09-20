package org.devefx.serv.core;

public interface MessageHandler {

	short getId();
	
	void onReceive(MessageEvent event);
	
}
