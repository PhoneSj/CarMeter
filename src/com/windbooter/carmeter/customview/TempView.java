package com.windbooter.carmeter.customview;

import com.windbooter.carmeter.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;

public class TempView extends View {

	private int DEFAULT_START_POINT_COLOR = 0xff4583be;
	private int DEFAULT_END_POINT_COLOR = 0xffff0000;
	private int DEFAULT_CIRCLE_COLOR = 0xff0000ff;
	private int DEFAULT_BACKGROUND_COLOR = 0xffaaaaaa;
	private int DEFAULT_BALL_STROKE_COLOR = 0xffffffff;
	private int DEFAULT_TEXT_COLOR = 0xffffffff;

	private int DEFAULT_CIRCLE_STROKE_WIDTH = 10;
	private int DEFAULT_BALL_STROKE_WIDTH = 3;
	private int DEFAULT_TEXT_SIZE = 20;

	private int DEFAULT_START_ANGLE = 0;
	private int DEFAULT_SWEEP_ANGLE = 180;
	private int DEFAULT_CURRENT_ANGLE = 0;

	private int DEFAULT_MAX_TEMPRATURE = 100;
	private int DEFAULT_MIN_TEMPRATURE = 0;

	private Paint mCirclePaint;
	private Paint mPointPaint;
	private Paint mBallPaint;
	private Paint mTextPaint;

	private int mPadding = 15;

	private int mStartPointColor = DEFAULT_START_POINT_COLOR;
	private int mEndPointColor = DEFAULT_END_POINT_COLOR;
	private int mCircleColor = DEFAULT_CIRCLE_COLOR;
	private int mBackgroundColor = DEFAULT_BACKGROUND_COLOR;
	private int mBallStrokeColor = DEFAULT_BALL_STROKE_COLOR;
	private int mTextColor = DEFAULT_TEXT_COLOR;

	private int mCircleStrokeWidth = DEFAULT_CIRCLE_STROKE_WIDTH;
	private int mBallStrokeWidth = DEFAULT_BALL_STROKE_WIDTH;
	private int mTextSize = DEFAULT_TEXT_SIZE;

	private int mStartAngle = DEFAULT_START_ANGLE;
	private int mSweepAngle = DEFAULT_SWEEP_ANGLE;
	private int mCurrentAngle = DEFAULT_CURRENT_ANGLE;

	private int tempMax = DEFAULT_MAX_TEMPRATURE;
	private int tempMin = DEFAULT_MIN_TEMPRATURE;
	private int tempCurrent = 0;

	private int width;
	private int height;
	private int mCircleRadius;

	public TempView(Context context) {
		this(context, null);
	}

