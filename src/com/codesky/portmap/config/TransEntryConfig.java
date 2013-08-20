package com.codesky.portmap.config;

import java.util.Map;

import com.codesky.portmap.ServerLogger;

/**
 * 
 * @author Goldman <274851@qq.com>
 *
 */
public class TransEntryConfig {

	private String localName;
	private int localPort;
	private String destName;
	private int destPort;
	private int readBufferSize = 1024;
	private int writeBufferSize = 1024;
	private boolean keepAlive = true;
		
	public TransEntryConfig(Map<String, String> params) {
		String src = params.get("src");
		this.localName = src.substring(0, src.indexOf(":"));
		this.localPort = Integer.valueOf(src.substring(src.indexOf(":") + 1, src.length()));
		
		String dest = params.get("dst");
		this.destName = dest.substring(0, dest.indexOf(":"));
		this.destPort = Integer.valueOf(dest.substring(dest.indexOf(":") + 1, dest.length()));
		
		if (params.containsKey("rbsize")) {
			int value = Integer.valueOf(params.get("rbsize"));
			this.readBufferSize = (value <= 0) ? readBufferSize : value;
			if (value <= 0)
				ServerLogger.warn(String.format("Invalid parameter: rbsize => %d", value));
		}
		
		if (params.containsKey("wbsize")) {
			int value = Integer.valueOf(params.get("wbsize"));
			this.writeBufferSize = (value <= 0) ? writeBufferSize : value;
			if (value <= 0)
				ServerLogger.warn(String.format("Invalid parameter: wbsize => %d", value));
		}
		
		if (params.containsKey("keepalive")) {
			this.keepAlive = Boolean.valueOf(params.get("keepalive"));
		}
	}
	
	public String getLocalName() {
		return localName;
	}
	public void setLocalName(String localName) {
		this.localName = localName;
	}
	public int getLocalPort() {
		return localPort;
	}
	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}
	public String getDestName() {
		return destName;
	}
	public void setDestName(String destName) {
		this.destName = destName;
	}
	public int getDestPort() {
		return destPort;
	}
	public void setDestPort(int destPort) {
		this.destPort = destPort;
	}
	public int getReadBufferSize() {
		return readBufferSize;
	}
	public void setReadBufferSize(int readBufferSize) {
		this.readBufferSize = readBufferSize;
	}
	public int getWriteBufferSize() {
		return writeBufferSize;
	}
	public void setWriteBufferSize(int writeBufferSize) {
		this.writeBufferSize = writeBufferSize;
	}
	public boolean isKeepAlive() {
		return keepAlive;
	}
	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}
	
}
