package org.devefx.bootstrap;

import java.security.Principal;
import java.util.Date;

import org.devefx.serv.Message;
import org.devefx.serv.Session;
import org.devefx.serv.core.StandardEngine;
import org.devefx.serv.session.StandardManager;

public class Main {
	
	public static void main(String[] args) throws Exception {

		/*ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("server.xml");
		context.start();*/
		
		StandardServer server = new StandardServer();
		server.setHost("0.0.0.0");
		server.setPort(8888);
		server.addServer(new LoginService());
		
		
		StandardEngine engine = new StandardEngine();
		engine.setServer(server);
		engine.start();
	}
	
	class LoginService implements Service<MSG_TYPE> {
		
		public MSG_TYPE getType() {
			return Login_Request;
		}
		
		public void service(Request request) {
			User user = request.getPrincipal(); 	// 获取用户
			long time1 = request.getCreationTime();	// 获取创建时间
			String id = request.getSessionId();		// 获取会话ID
			Session session = request.getSession();	// 获取会话
			session.write("hello".getBytes());		// 发送消息
			Message message = request.readObject(Message.class);	// 读取消息（需要想引擎注册 消息解密器）
			
			Context context = request.getContext();	// 获取上下文
			Manager manager = context.getManager(); // 获取管理器
			Session[] sessions = manager.findSession(); // 获取全部会话
			Session session = manager.findSession("sessionID"); // 根据会话ID获取一个会话
			request.getLocalAddr();	// 获取本地IP
			request.getRemoteAddr(); // 获取远程IP
			request.getRemoteHost(); // 获取远程host
			request.getRemotePort(); // 获取远程port
			request.getContentLength(); // 获取消息长度
			request.getContentType(); // 获取消息类型
			request.getProtocol(); // 获取请求协议 UDP/TCP
			
		}
	}
	
}
