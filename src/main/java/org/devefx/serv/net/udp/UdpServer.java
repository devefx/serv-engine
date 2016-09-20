package org.devefx.serv.net.udp;

import java.io.IOException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import org.devefx.serv.net.ServerConfig;
import org.devefx.serv.net.ServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class UdpServer extends ServerConfig implements InitializingBean {
	
	private static final long serialVersionUID = -8839820873309300960L;
	
	private static final Logger log = LoggerFactory.getLogger(UdpServer.class);
	
	public void start() {
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
			 .channel(NioDatagramChannel.class)
			 .option(ChannelOption.SO_BROADCAST, true)
			 .handler(new ServerHandler(getRegistry()));
			
			b.bind(host, port).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						if (log.isInfoEnabled()) {
							log.info("udp server start successfully, port:" + port);
						}
					} else {
						throw new IOException(future.cause());
					}
				}
			}).sync().channel().closeFuture().await();
		} catch (Exception e) {
			log.error("An error occurred:", e);
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		new Thread(new Runnable() {
			@Override
			public void run() {
				start();
			}
		}).start();
	}
}
