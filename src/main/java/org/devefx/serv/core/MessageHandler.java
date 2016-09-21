package org.devefx.serv.core;

import org.devefx.serv.net.Sender;

import io.netty.buffer.ByteBuf;

public interface MessageHandler<T> {

	T getId();
	
	void onMessage(Sender sender, ByteBuf msg);
	
}
