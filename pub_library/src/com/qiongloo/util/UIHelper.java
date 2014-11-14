package com.qiongloo.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Process;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.qiongloo.publib.R;
import com.qiongloo.widget.LoadingDialog;

public class UIHelper {

	// 加载使用的dialog
	public static LoadingDialog loadingDialog;

	public static void dialog(final Activity atv, String paramString) {

		AlertDialog.Builder localBuilder = new AlertDialog.Builder(atv);
		localBuilder.setMessage(paramString);
		localBuilder.setTitle("提示");
		localBuilder.setCancelable(false);
		localBuilder.setPositiveButton("确认",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramDialogInterface,
							int paramInt) {
						paramDialogInterface.dismiss();
						atv.finish();
					}
				});
		localBuilder.create().show();
	}

	/**
	 * 提示错误信息
	 * 
	 * @param paramString
	 */
	public static void dialogError(final Activity atv, String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(atv);
		localBuilder.setMessage(paramString);
		localBuilder.setTitle("提示");
		localBuilder.setCancelable(false);
		localBuilder
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface paramDialogInterface) {
						Process.killProcess(Process.myPid());
						atv.finish();
					}

				});
		localBuilder.setPositiveButton("确认",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramDialogInterface,
							int paramInt) {
						paramDialogInterface.dismiss();
						atv.finish();
					}

				});
		localBuilder.create().show();
	}

	/**
	 * 提示错误信息（使用系统的资源）
	 * 
	 * @param paramString
	 */
	public static void dialogError(final Activity atv, int paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(atv);
		localBuilder.setMessage(paramString);
		localBuilder.setTitle("提示");
		localBuilder.setCancelable(false);
		localBuilder
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface paramDialogInterface) {
						Process.killProcess(Process.myPid());
						atv.finish();
					}

				});
		localBuilder.setPositiveButton("确认",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramDialogInterface,
							int paramInt) {
						paramDialogInterface.dismiss();
						atv.finish();
					}

				});
		localBuilder.create().show();
	}

	/**
	 * 弹出Toast消息
	 * 
	 * @param msg
	 */
	public static void Toast(Activity context, int content) {
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.TOP, 50, 50);
		android.widget.Toast.makeText(context, content, Toast.LENGTH_SHORT)
				.show();
	}

	public static void Toast(Context context, String content) {
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.TOP, 50, 50);
		android.widget.Toast.makeText(context, content, Toast.LENGTH_SHORT)
				.show();
	}

	public static void Toast(Context context, int content) {
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.TOP, 50, 50);
		android.widget.Toast.makeText(context, content, Toast.LENGTH_SHORT)
				.show();
	}

	public static void Toast(Activity context, String content) {
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.TOP, 50, 50);
		android.widget.Toast.makeText(context, content, Toast.LENGTH_SHORT)
				.show();
	}

	public static void show(Activity context, String content) {
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();
	}

	/**
	 * 开启加载图标
	 * 
	 * @param context
	 * @param paramString
	 */
	public static void showLoadingDialog(Context context, String paramString) {

		loadingDialog = new LoadingDialog(context, R.style.ql_loading_dialog,
				R.layout.ql_dialog_common_loading);
		
		//loadingDialog.setCanceledOnTouchOutside(false);
		//loadingDialog.setCancelable(false);
		loadingDialog.show();
		
		loadingDialog.setText(paramString);
		ImageView animationIV = (ImageView)(loadingDialog.findViewById(R.id.progress));
		animationIV.setImageResource(R.drawable.cnbpi_common_progress);
		AnimationDrawable  animationDrawable = (AnimationDrawable) animationIV.getBackground();
		
        animationDrawable.start();
       
	}

	/**
	 * 关闭加载的图标
	 */
	public static void closeLoadingDialog() {
		if (loadingDialog != null)
			loadingDialog.dismiss();
	}

	/**
	 * 按钮失效
	 * 
	 * @param context
	 * @param paramString
	 */
	public static void enabledButton(Activity context, int buttonId) {

		Button button = (Button) context.findViewById(buttonId);
		button.setEnabled(true);

	}

	/**
	 * 按钮恢复
	 * 
	 * @param context
	 * @param paramString
	 */
	public static void abledButton(Activity context, int buttonId) {

		Button button = (Button) context.findViewById(buttonId);
		button.setEnabled(false);

	}

	/**
	 * 展示图片——使用dialog的形式，再次点击消失
	 * 
	 * @param paramString
	 * @param mContext
	 */
	public static void dialogPhotoShow(final Context mContext, String imagUrl) {

		LayoutInflater inflater = LayoutInflater.from(mContext);
		View imgEntryView = inflater.inflate(R.layout.ql_dialog_photo_entry,
				null); // 加载自定义的布局文件

		// final AlertDialog dialog = new
		// AlertDialog.Builder(mContext).create();
		final Dialog dialog = new Dialog(mContext, R.style.ql_img_dialog);

		ImageView img = (ImageView) imgEntryView.findViewById(R.id.large_image);
		img.setImageBitmap(BitmapManager.getRotatePhoto(imagUrl));// 放置图片

		dialog.setContentView(imgEntryView); // 自定义dialog
		dialog.show();

		imgEntryView.findViewById(R.id.dialog_photo_show).setOnClickListener(
				new OnClickListener() {
					public void onClick(View paramView) {
						dialog.dismiss();
					}
				});

	}

}
