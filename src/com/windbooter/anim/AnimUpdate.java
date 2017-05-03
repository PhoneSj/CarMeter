//package com.windbooter.anim;
//
//import android.animation.AnimatorSet;
//import android.animation.ObjectAnimator;
//import android.animation.PropertyValuesHolder;
//import android.view.animation.AccelerateDecelerateInterpolator;
//import android.widget.LinearLayout;
//
//import com.windbooter.carmeter.MainActivity.RunState;
//import com.windbooter.carmeter.MainActivity.TemperatureType;
//import com.windbooter.carmeter.customview.AnimTextView;
//import com.windbooter.carmeter.customview.ColorView;
//import com.windbooter.carmeter.customview.CustomView;
//import com.windbooter.carmeter.customview.MagicProgressCircle;
//import com.windbooter.carmeter.customview.NumberView;
//import com.windbooter.carmeter.utils.ConstantUtils;
//
//public class AnimUpdate {
//	/**
//	 * 更新转速
//	 * 
//	 * @param rotationalSpeed
//	 */
//	public void updateRotationalSpeed(AnimTextView mAnimTextView,
//			MagicProgressCircle mMagicProgressCircle, int rotationalSpeed) {
//		mAnimTextView.setText(rotationalSpeed + "");
//		float start_percent_rotational = mMagicProgressCircle.getPercent();
//		float end_percent_rotational = rotationalSpeed * 1.0f
//				/ ConstantUtils.MAX_ROTATIONAL_SPEED;
//		int start_score_rotational = mAnimTextView.getScore();
//		int end_score_rotational = rotationalSpeed * 360
//				/ ConstantUtils.MAX_ROTATIONAL_SPEED;
//		ObjectAnimator animator0 = ObjectAnimator.ofFloat(mMagicProgressCircle,
//				"percent", start_percent_rotational, end_percent_rotational);
//		ObjectAnimator animator1 = ObjectAnimator.ofInt(mAnimTextView, "score",
//				start_score_rotational, end_score_rotational);
//		AnimatorSet set = new AnimatorSet();
//		set.playTogether(animator0, animator1);
//		set.setDuration(2000);
//		set.setInterpolator(new AccelerateDecelerateInterpolator());
//		set.start();
//
//	}
//
//	/**
//	 * 更新移速
//	 * 
//	 * @param moveSpeed
//	 */
//	public void updateMoveSpeed(AnimTextView mAnimTextView,
//			MagicProgressCircle mMagicProgressCircle, int moveSpeed) {
//		mAnimTextView.setText(moveSpeed + "");
//		float start_percen_move = mMagicProgressCircle.getPercent();
//		float end_percen_move = moveSpeed * 1.0f / ConstantUtils.MAX_MOVE_SPEED;
//		int start_score_move = mAnimTextView.getScore();
//		int end_score_move = moveSpeed;
//		ObjectAnimator animator0 = ObjectAnimator.ofFloat(mMagicProgressCircle,
//				"percent", start_percen_move, end_percen_move);
//		ObjectAnimator animator1 = ObjectAnimator.ofInt(mAnimTextView, "score",
//				start_score_move, end_score_move);
//		AnimatorSet set = new AnimatorSet();
//		set.playTogether(animator0, animator1);
//		set.setDuration(2000);
//		set.setInterpolator(new AccelerateDecelerateInterpolator());
//		set.start();
//	}
//
//	/**
//	 * 更新温度
//	 * 
//	 * @param mTemperature
//	 *            ：温度值
//	 * @param type
//	 *            ：哪种仪表的温度（水温、油温、机器温度、逆变器温度）
//	 */
//	public void updateTemperature(CustomView mCustomView, int mTemperature,
//			TemperatureType type) {
//		int mLightSum = 0;
//		switch (type) {
//		case waterTemperature:
//			mLightSum = mTemperature * mCustomView.getmItemSum()
//					/ ConstantUtils.MAX_WATER_TEMPERATURE;
//			mCustomView.setmLightSum(mLightSum);
//			mCustomView.setmText(mTemperature + "C");
//			break;
//		case oilTemperature:
//			mLightSum = mTemperature * mCustomView.getmItemSum()
//					/ ConstantUtils.MAX_OIL_TEMPERATURE;
//			mCustomView.setmLightSum(mLightSum);
//			mCustomView.setmText(mTemperature + "C");
//			break;
//		case machineTemperature:
//			mLightSum = mTemperature * mCustomView.getmItemSum()
//					/ ConstantUtils.MAX_MACHINE_TEMPERATURE;
//			mCustomView.setmLightSum(mLightSum);
//			mCustomView.setmText(mTemperature + "C");
//			break;
//		case inverterTemperature:
//			mLightSum = mTemperature * mCustomView.getmItemSum()
//					/ ConstantUtils.MAX_INVERTER_TEMPERATURE;
//			mCustomView.setmLightSum(mLightSum);
//			mCustomView.setmText(mTemperature + "C");
//			break;
//		}
//
//	}
//
//	/**
//	 * 更新总里程
//	 * 
//	 * @param mMileageGroup
//	 * @param current_mileage
//	 */
//	public void updateMileage(LinearLayout mMileageGroup, int current_mileage) {
//		int last_mileage = getMileage(mMileageGroup);
//		if (current_mileage != last_mileage) {
//			for (int i = 0; i < mMileageGroup.getChildCount(); i++) {
//				if (getNumberAtIndex(current_mileage, i) != getNumberAtIndex(
//						last_mileage, i)) {
//					startMileageAnim(mMileageGroup, i, last_mileage,
//							current_mileage);
//				}
//			}
//		}
//
//	}
//
//	/**
//	 * 获取里程中某一位的数字
//	 * 
//	 * @param current_mileage
//	 * @param i
//	 */
//	private int getNumberAtIndex(int current_mileage, int i) {
//		return current_mileage % (int) (Math.pow(10, i));
//	}
//
//	/**
//	 * 
//	 * @param mMileageGroup
//	 *            :父容器
//	 * @param i
//	 *            ：数据发生改变的view在父容器的下标
//	 */
//	private void startMileageAnim(LinearLayout mMileageGroup, int i,
//			int last_mileage, int current_mileage) {
//		NumberView child = (NumberView) mMileageGroup.getChildAt(i);
//		ObjectAnimator animator = ObjectAnimator.ofInt(child, "score",
//				getNumberAtIndex(current_mileage, i),
//				getNumberAtIndex(current_mileage, current_mileage), i);
//		animator.setDuration(1000);
//		animator.start();
//
//	}
//
//	/**
//	 * 获得总里程
//	 * 
//	 * @param mMileageGroup
//	 * @return
//	 */
//	private int getMileage(LinearLayout mMileageGroup) {
//		int count = mMileageGroup.getChildCount();
//		NumberView child = null;
//		int data[] = new int[count];
//		for (int i = 0; i < count; i++) {
//			child = (NumberView) mMileageGroup.getChildAt(i);
//			data[count - 1 - i] = Integer.parseInt(child.getText().toString()
//					.trim());
//		}
//		int mMileage = 0;
//		for (int i = 0; i < count; i++) {
//			mMileage = (int) (mMileage + data[i] * Math.pow(10, i - 1));
//		}
//		return mMileage;
//	}
//
//	/**
//	 * 更新车的运行状态：正常速度、高速、极限速度
//	 * 
//	 * @param mCircle0
//	 *            :需要更改图片RGB的控件
//	 * @param mRunState
//	 *            ：需要切换到的状态
//	 */
//	public void updateRunState(ColorView mCircle0, ColorView mCircle1,
//			RunState mRunState) {
//
//		float last_red = mCircle0.getRed();
//		float last_green = mCircle0.getGreen();
//		float last_blue = mCircle0.getBlue();
//		float last_alpha = mCircle0.getAlpha();
//		float current_red = 0f;
//		float current_green = 0f;
//		float current_blue = 0f;
//		float current_alpha = 1f;
//
//		switch (mRunState) {
//		case normalSpeed:
//			current_red = 0f;
//			current_green = 1.0f;
//			current_blue = 0.4f;
//			current_alpha = 0.5f;
//			break;
//		case heightSpeed:
//			current_red = 1f;
//			current_green = 1f;
//			current_blue = 0f;
//			current_alpha = 0.5f;
//			break;
//		case SuperSpeed:
//			current_red = 1f;
//			current_green = 0f;
//			current_blue = 0f;
//			current_alpha = 0.5f;
//			break;
//		}
//		PropertyValuesHolder pvhRed = PropertyValuesHolder.ofFloat("red",
//				last_red, current_red);
//		PropertyValuesHolder pvhGreen = PropertyValuesHolder.ofFloat("green",
//				last_green, current_green);
//		PropertyValuesHolder pvhBlue = PropertyValuesHolder.ofFloat("blue",
//				last_blue, current_blue);
//		PropertyValuesHolder pvhAlpha = PropertyValuesHolder.ofFloat("alpha",
//				last_alpha, current_alpha);
//		ObjectAnimator animator0 = ObjectAnimator.ofPropertyValuesHolder(
//				mCircle0, pvhRed, pvhGreen, pvhBlue, pvhAlpha);
//		ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(
//				mCircle1, pvhRed, pvhGreen, pvhBlue, pvhAlpha);
//		AnimatorSet set = new AnimatorSet();
//		set.playTogether(animator0, animator1);
//		set.setDuration(3000);
//		set.start();
//	}
//}
