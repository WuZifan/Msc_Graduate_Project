package com.wzf.utils;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.view.WindowManager;

public class ImageUtils {

	/**
	 * 拿到图片的副本，以供修改
	 * 
	 * @return
	 */
	public static Bitmap copyOfImage(Bitmap bitmap) {
		// 1.拿到和原图一样分辨率,设置一样的白纸
		// 1.1 由于人脸识别要求图片宽度必须为偶数，这里判断一下
		Bitmap copyImage;
		if (bitmap.getWidth() % 2 == 0) {
			copyImage = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
		}else{
			copyImage = Bitmap.createBitmap(bitmap.getWidth()+1, bitmap.getHeight(), bitmap.getConfig());
		}
		// 2. 得到画笔
		Paint paint = new Paint();
		// 3. 得到画布,并把白纸添加进来
		Canvas canvas = new Canvas(copyImage);
		// 4.利用画布开始作画
		// 4.1 第一个参数表示画画时参考的对象
		// 4.2 第二个参数设置一些对称，平移，旋转等效果
		// 4.3 paint为画笔
		canvas.drawBitmap(bitmap, new Matrix(), paint);
		return copyImage;
	}

	/**
	 * 将图片进行缩放，使其能够匹配手机屏幕大小
	 * 
	 * @param uri
	 * @param activity
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "unused" })
	public static Bitmap scaleToAndroidImage(Uri uri, Activity activity) {
		// uri通过getPath拿到的路径不是绝对路径，无法读取
		InputStream iStream;
		try {
			// -2. 通过activity调用内容解析者，通过uri拿到输入流
			iStream = activity.getContentResolver().openInputStream(uri);
			// -1. 将InputStream强转为FileInputStream
			FileInputStream fileInputStream = (FileInputStream) iStream;
			// 0. 利用FileInputStream调用getFD方法，得到FileDescriptor方法。
			FileDescriptor fileDescriptor = fileInputStream.getFD();
			// 1. 创建options对象
			Options opts = new Options();
			// 2. 设置opts属性，进行伪加载
			opts.inJustDecodeBounds = true;
			// 3. 调用bitmapfactory,加载FileDescriptor方法得到bitmap对象
			Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, opts);
			// 4. 得到图片的分辨率
			int image_width = opts.outWidth;
			int image_height = opts.outHeight;
			// 5. 计算缩放比例
			int scale = scaleCalu(activity, image_width, image_height);
			// 7. 设置缩放比例
			opts.inSampleSize = scale;
			// 8. 设置为真加载
			opts.inJustDecodeBounds = false;
			// bitmap = BitmapFactory.decodeStream(iStream, null, opts);
			bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, opts);
			iStream.close();
			return bitmap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将图片进行缩放，使其能够匹配手机自定义大小
	 * 
	 * @param uri
	 * @param activity
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "unused" })
	public static Bitmap scaleToAndroidImage(Uri uri, Activity activity, int diy_width, int diy_height) {
		// uri通过getPath拿到的路径不是绝对路径，无法读取
		InputStream iStream;
		try {
			// -2. 通过activity调用内容解析者，通过uri拿到输入流
			iStream = activity.getContentResolver().openInputStream(uri);
			// -1. 将InputStream强转为FileInputStream
			FileInputStream fileInputStream = (FileInputStream) iStream;
			// 0. 利用FileInputStream调用getFD方法，得到FileDescriptor方法。
			FileDescriptor fileDescriptor = fileInputStream.getFD();
			// 1. 创建options对象
			Options opts = new Options();
			// 2. 设置opts属性，进行伪加载
			opts.inJustDecodeBounds = true;
			// 3. 调用bitmapfactory,加载FileDescriptor方法得到bitmap对象
			Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, opts);
			// 4. 得到图片的分辨率
			int image_width = opts.outWidth;
			int image_height = opts.outHeight;
			// 5. 计算缩放比例
			int scale = scale(diy_width, diy_height, image_width, image_height);
			// 7. 设置缩放比例
			opts.inSampleSize = scale;
			// 8. 设置为真加载
			opts.inJustDecodeBounds = false;
			// bitmap = BitmapFactory.decodeStream(iStream, null, opts);
			bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, opts);
			iStream.close();
			return bitmap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 计算图片的缩放大小，默认以手机屏幕为标准缩放
	 * 
	 * @param activity
	 * @param image_width
	 * @param image_height
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static int scaleCalu(Activity activity, int image_width, int image_height) {
		// 5.1 得到windowManager对象
		WindowManager wManager = activity.getWindowManager();
		int phone_width = wManager.getDefaultDisplay().getWidth();
		int phone_height = wManager.getDefaultDisplay().getHeight();
		// 6. 计算缩放比例,并返回
		return scale(phone_width, phone_height, image_width, image_height);
	}

	/**
	 * 通过四个参数计算缩放比例
	 * 
	 * @param activity
	 * @param org_width
	 * @param org_height
	 * @param image_width
	 * @param image_height
	 * @return
	 */
	private static int scale(int org_width, int org_height, int image_width, int image_height) {
		// 6. 计算缩放比例
		int scale = 1;
		int scale_width = image_width / org_width;
		int scale_height = image_height / org_height;
		if (scale_width >= scale_height && scale_width > 1) {
			scale = scale_width;
		} else if (scale_height > scale_width && scale_height > 1) {
			scale = scale_height;
		}
		return scale;
	}
}
