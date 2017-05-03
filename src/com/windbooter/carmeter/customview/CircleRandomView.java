package com.windbooter.carmeter.customview;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;

public class CircleRandomView extends View {

	private final int DEFAULT_ITEM_DEGREE = 1;
	private final int DEFAULT_ITEM_LENGTH = 20;

	private int width;
	private int height;

	private int mItemDegree = DEFAULT_ITEM_DEGREE;
	private int mItemLength = DEFAULT_ITEM_LENGTH;

	private int mRadius;
	private Random mRandom;

	private Paint mPaint;
	private LinearGradient mLinearGradient;

	public CircleRandomView(Context context) {
		this(context, null);
	}

	public CircleRandomView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleRandomView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(0xffff0000);
		mPaint.setStrokeWidth(1);
		mPaint.setStyle(Style.STROKE);

		mRandom = new Random();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();

		width = getWidth();
		height = getHeight();

		mRadius = Math.min(width, height) / 2 - mItemLength;

		for (int i = 0; i < 360; i++) {
			int startX = (int) (width / 2 + mRadius * Math.cos(mItemDegree * i));
			int startY = (int) (height / 2 + mRadius
					* Math.sin(mItemDegree * i));
			int stopX = (int) (width / 2 + (mRadius + mRandom
					.nextInt(mItemLength)) * Math.cos(mItemDegree * i));
			int stopY = (int) (height / 2 + (mRadius + mRandom
					.nextInt(mItemLength)) * Math.sin(mItemDegree * i));
			mLinearGradient = new LinearGradient(startX, startY, stopX, stopY,
					Color.GREEN, Color.RED, TileMode.CLAMP);
			mPaint.setShader(mLinearGradient);
			canvas.drawLine(startX, startY, stopX, stopY, mPaint);
		}

		canvas.restore();

	}
}
