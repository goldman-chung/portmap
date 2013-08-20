package com.codesky.portmap;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.codesky.portmap.config.TransEntryConfig;

/**
 * 
 * @author Goldman <274851@qq.com>
 *
 */
public class SocketChannel implements Runnable {

	private final int id;
	private final Socket inSocket;
	private final Socket outSocket;
	private final TransEntryConfig config;
	private final AtomicBoolean running = new AtomicBoolean(true);
	private final Semaphore singal = new Semaphore(2);
	private final static AtomicInteger UNIQUE_ID = new AtomicInteger(1);
	private final static ExecutorService EXECUTORS = Executors.newCachedThreadPool();
	
	private final byte[] readBuffer;
	private final byte[] writeBuffer;
		
	public SocketChannel(Socket inSocket, TransEntryConfig config) 
		throws Exception {
		this.id = UNIQUE_ID.getAndIncrement();
		this.config = config;
		this.inSocket = initSocket(inSocket);
		this.outSocket = initSocket(new Socket(config.getDestName(), config.getDestPort()));
		this.readBuffer = new byte[config.getReadBufferSize()];
		this.writeBuffer = new byte[config.getWriteBufferSize()];
		
		ServerLogger.info(String.format("Accept connection #%d: %s", id, inSocket.getInetAddress().getHostAddress()));
	}
	
	private Socket initSocket(Socket socket) throws Exception {
		socket.setKeepAlive(config.isKeepAlive());
		socket.setTcpNoDelay(true);
		return socket;
	}
	
	private void initThreads() {
		EXECUTORS.execute(new Runnable() {
			@Override
			public void run() {
				try {
					int size = 0;
					Thread.currentThread().setName(getThreadName());
					while (running.get()) {
						InputStream in0 = inSocket.getInputStream();
						OutputStream out0 = outSocket.getOutputStream();
						if ((size = in0.read(writeBuffer)) > 0) {
							out0.write(writeBuffer, 0, size);
							out0.flush();
							if (ServerLogger.isDebugEnabled()) {
								ServerLogger.debug(String.format("#%d >>> DownStream transfer: %d bytes", id, size));
							}
						}
						Thread.sleep(60);
					}
				}
				catch (Throwable ex) {
					running.set(false);
				}
				singal.release();
			}
		});
				
		EXECUTORS.execute(new Runnable() {
			@Override
			public void run() {
				try {
					int size = 0;
					Thread.currentThread().setName(getThreadName());
					while (running.get()) {
						InputStream in1 = outSocket.getInputStream();
						OutputStream out1 = inSocket.getOutputStream();
						if ((size = in1.read(readBuffer)) > 0) {
							out1.write(readBuffer, 0, size);
							out1.flush();
							if (ServerLogger.isDebugEnabled()) {
								ServerLogger.debug(String.format("#%d <<< UpStream transfer: %d bytes", id, size));
							}
						}
						Thread.sleep(60);
					}
				}
				catch (Throwable ex) {
					running.set(false);
				}
				singal.release();
			}
		});
	}
	
	private String getThreadName() {
		return String.format("Socket[%d]-::%d->%s:%d-IO", id, config.getLocalPort(), config.getDestName(), config.getDestPort());
	}
	
	@Override
	public void run() {
		try {
			singal.acquire(2);
			initThreads();
			singal.acquire(2);
			dispose();
		}
		catch (Throwable ex) {
			ex.printStackTrace();
		}
	}
	
	private void dispose() {
		try {
			outSocket.close();
			inSocket.close();
			ServerLogger.info(String.format("Discount connection #%d", id));
		}
		catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

}
