package com.codesky.portmap;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author Goldman <274851@qq.com>
 *
 */
public class SimpleThreadFactory implements ThreadFactory {

	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private ThreadGroup group;
	private String groupName;

	public SimpleThreadFactory() {
		init();
		groupName = group.getName();
	}

	public SimpleThreadFactory(String groupName) {
		init();
		this.groupName = groupName;
	}

	private void init() {
		Executors.defaultThreadFactory();
		SecurityManager securitymanager = System.getSecurityManager();
		group = securitymanager == null ? Thread.currentThread().getThreadGroup() : securitymanager.getThreadGroup();
	}

	@Override
	public Thread newThread(Runnable runnable) {
		String treadName = groupName + "-thread-" + threadNumber.getAndIncrement();
		Thread t = new Thread(group, runnable, treadName);
		if (t.isDaemon())
			t.setDaemon(false);
		if (t.getPriority() != Thread.NORM_PRIORITY)
			t.setPriority(Thread.NORM_PRIORITY);
		return t;
	}

}
