package com.peter.coco.activity;

import java.net.URLEncoder;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.peter.coco.R;
import com.peter.coco.model.CurrentWeather;
import com.peter.coco.model.FutureWeather;
import com.peter.coco.model.TodayWeather;
import com.peter.coco.service.AutoUpdateService;
import com.peter.coco.util.HttpCallbackListence;
import com.peter.coco.util.HttpUtil;
import com.peter.coco.util.Utility;

/**
 * @Title: WeatherActivity.java
 * @Package com.peter.activity
 * @Description: TODO
 * @author: PeterChen E-mail:neu20133788@163.com
 * @date: 2016-8-4 下午9:04:25
 * @version V1.0
 */
public class Weather extends Activity implements SwipeRefreshLayout.OnRefreshListener {
	private static final String KEY = "c5b7923738eb0ee654d4a774a7e98d60";
	private Boolean index = false;
	JSONArray chuanyi;
	JSONArray ganmao;
	JSONArray kongtiao;
	JSONArray wuran;
	JSONArray xiche;
	JSONArray yundong;
	JSONArray ziwaixian;
	//weather.xml数据
	private SwipeRefreshLayout swipeLayout;
	private Button switchCounty;
	private TextView titleCountyName;
	private Button refreshWeather;

	private ScrollView weatherBackground;

	private ImageView temperatureDecade;
	private ImageView temperatureUnit;

	private TextView windDirection;
	private TextView windStrength;
	private TextView humidity;
	private TextView time;

	private TextView firstPre;
	private ImageView firstImage;
	private TextView firstNext;
	private TextView secondPre;
	private ImageView secondImage;
	private TextView secondNext;
	private TextView threeWeek;
	private TextView threePre;
	private ImageView threeImage;
	private TextView threeNext;
	private TextView fourWeek;
	private TextView fourPre;
	private ImageView fourImage;
	private TextView fourNext;
	private TextView fiveWeek;
	private TextView fivePre;
	private ImageView fiveImage;
	private TextView fiveNext;

	private ImageView chuanyiImg;
	private ImageView ziwaixianImg;
	private ImageView xicheImg;
	private ImageView lvyoutImg;
	private ImageView yundongImg;
	private ImageView ganzhaoImg;
	/*private TextView chuanYiText;
	private TextView ziWaiXianText;
	private TextView xiCheText;
	private TextView lvYouText;
	private TextView yunDongText;
	private TextView ganZhaoText;*/

