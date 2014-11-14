package com.qiongloo.event;

import java.util.concurrent.locks.Condition;

import android.os.Handler;

public abstract class AsycnTaskListener implements IAsyncEventListener{
	protected DefaultTaskRunnable mAsyncTask = new DefaultTaskRunnable(this);

	/**
	 * 要在UI线程发起
	 * @param what
	 * @return
	 */
	public boolean submit(int what) {
		return mAsyncTask.submit(what);
	}

	@Override
	public void onDestory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoading(int what) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(int what, int errorCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEmpty(int what) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(int what, Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public abstract Object onExecute(int what, Condition condition, Handler handler)
			throws Exception; 
	
	
}
