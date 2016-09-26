package org.devefx.serv.core;

import org.devefx.serv.net.Sender;

import io.netty.buffer.ByteBuf;

public interface MessageDecoder<T> {
	
	MessageEvent<T> decode(ByteBuf buf, Sender sender);
	
}
