package com.windbooter.carmeter.utils;

public class ConstantUtils {

	public static final int MAX_ROTATIONAL_SPEED = 240;
	public static final int MAX_MOVE_SPEED = 240;
	public static final int MAX_WATER_TEMPERATURE = 100;
	public static final int MAX_OIL_TEMPERATURE = 100;
	public static final int MAX_MACHINE_TEMPERATURE = 100;
	public static final int MAX_INVERTER_TEMPERATURE = 100;
	public static final int HANDLER_WHAT_ANIMATOR_RECYCLE = 0;

	public static final String SP_FILENAME = "sp_data";

	public static final int METER_TEMPERATURE_BATTERY = 0x01;
	public static final int METER_TEMPERATURE_WATER = 0x03;
	public static final int METER_TEMPERATURE_MACHINE = 0x05;
	public static final int METER_TEMPERATURE_INVERTER = 0x07;
	public static final int METER_POWER_PERCENT = 0x11;
	public static final int METER_MOVE_SPPED = 0x13;
	public static final int METER_MILEAGE=0x15;
	public static final int METER_MODE_TYPE = 0x21;
	public static final int METER_BATTERY = 0x23;
	public static final int METER_DOOR_STATE = 0x31;
	public static final int METER_REMOTE_LIGHT_STATE = 0x33;
	public static final int METER_CORNER_STATE = 0x35;
	public static final int METER_CHARGE_STATE = 0x37;
	public static final int METER_SWITCH_STATE = 0x41;

	public static final String INTENT_KEY_SERIALPORT_DATA = "intent_key_serialport_data";

}
