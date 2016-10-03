package com.peter.coco.util;

import com.peter.coco.model.CurrentWeather;
import com.peter.coco.model.District;
import com.peter.coco.model.FutureWeather;
import com.peter.coco.model.TodayWeather;
import com.peter.coco.model.WeatherType;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * @Title: Utility.java
 * @Package com.peter.util
 * @Description: TODO工具类，处理各种数据，目前只有处理JSON数据
 * @author: PeterChen E-mail:neu20133788@163.com
 * @date: 2016-8-4 上午10:56:20
 * @version V1.0
 */
public class Utility {

	/*
	 * 解析服务器返回的JSON数据
	 */

	//解析JSON中的sk项
	public static CurrentWeather getCurrentWeatherWithJSON(JSONObject resultObject) {
		CurrentWeather currentWeather = null;
		try {
			JSONObject result = resultObject.getJSONObject("result");
			JSONObject skObject = result.getJSONObject("sk");
			currentWeather = new CurrentWeather();
			currentWeather.setTemp(skObject.getString("temp"));
			currentWeather.setWindDirection(skObject.getString("wind_direction"));
			currentWeather.setWindStrength(skObject.getString("wind_strength"));
			currentWeather.setHumidity(skObject.getString("humidity"));
			currentWeather.setTime(skObject.getString("time"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return currentWeather;
	}

	//解析JSON中的today 项
	public static TodayWeather getTodayWeatherWithJSON(JSONObject resultObject) {
		TodayWeather todayWeather = null;
		try {
			JSONObject result = resultObject.getJSONObject("result");
			JSONObject todayObject = result.getJSONObject("today");
			todayWeather = new TodayWeather();
			todayWeather.setCity(todayObject.getString("city"));
			todayWeather.setDate_y(todayObject.getString("date_y"));
			todayWeather.setWeek(todayObject.getString("week"));
			todayWeather.setTemperature(todayObject.getString("temperature"));
			todayWeather.setWeather(todayObject.getString("weather"));
			JSONObject weratherId = todayObject.getJSONObject("weather_id");
			todayWeather.setFa(weratherId.getString("fa"));
			todayWeather.setFb(weratherId.getString("fb"));
			todayWeather.setWind(todayObject.getString("wind"));
			todayWeather.setDressingIndex(todayObject.getString("dressing_index"));
			todayWeather.setDressingAdvice(todayObject.getString("dressing_advice"));
			todayWeather.setUvIndex(todayObject.getString("uv_index"));
			todayWeather.setComfortIndex(todayObject.getString("comfort_index"));
			todayWeather.setWashIndex(todayObject.getString("wash_index"));
			todayWeather.setTravelIndex(todayObject.getString("travel_index"));
			todayWeather.setExerciseIndex(todayObject.getString("exercise_index"));
			todayWeather.setDryingIndex(todayObject.getString("drying_index"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return todayWeather;
	}

	//解析JSON中的future 项
	public static List<FutureWeather> getFutureWeatherWithJSON(JSONObject resultObject) {
		List<FutureWeather> list = null;
		try {
			JSONObject result = resultObject.getJSONObject("result");
			JSONArray arr = result.getJSONArray("future");
			list = new ArrayList<FutureWeather>();
			for(int i = 0; i <arr.length(); i++) {
				JSONObject temp = (JSONObject) arr.get(i);
				FutureWeather future = new FutureWeather();
				future.setTemperature(temp.getString("temperature"));
				future.setWeather(temp.getString("weather"));
				JSONObject weratherId = temp.getJSONObject("weather_id");
				future.setFa(weratherId.getString("fa"));
				future.setFb(weratherId.getString("fb"));
				future.setWind(temp.getString("wind"));
				future.setWeek(temp.getString("week"));
				future.setDate(temp.getString("date"));
				list.add(future);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	//解析从聚合返回的天气类型JSON
	public static List<WeatherType> getWeatherTypeWithJSON(JSONArray resultArray) {
		List<WeatherType> list = null;
		try {
			list = new ArrayList<WeatherType>();
			for(int i = 0; i < resultArray.length(); i++) {
				JSONObject temp = (JSONObject) resultArray.get(i);
				WeatherType weatherType = new WeatherType();
				weatherType.setWid(temp.getString("wid"));
				weatherType.setWeather(temp.getString("weather"));
				list.add(weatherType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	//解析从聚合返回的全国县级地区信息
	public static List<District> getDistrictWithJSON(JSONArray resultArray) {
		List<District> list = null;
		try {
			list = new ArrayList<District>();
			for(int i = 0; i < resultArray.length(); i++) {
				JSONObject temp = (JSONObject) resultArray.get(i);
				District district = new District();
				district.setDistrictId(temp.getString("id"));
				district.setDistrictProvince(temp.getString("province"));
				district.setDistrictCity(temp.getString("city"));
				district.setDistrictDistrict(temp.getString("district"));
				list.add(district);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
