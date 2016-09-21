package org.devefx.serv.core;

import org.devefx.serv.net.Sender;

import io.netty.buffer.ByteBuf;

public class MessageEvent {
	
	private final Object id;
	private final ByteBuf content;
	private final Sender sender;
	
	public MessageEvent(Object id, ByteBuf content, Sender sender) {
		this.id = id;
		this.content = content;
		this.sender = sender;
	}
	
	public Object getId() {
		return id;
	}
	
	public ByteBuf getContent() {
		return content;
	}
	
	public Sender getSender() {
		return sender;
	}
}
