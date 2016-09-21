package org.devefx.serv.config;

import io.netty.buffer.ByteBuf;

public interface HandlerIdentifier {
	
	Object checkId(ByteBuf buf);
	
}
