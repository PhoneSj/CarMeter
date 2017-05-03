package com.windbooter.carmeter.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.windbooter.carmeter.R;
import com.windbooter.carmeter.utils.LogUtils;

public class CustomView extends View {

	private static final String TAG = "CustomView";
	private final int DEFAULT_ITEM_SUM = 15;// 默认item总数量
	private final int DEFAULT_ITEM_WIDTH = 15;// 默认item宽度
	private final int DEFAULT_ITEM_HEIGHT = 2;// 默认item高度
	private final int DEFAULT_ITEM_SELECTED_COLOR = 0xff02FCFF;// 默认选中的颜色
	private final int DEFAULT_ITEM_UNSELECTED_COLOR = 0xffF8F8F8;// 默认未选中的颜色
	private final Type DEFAULT_TYEP = Type.UpLeft;// 默认方向(左上、右上、左下、右下)

	private int mItemSum = DEFAULT_ITEM_SUM;
	private int mItemWidth = dp2px(DEFAULT_ITEM_WIDTH);
	private int mItemHeight = dp2px(DEFAULT_ITEM_HEIGHT);

	private int mItemSelectedColor = DEFAULT_ITEM_SELECTED_COLOR;
	private int mItemUnselectedColor = DEFAULT_ITEM_UNSELECTED_COLOR;

	private double mOffsetAngle = Math.PI / 360 * 10;// 起始偏移角度

	private int mLightSum = 1;// 点亮的条目总数

	private String mText;// 显示的文本内容
	private int mDrawable;// 显示的图标

	private int width;
	private int height;
	private int radius;
	double dDegree = Math.PI / 60;
	private Paint mPaint;
	private Paint mTextPaint;
	private Paint mIconPaint;

	int isClockwise = 1;// 顺时针为1，逆时针为-1
	int isLeft = 1;// 左侧为0，右侧为-1
	int isUp = 1;// 上册为0，下册为-1

	private enum Type {
		UpLeft, UpRight, DownLeft, DownRight
	}

	private Type mType = DEFAULT_TYEP;

	public CustomView(Context context) {
		this(context, null);
	}

	public CustomView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		obtainStyledAttributes(attrs);
		init();
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setStyle(Style.FILL);

		mTextPaint = new Paint();
		mTextPaint.setColor(0xffffffff);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(20);

