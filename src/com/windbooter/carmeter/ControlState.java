package com.windbooter.carmeter;

import com.windbooter.carmeter.MainActivity.ChargeState;
import com.windbooter.carmeter.MainActivity.CornerState;
import com.windbooter.carmeter.MainActivity.DoorState;
import com.windbooter.carmeter.MainActivity.ModeType;
import com.windbooter.carmeter.MainActivity.RemoteLightState;

public class ControlState {

	/**
	 * 行驶模式状态
	 * 
	 * @param data
	 * @return
	 */
	public static ModeType getModeType(int data) {
		ModeType modeType;
		switch (data) {
		case 0:
			modeType = ModeType.normalMode;
			break;
		case 1:
			modeType = ModeType.sportMode;
			break;
		case 2:
			modeType = ModeType.superMode;
			break;
		default:
			modeType = ModeType.normalMode;
			break;
		}
		return modeType;
	}

	/**
	 * 翻译车门开关状态
	 * 
	 * @param data
	 * @return
	 */
	public static DoorState getCarState(int data) {
		DoorState mCarState;
		switch (data) {
		case 0:
			mCarState = DoorState.doorLeftOpen;
			break;
		case 1:
			mCarState = DoorState.doorRightOpen;
			break;
		case 2:
			mCarState = DoorState.doorAllOpen;
			break;
		case 3:
			mCarState = DoorState.doorAllClose;
			break;
		default:
			mCarState = DoorState.doorAllClose;
			break;
		}
		return mCarState;
	}

	/**
	 * 翻译远光灯状态
	 * 
	 * @param data
	 * @return
	 */
	public static RemoteLightState getRemoteLightState(int data) {
		RemoteLightState mRemoteLightState;
		switch (data) {
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

		default:
			mRemoteLightState = RemoteLightState.remoteLightAllClose;
			break;
		}
		return mRemoteLightState;
	}

	/**
	 * 翻译转弯状态
	 * 
	 * @param data
	 * @return
	 */
	public static CornerState getCornerState(int data) {
		CornerState mCornerState;
		switch (data) {
		case 0:
			mCornerState = CornerState.leftCorner;
			break;
		case 1:
			mCornerState = CornerState.rightCorner;
			break;
		case 2:
			mCornerState = CornerState.noneCorner;
			break;

		default:
			mCornerState = CornerState.noneCorner;
			break;
		}
		return mCornerState;
	}

	/**
	 * 翻译充电状态
	 * 
	 * @param data
	 * @return
	 */
	public static ChargeState getChargeState(int data) {
		ChargeState mChargeState;
		switch (data) {
		case 0:
			mChargeState = ChargeState.isCharging;
			break;
		case 1:
			mChargeState = ChargeState.notCharging;
			break;
		default:
			mChargeState = ChargeState.notCharging;
			break;
		}
		return mChargeState;
	}
}
