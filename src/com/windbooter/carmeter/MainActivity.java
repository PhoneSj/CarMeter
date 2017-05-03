package com.windbooter.carmeter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.windbooter.carmeter.customview.AnimTextView;
import com.windbooter.carmeter.customview.ColorView;
import com.windbooter.carmeter.customview.MileageGroup;
import com.windbooter.carmeter.customview.ModeView;
import com.windbooter.carmeter.customview.MonochromeProgressCircle;
import com.windbooter.carmeter.customview.ShaderCircle;
import com.windbooter.carmeter.customview.TempView;
import com.windbooter.carmeter.utils.ConstantUtils;
import com.windbooter.carmeter.utils.LogUtils;

public class MainActivity extends Activity {

	private final String TAG = "MainActivity";

	private AnimTextView animTextView_powerPercent;
	private AnimTextView animTextView_moveSpeed;

	private ImageView imageView_powerPercent;
	private ShaderCircle shaderCircle_percenPower;
	private ImageView imageView_moveSpeed;

	private ColorView colorView_leftCircle0;
	private ColorView colorView_leftCircle1;
	private ColorView colorView_leftCircle2;
	private ImageView imageView_leftCircle3;
	private ColorView colorView_rightCircle0;
	private ColorView colorView_rightCircle1;
	private ColorView colorView_rightCircle2;
	private ImageView imageView_rightCircle3;

	private TempView tempView_water;
	private TempView tempView_battery;
	private TempView tempView_inverter;
	private TempView tempView_machine;

	private ModeView modeView_mode;
	private MonochromeProgressCircle monochromeProgressCircle_battery;

	private LinearLayout linearLayout_mode;
	private RelativeLayout relativeLayout_normal;
	private RelativeLayout relativeLayout_sport;
	private RelativeLayout relativeLayout_super;
	private ImageView imageView_mode;

	private RelativeLayout relativeLayout_battery;
	private TextView textView_battery;
	private TextView textView_available;

	private ImageView imageView_car_direction;
	private ImageView imageView_car_left;
	private ImageView imageView_car_right;

	private ImageView imageView_car_light_left;
	private ImageView imageView_car_light_right;

	private MileageGroup mileageGroup_mileage;

	private Button button_test;

	public enum TemperatureType {
		waterTemperature, batteryTemperature, machineTemperature, inverterTemperature
	}

	public enum SpeedType {
		powerPercent, moveSpeed
	}

	public enum ModeType {
		normalMode, sportMode, superMode
	}

	public enum CornerState {
		noneCorner, leftCorner, rightCorner
	}

	public enum DoorState {
		doorLeftOpen, doorRightOpen, doorAllOpen, doorAllClose
	}

	public enum RemoteLightState {
		remoteLightLeftOpen, remoteLightRightOpen, remoteLightAllOpen, remoteLightAllClose
	}

	public enum ChargeState {
		isCharging, notCharging
	}

	private Parse mParse = new Parse();

	private ModeType currentModeType = ModeType.normalMode;
	private int currentBattery = 0;

