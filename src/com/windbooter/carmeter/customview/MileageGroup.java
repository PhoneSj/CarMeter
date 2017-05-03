package com.windbooter.carmeter.customview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

public class MileageGroup extends LinearLayout {

	private int mileage = 0;

	public MileageGroup(Context context) {
		this(context, null);
	}

	public MileageGroup(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MileageGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public int getMileage() {
		return mileage;
	}

	public void setMileage(int mMileage) {
		this.mileage = mMileage;
		int count = getChildCount();
		NumberView child = null;
		for (int i = 0; i < count; i++) {
			child = (NumberView) getChildAt(i);
			int score = (int) (mMileage / Math.pow(10, count - i - 1) % 10);
			ObjectAnimator animator = ObjectAnimator.ofInt(child, "score",
					child.getScore(), score);
			animator.setDuration(500);
			animator.setInterpolator(new AccelerateDecelerateInterpolator());
			animator.start();
		}
		invalidate();
	}

}
