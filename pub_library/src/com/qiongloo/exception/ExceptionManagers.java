package com.qiongloo.exception;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;

import org.apache.http.HttpException;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.qiongloo.publib.R;

/**
 * 全局异常类
 * 
 * @Function 保存错误信息和友情提示
 * 
 */
public class ExceptionManagers extends Exception {

	private final static boolean DEBUG = false;// 是否保存错误日志
	private final static String ErrorLOG_FILE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/cnapp/Log/";// 错误日志的而文件地址

	/** 定义异常类型 */
	public final static byte TYPE_NETWORK = 0x01;
	public final static byte TYPE_SOCKET = 0x02;
	public final static byte TYPE_HTTP_CODE = 0x03;
	public final static byte TYPE_HTTP_ERROR = 0x04;
	public final static byte TYPE_XML = 0x05;
	public final static byte TYPE_IO = 0x06;
	public final static byte TYPE_RUN = 0x07;
	public final static byte TYPE_JSON = 0x08;

	private byte type;
	private int code;

	/*** 对于错误日志的处理 ****/
	private Properties mDeviceCrashInfo = new Properties();
	private static final String CRASH_REPORTER_EXTENSION = ".cr";
	private static final String STACK_TRACE = "STACK_TRACE";
	private static final String VERSION_NAME = "versionName";
	private static final String VERSION_CODE = "versionCode";
	public static final String TAG = "AppException";

	/** 系统默认的UncaughtException处理类 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	private ExceptionManagers() {
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	private ExceptionManagers(byte type, int code, Exception excp) {
		super(excp);
		this.type = type;
		this.code = code;
		// handleException();//处理异常
		if (DEBUG) {
			this.saveErrorLog(excp);
		}
	}

	public int getCode() {
		return this.code;
	}

	public int getType() {
		return this.type;
	}

	public void handleException() {
		switch (this.getType()) {
		case TYPE_HTTP_CODE:
			break;
		case TYPE_HTTP_ERROR:
			break;
		case TYPE_SOCKET:
			break;
		case TYPE_NETWORK:
			break;
		case TYPE_XML:
			break;
		case TYPE_IO:
			break;
		case TYPE_RUN:
		case TYPE_JSON:
			break;
		}

	}

	/**
	 * 提示友好的错误信息
	 * 
	 * @param ctx
	 */
	/*
	 * public void makeToast(Context ctx){ switch(this.getType()){ case
	 * TYPE_HTTP_CODE: String err =
	 * ctx.getString(R.string.http_status_code_error, this.getCode());
	 * Toast.makeText(ctx, err, Toast.LENGTH_SHORT).show(); break; case
	 * TYPE_HTTP_ERROR: Toast.makeText(ctx, R.string.http_exception_error,
	 * Toast.LENGTH_SHORT).show(); break; case TYPE_SOCKET: Toast.makeText(ctx,
	 * R.string.socket_exception_error, Toast.LENGTH_SHORT).show(); break; case
	 * TYPE_NETWORK: Toast.makeText(ctx, R.string.network_not_connected,
	 * Toast.LENGTH_SHORT).show(); break; case TYPE_XML: Toast.makeText(ctx,
	 * R.string.xml_parser_failed, Toast.LENGTH_SHORT).show(); break; case
	 * TYPE_IO: Toast.makeText(ctx, R.string.io_exception_error,
	 * Toast.LENGTH_SHORT).show(); break; case TYPE_RUN: Toast.makeText(ctx,
	 * R.string.app_run_code_error, Toast.LENGTH_SHORT).show(); case TYPE_JSON:
	 * Toast.makeText(ctx, R.string.json_parser_failed,
	 * Toast.LENGTH_SHORT).show();
	 * 
	 * break; } }
	 */

	/**
	 * 保存异常日志
	 * 
	 * @param excp
	 */
	public void saveErrorLog(Exception excp) {
		String errorlog = "errorlog.txt";
		String savePath = "";
		String logFilePath = "";
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			// 判断是否挂载了SD卡
			String storageState = Environment.getExternalStorageState();
			if (storageState.equals(Environment.MEDIA_MOUNTED)) {
				savePath = ErrorLOG_FILE_PATH;
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				logFilePath = savePath + errorlog;
			}
			// 没有挂载SD卡，无法写文件
			if (logFilePath == "") {
				return;
			}
			File logFile = new File(logFilePath);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			fw = new FileWriter(logFile, true);
			pw = new PrintWriter(fw);
			pw.println("--------------------" + (new Date().toLocaleString())
					+ "---------------------");
			excp.printStackTrace(pw);
			pw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
				}
			}
		}

	}

	public static ExceptionManagers http(int code) {
		return new ExceptionManagers(TYPE_HTTP_CODE, code, null);
	}

	public static ExceptionManagers http(Exception e) {
		return new ExceptionManagers(TYPE_HTTP_ERROR, 0, e);
	}

	public static ExceptionManagers httpThread(Exception e) {
		return new ExceptionManagers(TYPE_HTTP_ERROR, 0, e);
	}

	public static ExceptionManagers socket(Exception e) {
		return new ExceptionManagers(TYPE_SOCKET, 0, e);
	}

	public static ExceptionManagers io(Exception e) {
		if (e instanceof UnknownHostException || e instanceof ConnectException) {
			return new ExceptionManagers(TYPE_NETWORK, 0, e);
		} else if (e instanceof IOException) {
			return new ExceptionManagers(TYPE_IO, 0, e);
		}
		return run(e);
	}

	public static ExceptionManagers xml(Exception e) {
		return new ExceptionManagers(TYPE_XML, 0, e);
	}

	public static ExceptionManagers json(Exception e) {
		return new ExceptionManagers(TYPE_JSON, 0, e);
	}

	public static ExceptionManagers network(Exception e) {
		if (e instanceof UnknownHostException || e instanceof ConnectException) {
			return new ExceptionManagers(TYPE_NETWORK, 0, e);
		} else if (e instanceof HttpException) {
			return http(e);
		} else if (e instanceof SocketException) {
			return socket(e);
		}
		return http(e);
	}

	public static ExceptionManagers run(Exception e) {
		return new ExceptionManagers(TYPE_RUN, 0, e);
	}

	/**
	 * 获取APP异常崩溃处理对象
	 * 
	 * @param context
	 * @return
	 */
	public static ExceptionManagers getAppExceptionHandler() {
		return new ExceptionManagers();
	}

}
