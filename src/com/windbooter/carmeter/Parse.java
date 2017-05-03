package com.windbooter.carmeter;

import com.windbooter.carmeter.utils.LogUtils;

public class Parse {

	private final String TAG = "Parse";

	// 字节缓存
	private byte bytes[] = new byte[2];

	private MeterData meterData = new MeterData();

	public MeterData constructData(byte one) {
		if (bytes[0] + bytes[1] + 1 == 0) {
			LogUtils.i(TAG, "取反成功");
			meterData.setId(byte2Int(bytes[0]));
			meterData.setData(byte2Int(one));
			gression(one);
		} else {
			gression(one);
		}
		LogUtils.i(TAG, "byte[0]:" + bytes[0]);
		LogUtils.i(TAG, "byte[1]:" + bytes[1]);
		LogUtils.i(TAG, "one:" + one);

		return meterData;
	}

	/**
	 * 字节移位
	 * 
	 * @param one
	 */
	private void gression(byte one) {
		bytes[0] = bytes[1];
		bytes[1] = one;
	}

	/**
	 * byte转int
	 * 
	 * @param one
	 * @return
	 */
	private int byte2Int(byte one) {
		int data = 0;
		data = one & 0xff;
		LogUtils.i(TAG, "one1:" + one);
		LogUtils.i(TAG, "data1:" + data);
		return data;
	}

}