	private static final int RAIN = 1;
	private static final int SNOW = 2;
	private static final int FOG = 3;
	private static final int SAND = 4;
	private static final int CLOUD = 5;
	private static final int SUN = 6;
	//weather.xml数据
	private CurrentWeather currentWeather;
	private TodayWeather todayWeather;
	private List<FutureWeather> futureWeatherList;
	private static final int GET_DATA = 2;
	private ProgressDialog loading;
	private boolean isFirstDisplay;
	private String displayCountyName;
	private String defaultCountyName;
	private String countyName;
	private SharedPreferences pref;
	//private CoCoWeatherDB coCoWeatherDB;
	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case GET_DATA : {
				refreshUI();
				handler.postDelayed(loadingTask, 1000);
				handler.removeCallbacks(task);
			}
				break;
			}
		};
	};
	private Runnable loadingTask = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (loading.isShowing()) {
				loading.dismiss();
			}
			swipeLayout.setRefreshing(false);
		}
	};
	private Runnable task = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			new AlertDialog.Builder(Weather.this)
					.setTitle("系统提示")
					.setMessage("无法获取天气信息\n请检查手机是否联网！")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									if(loading.isShowing()) {
										loading.dismiss();
									}
									swipeLayout.setRefreshing(false);
								}
							}).show();
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather);
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorSchemeResources(android.R.color.holo_purple,
				android.R.color.holo_blue_bright,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		//coCoWeatherDB = CoCoWeatherDB.getInstance(this);
		pref = getSharedPreferences("data", MODE_PRIVATE);
		defaultCountyName = pref.getString("default_county_name", "沈阳");
		isFirstDisplay = true;
		countyName = defaultCountyName;
		loading = new ProgressDialog(this);
		//xml变量初始化
		switchCounty = (Button) findViewById(R.id.switch_county);
		switchCounty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isFirstDisplay = false;
				Intent intent = new Intent(Weather.this, SelectLocation.class);
				startActivity(intent);
			}
		});
		titleCountyName = (TextView) findViewById(R.id.title_county_name);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		refreshWeather.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handler.removeCallbacks(task);
				loading.show();
				handler.postDelayed(task, 10000);
				getWeatherCondition();
			}
		});
		weatherBackground = (ScrollView) findViewById(R.id.weather_background);
		temperatureDecade = (ImageView) findViewById(R.id.temperature_decade);
		temperatureUnit = (ImageView) findViewById(R.id.temperature_unit);
		windDirection = (TextView) findViewById(R.id.wind_direction);
		windStrength = (TextView) findViewById(R.id.wind_strength);
		humidity = (TextView) findViewById(R.id.humidity);
		time = (TextView) findViewById(R.id.time);
		firstPre = (TextView) findViewById(R.id.first_pre);
		firstImage = (ImageView) findViewById(R.id.first_image);
		firstNext = (TextView) findViewById(R.id.first_next);
		secondPre = (TextView) findViewById(R.id.second_pre);
		secondImage = (ImageView) findViewById(R.id.second_image);
		secondNext = (TextView) findViewById(R.id.second_next);
		threeWeek = (TextView) findViewById(R.id.three_week);
		threePre = (TextView) findViewById(R.id.three_pre);
		threeImage = (ImageView) findViewById(R.id.three_image);
		threeNext = (TextView) findViewById(R.id.three_next);
		fourWeek = (TextView) findViewById(R.id.four_week);
		fourPre = (TextView) findViewById(R.id.four_pre);
		fourImage = (ImageView) findViewById(R.id.four_image);
		fourNext = (TextView) findViewById(R.id.four_next);
		fiveWeek = (TextView) findViewById(R.id.five_week);
		fivePre = (TextView) findViewById(R.id.five_pre);
		fiveImage = (ImageView) findViewById(R.id.five_image);
		fiveNext = (TextView) findViewById(R.id.five_next);
		/*chuanYiText = (TextView) findViewById(R.id.chuanyi_text);
		ziWaiXianText = (TextView) findViewById(R.id.ziwaixian_text);
		xiCheText = (TextView) findViewById(R.id.xiche_text);
		lvYouText = (TextView) findViewById(R.id.lvyou_text);
		yunDongText = (TextView) findViewById(R.id.yundong_text);
		ganZhaoText = (TextView) findViewById(R.id.ganzhao_text);*/

		chuanyiImg = (ImageView) findViewById(R.id.chuanyi_img);
		chuanyiImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String top = "null";
				String temp = "null";
				try {
					if(index) {
						temp = chuanyi.get(1).toString();
						top = chuanyi.get(0).toString();
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
				new AlertDialog.Builder(Weather.this).setTitle(top)
				.setMessage(temp)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).show();

			}
		});
		ziwaixianImg = (ImageView) findViewById(R.id.ziwaixian_img);
		ziwaixianImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String top = "null";
				String temp = "null";
				try {
					if(index) {
						temp = ziwaixian.get(1).toString();
						top = ziwaixian.get(0).toString();
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
				new AlertDialog.Builder(Weather.this).setTitle(top)
				.setMessage(temp)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).show();
			}
		});
		xicheImg = (ImageView) findViewById(R.id.xiche_img);
		xicheImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String top = "null";
				String temp = "null";
				try {
					if(index) {
						temp = xiche.get(1).toString();
						top = xiche.get(0).toString();
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
				new AlertDialog.Builder(Weather.this).setTitle(top)
				.setMessage(temp)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).show();
			}
		});
		lvyoutImg = (ImageView) findViewById(R.id.lvyou_img);
		lvyoutImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String top = "null";
				String temp = "null";
				try {
					if(index) {
						temp = wuran.get(1).toString();
						top = wuran.get(0).toString();
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
				new AlertDialog.Builder(Weather.this).setTitle(top)
				.setMessage(temp)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).show();
			}
		});
		yundongImg = (ImageView) findViewById(R.id.yundong_img);
		yundongImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String top = "null";
				String temp = "null";
				try {
					if(index) {
						temp = yundong.get(1).toString();
						top = yundong.get(0).toString();
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
				new AlertDialog.Builder(Weather.this).setTitle(top)
				.setMessage(temp)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).show();
			}
		});
		ganzhaoImg = (ImageView) findViewById(R.id.ganzhao_img);
		ganzhaoImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String top = "null";
				String temp = "null";
				try {
					if(index) {
						temp = kongtiao.get(1).toString();
						top = kongtiao.get(0).toString();
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
				new AlertDialog.Builder(Weather.this).setTitle(top)
				.setMessage(temp)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).show();
			}
		});
		//xml变量初始化
		if(isFirstDisplay == true) {
			Intent intent = new Intent(this, AutoUpdateService.class);
			startService(intent);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(isFirstDisplay == false) {
			defaultCountyName = pref.getString("default_county_name", "沈阳");
			displayCountyName = pref.getString("display_county_name", defaultCountyName);
			countyName = displayCountyName;

			SharedPreferences.Editor editor = pref.edit();
			editor.putString("display_county_name", defaultCountyName);
			editor.commit();
			editor.clear();
		}
		handler.removeCallbacks(task);
		loading.setTitle("正在加载天气信息...");
		loading.setMessage("请稍后...");
		loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		loading.show();
		handler.postDelayed(task, 10000);
		getWeatherCondition();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this).setTitle("系统提示")
		.setMessage("您要退出程序吗？")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				(Weather.this).finish();
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		}).show();
	}

	private void getIndexCondition() {
		try {
			String URLCountyName;
			if (!TextUtils.isEmpty(countyName)) {
				URLCountyName = URLEncoder.encode(countyName, "utf-8");
				String addressIndex = "http://op.juhe.cn/onebox/weather/query?cityname="
						+ URLCountyName
						+ "&dtype=&key=99ac96d2f7103db4d40bc6d3d16f15b0";
				HttpUtil.sendHttpRequest(addressIndex,
						new HttpCallbackListence() {

							@Override
							public void onFinish(String response) {
								// TODO Auto-generated method stub
								if (!TextUtils.isEmpty(response)) {
									try {
										JSONObject resultObject = new JSONObject(response);
										String reason = resultObject.getString("reason");
										if(reason != null && reason.equals("successed!")) {
											JSONObject result = resultObject.getJSONObject("result");
											JSONObject data = result.getJSONObject("data");
											JSONObject life = data.getJSONObject("life");
											JSONObject info_ = life.getJSONObject("info");
											chuanyi = info_.getJSONArray("chuanyi");
											ganmao = info_.getJSONArray("ganmao");
											kongtiao = info_.getJSONArray("kongtiao");
											wuran = info_.getJSONArray("wuran");
											xiche = info_.getJSONArray("xiche");
											yundong = info_.getJSONArray("yundong");
											ziwaixian = info_.getJSONArray("ziwaixian");
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
									index = true;
								}
							}

							@Override
							public void onError(Exception e) {
								// TODO Auto-generated method stub
								e.printStackTrace();
							}
						});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private synchronized void getWeatherCondition() {
		try {
			String URLCountyName;
			if(!TextUtils.isEmpty(countyName)) {
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
									futureWeatherList = Utility.getFutureWeatherWithJSON(resultObject);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							getIndexCondition();
							Message msg = new Message();
							msg.what = GET_DATA;
							handler.sendMessage(msg);
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void refreshUI() {
		titleCountyName.setText(countyName);

		//set background weather image
		String[] tempTime = (currentWeather.getTime()).split(":");
		if ((Integer.parseInt(tempTime[0]) >= 6) && (Integer.parseInt(tempTime[0]) <= 18)) {
			if(todayWeather.getWeather().contains("雨")) {
				weatherBackground.setBackgroundResource(R.drawable.bg_rain);
			} else if(todayWeather.getWeather().contains("雪")) {
				weatherBackground.setBackgroundResource(R.drawable.bg_snow);
			} else if(todayWeather.getWeather().contains("雾")) {
				weatherBackground.setBackgroundResource(R.drawable.bg_fog);
			} else if(todayWeather.getWeather().contains("沙") || todayWeather.getWeather().contains("尘")) {
				weatherBackground.setBackgroundResource(R.drawable.bg_sand_storm);
			} else if(todayWeather.getWeather().contains("阴") || todayWeather.getWeather().contains("云")) {
				weatherBackground.setBackgroundResource(R.drawable.bg_overcast);
			} else if(todayWeather.getWeather().contains("晴")) {
				weatherBackground.setBackgroundResource(R.drawable.bg_fine_day);
			} else {
				weatherBackground.setBackgroundResource(R.drawable.bg_fine_day);
			}
		} else {
			weatherBackground.setBackgroundResource(R.drawable.bg_night);
		}
		//

		//currentWeather
		switch(Integer.parseInt(currentWeather.getTemp())/10) {
		case 0: temperatureDecade.setImageResource(R.drawable.nw0);
			break;
		case 1: temperatureDecade.setImageResource(R.drawable.nw1);
			break;
		case 2: temperatureDecade.setImageResource(R.drawable.nw2);
			break;
		case 3: temperatureDecade.setImageResource(R.drawable.nw3);
			break;
		case 4: temperatureDecade.setImageResource(R.drawable.nw4);
			break;
		}
		switch(Integer.parseInt(currentWeather.getTemp())%10) {
		case 0: temperatureUnit.setImageResource(R.drawable.nw0);
			break;
		case 1: temperatureUnit.setImageResource(R.drawable.nw1);
			break;
		case 2: temperatureUnit.setImageResource(R.drawable.nw2);
			break;
		case 3: temperatureUnit.setImageResource(R.drawable.nw3);
			break;
		case 4: temperatureUnit.setImageResource(R.drawable.nw4);
			break;
		case 5: temperatureUnit.setImageResource(R.drawable.nw5);
			break;
		case 6: temperatureUnit.setImageResource(R.drawable.nw6);
			break;
		case 7: temperatureUnit.setImageResource(R.drawable.nw7);
			break;
		case 8: temperatureUnit.setImageResource(R.drawable.nw8);
			break;
		case 9: temperatureUnit.setImageResource(R.drawable.nw9);
			break;
		}
		windDirection.setText("风向：" + currentWeather.getWindDirection());
		windStrength.setText("风力：" + currentWeather.getWindStrength());
		humidity.setText("空气湿度：" + currentWeather.getHumidity());
		time.setText("发布时间：" + currentWeather.getTime());
		//

		//futureWeather
		firstPre.setText(futureWeatherList.get(0).getWeather());
		setWeatherType(futureWeatherList.get(0).getWeather(), firstImage);
		firstNext.setText(futureWeatherList.get(0).getTemperature()
				+ "\n" + futureWeatherList.get(0).getWind());

		secondPre.setText(futureWeatherList.get(1).getWeather());
		setWeatherType(futureWeatherList.get(1).getWeather(), secondImage);
		secondNext.setText(futureWeatherList.get(1).getTemperature()
				+ "\n" + futureWeatherList.get(1).getWind());

		threeWeek.setText(futureWeatherList.get(2).getWeek());
		threePre.setText(futureWeatherList.get(2).getWeather());
		setWeatherType(futureWeatherList.get(2).getWeather(), threeImage);
		threeNext.setText(futureWeatherList.get(2).getTemperature()
				+ "\n" + futureWeatherList.get(2).getWind());

		fourWeek.setText(futureWeatherList.get(3).getWeek());
		fourPre.setText(futureWeatherList.get(3).getWeather());
		setWeatherType(futureWeatherList.get(3).getWeather(), fourImage);
		fourNext.setText(futureWeatherList.get(3).getTemperature()
				+ "\n" + futureWeatherList.get(3).getWind());

		fiveWeek.setText(futureWeatherList.get(4).getWeek());
		fivePre.setText(futureWeatherList.get(4).getWeather());
		setWeatherType(futureWeatherList.get(4).getWeather(), fiveImage);
		fiveNext.setText(futureWeatherList.get(4).getTemperature()
				+ "\n" + futureWeatherList.get(4).getWind());
		//

		//六种指数
		/*chuanYiText.setText(todayWeather.getDressingIndex());
		ziWaiXianText.setText(todayWeather.getUvIndex());
		xiCheText.setText(todayWeather.getWashIndex());
		try {
			lvYouText.setText(wuran.get(0).toString());
		} catch(Exception e) {
			e.printStackTrace();
		}
		yunDongText.setText(todayWeather.getExerciseIndex());
		try {
			ganZhaoText.setText(kongtiao.get(0).toString());
		} catch(Exception e) {
			e.printStackTrace();
		}*/
		//
	}

	//set future weather image type.
	private void setWeatherType(String weatherType, ImageView imageView) {
		int weatherTypeInt;
		if(weatherType.contains("雨")) {
			weatherTypeInt = RAIN;
		} else if(weatherType.contains("雪")) {
			weatherTypeInt = SNOW;
		} else if(weatherType.contains("雾")) {
			weatherTypeInt = FOG;
		} else if(weatherType.contains("沙") || weatherType.contains("尘")) {
			weatherTypeInt = SAND;
		} else if(weatherType.contains("阴") || weatherType.contains("云")) {
			weatherTypeInt = CLOUD;
		} else if(weatherType.contains("晴")) {
			weatherTypeInt = SUN;
		} else {
			weatherTypeInt = 7;
		}

		switch(weatherTypeInt) {
		case RAIN : imageView.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
			break;
		case SNOW : imageView.setImageResource(R.drawable.biz_plugin_weather_daxue);
			break;
		case FOG : imageView.setImageResource(R.drawable.biz_plugin_weather_wu);
			break;
		case SAND : imageView.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
			break;
		case CLOUD : imageView.setImageResource(R.drawable.biz_plugin_weather_duoyun);
			break;
		case SUN : imageView.setImageResource(R.drawable.biz_plugin_weather_qing);
			break;
		default : imageView.setImageResource(R.drawable.biz_plugin_weather_qing);
			break;
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		swipeLayout.setRefreshing(true);
		handler.removeCallbacks(task);
		//loading.show();
		handler.postDelayed(task, 10000);
		getWeatherCondition();
	}
}
