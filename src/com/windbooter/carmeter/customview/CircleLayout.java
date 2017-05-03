package com.windbooter.carmeter.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.windbooter.carmeter.R;
import com.windbooter.carmeter.utils.LogUtils;

public class CircleLayout extends ViewGroup {

	private static final String TAG = "CircleLayout";
	private final int DEFAULT_START_DEGREE = 0;
	private final int DEFAULT_END_DEGREE = 360;
	private final Type default_type = Type.clockwise;

	private float startDegree = DEFAULT_START_DEGREE;
	private float endDegree = DEFAULT_END_DEGREE;
	private Type type;
	private int childCount;
	private float deltaDegree;
	private int radius;

	private enum Type {
		clockwise, anti_clockwise
	}

	public CircleLayout(Context context) {
		this(context, null);
	}

	public CircleLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		obtainStyledAttributes(attrs);
	}

	private void obtainStyledAttributes(AttributeSet attrs) {
		TypedArray attributes = getContext().obtainStyledAttributes(attrs,
				R.styleable.CircleLayout);
		startDegree = (float) (attributes.getFloat(
				R.styleable.CircleLayout_start_degree, DEFAULT_START_DEGREE) / 180 * Math.PI);
		endDegree = (float) (attributes.getFloat(
				R.styleable.CircleLayout_end_degree, DEFAULT_END_DEGREE) / 180 * Math.PI);
		int direction = attributes.getInt(R.styleable.CircleLayout_layout_type,
				0);
		switch (direction) {
		case 0:
			type = Type.clockwise;
			break;
		case 1:
			type = Type.anti_clockwise;
			break;

		}
		attributes.recycle();

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		childCount = getChildCount();
		if (childCount == 0) {
			return;
		}
		deltaDegree = (endDegree - startDegree) / (childCount - 1);
		radius = Math.min(getWidth(), getHeight()) / 2 - 50;
		ImageView child = null;
		int left = 0;
		int top = 0;

		switch (type) {
		case clockwise:
			for (int i = 0; i < childCount; i++) {
				child = (ImageView) getChildAt(i);
				left = (int) (getWidth() / 2 + radius
						* Math.cos(startDegree + deltaDegree * i)) - 20;
				top = (int) (getHeight() / 2 + radius
						* Math.sin(startDegree + deltaDegree * i)) - 20;
				LogUtils.e(TAG, "width:" + child.getWidth() + "  height:"
						+ child.getHeight());
				child.layout(left, top, left + 40, top + 40);
			}
			break;
		case anti_clockwise:
			for (int i = 0; i < childCount; i++) {
				child = (ImageView) getChildAt(i);
				left = (int) (getWidth() / 2 + radius
						* Math.cos(endDegree - deltaDegree * i)) - 20;
				top = (int) (getHeight() / 2 + radius
						* Math.sin(endDegree - deltaDegree * i)) - 20;
				LogUtils.e(TAG, "width:" + child.getWidth() + "  height:"
						+ child.getHeight());
				child.layout(left, top, left + 40, top + 40);
			}
			break;
		}

	}
}
