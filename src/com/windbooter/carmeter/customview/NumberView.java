package com.windbooter.carmeter.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.widget.TextView;

public class NumberView extends TextView {

	private int width;
	private int height;

	private Paint mPaint;

	private int score = 0;

	public NumberView(Context context) {
		this(context, null);
	}

	public NumberView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public NumberView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
		this.setText(String.valueOf(score));
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setColor(0xaa666666);
		mPaint.setAntiAlias(true);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.save();

		width = getWidth();
		height = getHeight();
		drawSqure(canvas);
		canvas.restore();
	}

	private void drawSqure(Canvas canvas) {
		int colors[] = { Color.BLACK, Color.GRAY, Color.BLACK };
		float positions[] = { 0, 0.3f, 1.0f };
		LinearGradient mLinearGradient = new LinearGradient(0, 0, 0, height,
				colors, positions, TileMode.CLAMP);
		mPaint.setShader(mLinearGradient);
		Rect r = new Rect(0, 0, width, height);
		canvas.drawRect(r, mPaint);
	}
}
