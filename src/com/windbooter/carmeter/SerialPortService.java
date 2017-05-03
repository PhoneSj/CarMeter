package com.windbooter.carmeter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.windbooter.carmeter.serialport.SerialPort;
import com.windbooter.carmeter.utils.ConstantUtils;
import com.windbooter.carmeter.utils.LogUtils;
import com.windbooter.carmeter.utils.ToastUtils;

public class SerialPortService extends Service {

	private final String TAG = "SerialPortService";

	public final static String ACTION_DATA = "com.windbooter.carmeter.ACTION_SERIALPORT_DATA";

	protected Application mApplication;
	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;

	private final IBinder mBinder = new LocalBinder();

	public class LocalBinder extends Binder {
		public SerialPortService getService() {
			return SerialPortService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = (Application) getApplication();
		try {
			mSerialPort = mApplication.getSerialPort();
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();

			/* Create a receiving thread */
			mReadThread = new ReadThread();
			mReadThread.start();
		} catch (SecurityException e) {
			DisplayError(R.string.error_security);
		} catch (IOException e) {
			DisplayError(R.string.error_unknown);
		} catch (InvalidParameterException e) {
			DisplayError(R.string.error_configuration);
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		if (mReadThread != null)
			mReadThread.interrupt();
		mApplication.closeSerialPort();
		mSerialPort = null;
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	private class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			while (!isInterrupted()) {
				int size;
				try {
					byte[] buffer = new byte[64];
					if (mInputStream == null)
						return;
					size = mInputStream.read(buffer);
					if (size > 0) {
						onDataReceived(buffer, size);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	private void DisplayError(int resourceId) {
		ToastUtils.showShort(getApplicationContext(), "异常");
		// AlertDialog.Builder b = new AlertDialog.Builder(this);
		// b.setTitle("Error");
		// b.setMessage(resourceId);
		// b.setPositiveButton("OK", new OnClickListener() {
		// public void onClick(DialogInterface dialog, int which) {
		// SerialPortActivity.this.finish();
		// }
		// });
		// b.show();
	}

	/**
	 * 从串口中读取到的数据
	 * 
	 * @param buffer
	 * @param size
	 */

	protected void onDataReceived(final byte[] buffer, final int size) {
		// runOnUiThread(new Runnable() {
		// public void run() {
		// if (mReception != null) {
		// mReception.append(new String(buffer, 0, size));
		// }
		// }
		// });
		// ToastUtils.showShort(getApplicationContext(),
		// "data:" + buffer.toString());
		LogUtils.i(TAG, "data:" + new String(buffer, 0, size));
		for (int i = 0; i < buffer.length; i++) {
			LogUtils.i(TAG, "buffer" + i + ":" + buffer[i]);
		}
		Intent intent = new Intent();
		// intent.putExtra("data", new String(buffer, 0, size));
		intent.putExtra(ConstantUtils.INTENT_KEY_SERIALPORT_DATA, buffer[0]);
		intent.setAction(SerialPortService.ACTION_DATA);
		sendBroadcast(intent);
		// new Thread() {
		// @Override
		// public void run() {
		// super.run();
		// Intent intent = new Intent();
		// intent.putExtra("data", new String(buffer, 0, size));
		// intent.setAction(SerialPortService.ACTION_DATA);
		// sendBroadcast(intent);
		// }
		// }.start();
	}

}
