package org.devefx.serv.core;

import org.devefx.serv.net.Sender;

import io.netty.buffer.ByteBuf;

public class MessageEvent {
	
	private final Object id;
	private final Sender sender;
	private final ByteBuf content;
	
	public MessageEvent(Object id, Sender sender, ByteBuf content) {
		this.id = id;
		this.sender = sender;
		this.content = content;
		this.content.retain();
	}
	
	public Object getId() {
		return id;
	}
	
	public Sender getSender() {
		return sender;
	}
	
	public ByteBuf getContent() {
		return content;
	}
}
