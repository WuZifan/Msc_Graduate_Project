package com.wzf.graduate_project;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.wzf.utils.InfoUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

public class FacesDisplayView extends View {

	private Bitmap mBitmap;
	// 这个应该用的是com.google.android.gms.vision.face.Face这个包下的face
	private SparseArray<Face> mFaces;

	public FacesDisplayView(Context context) {
		super(context);
	}

	public FacesDisplayView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FacesDisplayView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/**
	 * 将本地mBitmap赋值
	 * 
	 * @param bitmap
	 */
	public void setBitmap(Bitmap bitmap) {
		// 1. 给本地图片文件赋值
		this.mBitmap = bitmap;
		// 2. 得到检测器(链式构造)
		FaceDetector faceDetector = new FaceDetector.Builder(getContext()).setTrackingEnabled(false)
				.setLandmarkType(FaceDetector.ALL_LANDMARKS).setMode(FaceDetector.FAST_MODE).build();
		// 3. 判断detector可用性
		if (!faceDetector.isOperational()) {
			InfoUtils.showInfo("检测不可用", getContext());
//			while(!faceDetector.isOperational()){
//				
//			}
//			InfoUtils.showInfo("检测可用", getContext());
		} else {

		}
		faceDetector.release();
	}

}
