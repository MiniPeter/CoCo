package com.peter.coco.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.peter.coco.service.AutoUpdateService;

/**
 * @Title: AutoUpdateReceiver.java
 * @Package com.peter.receiver
 * @Description: TODO
 * @author: PeterChen E-mail:neu20133788@163.com
 * @date: 2016-8-6 обнГ2:40:39
 * @version V1.0
 */
public class AutoUpdateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent i = new Intent(context, AutoUpdateService.class);
		context.startService(i);
	}

}
