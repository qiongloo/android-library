package com.cnbpi.event;

import java.util.concurrent.locks.Condition;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.cnbpi.event.threadpool.ThreadPool;
import com.cnbpi.exception.ExceptionManagers;

public class DefaultTaskRunnable extends Handler implements Runnable,
IAsyncEventListener {

public static final int STATE_INIT = 0;

public static final int STATE_LOADING = 1;

public static final int STATE_FINISH = 2;

public static final int STATE_ERROR = 3;

public static final int STATE_HTTP_TIMEOUT = 4;//网络超时 mod

public static final int MSG_WHAT_DEFAFULT = Integer.MAX_VALUE;

private int state = STATE_INIT;

private Object mData;

private Exception errorMsg;

private IAsyncEventListener mCallBack;

private Condition mCondition;

private int mWhat = MSG_WHAT_DEFAFULT;

public void setWhat(int what) {
this.mWhat = what;
}

public Condition getCondition() {
return mCondition;
}

public void setCondition(Condition mCondition) {
this.mCondition = mCondition;
}

public DefaultTaskRunnable() {
super(Looper.getMainLooper());
}

public DefaultTaskRunnable(IAsyncEventListener callBack) {
super(Looper.getMainLooper());
mCallBack = callBack;
}

public synchronized boolean checkLoading() {
return state == STATE_INIT;
}

public synchronized boolean isLoading() {
return state == STATE_LOADING;
}

public synchronized void setState(int state) {
this.state = state;
}

public synchronized void reset() {
state = STATE_INIT;
mData = null;
errorMsg = null;
}

public synchronized boolean isFinished() {
return state == STATE_FINISH;
}

public synchronized boolean isError() {
return state == STATE_ERROR;
}

public Handler getHandler() {
return this;
}

/**
* 一定要在UI线程中发起
*/
public final boolean submit() {
return submit(MSG_WHAT_DEFAFULT);
}
/**
* 一定要在UI线程中发起
*/
public final boolean submit(int what) {
if (isLoading() || isFinished()) {
	return false;
}
mWhat = what;
onLoading(what);
ThreadPool.submitText(this);
return true;
}

@Override
public void handleMessage(android.os.Message msg) {
try {
	int what = msg.what;
	int state = getState();
	switch (state) {
	case STATE_ERROR:
		onError(what, -1);
		break;
	case STATE_HTTP_TIMEOUT: //mod 网络超时
		onError(what, -2);
	    break;
	case STATE_FINISH:
	case STATE_INIT:
		if (mData == null) {
			onEmpty(what);
		} else {
			onSuccess(what, mData);
		}
		break;
	default:
		onEmpty(what);
		break;
	}
} finally {
	reset();
}
}

public void setDataListener(IAsyncEventListener listener) {
if (mCallBack == listener) {
	return;
}
if (mCallBack != null) {
	mCallBack.onDestory();
	mCallBack = null;
}
mCallBack = listener;
}

public final boolean isAsyncTaskListenerNULL() {
return mCallBack == null;
}

public final synchronized int getState() {
return state;
}


@Override
public final void run() {
setState(STATE_LOADING);
mData = null;
errorMsg = null;
try {
	mData = onExecute(mWhat, mCondition, this);
	setState(STATE_FINISH);
} catch(ExceptionManagers e) {
	e.printStackTrace();
	setState(STATE_HTTP_TIMEOUT);
	errorMsg = e;
}catch(Exception e) {
	e.printStackTrace();
	setState(STATE_ERROR);
	errorMsg = e;
}



Message msg = Message.obtain();
msg.what = mWhat;
sendMessage(msg);
}

public void onDestory() {
// TODO Auto-generated method stub
if (mCallBack != null) {
	mCallBack.onDestory();
	mCallBack = null;
}
}

@Override
public Object onExecute(int what, Condition condition, Handler handler)
	throws Exception {
// TODO Auto-generated method stub
if (mCallBack != null) {
	return mCallBack.onExecute(what, condition, handler);
}
return null;
}

@Override
public void onLoading(int what) {
// TODO Auto-generated method stub
if (mCallBack != null) {
	mCallBack.onLoading(what);
}
}

@Override
public void onError(int what, int errorCode) {
// TODO Auto-generated method stub
if (mCallBack != null) {
	mCallBack.onError(what, errorCode);
}
}

@Override
public void onEmpty(int what) {
// TODO Auto-generated method stub
if (mCallBack != null) {
	mCallBack.onEmpty(what);
}
}

@Override
public void onSuccess(int what, Object object) {
// TODO Auto-generated method stub
if (mCallBack != null) {
	mCallBack.onSuccess(what, object);
}
}

@Override
protected void finalize() throws Throwable {
// TODO Auto-generated method stub
try {
	setDataListener(null);
} finally {
	super.finalize();
}
}

}