	public TempView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TempView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		obtainStyledAttributes(attrs);
	}

	public int getTempCurrent() {
		return tempCurrent;
	}

	public void setTempCurrent(int tempCurrent) {
		this.tempCurrent = tempCurrent;
		invalidate();
	}

	private void obtainStyledAttributes(AttributeSet attrs) {
		TypedArray attributes = getContext().obtainStyledAttributes(attrs,
				R.styleable.TempView);
		mStartPointColor = attributes.getColor(
				R.styleable.TempView_temp_start_point_color,
				DEFAULT_START_POINT_COLOR);
		mEndPointColor = attributes.getColor(
				R.styleable.TempView_temp_end_point_color,
				DEFAULT_END_POINT_COLOR);
		mCircleColor = attributes.getColor(
				R.styleable.TempView_temp_circle_color, DEFAULT_CIRCLE_COLOR);
		mBackgroundColor = attributes.getColor(
				R.styleable.TempView_temp_background_color,
				DEFAULT_BACKGROUND_COLOR);
		mBallStrokeColor = attributes.getColor(
				R.styleable.TempView_temp_ball_stroke_color,
				DEFAULT_BALL_STROKE_COLOR);
		mTextColor = attributes.getColor(R.styleable.TempView_temp_text_color,
				DEFAULT_TEXT_COLOR);
		mCircleStrokeWidth = (int) attributes.getDimension(
				R.styleable.TempView_temp_circle_storke_width,
				DEFAULT_CIRCLE_STROKE_WIDTH);
		mBallStrokeWidth = (int) attributes.getDimension(
				R.styleable.TempView_temp_ball_storke_width,
				DEFAULT_BALL_STROKE_WIDTH);
		mTextSize = (int) attributes.getDimension(
				R.styleable.TempView_temp_text_size, DEFAULT_TEXT_SIZE);
		mStartAngle = attributes.getInt(R.styleable.TempView_temp_start_angle,
				DEFAULT_START_ANGLE);
		mSweepAngle = attributes.getInt(R.styleable.TempView_temp_sweep_angle,
				DEFAULT_SWEEP_ANGLE);
		tempMax = attributes.getInt(R.styleable.TempView_temp_max_temperature,
				DEFAULT_MAX_TEMPRATURE);
		tempMin = attributes.getInt(R.styleable.TempView_temp_min_temperature,
				DEFAULT_MIN_TEMPRATURE);
		attributes.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		initPaint();
		canvas.save();
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));
		width = getWidth();
		height = getHeight();
		mCircleRadius = Math.min(width, height) / 2 - mPadding;
		mCurrentAngle = (int) (mStartAngle + (tempCurrent - tempMin) * 1.0f
				/ (tempMax - tempMin) * mSweepAngle);
		drawCircle(canvas);
		drawPoint(canvas);
		drawBall(canvas);
		drawText(canvas);
		canvas.restore();
	}

	private void initPaint() {
		mCirclePaint = new Paint();
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setColor(mCircleColor);
		mCirclePaint.setStyle(Style.STROKE);
		mCirclePaint.setStrokeWidth(mCircleStrokeWidth);

		mPointPaint = new Paint();
		mPointPaint.setAntiAlias(true);
		mPointPaint.setStyle(Style.FILL);

		mBallPaint = new Paint();
		mBallPaint.setAntiAlias(true);
		mBallPaint.setColor(mBallStrokeColor);
		mBallPaint.setStrokeWidth(mBallStrokeWidth);
		mBallPaint.setStyle(Style.STROKE);

		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(Color.RED);
		mTextPaint.setTextSize(mTextSize);
		mTextPaint.setTextAlign(Align.CENTER);
	}

	private void drawText(Canvas canvas) {
		// 画文字
		float x = (float) (width / 2 + mCircleRadius
				* Math.cos(mCurrentAngle * 1.0f / 180 * Math.PI));
		float y = (float) (height / 2 + mCircleRadius
				* Math.sin(mCurrentAngle * 1.0f / 180 * Math.PI));
		canvas.drawText(String.valueOf(tempCurrent), x, y + mTextSize / 2 - 2,
				mTextPaint);

	}

	private void drawBall(Canvas canvas) {
		float x = (float) (width / 2 + mCircleRadius
				* Math.cos(mCurrentAngle * 1.0f / 180 * Math.PI));
		float y = (float) (height / 2 + mCircleRadius
				* Math.sin(mCurrentAngle * 1.0f / 180 * Math.PI));
		int ball_raduis = 20;
		// 画边界
		mBallPaint.setColor(mBallStrokeColor);
		mBallPaint.setStyle(Style.FILL);
		mBallPaint.setStrokeWidth(mBallStrokeWidth);
		canvas.drawCircle(x, y, ball_raduis, mBallPaint);
		// 画填充
		mBallPaint.setStyle(Style.FILL);
		// Shader mRadialGradient = new RadialGradient(x, y, ball_raduis,
		// Color.WHITE, Color.BLACK, TileMode.CLAMP);
		// mBallPaint.setShader(mRadialGradient);
		mBallPaint.setColor(Color.BLACK);
		canvas.drawCircle(x, y, ball_raduis - mBallStrokeWidth * 1.0f / 2,
				mBallPaint);
	}

	private void drawPoint(Canvas canvas) {
		int point_raduis = 5;
		// 画起点
		// mPointPaint.setColor(mStartPointColor);
		float x0 = (float) (width / 2 + mCircleRadius
				* Math.cos(mStartAngle * 1.0f / 180 * Math.PI));
		float y0 = (float) (height / 2 + mCircleRadius
				* Math.sin(mStartAngle * 1.0f / 180 * Math.PI));
		Shader mRadialGradient0 = new RadialGradient(x0, y0, point_raduis,
				Color.WHITE, Color.GREEN, TileMode.CLAMP);
		mPointPaint.setShader(mRadialGradient0);
		canvas.drawCircle(x0, y0, point_raduis, mPointPaint);
		// 画终点
		// mPointPaint.setColor(mEndPointColor);
		float x1 = (float) (width / 2 + mCircleRadius
				* Math.cos((mStartAngle + mSweepAngle) * 1.0f / 180 * Math.PI));
		float y1 = (float) (height / 2 + mCircleRadius
				* Math.sin((mStartAngle + mSweepAngle) * 1.0f / 180 * Math.PI));
		Shader mRadialGradient1 = new RadialGradient(x1, y1, point_raduis,
				Color.WHITE, Color.RED, TileMode.CLAMP);
		mPointPaint.setShader(mRadialGradient1);
		canvas.drawCircle(x1, y1, point_raduis, mPointPaint);

	}

	private void drawCircle(Canvas canvas) {
		RectF oval = new RectF(width / 2 - mCircleRadius, height / 2
				- mCircleRadius, width / 2 + mCircleRadius, height / 2
				+ mCircleRadius);
		mCirclePaint.setColor(mBackgroundColor);
		canvas.drawArc(oval, mStartAngle, mSweepAngle, false, mCirclePaint);
		mCirclePaint.setColor(mCircleColor);
		canvas.drawArc(oval, mStartAngle, mCurrentAngle - mStartAngle, false,
				mCirclePaint);
	}
}
