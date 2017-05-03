package com.windbooter.carmeter.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.windbooter.carmeter.R;
import com.windbooter.carmeter.utils.LogUtils;

public class ShaderCircle extends View {

	private final int default_start_angle = 0;
	private final int default_sweep_angle = 360;
	private final int default_start_color = 0xff00ffff;
	private final int default_end_color = 0xffffff00;
	private final int default_stroke_width = 10;
	private final int default_warning_color = 0xffff0000;
	private final int default_reverseColor = 0xff00ff00;

	private int startAngle;
	private int sweepAngle;
	private int startColor;
	private int endColor;
	private int warningColor;
	private int reverseColor;
	private int strokeWidth;

	private Paint mPaint;

	private float percent;

	public ShaderCircle(Context context) {
		this(context, null);
	}

	public ShaderCircle(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ShaderCircle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		obtainStyledAttributes(attrs);
	}

	public float getPercent() {
		return percent;
	}

	public void setPercent(float percent) {
		this.percent = percent;
		invalidate();
	}

	private void obtainStyledAttributes(AttributeSet attrs) {
		TypedArray attributes = getContext().obtainStyledAttributes(attrs,
				R.styleable.ShaderCircle);
		startAngle = attributes.getInt(R.styleable.ShaderCircle_sc_start_angle,
				default_start_angle);
		sweepAngle = attributes.getInt(R.styleable.ShaderCircle_sc_sweep_angle,
				default_sweep_angle);
		startColor = attributes.getColor(
				R.styleable.ShaderCircle_sc_start_color, default_start_color);
		endColor = attributes.getColor(R.styleable.ShaderCircle_sc_end_color,
				default_end_color);
		warningColor = attributes.getColor(
				R.styleable.ShaderCircle_sc_warning_color,
				default_warning_color);
		reverseColor = attributes
				.getColor(R.styleable.ShaderCircle_sc_reverse_color,
						default_reverseColor);
		strokeWidth = (int) attributes.getDimension(
				R.styleable.ShaderCircle_sc_stroke_width, default_stroke_width);
		attributes.recycle();

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.save();

		int width = getWidth();
		int height = getHeight();
		int radius = Math.min(width, height) / 2;

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(strokeWidth);
		float cx = width / 2;
		float cy = height / 2;

		canvas.rotate(180, cx, cy);

		int colors[];
		float positions[];
		colors = new int[] { startColor, endColor };
		positions = new float[] { 0.5f, 1.0f };
		if (percent > 0 && percent <= 1.0f) {
			colors = new int[] { startColor, endColor };
			positions = new float[] { 0.5f, 1.0f };
		} else if (percent > 1.0f) {
			colors = new int[] { warningColor, warningColor };
			positions = new float[] { 0.5f, 1.0f };
		} else {
			colors = new int[] { reverseColor, reverseColor };
			positions = new float[] { 0.5f, 1.0f };
		}
		LogUtils.i("test", "percent:" + percent);
		SweepGradient mSweepGradient = new SweepGradient(cx, cy, colors,
				positions);
		mPaint.setShader(mSweepGradient);
		RectF oval = new RectF(cx - radius + strokeWidth / 2, cy - radius
				+ strokeWidth / 2, cx + radius - strokeWidth / 2, cy + radius
				- strokeWidth / 2);
		canvas.drawArc(oval, startAngle, sweepAngle * percent, false, mPaint);

		canvas.restore();

	}
}
