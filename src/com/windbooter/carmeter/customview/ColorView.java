package com.windbooter.carmeter.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.windbooter.carmeter.R;

public class ColorView extends View {

	private Paint myPaint = null;
	private Bitmap bitmap = null;
	private ColorMatrix myColorMatrix = null;

	private float red;
	private float green;
	private float blue;
	private float alpha;

	private float[] colorArray = { 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0,
			0, 0, 0, 1, 0 };

	public ColorView(Context context) {
		this(context, null);
	}

	public ColorView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ColorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		obtainStyledAttributes(context, attrs);
	}

	private void obtainStyledAttributes(Context context, AttributeSet attrs) {
		TypedArray attributes = getContext().obtainStyledAttributes(attrs,
				R.styleable.ColorView);
		int resId = attributes.getResourceId(R.styleable.ColorView_img,
				R.drawable.img03);
		bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
		attributes.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (bitmap == null) {
			return;
		}
		// 新建画笔对象
		myPaint = new Paint();
		// 描画（原始图片）
		canvas.drawBitmap(bitmap, getWidth() / 2 - bitmap.getWidth() / 2,
				getHeight() / 2 - bitmap.getHeight() / 2, myPaint);
		// 新建颜色矩阵对象
		myColorMatrix = new ColorMatrix();
		// 设置颜色矩阵的值
		myColorMatrix.set(colorArray);
		// 设置画笔颜色过滤器
		myPaint.setColorFilter(new ColorMatrixColorFilter(myColorMatrix));
		// 描画（处理后的图片）
		canvas.drawBitmap(bitmap, getWidth() / 2 - bitmap.getWidth() / 2,
				getHeight() / 2 - bitmap.getHeight() / 2, myPaint);

	}

	// 设置颜色数值
	public void setColorArray(float[] colorArray) {
		this.colorArray = colorArray;
	}

	// 设置图片
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public float getRed() {
		return red;
	}

	public void setRed(float red) {
		this.red = red;
		colorArray[0] = red;
		invalidate();
	}

	public float getGreen() {
		return green;
	}

	public void setGreen(float green) {
		this.green = green;
		colorArray[6] = green;
		invalidate();
	}

	public float getBlue() {
		return blue;
	}

	public void setBlue(float blue) {
		this.blue = blue;
		colorArray[12] = blue;
		invalidate();
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
		colorArray[18] = alpha;
		invalidate();
	}

}
