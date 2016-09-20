package org.devefx.serv.core;

public interface MessageHandler {

	short getId();
	
	void onMessage(MessageEvent event);
	
}
