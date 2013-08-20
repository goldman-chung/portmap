package com.codesky.portmap;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codesky.portmap.config.TransEntryConfig;

/**
 * 
 * Usage:
 * 		java -jar portmap.jar --src=127.0.0.1:4010 --dst=192.168.40.16:4010 --rbsize=2048 --wbsize=2048 --keepalive=true
 * 
 * Stop Server Command:
 * 		kill -15 process id
 * 
 * @author Goldman <274851@qq.com>
 *
 */
public class Start {

	public static void main(String[] args) throws Exception {
		Map<String, String> params = parseArguments(args);
		TransEntryConfig config = new TransEntryConfig(params);
		Server server = new Server(config);
		server.start();
		server.join();
	}
	
	private static Map<String, String> parseArguments(String[] args) {
		StringBuilder sb = new StringBuilder();
		for (String item : args) sb.append(item);
		Map<String, String> params = new HashMap<String, String>(args.length);

		Pattern pattern = Pattern.compile("--([a-zA-Z]+)\\s*=([^--]+)");
		Matcher matcher = pattern.matcher(sb.toString());
		while(matcher.find()) {
			String key = matcher.group(1);
			String value = matcher.group(2);
			params.put(key, value);
		}
		return params;
	}
	
}
