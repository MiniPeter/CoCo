package com.peter.coco.model;
/**
 * @Title: CurrentWeather.java
 * @Package com.peter.model
 * @Description: TODO对应聚合天气预报网站数据的sk:JSONObject，也就是当前实况天气
 * @author: PeterChen E-mail:neu20133788@163.com
 * @date: 2016-8-1 下午9:48:06
 * @version V1.0
 */
public class CurrentWeather {
	//当前温度
	private String temp;
	//当前风向
	private String windDirection;
	//当前风力
	private String windStrength;
	//当前湿度
	private String humidity;
	//更新时间
	private String time;

	public CurrentWeather() {
		temp = "null";
		windDirection = "null";
		windStrength = "null";
		humidity = "null";
		time = "null";
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getWindDirection() {
		return windDirection;
	}
	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}
	public String getWindStrength() {
		return windStrength;
	}
	public void setWindStrength(String windStrength) {
		this.windStrength = windStrength;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
