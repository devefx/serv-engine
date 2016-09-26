package org.devefx.serv.net;

import org.devefx.serv.codec.BytesMessageDecoder;
import org.devefx.serv.config.HandlerRegistry;
import org.devefx.serv.core.MessageDecoder;

import java.io.Serializable;

public abstract class ServerConfig implements Serializable {
	
	private static final long serialVersionUID = -4517206023196961515L;
	
	protected String host = "0.0.0.0";
	protected int port = 81194;
	protected HandlerRegistry registry;
	protected MessageDecoder<?> decoder;
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public HandlerRegistry getRegistry() {
		return registry;
	}

	public void setRegistry(HandlerRegistry registry) {
		this.registry = registry;
	}
	
	public MessageDecoder<?> getDecoder() {
		if (decoder == null) {
			decoder = new BytesMessageDecoder();
		}
		return decoder;
	}
	
	public void setDecoder(MessageDecoder<?> decoder) {
		this.decoder = decoder;
	}
}
