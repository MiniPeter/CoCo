package com.peter.coco.service;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.peter.coco.R;
import com.peter.coco.activity.Weather;
import com.peter.coco.model.CurrentWeather;
import com.peter.coco.model.TodayWeather;
import com.peter.coco.receiver.AutoUpdateReceiver;
import com.peter.coco.util.HttpCallbackListence;
import com.peter.coco.util.HttpUtil;
import com.peter.coco.util.Utility;

/**
 * @Title: AutoUpdateService.java
 * @Package com.peter.service
 * @Description: TODO
 * @author: PeterChen E-mail:neu20133788@163.com
 * @date: 2016-8-6 下午2:39:29
 * @version V1.0
 */
public class AutoUpdateService extends Service {
	private static final String KEY = "c5b7923738eb0ee654d4a774a7e98d60";
	private CurrentWeather currentWeather;
	private TodayWeather todayWeather;
	private String countyName;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				getWeatherCondition();
			}
		}).start();
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = 8 * 60 * 60 * 1000; // 这是8小时的毫秒数
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
		//指定处理的广播接收器为AutoUpdateReceiver
		Intent i = new Intent(this, AutoUpdateReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		//表示让定时任务的触发时间从系统开机开始算起
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

		return super.onStartCommand(intent, flags, startId);
	}

	private synchronized void getWeatherCondition() {
		try {
			SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
			countyName = pref.getString("default_county_name", "沈阳");
			String URLCountyName;
			URLCountyName = URLEncoder.encode(countyName, "utf-8");
			String address = "http://v.juhe.cn/weather/index?format=2&cityname=" + URLCountyName + "&key=" + KEY;
			HttpUtil.sendHttpRequest(address, new HttpCallbackListence() {

				@Override
				public void onFinish(String response) {
					// TODO Auto-generated method stub
					if (!TextUtils.isEmpty(response)) {
						try {
							JSONObject resultObject = new JSONObject(response);
							String code = resultObject.getString("resultcode");
							if(code != null && code.equals("200")) {
								currentWeather = Utility.getCurrentWeatherWithJSON(resultObject);
								todayWeather = Utility.getTodayWeatherWithJSON(resultObject);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						Intent intent = new Intent(AutoUpdateService.this, Weather.class);
						PendingIntent pi = PendingIntent.getActivity(AutoUpdateService.this, 0, intent,
						PendingIntent.FLAG_CANCEL_CURRENT);
						Notification notify = new Notification.Builder(AutoUpdateService.this)
			        		.setSmallIcon(R.mipmap.ic_launcher)
			        		.setContentTitle(countyName + ":" + todayWeather.getWeather())
			        		.setContentText(currentWeather.getTemp() + "℃  " + currentWeather.getWindDirection() + currentWeather.getWindStrength())
			        		.setContentIntent(pi)
			        		.setWhen(System.currentTimeMillis()).build();
						startForeground(1, notify);
					} else {
						Log.d("Weather", "getWeatherCondition() is fail!");
					}
				}

				@Override
				public void onError(Exception e) {
					// TODO Auto-generated method stub
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