		mIconPaint = new Paint();

	}

	private void obtainStyledAttributes(AttributeSet attrs) {
		TypedArray attributes = getContext().obtainStyledAttributes(attrs,
				R.styleable.CustomView);
		mItemSum = attributes.getInt(
				R.styleable.CustomView_customview_item_sum, DEFAULT_ITEM_SUM);
		mItemWidth = (int) attributes.getDimension(
				R.styleable.CustomView_customview_item_width,
				DEFAULT_ITEM_WIDTH);
		mItemHeight = (int) attributes.getDimension(
				R.styleable.CustomView_customview_item_height,
				DEFAULT_ITEM_HEIGHT);
		mItemSelectedColor = attributes.getColor(
				R.styleable.CustomView_customview_item_selected_color,
				DEFAULT_ITEM_SELECTED_COLOR);
		mItemUnselectedColor = attributes.getColor(
				R.styleable.CustomView_customview_item_unselected_color,
				DEFAULT_ITEM_UNSELECTED_COLOR);
		mText = attributes.getString(R.styleable.CustomView_customview_text);
		mDrawable = attributes.getResourceId(
				R.styleable.CustomView_customview_drawable,
				R.drawable.ic_launcher);
		int type = attributes.getInt(R.styleable.CustomView_customview_type, 0);
		attributes.recycle();
		LogUtils.i(TAG, "type:" + type);
		switch (type) {
		case 0:
			mType = Type.UpLeft;
			break;
		case 1:
			mType = Type.UpRight;
			break;
		case 2:
			mType = Type.DownLeft;
			break;
		case 3:
			mType = Type.DownRight;
			break;
		}
		LogUtils.i(TAG, "mType:" + mType);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		width = getWidth();
		height = getHeight();
		radius = (int) (Math.min(width, height) / 2 * 0.8);

		canvas.drawCircle(width / 2, height / 2, 10, mPaint);

		double initDegree = 0;

		switch (mType) {
		case UpLeft:
			initDegree = Math.PI + mOffsetAngle;
			isClockwise = 1;
			isLeft = 0;
			isUp = 0;
			break;
		case UpRight:
			initDegree = 0 - mOffsetAngle;
			isClockwise = -1;
			isLeft = -1;
			isUp = 0;
			break;
		case DownLeft:
			initDegree = Math.PI - mOffsetAngle;
			isClockwise = -1;
			isLeft = 0;
			isUp = -1;
			break;
		case DownRight:
			initDegree = 0 + mOffsetAngle;
			isClockwise = 1;
			isLeft = -1;
			isUp = -1;
			break;
		}
		drawItems(initDegree, canvas);
		drawText(initDegree, isClockwise, isLeft, isUp, canvas);
		drawIcon(initDegree, canvas);

	}

	/**
	 * 绘制文本
	 * 
	 * @param initDegree
	 * @param isClockwise
	 * @param canvas
	 */
	private void drawText(double initDegree, int isClockwise, int isLeft,
			int isBottom, Canvas canvas) {
		int symbol = isUp == 0 ? -1 : 1;
		int x = (int) (width / 2 + radius
				* Math.cos(initDegree + dDegree * mItemSum * isClockwise));
		int y = (int) (width / 2 + radius
				* Math.sin(initDegree + dDegree * mItemSum * isClockwise) + 10 * symbol);
		if (mText == null) {
			mText = "未设置";
		}
		canvas.drawText(mText, x - 10, y + 10, mTextPaint);
	}

	/**
	 * 绘制图标
	 * 
	 * @param initDegree
	 * @param isClockwise
	 * @param canvas
	 */
	private void drawIcon(double initDegree, Canvas canvas) {
		int symbol = isUp == 0 ? -1 : 1;
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mDrawable);
		if (bitmap == null) {
			return;
		}
		int drawable_width = bitmap.getWidth();
		int drawable_height = bitmap.getHeight();
		int iconLeft = (int) (width
				/ 2
				+ radius
				* Math.cos(initDegree + (dDegree * mItemSum + Math.PI / 18)
						* isClockwise) + drawable_width * isLeft);
		int iconTop = (int) (height
				/ 2
				+ radius
				* Math.sin(initDegree + (dDegree * mItemSum + Math.PI / 18)
						* isClockwise) + drawable_height * isUp + drawable_height
				* symbol);

		canvas.drawBitmap(bitmap, iconLeft, iconTop, mIconPaint);
	}

	/**
	 * 绘制显示“横条”
	 * 
	 * @param initDegree
	 * @param isClockwise
	 * @param canvas
	 */
	private void drawItems(double initDegree, Canvas canvas) {

		for (int i = 0; i < mItemSum; i++) {
			int left = (int) (width / 2 + radius
					* Math.cos(initDegree + dDegree * i * isClockwise) + mItemWidth
					* isLeft);
			int top = (int) (height / 2 + radius
					* Math.sin(initDegree + dDegree * i * isClockwise) + mItemHeight
					* isUp);

			RectF rect = new RectF(left, top, left + mItemWidth, top
					+ mItemHeight);
			if (i < mLightSum) {
				mPaint.setColor(mItemSelectedColor);
			} else {
				mPaint.setColor(mItemUnselectedColor);
			}
			canvas.drawRect(rect, mPaint);
		}
	}

	public int getmLightSum() {
		return mLightSum;
	}

	public void setmLightSum(int mLightSum) {
		if (mLightSum > 0 && mLightSum < mItemSum) {
			this.mLightSum = mLightSum;
			invalidate();
		}
	}

	public int getmItemSum() {
		return mItemSum;
	}

	public void setmItemSum(int mItemSum) {
		if (mItemSum != 0) {
			this.mItemSum = mItemSum;
			invalidate();
		}

	}

	public String getmText() {
		return mText;
	}

	public void setmText(String mText) {
		this.mText = mText;
	}

	/**
	 * dp 2 px
	 * 
	 * @param dpVal
	 */
	protected int dp2px(int dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, getResources().getDisplayMetrics());
	}

	/**
	 * sp 2 px
	 * 
	 * @param spVal
	 * @return
	 */
	protected int sp2px(int spVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spVal, getResources().getDisplayMetrics());

	}

}
