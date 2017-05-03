package com.windbooter.carmeter.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.windbooter.carmeter.R;

public class TemperatureView extends View {

	private final int DEFAULT_ITEM_SUM = 10;// 默认item总数量
	private final int DEFAULT_START_ANGLE = 90;
	private final int DEFAULT_END_ANGLE = 45;
	private final int DEFAULT_ITEM_LENGTH = 20;// item的长度
	private final int DEFAULT_WARNING_COLOR = 0xffff0000;
	private final int DEFAULT_SELECTED_COLOR = 0xff00ff00;
	private final int DEFAULT_UNSELECTED_COLOR = 0xffaaaaaa;
	private final int DEFAULT_MAX_TEMPERATURE = 100;
	private final int DEFAULT_MIN_TEMPERATURE = 0;
	private final int DEFAULT_WARNING_TEMPERATURE = 80;

	private int itemSum = DEFAULT_ITEM_SUM;
	private double startAngle = DEFAULT_START_ANGLE * 1.0f / 180 * Math.PI;
	private double endAngle = DEFAULT_END_ANGLE * 1.0f / 180 * Math.PI;
	private int itemLength = DEFAULT_ITEM_LENGTH;
	private int warningColor = DEFAULT_WARNING_COLOR;
	private int selectedColor = DEFAULT_SELECTED_COLOR;
	private int unselectedColor = DEFAULT_UNSELECTED_COLOR;
	private int maxTemperature = DEFAULT_MAX_TEMPERATURE;
	private int minTemperature = DEFAULT_MIN_TEMPERATURE;
	private int warningTemperature = DEFAULT_WARNING_TEMPERATURE;
	private int currentTemperature = 80;

	public int getCurrentTemperature() {
		return currentTemperature;
	}

	public void setCurrentTemperature(int currentTemperature) {
		this.currentTemperature = currentTemperature;
		invalidate();
	}

	public TemperatureView(Context context) {
		this(context, null);
	}

	public TemperatureView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TemperatureView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		obtainStyledAttributes(attrs);
	}

	private void obtainStyledAttributes(AttributeSet attrs) {
		TypedArray attributes = getContext().obtainStyledAttributes(attrs,
				R.styleable.TemperatureView);
		itemSum = attributes.getInt(R.styleable.TemperatureView_tv_item_sum,
				DEFAULT_ITEM_SUM);
		int startAngleTemp = attributes
				.getInt(R.styleable.TemperatureView_tv_start_angle,
						DEFAULT_START_ANGLE);
		startAngle = startAngleTemp * 1.0f / 180 * Math.PI;
		int endAngleTemp = attributes.getInt(
				R.styleable.TemperatureView_tv_end_angle, DEFAULT_END_ANGLE);
		endAngle = endAngleTemp * 1.0f / 180 * Math.PI;
		maxTemperature = attributes.getInt(
				R.styleable.TemperatureView_tv_max_temperature,
				DEFAULT_MAX_TEMPERATURE);
		minTemperature = attributes.getInt(
				R.styleable.TemperatureView_tv_min_temperature,
				DEFAULT_MIN_TEMPERATURE);
		warningTemperature = attributes.getInt(
				R.styleable.TemperatureView_tv_warning_temperature,
				DEFAULT_WARNING_TEMPERATURE);
		attributes.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int width = getWidth();
		int height = getHeight();
		int radius = Math.min(width, height) / 2;

		double itemAngle = (endAngle - startAngle) / itemSum;
		double gapAngle = itemAngle / 5;

		for (int i = 0; i < itemSum; i++) {
			double itemStartAngle = startAngle + i * itemAngle;
			double itemEndAngle = itemStartAngle + itemAngle - gapAngle;
			int startX0 = (int) (radius * Math.cos(itemStartAngle)) + radius;
			int startY0 = (int) (radius * Math.sin(itemStartAngle)) + radius;
			int endX0 = (int) ((radius - itemLength) * Math.cos(itemStartAngle))
					+ radius;
			int endY0 = (int) ((radius - itemLength) * Math.sin(itemStartAngle))
					+ radius;

			int startX1 = (int) (radius * Math.cos(itemEndAngle)) + radius;
			int startY1 = (int) (radius * Math.sin(itemEndAngle)) + radius;
			int endX1 = (int) ((radius - itemLength) * Math.cos(itemEndAngle))
					+ radius;
			int endY1 = (int) ((radius - itemLength) * Math.sin(itemEndAngle))
					+ radius;

			Path path = new Path();
			path.moveTo(startX0, startY0);
			path.lineTo(endX0, endY0);
			path.lineTo(endX1, endY1);
			path.lineTo(startX1, startY1);
			path.close();

			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStyle(Style.FILL);

			if (currentTemperature > warningTemperature) {
				// 当当前温度超过报警温度时，全部item显示“报警颜色 ”
				paint.setColor(warningColor);
			} else if ((maxTemperature - minTemperature) / itemSum * i > warningTemperature) {
				paint.setColor(warningColor);
			} else if ((maxTemperature - minTemperature) / itemSum * i < currentTemperature) {
				paint.setColor(selectedColor);
			} else {
				paint.setColor(unselectedColor);
			}

			canvas.drawPath(path, paint);
		}

	}
}
