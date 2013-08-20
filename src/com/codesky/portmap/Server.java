package com.codesky.portmap;

import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import com.codesky.portmap.config.TransEntryConfig;

/**
 * 
 * @author Goldman <274851@qq.com>
 *
 */
public class Server extends Thread {
	
	private final static AtomicBoolean STOP = new AtomicBoolean(false);
	private final static ExecutorService  EXECUTORS = Executors.newCachedThreadPool(new SimpleThreadFactory("SocketServer"));
	private final TransEntryConfig config;
	
	public Server(TransEntryConfig config) {
		this.config = config;
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				STOP.set(true);
			}
		});
	}
	
	@Override
	public void run() {
		try {
			ServerSocket server = new ServerSocket(config.getLocalPort(), 
				4, Inet4Address.getByName(config.getLocalName()));
			ServerLogger.info("Server start successfully");
			
			while (!STOP.get()) {
				Socket socket = server.accept();
				SocketChannel channel = new SocketChannel(socket, config);
				EXECUTORS.execute(channel);
			}
		}
		catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

}
