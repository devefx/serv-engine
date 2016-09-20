package org.devefx.serv.config;

import java.io.Serializable;

public abstract class ServerConfig implements Serializable {
	
	private static final long serialVersionUID = -4517206023196961515L;
	
	protected String host = "0.0.0.0";
	protected int port = 9999;
	
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
}
