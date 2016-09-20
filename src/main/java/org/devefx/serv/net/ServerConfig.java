package org.devefx.serv.net;

import org.devefx.serv.config.HandlerRegistry;

import java.io.Serializable;

public abstract class ServerConfig implements Serializable {
	
	private static final long serialVersionUID = -4517206023196961515L;
	
	protected String host = "0.0.0.0";
	protected int port = 9999;
	protected HandlerRegistry registry;
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}

	public HandlerRegistry getRegistry() {
		return registry;
	}

	public void setRegistry(HandlerRegistry registry) {
		this.registry = registry;
	}
}
