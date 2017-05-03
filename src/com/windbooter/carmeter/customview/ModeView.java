package com.windbooter.carmeter.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ModeView extends View {

	private Paint mPaint;
	private int circleColor = 0xffffff00;
	private int strokeWidth = 5;
	private int startAngle = 30;
	private int sweepAngle = -60;

	private int red;
	private int green;
	private int blue;

	public ModeView(Context context) {
		this(context, null);
	}

	public ModeView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ModeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public int getCircleColor() {
		return circleColor;
	}

	public void setCircleColor(int circleColor) {
		this.circleColor = circleColor;
		invalidate();
	}

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
		invalidate();
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
		invalidate();
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
		invalidate();
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setColor(circleColor);
		mPaint.setStyle(Style.STROKE);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(strokeWidth);
		mPaint.setStrokeCap(Cap.SQUARE);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.save();
		int width = getWidth();
		int height = getHeight();
		int raduis = Math.min(width, height) / 2;

		circleColor = 0xff000000 + (red << 16) + (green << 8) + blue;
		mPaint.setColor(circleColor);
		canvas.drawArc(new RectF(width / 2 - raduis + strokeWidth, height / 2
				- raduis + strokeWidth, width / 2 + raduis - strokeWidth,
				height / 2 + raduis - strokeWidth), startAngle, sweepAngle,
				false, mPaint);

		canvas.restore();

	}
}
