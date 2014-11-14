package com.qiongloo.util;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.qiongloo.exception.ExceptionManagers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/**
 * 异步线程加载图片工具类 使用说明： BitmapManager bmpManager; bmpManager = new
 * BitmapManager(BitmapFactory.decodeResource(context.getResources(),
 * R.drawable.loading)); bmpManager.loadBitmap(imageURL, imageView);
 */
public class BitmapManager {
	private static HashMap<String, SoftReference<Bitmap>> cache;
	private static ExecutorService pool;
	private static Map<ImageView, String> imageViews;
	private Bitmap defaultBmp;
//	private int defaultId;
	static {
		cache = new HashMap<String, SoftReference<Bitmap>>();
		pool = Executors.newFixedThreadPool(5); // 固定线程池
		imageViews = Collections
				.synchronizedMap(new WeakHashMap<ImageView, String>());
	}

	public BitmapManager() {
	}

	public BitmapManager(Bitmap defaultBmp) {
		this.defaultBmp=defaultBmp;
	}

	/**
	 * 设置默认图片
	 * 
	 * @param bmp
	 */
	public void setDefaultBmp(Bitmap defaultBmp) {
this.defaultBmp=defaultBmp;
	}

	/**
	 * 加载图片
	 * 
	 * @param url
	 * @param imageView
	 */
	public void loadBitmap(String url, ImageView imageView) {
		loadBitmap(url, imageView, defaultBmp, 0, 0);
	}

	/**
	 * 加载图片-可设置加载失败后显示的默认图片
	 * 
	 * @param url
	 * @param imageView
	 * @param defaultBmp
	 */
	public void loadBitmap(String url, ImageView imageView, Bitmap defaultBmp) {
		loadBitmap(url, imageView, defaultBmp, 0, 0);
	}

	/**
	 * 加载图片-可指定显示图片的高宽
	 * 
	 * @param url
	 * @param imageView
	 * @param width
	 * @param height
	 */
	public void loadBitmap(String url, ImageView imageView, Bitmap defaultBmp,
			int width, int height) {
		imageViews.put(imageView, url);
		Bitmap bitmap = getBitmapFromCache(url);

		if (bitmap != null) {
			// 显示缓存图片
			imageView.setImageBitmap(bitmap);
		} else {
			// 加载SD卡中的图片缓存
			String filename = FileUtils.getFileName(url);
			String filepath = imageView.getContext().getFilesDir()
					+ File.separator + filename;

			File file = new File(filepath);
			if (file.exists()) {
				// 显示SD卡中的图片缓存
				Bitmap bmp = ImageUtils.getBitmap(imageView.getContext(),
						filename);
				LogUtils.d("找到图片！" + filepath);
				imageView.setImageBitmap(bmp);
			} else {
				// 线程加载网络图片
				imageView.setImageBitmap(defaultBmp);
				queueJob(url, imageView, width, height);
			}
		}
	}

	/**
	 * 从缓存中获取图片
	 * 
	 * @param url
	 */
	public Bitmap getBitmapFromCache(String url) {
		Bitmap bitmap = null;
		if (cache.containsKey(url)) {
			bitmap = cache.get(url).get();
		}
		return bitmap;
	}

	/**
	 * 从网络中加载图片
	 * 
	 * @param url
	 * @param imageView
	 * @param width
	 * @param height
	 */
	public void queueJob(final String url, final ImageView imageView,
			final int width, final int height) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				String tag = imageViews.get(imageView);
				if (tag != null && tag.equals(url)) {
					if (msg.obj != null) {
						imageView.setImageBitmap((Bitmap) msg.obj);
						try {
							// 向SD卡中写入图片缓存
							ImageUtils.saveImage(imageView.getContext(),
									FileUtils.getFileName(url),
									(Bitmap) msg.obj);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		pool.execute(new Runnable() {
			public void run() {
				Message message = Message.obtain();
				message.obj = downloadBitmap(url, width, height);
				handler.sendMessage(message);
			}
		});
	}

	/**
	 * 下载图片-可指定显示图片的高宽
	 * 
	 * @param url
	 * @param width
	 * @param height
	 */
	private Bitmap downloadBitmap(String url, int width, int height) {
		Bitmap bitmap = null;
		try {
			// http加载图片
			bitmap = HttpUtils.getNetBitmap(url);
			if (width > 0 && height > 0) {
				// 指定显示图片的高宽
				bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
			}
			// 放入缓存
			cache.put(url, new SoftReference<Bitmap>(bitmap));
		} catch (ExceptionManagers e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 旋转图片指定角度
	 * 
	 * @param paramInt
	 * @param paramBitmap
	 * @return
	 */
	public static Bitmap rotaingImageView(int paramInt, Bitmap paramBitmap) {
		Matrix localMatrix = new Matrix();
		int i = paramBitmap.getWidth();
		int j = paramBitmap.getHeight();
		localMatrix.setScale(240.0F / i, 320.0F / j);
		localMatrix.postRotate(paramInt);
		return Bitmap.createBitmap(paramBitmap, 0, 0, i, j, localMatrix, true);
	}

	/**
	 * 获取图片旋转的角度
	 * 
	 * @param paramString
	 * @return
	 */
	public static int readPictureDegree(String paramString) {
		try {
			int i = new ExifInterface(paramString).getAttributeInt(
					"Orientation", ExifInterface.ORIENTATION_NORMAL);
			switch (i) {
			case ExifInterface.ORIENTATION_FLIP_VERTICAL:
			case ExifInterface.ORIENTATION_TRANSPOSE:
			case ExifInterface.ORIENTATION_TRANSVERSE:
			default:
				return 0;
			case ExifInterface.ORIENTATION_ROTATE_90:
				return 90;
			case ExifInterface.ORIENTATION_ROTATE_180:
				return 180;
			case ExifInterface.ORIENTATION_ROTATE_270:
			}
			return 270;
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
		return 0;
	}
	/**
	 * 获取图片，正确的角度
	 * 
	 * @param paramString
	 * @return
	 */
	public static Bitmap getRotatePhoto(String paramString) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 2;
		return rotaingImageView(readPictureDegree(paramString),
				BitmapFactory.decodeFile(paramString, opts));
	}

}