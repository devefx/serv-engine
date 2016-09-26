package org.devefx.serv.core;

import org.devefx.serv.net.Sender;

public class MessageEvent<T> {
	
	private final Object id;
	private final T content;
	private final Sender sender;
	
	public MessageEvent(Object id, T content, Sender sender) {
		this.id = id;
		this.content = content;
		this.sender = sender;
	}
	
	public Object getId() {
		return id;
	}
	
	public T getContent() {
		return content;
	}
	
	public Sender getSender() {
		return sender;
	}

}