	private BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtils.d(TAG, "onReceive...");
			String action = intent.getAction();
			if (action.equals(SerialPortService.ACTION_DATA)) {
				byte mByte = 0;
				byte data = intent.getByteExtra(
						ConstantUtils.INTENT_KEY_SERIALPORT_DATA, mByte);
				LogUtils.d(TAG, "data:" + data);
				MeterData meterData = mParse.constructData(data);
				button_test.setText("Data:" + meterData.getData());
				switch (meterData.getId()) {
				// 电池温度
				case ConstantUtils.METER_TEMPERATURE_BATTERY:
					if (meterData.getData() > 0 && meterData.getData() < 100) {
						updateTemperature(tempView_battery,
								meterData.getData(),
								TemperatureType.batteryTemperature);
					}
					break;
				// 水温
				case ConstantUtils.METER_TEMPERATURE_WATER:
					if (meterData.getData() > 0 && meterData.getData() < 100) {
						updateTemperature(tempView_water, meterData.getData(),
								TemperatureType.waterTemperature);
					}
					break;
				// 电机温度
				case ConstantUtils.METER_TEMPERATURE_MACHINE:
					if (meterData.getData() > 0 && meterData.getData() < 150) {
						updateTemperature(tempView_machine,
								meterData.getData(),
								TemperatureType.machineTemperature);
					}
					break;
				// 逆变器温度
				case ConstantUtils.METER_TEMPERATURE_INVERTER:
					if (meterData.getData() > 0 && meterData.getData() < 150) {
						updateTemperature(tempView_inverter,
								meterData.getData(),
								TemperatureType.inverterTemperature);
					}
					break;
				// 功率百分比
				case ConstantUtils.METER_POWER_PERCENT:
					updatePowerPercent(meterData.getData());
					break;
				// 车速
				case ConstantUtils.METER_MOVE_SPPED:
					updateSpeed(SpeedType.moveSpeed, meterData.getData());
					break;
				// 车速
				case ConstantUtils.METER_MILEAGE:
					updateMileage(meterData.getData());
					break;
				// 行驶模式
				case ConstantUtils.METER_MODE_TYPE:
					updateMode(ControlState.getModeType(meterData.getData()));
					if (currentModeType != ControlState.getModeType(meterData
							.getData())) {
						changeMode(ControlState
								.getModeType(meterData.getData()));
						changeModeCircle(
								ControlState.getModeType(meterData.getData()),
								2.0f, 1000);
						currentModeType = ControlState.getModeType(meterData
								.getData());
					}
					break;
				// 电量
				case ConstantUtils.METER_BATTERY:
					updateBattery(meterData.getData());
					if (currentBattery / 10 != meterData.getData()) {
						changeBattery(meterData.getData());
						currentBattery = meterData.getData();
					}
					break;
				// 车门
				case ConstantUtils.METER_DOOR_STATE:
					updateDoorState(ControlState.getCarState(meterData
							.getData()));
					break;
				// 远光灯
				case ConstantUtils.METER_REMOTE_LIGHT_STATE:
					updateRemoteLightState(ControlState
							.getRemoteLightState(meterData.getData()));
					break;
				// 转向灯
				case ConstantUtils.METER_CORNER_STATE:
					updateCornerState(ControlState.getCornerState(meterData
							.getData()));
					break;
				// 充电状态
				case ConstantUtils.METER_CHARGE_STATE:
					if (ControlState.getChargeState(meterData.getData()) == ChargeState.isCharging) {
						changeChargeState();
					}
					break;
				// 各路开关
				case ConstantUtils.METER_SWITCH_STATE:
					ImageView imageViews[] = { imageView_seatbelt,
							imageView_esp, imageView_wiper, imageView_abs,
							imageView_motorCheck, imageView_airbag,
							imageView_tcs, imageView_handrake };
					updateSwitch(imageViews, meterData.getData());
					break;

				default:
					LogUtils.i(TAG, "没有对应该编号的仪表");
					break;
				}
			}
		}

	};

	ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			((SerialPortService.LocalBinder) service).getService();
			LogUtils.d(TAG, "onServiceConnected");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			LogUtils.d(TAG, "onServiceDisconnected");
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		initView();
		recycleRoatation();
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mGattUpdateReceiver);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = new Intent(this, SerialPortService.class);
		startService(intent);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		initDisplayData();
	}

	private void initDisplayData() {
		relativeLayout_normal
				.setTranslationX(-relativeLayout_normal.getWidth());
		relativeLayout_sport.setTranslationX(-relativeLayout_sport.getWidth());
		relativeLayout_super.setTranslationX(-relativeLayout_super.getWidth());
		relativeLayout_battery.setTranslationX(relativeLayout_battery
				.getWidth());
		modeView_mode.setGreen(0xffff00ff);

		// imageView_cornerLeft.setVisibility(View.GONE);
		// imageView_remoteLightLeft.setVisibility(View.GONE);
		// imageView_cornerRight.setVisibility(View.GONE);
		// imageView_remoteLightRight.setVisibility(View.GONE);
		// imageView_seatbelt.setVisibility(View.INVISIBLE);
		// imageView_esp.setVisibility(View.INVISIBLE);
		// imageView_motorCheck.setVisibility(View.INVISIBLE);
		// imageView_airbag.setVisibility(View.INVISIBLE);
		// imageView_wiper.setVisibility(View.INVISIBLE);
		// imageView_abs.setVisibility(View.INVISIBLE);
		// imageView_tcs.setVisibility(View.INVISIBLE);
		// imageView_handrake.setVisibility(View.INVISIBLE);

	}

	/**
	 * 增加广播监听动作
	 * 
	 * @return
	 */
	private static IntentFilter makeGattUpdateIntentFilter() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(SerialPortService.ACTION_DATA);
		return intentFilter;
	}

	private void initView() {
		animTextView_powerPercent = (AnimTextView) findViewById(R.id.animTextView_powerPercent);
		animTextView_moveSpeed = (AnimTextView) findViewById(R.id.animTextView_moveSpeed);
		shaderCircle_percenPower = (ShaderCircle) findViewById(R.id.shaderCircle_percenPower);
		imageView_moveSpeed = (ImageView) findViewById(R.id.imageView_moveSpeed);

		colorView_leftCircle0 = (ColorView) findViewById(R.id.colorView_leftCircle0);
		colorView_leftCircle1 = (ColorView) findViewById(R.id.colorView_leftCircle1);
		colorView_leftCircle2 = (ColorView) findViewById(R.id.colorView_leftCircle2);
		imageView_leftCircle3 = (ImageView) findViewById(R.id.imageView_leftCircle3);
		colorView_rightCircle0 = (ColorView) findViewById(R.id.colorView_rightCircle0);
		colorView_rightCircle1 = (ColorView) findViewById(R.id.colorView_rightCircle1);
		colorView_rightCircle2 = (ColorView) findViewById(R.id.colorView_rightCircle2);
		imageView_rightCircle3 = (ImageView) findViewById(R.id.imageView_rightCircle3);

		tempView_water = (TempView) findViewById(R.id.tempView_water);
		tempView_battery = (TempView) findViewById(R.id.tempView_battery);
		tempView_inverter = (TempView) findViewById(R.id.tempView_inverter);
		tempView_machine = (TempView) findViewById(R.id.tempView_machine);

		modeView_mode = (ModeView) findViewById(R.id.modeView_mode);
		monochromeProgressCircle_battery = (MonochromeProgressCircle) findViewById(R.id.monochromeProgressCircle_battery);

		linearLayout_mode = (LinearLayout) findViewById(R.id.linearLayout_mode);
		relativeLayout_normal = (RelativeLayout) findViewById(R.id.relativeLayout_normal);
		relativeLayout_sport = (RelativeLayout) findViewById(R.id.relativeLayout_sport);
		relativeLayout_super = (RelativeLayout) findViewById(R.id.relativeLayout_super);
		imageView_mode = (ImageView) findViewById(R.id.imageView_mode);

		relativeLayout_battery = (RelativeLayout) findViewById(R.id.relativeLayout_battery);
		textView_battery = (TextView) findViewById(R.id.textView_battery);
		textView_available = (TextView) findViewById(R.id.textView_available);

		imageView_car_direction = (ImageView) findViewById(R.id.imageView_car_direction);
		imageView_car_left = (ImageView) findViewById(R.id.imageView_car_left);
		imageView_car_right = (ImageView) findViewById(R.id.imageView_car_right);

		mileageGroup_mileage = (MileageGroup) findViewById(R.id.mileageGroup_mileage);

		imageView_cornerLeft = (ImageView) findViewById(R.id.imageView_cornerLeft);
		imageView_remoteLightLeft = (ImageView) findViewById(R.id.imageView_remoteLightLeft);
		imageView_cornerRight = (ImageView) findViewById(R.id.imageView_cornerRight);
		imageView_remoteLightRight = (ImageView) findViewById(R.id.imageView_remoteLightRight);
		imageView_seatbelt = (ImageView) findViewById(R.id.imageView_seatbelt);
		imageView_esp = (ImageView) findViewById(R.id.imageView_esp);
		imageView_motorCheck = (ImageView) findViewById(R.id.imageView_motorCheck);
		imageView_airbag = (ImageView) findViewById(R.id.imageView_airbag);
		imageView_wiper = (ImageView) findViewById(R.id.imageView_wiper);
		imageView_abs = (ImageView) findViewById(R.id.imageView_abs);
		imageView_tcs = (ImageView) findViewById(R.id.imageView_tcs);
		imageView_handrake = (ImageView) findViewById(R.id.imageView_handrake);

		imageView_car_light_left = (ImageView) findViewById(R.id.imageView_car_light_left);
		imageView_car_light_right = (ImageView) findViewById(R.id.imageView_car_light_right);

		button_test = (Button) findViewById(R.id.button_test);

	}

	private ImageView imageView_cornerLeft;
	private ImageView imageView_remoteLightLeft;
	private ImageView imageView_cornerRight;
	private ImageView imageView_remoteLightRight;
	private ImageView imageView_seatbelt;
	private ImageView imageView_esp;
	private ImageView imageView_motorCheck;
	private ImageView imageView_airbag;
	private ImageView imageView_wiper;
	private ImageView imageView_abs;
	private ImageView imageView_tcs;
	private ImageView imageView_handrake;

	/**
	 * 更新温度
	 * 
	 * @param mTemperatureView
	 * @param mTemperature
	 * @param type
	 */
	public void updateTemperature(TempView mTempView, int mTemperature,
			TemperatureType type) {
		ObjectAnimator animator = ObjectAnimator.ofInt(mTempView,
				"tempCurrent", mTempView.getTempCurrent(), mTemperature);
		animator.setDuration(500);
		animator.setInterpolator(new AccelerateDecelerateInterpolator());
		animator.start();
	}

	/**
	 * 更新车速
	 * 
	 * @param type
	 * @param speed
	 */
	public void updateSpeed(SpeedType type, int Rotationspeed) {
		int speed = (int) (Rotationspeed * 10 * 60 * 2.16f / 1000);
		if (speed > 0 && speed < 240) {

			// ObjectAnimator animator0 =
			// ObjectAnimator.ofFloat(imageView_moveSpeed,
			// "rotation", imageView_moveSpeed.getRotation(),
			// speed * 1.0f / 240 * 240);
			// ObjectAnimator animator1 =
			// ObjectAnimator.ofInt(animTextView_moveSpeed,
			// "speed", animTextView_moveSpeed.getSpeed(), speed);
			// AnimatorSet set = new AnimatorSet();
			// set.playTogether(animator0, animator1);
			// set.setDuration(10);
			// set.setInterpolator(new AccelerateDecelerateInterpolator());
			// set.start();
			imageView_moveSpeed.setRotation(speed * 1.0f / 240 * 240);
			animTextView_moveSpeed.setSpeed(speed);
		}
	}

	public void updatePowerPercent(int percent) {
		if (percent > -10 && percent < 100) {

			// ObjectAnimator animator0 = ObjectAnimator.ofFloat(
			// shaderCircle_percenPower, "percent",
			// shaderCircle_percenPower.getPercent(), percent * 1.0f / 100);
			// ObjectAnimator animator1 = ObjectAnimator.ofInt(
			// animTextView_powerPercent, "speed",
			// animTextView_powerPercent.getSpeed(), percent);
			// AnimatorSet set = new AnimatorSet();
			// set.playTogether(animator0, animator1);
			// set.setDuration(1000);
			// set.setInterpolator(new AccelerateDecelerateInterpolator());
			// set.start();
			shaderCircle_percenPower.setPercent(percent * 1.0f / 100);
			animTextView_powerPercent.setSpeed(percent);
		}
	}

	private void updateMileage(int data) {
		mileageGroup_mileage.setMileage(data);
	}

	/**
	 * 更新模式
	 * 
	 * @param type
	 */
	public void updateMode(ModeType type) {
		int red = 0x00;
		int green = 0x00;
		int blue = 0x00;
		switch (type) {
		case normalMode:
			red = 0x9a;
			green = 0xff;
			blue = 0xff;
			imageView_mode.setImageResource(R.drawable.mode_normal);
			break;
		case sportMode:
			red = 0xec;
			green = 0x9a;
			blue = 0x10;
			imageView_mode.setImageResource(R.drawable.mode_sport);
			break;
		case superMode:
			red = 0xff;
			green = 0x00;
			blue = 0x00;
			imageView_mode.setImageResource(R.drawable.mode_super);
			break;
		}
		ObjectAnimator animator0 = ObjectAnimator.ofInt(modeView_mode, "red",
				modeView_mode.getRed(), red);
		ObjectAnimator animator1 = ObjectAnimator.ofInt(modeView_mode, "green",
				modeView_mode.getGreen(), green);
		ObjectAnimator animator2 = ObjectAnimator.ofInt(modeView_mode, "blue",
				modeView_mode.getBlue(), blue);

		AnimatorSet set = new AnimatorSet();
		set.playTogether(animator0, animator1, animator2);
		set.setDuration(1000);
		set.setInterpolator(new AccelerateDecelerateInterpolator());
		set.start();
	}

	/**
	 * 切换模式
	 * 
	 * @param type
	 */
	public void changeMode(ModeType type) {
		relativeLayout_normal
				.setTranslationX(-relativeLayout_normal.getWidth());
		relativeLayout_sport.setTranslationX(-relativeLayout_sport.getWidth());
		relativeLayout_super.setTranslationX(-relativeLayout_super.getWidth());
		RelativeLayout target = null;
		switch (type) {
		case normalMode:
			target = relativeLayout_normal;
			break;
		case sportMode:
			target = relativeLayout_sport;
			break;
		case superMode:
			target = relativeLayout_super;
			break;
		}
		ObjectAnimator animator0 = ObjectAnimator.ofFloat(target,
				"translationX", -target.getWidth(), 0);
		animator0.setDuration(500);
		ObjectAnimator animator1 = ObjectAnimator.ofFloat(target,
				"translationX", 0, 0);
		animator1.setDuration(3000);
		ObjectAnimator animator2 = ObjectAnimator.ofFloat(target,
				"translationX", 0, -target.getWidth());
		animator2.setDuration(500);
		AnimatorSet set = new AnimatorSet();
		set.playSequentially(animator0, animator1, animator2);
		set.setInterpolator(new AccelerateDecelerateInterpolator());
		set.start();
	}

	private void changeModeCircle(ModeType mode, Float scale, int duration) {
		float red = 1.0f;
		float green = 1.0f;
		float blue = 1.0f;
		float alpha = 1.0f;
		ColorView colorViews[] = { colorView_leftCircle0,
				colorView_leftCircle1, colorView_leftCircle2,
				colorView_rightCircle0, colorView_rightCircle1,
				colorView_rightCircle2 };
		ColorView target = null;
		List<Animator> animatorList = new ArrayList<Animator>();
		for (int i = 0; i < colorViews.length; i++) {
			target = colorViews[i];
			switch (mode) {
			case normalMode:
				red = 0f;
				green = 1.0f;
				blue = 1.0f;
				alpha = 1.0f;
				break;
			case sportMode:
				red = 1.2f;
				green = 0.8f;
				blue = 0.45f;
				alpha = 1.0f;
				break;
			case superMode:
				red = 1.2f;
				green = 0f;
				blue = 0f;
				alpha = 1.0f;
				break;
			}
			PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat("red",
					target.getRed(), red * scale, red);
			PropertyValuesHolder pvhG = PropertyValuesHolder.ofFloat("green",
					target.getGreen(), green * scale, green);
			PropertyValuesHolder pvhB = PropertyValuesHolder.ofFloat("blue",
					target.getBlue(), blue * scale, blue);
			ObjectAnimator animator0 = ObjectAnimator.ofPropertyValuesHolder(
					target, pvhR, pvhG, pvhB);
			animatorList.add(animator0);
		}
		switch (mode) {
		case normalMode:
			imageView_leftCircle3.setImageResource(R.drawable.img04_green);
			imageView_rightCircle3.setImageResource(R.drawable.img04_green);
			break;
		case sportMode:
			imageView_leftCircle3.setImageResource(R.drawable.img04_yellow);
			imageView_rightCircle3.setImageResource(R.drawable.img04_yellow);
			break;
		case superMode:
			imageView_leftCircle3.setImageResource(R.drawable.img04_red);
			imageView_rightCircle3.setImageResource(R.drawable.img04_red);
			break;
		}
		ObjectAnimator animator_other0 = ObjectAnimator.ofFloat(
				imageView_leftCircle3, "alpha", 0, 1.0f, 0);
		ObjectAnimator animator_other1 = ObjectAnimator.ofFloat(
				imageView_rightCircle3, "alpha", 0, 1.0f, 0);

		animatorList.add(animator_other0);
		animatorList.add(animator_other1);
		AnimatorSet set = new AnimatorSet();
		set.setDuration(duration);
		set.setInterpolator(new AccelerateDecelerateInterpolator());
		set.playTogether(animatorList);
		set.start();

	}

	/**
	 * 更新电量
	 * 
	 * @param percent
	 */
	public void updateBattery(int percent) {
		if (percent >= 0 && percent <= 100) {
			ObjectAnimator animator = ObjectAnimator.ofFloat(
					monochromeProgressCircle_battery, "percent",
					monochromeProgressCircle_battery.getPercent(), percent);
			animator.setDuration(500);
			animator.setInterpolator(new AccelerateDecelerateInterpolator());
			animator.start();
			textView_battery.setText(String.valueOf(percent));
			textView_available.setText(String.valueOf(getEndurance(percent))
					+ "Km");
		}

	}

	/**
	 * 根据当前点亮计算可续航
	 * 
	 * @param percent
	 * @return
	 */
	private int getEndurance(int percent) {

		return (int) (percent * 4.5f);
	}

	/**
	 * 改变电量
	 * 
	 * @param percent
	 */
	public void changeBattery(int percent) {
		if (percent >= 0 && percent <= 100) {
			relativeLayout_battery.setTranslationX(relativeLayout_battery
					.getTranslationX());
			ObjectAnimator animator0 = ObjectAnimator.ofFloat(
					relativeLayout_battery, "translationX",
					relativeLayout_battery.getWidth(), 0);
			ObjectAnimator animator1 = ObjectAnimator.ofFloat(
					relativeLayout_battery, "translationX", 0, 0);
			ObjectAnimator animator2 = ObjectAnimator.ofFloat(
					relativeLayout_battery, "translationX", 0,
					relativeLayout_battery.getWidth());
			animator0.setDuration(500);
			animator1.setDuration(3000);
			animator2.setDuration(500);
			AnimatorSet set = new AnimatorSet();
			set.playSequentially(animator0, animator1, animator2);
			set.setInterpolator(new AccelerateDecelerateInterpolator());
			set.start();
		}

	}

	/**
	 * 更新状态
	 * 
	 * @param state
	 */
	public void updateDoorState(DoorState state) {
		switch (state) {
		case doorLeftOpen:
			imageView_car_left.setImageResource(R.drawable.car_left_door);
			imageView_car_right.setImageResource(R.drawable.car_right_normal);
			break;
		case doorRightOpen:
			imageView_car_left.setImageResource(R.drawable.car_left_normal);
			imageView_car_right.setImageResource(R.drawable.car_right_door);
			break;
		case doorAllOpen:
			imageView_car_left.setImageResource(R.drawable.car_left_door);
			imageView_car_right.setImageResource(R.drawable.car_right_door);
			break;
		case doorAllClose:
			imageView_car_left.setImageResource(R.drawable.car_left_normal);
			imageView_car_right.setImageResource(R.drawable.car_right_normal);
			break;
		}

	}

	/**
	 * 切换充电状态
	 * 
	 * @param state
	 */
	private void changeChargeState() {
		Intent chargeIntent = new Intent(MainActivity.this,
				ChargeActivity.class);
		startActivity(chargeIntent);
	}

	/**
	 * 测试
	 * 
	 * @param view
	 */
	public void doButton(View view) {
		if (view.getId() == R.id.button_test) {
			updateTemperature(tempView_water, new Random().nextInt(100),
					TemperatureType.waterTemperature);
			updateTemperature(tempView_battery, new Random().nextInt(100),
					TemperatureType.batteryTemperature);
			updateTemperature(tempView_machine, new Random().nextInt(100),
					TemperatureType.machineTemperature);
			updateTemperature(tempView_inverter, new Random().nextInt(100),
					TemperatureType.inverterTemperature);
			updatePowerPercent(new Random().nextInt(110) - 10);
			updateSpeed(SpeedType.moveSpeed, new Random().nextInt(200));
			updateBattery(new Random().nextInt(100));
			ModeType currentMode = ModeType.normalMode;
			switch (new Random().nextInt(3)) {
			case 0:
				currentMode = ModeType.normalMode;
				break;
			case 1:
				currentMode = ModeType.sportMode;
				break;
			case 2:
				currentMode = ModeType.superMode;
				break;
			}
			updateMode(currentMode);
			changeMode(currentMode);
			changeModeCircle(currentMode, 3.0f, 1000);
			changeBattery(new Random().nextInt(100));

			DoorState mCarState = DoorState.doorAllClose;
			switch (new Random().nextInt(4)) {
			case 0:
				mCarState = mCarState.doorLeftOpen;
				break;
			case 1:
				mCarState = mCarState.doorRightOpen;
				break;
			case 2:
				mCarState = mCarState.doorAllOpen;
				break;
			case 3:
				mCarState = mCarState.doorAllClose;
				break;
			}
			updateDoorState(mCarState);
			mileageGroup_mileage.setMileage(new Random().nextInt(900000));
			// 测试转弯
			CornerState mCornerState = CornerState.noneCorner;
			switch (new Random().nextInt(3)) {
			case 0:
				mCornerState = CornerState.noneCorner;
				break;
			case 1:
				mCornerState = CornerState.leftCorner;
				break;
			case 2:
				mCornerState = CornerState.rightCorner;
				break;
			}
			updateCornerState(mCornerState);
			// 测试远光灯
			RemoteLightState mRemoteLightState = RemoteLightState.remoteLightAllClose;
			switch (new Random().nextInt(2)) {
			case 0:
				mRemoteLightState = RemoteLightState.remoteLightLeftOpen;
				break;
			case 1:
				mRemoteLightState = RemoteLightState.remoteLightRightOpen;
				break;
			case 2:
				mRemoteLightState = RemoteLightState.remoteLightAllOpen;
				break;
			case 3:
				mRemoteLightState = RemoteLightState.remoteLightAllClose;
				break;
			}
			updateRemoteLightState(mRemoteLightState);

			// 测试开关
			ImageView imageViews[] = { imageView_seatbelt, imageView_esp,
					imageView_wiper, imageView_abs, imageView_motorCheck,
					imageView_airbag, imageView_tcs, imageView_handrake };
			updateSwitch(imageViews, new Random().nextInt(256));
		} else if(view.getId()==R.id.button_battery){
			Intent chargeIntent = new Intent(this, ChargeActivity.class);
			startActivity(chargeIntent);
		}

	}
	

	/**
	 * 更新远光灯状态
	 * 
	 * @param state
	 */
	private void updateRemoteLightState(RemoteLightState state) {
		switch (state) {
		case remoteLightLeftOpen:
			imageView_remoteLightLeft.setVisibility(View.VISIBLE);
			imageView_remoteLightRight.setVisibility(View.GONE);
			imageView_car_light_left
					.setImageResource(R.drawable.car_light_left_open);
			imageView_car_light_right
					.setImageResource(R.drawable.car_light_right_close);
			break;
		case remoteLightRightOpen:
			imageView_remoteLightLeft.setVisibility(View.GONE);
			imageView_remoteLightRight.setVisibility(View.VISIBLE);
			imageView_car_light_left
					.setImageResource(R.drawable.car_light_left_close);
			imageView_car_light_right
					.setImageResource(R.drawable.car_light_right_open);
			break;
		case remoteLightAllOpen:
			imageView_remoteLightLeft.setVisibility(View.VISIBLE);
			imageView_remoteLightRight.setVisibility(View.VISIBLE);
			imageView_car_light_left
					.setImageResource(R.drawable.car_light_left_open);
			imageView_car_light_right
					.setImageResource(R.drawable.car_light_right_open);
			break;
		case remoteLightAllClose:
			imageView_remoteLightLeft.setVisibility(View.INVISIBLE);
			imageView_remoteLightRight.setVisibility(View.INVISIBLE);
			imageView_car_light_left
					.setImageResource(R.drawable.car_light_left_close);
			imageView_car_light_right
					.setImageResource(R.drawable.car_light_right_close);
			break;
		}

	}

	/**
	 * 更新转弯状态
	 * 
	 * @param state
	 */
	private void updateCornerState(CornerState state) {
		switch (state) {
		case noneCorner:
			imageView_cornerLeft.setVisibility(View.GONE);
			imageView_cornerRight.setVisibility(View.GONE);
			break;
		case leftCorner:
			imageView_cornerLeft.setVisibility(View.VISIBLE);
			imageView_cornerRight.setVisibility(View.GONE);
			break;
		case rightCorner:
			imageView_cornerLeft.setVisibility(View.GONE);
			imageView_cornerRight.setVisibility(View.VISIBLE);
			break;
		}

	}

	public void updateSwitch(ImageView imageViews[], int state) {
		for (int i = 0; i < 8; i++) {
			int itemtate = ((state >> i) & 0x0001);
			switch (itemtate) {
			case 0:
				imageViews[i].setVisibility(View.INVISIBLE);
				break;
			case 1:
				imageViews[i].setVisibility(View.VISIBLE);
				break;
			default:
				LogUtils.d(TAG, "error");
				break;
			}
		}
	}

	/**
	 * 循环动画
	 */
	public void recycleRoatation() {
		ObjectAnimator animator0 = ObjectAnimator.ofFloat(
				colorView_leftCircle1, "rotation", 0, 360);
		ObjectAnimator animator1 = ObjectAnimator.ofFloat(
				colorView_rightCircle1, "rotation", 0, 360);
		animator0.setDuration(1000);
		AnimatorSet set = new AnimatorSet();
		set.playTogether(animator0, animator1);
		set.setDuration(new Random().nextInt(10000) + 20000);
		set.setInterpolator(new LinearInterpolator());
		set.start();
		set.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				recycleRoatation();
			}

		});
	}
}
