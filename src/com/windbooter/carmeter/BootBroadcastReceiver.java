package com.windbooter.carmeter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent bootIntent = context.getPackageManager()
				.getLaunchIntentForPackage("com.windbooter.carmeter");
		context.startActivity(bootIntent);

	}

}
