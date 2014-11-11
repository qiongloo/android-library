package com.cnbpi.event;

import java.util.concurrent.locks.Condition;

import android.os.Handler;

public interface IAsyncEventListener {
	Object onExecute(int what, Condition condition, Handler handler)
			throws Exception;

	void onDestory();
	
	void onLoading(int what);

	void onError(int what, int errorCode);

	void onEmpty(int what);

	void onSuccess(int what, Object object);
}
