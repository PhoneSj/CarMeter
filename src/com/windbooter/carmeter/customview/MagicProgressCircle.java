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

public class MagicProgressCircle extends View {

	private static final String TAG = "MagicProgressCircle";
	private final int DEFAULT_START_COLOR = 0xffffffff;
	private final int DEFAULT_END_COLOR = 0xffff0000;
	private final int DEFAULT_EMPTY_COLOR = 0xff000000;
	private int default_stroke_width;

	private int width;
	private int height;

	private Paint mPaint;
	private Paint mStartPaint;
	private Paint mEndPaint;

	private int mStartColor = DEFAULT_START_COLOR;// 起始颜色
	private int mEndColor = DEFAULT_END_COLOR; // 满刻度时的终止颜色
	private int mEmptyColor = DEFAULT_EMPTY_COLOR;// 未到达的颜色
	private int mPercentColor;// 未满刻度的终止颜色

	private int mStrokeWidth = (int) (18 * getResources().getDisplayMetrics().density + 0.5f);

	private float percent =0.7f;
	private int startR;
	private int startG;
	private int startB;
	private int deltaR;
	private int deltaG;
	private int deltaB;

	private int mPercentColors[];
	private int mEndColors[];
	private int mEmptyColors[];

	public MagicProgressCircle(Context context) {
		this(context, null);
	}

	public MagicProgressCircle(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MagicProgressCircle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		obtainStyledAttributes(attrs);
		init();
	}

	public float getPercent() {
		return percent;
	}

	public void setPercent(float percent) {
		this.percent = percent;
		mPercentColor = calculatePercentColor();
		invalidate();
	}

	private void obtainStyledAttributes(AttributeSet attrs) {
		TypedArray attributes = getContext().obtainStyledAttributes(attrs,
				R.styleable.MagicProgressCircle);
		mStartColor = attributes.getColor(
				R.styleable.MagicProgressCircle_start_color,
				DEFAULT_START_COLOR);
		mEndColor = attributes.getColor(
				R.styleable.MagicProgressCircle_end_color, DEFAULT_END_COLOR);
		mEmptyColor = attributes.getColor(
				R.styleable.MagicProgressCircle_empty_color,
				DEFAULT_EMPTY_COLOR);
		mStrokeWidth = (int) attributes.getDimension(
				R.styleable.MagicProgressCircle_circle_width,
				default_stroke_width);
		attributes.recycle();
	}

	private void init() {

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(mStrokeWidth);

		mStartPaint = new Paint();
		mStartPaint.setColor(mStartColor);
		mStartPaint.setAntiAlias(true);
		mStartPaint.setStyle(Style.FILL);

		mEndPaint = new Paint();
		mEndPaint.setColor(mEndColor);
		mEndPaint.setAntiAlias(true);
		mEndPaint.setStyle(Style.FILL);

		// 分离RGB三原色
		int endR = (mEndColor & 0xff0000) >> 16;
		int endG = (mEndColor & 0x00ff00) >> 8;
		int endB = (mEndColor & 0x0000ff);

		startR = (mStartColor & 0xff0000) >> 16;
		startG = (mStartColor & 0x00ff00) >> 8;
		startB = (mStartColor & 0x0000ff);
		// 三原色的可变范围
		deltaR = endR - startR;
		deltaG = endG - startG;
		deltaB = endB - startB;
		// 计算进度颜色
		mPercentColor = calculatePercentColor();

		mPercentColors = new int[] { 0x00ffffff, mStartColor, mEndColor,
				mEmptyColor, mEmptyColor };
		mEndColors = new int[] { mStartColor, mEndColor };
		mEmptyColors = new int[] { mEmptyColor, mEmptyColor };

		LogUtils.e(TAG, Integer.toHexString(deltaR));
		LogUtils.e(TAG, Integer.toHexString(deltaG));
		LogUtils.e(TAG, Integer.toHexString(deltaB));
		LogUtils.e(TAG, Integer.toHexString(mPercentColor));

	}

	/**
	 * 计算进度条的进度值颜色
	 * 
	 * @param mPercent2
	 * @return
	 */
	private int calculatePercentColor() {
		int percentR = (int) (startR + deltaR * percent);
		int percentG = (int) (startG + deltaG * percent);
		int percentB = (int) (startB + deltaB * percent);
		return ((percentR << 16) + (percentG << 8) + (percentB)) + 0xff000000;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		width = getWidth();
		height = getHeight();

		int cx = width / 2;
		int cy = height / 2;
		final int radius = Math.min(width, height) / 2 - mStrokeWidth / 2;

		float drawPercent = percent;
		if (drawPercent > 0.97 && drawPercent < 1) {
			drawPercent = 0.97f;
		}

		canvas.save();
		canvas.rotate(90, cx, cy);
		int colors[];
		float positions[];
		if (drawPercent > 0 && drawPercent < 1) {
			colors = mPercentColors;
			positions = new float[] { 0f, drawPercent * 0.5f, drawPercent,
					drawPercent, 1f };
		} else if (drawPercent == 1) {
			colors = mEndColors;
			positions = new float[] { 0f, 1f };
		} else {
			colors = mEmptyColors;
			positions = new float[] { 0f, 1f };
		}
		SweepGradient mSweepGradient = new SweepGradient(cx, cy, colors,
				positions);
		mPaint.setShader(mSweepGradient);
		canvas.drawCircle(cx, cy, radius, mPaint);

		canvas.restore();
	}
}
