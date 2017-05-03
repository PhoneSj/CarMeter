package com.windbooter.carmeter.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class AnimTextView extends TextView {

	private int speed;

	public AnimTextView(Context context) {
		this(context, null);
	}

	public AnimTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AnimTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		Typeface fontFace = Typeface.createFromAsset(context.getAssets(),
				"fonts/Elements.ttf");
		setTypeface(fontFace);
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
		this.setText(String.valueOf(speed));
	}

}
