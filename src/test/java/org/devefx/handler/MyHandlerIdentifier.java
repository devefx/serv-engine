package org.devefx.handler;

import io.netty.buffer.ByteBuf;

import org.devefx.serv.config.HandlerIdentifier;

public class MyHandlerIdentifier implements HandlerIdentifier {

	@Override
	public Object checkId(ByteBuf buf) {
		
		return 0;
	}

}
