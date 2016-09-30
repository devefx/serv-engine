package org.devefx.serv;

import java.util.EventObject;

public final class MessageEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private Object data;
	private Message message;
	private String type;
	
	public MessageEvent(Message message, String type, Object data) {
		super(message);
		this.message = message;
		this.type = type;
		this.data = data;
	}
	
	public Object getData() {
		return data;
	}
	
	public Message getMessage() {
		return message;
	}
	
	public String getType() {
		return type;
	}
}
