package com.qiongloo.event.threadpool;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * http连接线程，正常只创建一次，并保持长连接不主动断开
 * */
public class HttpThread extends Thread implements UncaughtExceptionHandler {
	public HttpClient defaultClient;

	public HttpRequestBase mDefaultPost;

	public HttpRequestBase getDefaultPost() {
		return mDefaultPost;
	}
	
	public HttpThread(){
		super();
	}

	public HttpThread(Runnable runnable) {
		this("net_5", 5, runnable);
	}

	public HttpThread(String name, int priority, Runnable ruannable) {
		super(ruannable);
		setName(name);
		setPriority(priority);
		// 线程run异常捕获
		setUncaughtExceptionHandler(this);
	}

	public HttpThread(String name, int priority) {
		this(name, priority, null);
	}

	public HttpThread(ThreadGroup group, Runnable runnable, String threadName,
			long stackSize) {
		super(group, runnable, threadName, stackSize);
		// 线程run异常捕获
		setUncaughtExceptionHandler(this);
	}

	public void abort() {
		interrupt();
		if (mDefaultPost != null) {
			mDefaultPost.abort();
		}
	}

	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (defaultClient != null) {
			defaultClient.getConnectionManager().shutdown();
			defaultClient = null;
		}
		if (ex != null) {
			ex.printStackTrace();
		}

	}

	
	@Override
	protected void finalize() throws Throwable {
		try {
			uncaughtException(this, null);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			super.finalize();
		}
	}

}