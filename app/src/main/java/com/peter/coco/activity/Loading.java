package com.peter.coco.activity;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.peter.coco.R;
import com.peter.coco.db.CoCoWeatherDB;
import com.peter.coco.model.City;
import com.peter.coco.model.County;
import com.peter.coco.model.District;
import com.peter.coco.model.Province;
import com.peter.coco.util.HttpCallbackListence;
import com.peter.coco.util.HttpUtil;
import com.peter.coco.util.Utility;

/**
 * @Title: LoadActivity.java
 * @Package com.peter.activity
 * @Description: TODO加载数据Activity
 * @author: PeterChen E-mail:neu20133788@163.com
 * @date: 2016-8-4 下午9:04:53
 * @version V1.0
 */
public class Loading extends Activity {
	private static final String KEY = "c5b7923738eb0ee654d4a774a7e98d60";
	//存放is_first and countyName;
	SharedPreferences pref;
	//线程信息
	private static final int INIT_DB_MSG = 1;
	//数据库操作接口
	private CoCoWeatherDB DB;
	//线程处理器
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case INIT_DB_MSG: {
				handler.removeCallbacks(task);
				SharedPreferences.Editor editor = pref.edit();
				editor.putBoolean("is_first", false);
				editor.commit();
				editor.clear();
				Intent intent = new Intent(Loading.this, Weather.class);
				startActivity(intent);
				(Loading.this).finish();
			}
				break;
			}
		};
	};
	private Runnable task = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			new AlertDialog.Builder(Loading.this)
			.setTitle("系统提示")
			.setMessage("无法获取天气信息\n请检查手机是否联网！")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					(Loading.this).finish();
				}
			}).show();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.load);
		pref = getSharedPreferences("data", MODE_PRIVATE);
		DB = CoCoWeatherDB.getInstance(this);
		initDB();
		handler.postDelayed(task, 10000);
	}

	//从聚合网站api提取全国县地区的数据
	private synchronized void initDB() {
		final String CityNameListAddress = "http://v.juhe.cn/weather/citys?key=" + KEY;
		HttpUtil.sendHttpRequest(CityNameListAddress,
				new HttpCallbackListence() {

					@Override
					public void onFinish(String response) {
						// TODO Auto-generated method stub
						List<District> list = null;
						/*SharedPreferences.Editor ed = getSharedPreferences("citys", MODE_PRIVATE).edit();
						ed.putString("data", response);
						ed.commit();
						ed.clear();*/
						FileOutputStream out = null;
						BufferedWriter writer = null;
						try {
							out = openFileOutput("data", MODE_PRIVATE);
							writer = new BufferedWriter(new OutputStreamWriter(
									out));
							writer.write(response);
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							try {
								if (writer != null) {
									writer.close();
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						try {
							JSONObject resultObject = new JSONObject(response);
							String code = resultObject.getString("resultcode");
							if (code != null && code.equals("200")) {
								JSONArray arr = resultObject
										.getJSONArray("result");
								list = Utility.getDistrictWithJSON(arr);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (list != null && list.size() > 0) {
							DB.getSQLiteDatabase().beginTransaction();
							for (District district : list) {
								DB.saveDistrict(district);
							}
							list.clear();
							list = DB.loadDistrict();
							District oldDistrict = new District();
							Province temProvince = new Province();
							City tempCity = new City();
							County tempCounty = new County();
							int provinceId = -1;
							int cityId = -1;
							for (District district : list) {
								if (!oldDistrict.getDistrictProvince().equals(
										district.getDistrictProvince())) {
									temProvince.setProvinceName(district.getDistrictProvince());
									DB.saveProvince(temProvince);
									provinceId = DB.getProvinceId(district.getDistrictProvince());
									oldDistrict.setDistrictProvince(district.getDistrictProvince());
								}
								if (!oldDistrict.getDistrictCity().equals(
										district.getDistrictCity())) {
									tempCity.setCityName(district.getDistrictCity());
									tempCity.setProvinceId(provinceId);
									DB.saveCity(tempCity);
									cityId = DB.getCityId(district.getDistrictCity());
									oldDistrict.setDistrictCity(district.getDistrictCity());
								}
								tempCounty.setCountyName(district.getDistrictDistrict());
								tempCounty.setCityId(cityId);
								tempCounty.setDistrictId(district.getId());
								DB.saveCounty(tempCounty);
							}
							DB.getSQLiteDatabase().setTransactionSuccessful();
							DB.getSQLiteDatabase().endTransaction();
						}
						Message message = new Message();
						message.what = INIT_DB_MSG;
						handler.sendMessage(message);
					}

					@Override
					public void onError(Exception e) {
						// TODO Auto-generated method stub
						e.printStackTrace();
					}
				});

		/*String weatherTypeAddress = "http://v.juhe.cn/weather/uni?key=c5b7923738eb0ee654d4a774a7e98d60";
		HttpUtil.sendHttpRequest(weatherTypeAddress,
				new HttpCallbackListence() {

					@Override
					public void onFinish(String response) {
						// TODO Auto-generated method stub
						List<WeatherType> list = null;
						try {
							list = new ArrayList<WeatherType>();
							JSONObject resultObject = new JSONObject(response);
							String code = resultObject.getString("resultcode");
							if (code != null && code.equals("200")) {
								JSONArray arr = resultObject
										.getJSONArray("result");
								list = Utility.getWeatherTypeWithJSON(arr);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (list != null) {
							for (WeatherType weatherType : list) {
								DB.saveWeather(weatherType);
							}
						}
						Message message = new Message();
						message.what = WEATHER_THREAD_MSG;
						handler.sendMessage(message);
					}

					@Override
					public void onError(Exception e) {
						// TODO Auto-generated method stub
						e.printStackTrace();
					}
				});*/
	}
}
