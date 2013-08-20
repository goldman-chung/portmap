package com.codesky.portmap;

/**
 * 
 * @author Goldman <274851@qq.com>
 *
 */
public class ServerLogger {

	private static boolean debugEnabled = true;
	private static boolean errorEnabled = true;
	private static boolean warnEnabled = true;
	private static boolean infoEnabled = true;
	
	public static boolean isDebugEnabled() {
		return debugEnabled;
	}
	
	public static boolean isErrorEnabled() {
		return errorEnabled;
	}
	
	public static boolean isWarnEnabled() {
		return warnEnabled;
	}
	
	public static boolean isInfoEnabled() {
		return infoEnabled;
	}
	
	/**
	 * 记录失败消息
	 * 
	 * @param message
	 */
	public static void error(String message) {
		if (isErrorEnabled())
			System.err.println(message);
	}
	
	/**
	 * 记录成功消息
	 * @param message
	 */
	public static void info(String message) {
		if (isInfoEnabled())
			System.out.println(message);
	}
	
	/**
	 * 记录调试信息
	 * @param message
	 */
	public static void debug(String message) {
		if (isDebugEnabled())
			System.out.println(message);
	}
	
	/**
	 * 记录警告信息
	 * @param message
	 */
	public static void warn(String message) {
		if (isWarnEnabled())
			System.err.println(message);
	}
	
}
