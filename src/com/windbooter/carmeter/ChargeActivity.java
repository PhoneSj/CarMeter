package com.windbooter.carmeter;

import java.util.Random;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.renderscript.RenderScript.ContextType;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.windbooter.carmeter.MainActivity.ChargeState;
import com.windbooter.carmeter.MainActivity.SpeedType;
import com.windbooter.carmeter.MainActivity.TemperatureType;
import com.windbooter.carmeter.customview.AnimTextView;
import com.windbooter.carmeter.customview.GifView;
import com.windbooter.carmeter.utils.ConstantUtils;
import com.windbooter.carmeter.utils.LogUtils;
import com.windbooter.carmeter.utils.ToastUtils;

public class ChargeActivity extends Activity {

	protected static final String TAG = "ChargeActivity";
	private GifView gifView_chargeLeft;
	private GifView gifView_chargeCenter;
	private AnimTextView animTextView_charge_currentBattery;

	private Button button_charge;
	Parse mParse = new Parse();
	private BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtils.i("data", "data is rec");
			String action = intent.getAction();
			if (action.equals(SerialPortService.ACTION_DATA)) {
				byte mByte = 0;
				byte data = intent.getByteExtra(
						ConstantUtils.INTENT_KEY_SERIALPORT_DATA, mByte);
				LogUtils.d(TAG, "data:" + data);
				MeterData meterData = mParse.constructData(data);
				switch (meterData.getId()) {
				// 电量
				case ConstantUtils.METER_BATTERY:
					updateBatteryPercent(meterData.getData());
					break;
				// 充电状态
				case ConstantUtils.METER_CHARGE_STATE:
					if (ControlState.getChargeState(meterData.getData()) == ChargeState.notCharging) {
						finish();
					}
					break;
				default:
					LogUtils.i(TAG, "没有对应该编号的仪表");
					break;
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.charge);

		initView();

		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mGattUpdateReceiver);
	}

	private void initView() {
		gifView_chargeLeft = (GifView) findViewById(R.id.gifView_chargeLeft);
		gifView_chargeCenter = (GifView) findViewById(R.id.gifView_chargeCenter);
		animTextView_charge_currentBattery = (AnimTextView) findViewById(R.id.animTextView_charge_currentBattery);

		button_charge = (Button) findViewById(R.id.button_charge);
	}

	public void updateBatteryPercent(int percent) {
		ObjectAnimator animator = ObjectAnimator.ofInt(
				animTextView_charge_currentBattery, "speed",
				animTextView_charge_currentBattery.getSpeed(), percent);
		animator.setDuration(500);
		animator.setInterpolator(new AccelerateDecelerateInterpolator());
		animator.start();
	}

	public void doButton(View view) {
		updateBatteryPercent(new Random().nextInt(100));
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
}
