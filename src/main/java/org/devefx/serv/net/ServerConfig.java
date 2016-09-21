package org.devefx.serv.net;

import org.devefx.serv.config.HandlerIdentifier;
import org.devefx.serv.config.HandlerRegistry;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;

public abstract class ServerConfig implements Serializable {
	
	private static final long serialVersionUID = -4517206023196961515L;
	
	protected String host = "0.0.0.0";
	protected int port = 81194;
	protected HandlerRegistry registry;
	protected HandlerIdentifier identifier;
	
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
	
	public HandlerIdentifier getIdentifier() {
		if (identifier == null) {
			identifier = new DefaultHandlerIdentifier();
		}
		return identifier;
	}

	public void setIdentifier(HandlerIdentifier identifier) {
		this.identifier = identifier;
	}
	
	class DefaultHandlerIdentifier implements HandlerIdentifier {
		@Override
		public Object checkId(ByteBuf buf) {
			return buf.readShort();
		}
	}
}
