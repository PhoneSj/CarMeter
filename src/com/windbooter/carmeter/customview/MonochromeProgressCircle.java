package com.windbooter.carmeter.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.windbooter.carmeter.R;

public class MonochromeProgressCircle extends View {

	private final int default_start_angle = 0;
	private final int default_sweep_angle = 360;
	private final int default_circle_color = 0xffff0000;
	private final int default_background_color = 0xffffffff;
	private final int default_warning_color = 0xffff0000;
	private final int default_stroke_width = 20;
	private final int default_padding = 0;

	private int startAngle;
	private int sweepAngle;
	private Paint mPaint;
	private int circleColor;
	private int backgroundColor;
	private int warningColor;
	private int strokeWidth;
	private int padding;

	private float percent;

	public float getPercent() {
		return percent;
	}

	public void setPercent(float percent) {
		this.percent = percent;
		invalidate();

	}

	private enum Type {
		clockwise, anti_clockwise
	}

	private Type mType = Type.clockwise;

	public MonochromeProgressCircle(Context context) {
		this(context, null);
	}

	public MonochromeProgressCircle(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MonochromeProgressCircle(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

		obtainStyledAttributes(attrs);
		init();
	}

	private void obtainStyledAttributes(AttributeSet attrs) {
		TypedArray attributes = getContext().obtainStyledAttributes(attrs,
				R.styleable.MonochromeProgressCircle);
		startAngle = attributes.getInt(
				R.styleable.MonochromeProgressCircle_mpc_start_angle,
				default_start_angle);
		sweepAngle = attributes.getInt(
				R.styleable.MonochromeProgressCircle_mpc_sweep_angle,
				default_sweep_angle);
		circleColor = attributes.getColor(
				R.styleable.MonochromeProgressCircle_mpc_circle_color,
				default_circle_color);
		backgroundColor = attributes.getColor(
				R.styleable.MonochromeProgressCircle_mpc_background_color,
				default_background_color);
		warningColor = attributes.getColor(
				R.styleable.MonochromeProgressCircle_mpc_warning_color,
				default_warning_color);
		strokeWidth = (int) attributes.getDimension(
				R.styleable.MonochromeProgressCircle_mpc_stroke_width,
				default_stroke_width);
		padding = (int) attributes.getDimension(
				R.styleable.MonochromeProgressCircle_mpc_padding,
				default_padding);
		int typeValue = attributes.getInt(
				R.styleable.MonochromeProgressCircle_mpc_type, 0);
		switch (typeValue) {
		case 0:
			mType = Type.clockwise;
			break;
		case 1:
			mType = Type.anti_clockwise;
			break;
		}
		attributes.recycle();
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
		int raduis = Math.min(width, height) / 2 - padding;
		int temp = 1;
		switch (mType) {
		case clockwise:
			temp = 1;
			break;
		case anti_clockwise:
			temp = -1;
			break;
		}

		// 绘制底色
		mPaint.setColor(backgroundColor);
		mPaint.setStrokeWidth(strokeWidth);
		canvas.drawArc(new RectF(width / 2 - raduis + strokeWidth, height / 2
				- raduis + strokeWidth, width / 2 + raduis - strokeWidth,
				height / 2 + raduis - strokeWidth), startAngle, sweepAngle
				* temp, false, mPaint);
		mPaint.setColor(warningColor);
		// 绘制报警区域
		// mPaint.setStrokeWidth(strokeWidth);
		// canvas.drawArc(new RectF(width / 2 - raduis + strokeWidth, height / 2
		// - raduis + strokeWidth, width / 2 + raduis - strokeWidth,
		// height / 2 + raduis - strokeWidth), startAngle + sweepAngle
		// * 0.9f * temp, sweepAngle * 0.1f * temp, false, mPaint);
		// 绘制当前区域
		mPaint.setColor(circleColor);
		mPaint.setStrokeWidth(strokeWidth);
		canvas.drawArc(new RectF(width / 2 - raduis + strokeWidth, height / 2
				- raduis + strokeWidth, width / 2 + raduis - strokeWidth,
				height / 2 + raduis - strokeWidth), startAngle,
				(int) (sweepAngle * 1.0f * percent / 100) * temp, false, mPaint);

		canvas.restore();
	}
}
