package org.devefx.serv.net.tcp;

import java.io.IOException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.devefx.serv.net.ServerConfig;
import org.devefx.serv.net.ServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class TcpServer extends ServerConfig implements InitializingBean {

	private static final long serialVersionUID = -6056722357615314400L;
	
	private static final Logger log = LoggerFactory.getLogger(TcpServer.class);
	
	public void start() {
		EventLoopGroup boosGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(boosGroup, workerGroup)
			 .channel(NioServerSocketChannel.class)
			 .option(ChannelOption.SO_BACKLOG, 1024)
			 .handler(new LoggingHandler(LogLevel.INFO))
			 .childHandler(new MyChannelInitializer());
			
			b.bind(host, port).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						if (log.isInfoEnabled()) {
							log.info("tcp server start successfully, bind: /" + host + ":" + port);
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
			boosGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
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
	
	class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			// decoder
			pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 2, 0, 2));
			// encoder
			pipeline.addLast(new LengthFieldPrepender(2));
			// business
			pipeline.addLast(new ServerHandler(getRegistry(), getDecoder()));
		}
	}
}
