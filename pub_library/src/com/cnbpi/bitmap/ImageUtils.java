package com.cnbpi.bitmap;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.cnbpi.app.DefaultConfig;
import com.lidroid.xutils.util.LogUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
/** 
 * 图片操作工具包
 */
public class ImageUtils{
	
/*    public final static String SDCARD_MNT = "/mnt/sdcard";
    public final static String SDCARD = "/sdcard";
    
    /** 请求相册 */
//    public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
    /** 请求相机 */
    public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
    /** 请求裁剪 */
//    public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;
    /**
  	 * 从指定输入流解析图片
  	 * 
  	 * @param is
  	 * @return
  	 */
  	public static Bitmap getBitmap(InputStream is) {
  		return BitmapFactory.decodeStream(is);
  	}
  	/**
  	 * 从指定文件加载图片
  	 * 
  	 * @param path
  	 * @return
  	 */
  	public static Bitmap getBitmap(String path) {
  		return BitmapFactory.decodeFile(path);
  	}
  	/** 
     * 把Bitmap转Byte 
     * @Author HEH 
     * @EditTime 2010-07-19 上午11:45:56 
     */  
    public static byte[] Bitmap2Bytes(Bitmap bm){  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);  
        return baos.toByteArray();  
    }  
    /**
     * 按照指定的质量压缩
     * @param bm
     * @param quality
     * @return
     */
    public static byte[] Bitmap2Bytes(Bitmap bm,int quality){  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos);  
        return baos.toByteArray();  
    }  
  	/**
	 * 从指定字节数组按指定收缩比例 加载图片
	 * 
	 * @param data
	 * @param scale
	 * @return
	 */
	public static Bitmap getBitmap(byte[] data, int scale) {
		Options opts = new Options();
		opts.inSampleSize = scale;
		return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
	}

	/**
	 * 从指定字节数组按指定宽高设置 保持纵横比收缩加载图片
	 * 
	 * @param data
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getBitmap(byte[] data, int width, int height) {
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		int x = opts.outWidth / width;
		int y = opts.outHeight / height;
		int scale = x > y ? x : y;
		return getBitmap(data, scale);
	}

	/**
	 * 保存图片到指定路径
	 * 
	 * @param bm
	 * @param savePath
	 * @throws IOException
	 */
	public static void save(Bitmap bm, File savePath) throws IOException {
		if (bm != null && savePath != null) {
			// 如果保存路径的 父目录不存在，则创建该目录
			if (!savePath.getParentFile().exists()) {
				savePath.getParentFile().mkdirs();
			}
			// 如果该文件不存在，则创建该文件
			if (!savePath.exists()) {
				savePath.createNewFile();
				FileOutputStream out = new FileOutputStream(savePath);
				// 按指定格式 、质量、路径 保存图片
				bm.compress(CompressFormat.JPEG, 100, out);// 以文件流的形式写到文件里面。
			}
		}
	}
  	
	/**
	 * 写图片文件
	 * 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
	 * @throws IOException 
	 */
	public static void saveImage(Context context, String fileName, Bitmap bitmap) throws IOException 
	{ 
		saveImage(context, fileName, bitmap, 100);
	}
	public static void saveImage(Context context, String fileName, Bitmap bitmap, int quality) throws IOException 
	{ 
		if(bitmap==null || fileName==null || context==null)	return;		
		LogUtils.i(fileName);
		FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, quality, stream);
		byte[] bytes = stream.toByteArray();
		fos.write(bytes); 			
		fos.close();
	}
	/**
	 * 获取bitmap
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static Bitmap getBitmap(Context context,String fileName) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			fis = context.openFileInput(fileName);
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}finally{
			try {
				fis.close();
			} catch (Exception e) {}
		}
		return bitmap;
	}
	
	
	
	
	
	
	/**
	 * 写图片文件到SD卡 quality 表示要传入的质量100为完全的 90的话为90%
	 * @throws IOException 
	 */
	public static void saveImageToSD(String filePath, Bitmap bitmap, int quality) throws IOException
	{
		if(bitmap != null) {
			filePath = filePath==null?DefaultConfig.BITMAP_PATH:filePath;
			FileOutputStream fos = new FileOutputStream(filePath);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, quality, stream);
			byte[] bytes = stream.toByteArray();
			fos.write(bytes); 			
			fos.close();
		}
	}
    

	/**
	 * 获取bitmap，不要锁
	 * @param filePath
	 * @return
	 */
	public static Bitmap getBitmapByPath(String filePath) {
		return getBitmapByPath(filePath, null);
	}
	public static Bitmap getBitmapByPath(String filePath, BitmapFactory.Options opts) {
		FileInputStream fis = null;
		Bitmap bitmap =null; 
		try { 
			File file = new File(filePath);
			fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis,null,opts);
		} catch (FileNotFoundException e) {  
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally{
			try {
				fis.close();
			} catch (Exception e) {}
		}
		return bitmap;
	}
	/**
	 * 获取bitmap
	 * @param file
	 * @return
	 */
	public static Bitmap getBitmapByFile(File file) {
		FileInputStream fis = null;
		Bitmap bitmap =null; 
		try { 
			fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {  
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally{
			try {
				fis.close();
			} catch (Exception e) {}
		}
		return bitmap;
	}
	
	/**
	 * 使用当前时间戳拼接一个唯一的文件名
	 * @param format
	 * @return
	 */
    public static String getTempFileName() 
    {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
    	String fileName = format.format( new Timestamp( System.currentTimeMillis()) );
    	return fileName;
    }
	/*
	 * 把方形图片剪切成圆形的
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) { 
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), 
		bitmap.getHeight(), Config.ARGB_8888); 
		Canvas canvas = new Canvas(output); 

		final int color = 0xff424242; 
		final Paint paint = new Paint(); 
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()); 
		final RectF rectF = new RectF(rect); 
		final float roundPx = bitmap.getWidth() / 2; 
		paint.setAntiAlias(true); 
		canvas.drawARGB(0, 0, 0, 0); 
		paint.setColor(color); 
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint); 
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
		canvas.drawBitmap(bitmap, rect, rect, paint); 
		return output; 
		}  
    /**
     * 将bitmap转化为drawable
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
    	Drawable drawable = new BitmapDrawable(bitmap);
    	return drawable;
    }
}
