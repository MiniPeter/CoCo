package com.peter.coco.model;
/**
 * @Title: FutuerWeather.java
 * @Package com.peter.model
 * @Description: TODO聚合数据天气数据中未来一星期的某一天天气情况
 * @author: PeterChen E-mail:neu20133788@163.com
 * @date: 2016-7-31 下午9:54:24
 * @version V1.0
 */
public class FutureWeather {
	//温度
	private String temperature;
	//天气类型
	private String weather;
	//天气类型所属编码
	private String fa;
	//天气类型所属编码，fa与fb不一致时表明是组合天气
	private String fb;
	//风向
	private String wind;
	//星期几
	private String week;
	//日期
	private String date;

	public FutureWeather() {
		temperature = "null";
		weather = "null";
		fa = "null";
		fb = "null";
		wind = "null";
		week = "null";
		date = "null";
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getFa() {
		return fa;
	}
	public void setFa(String fa) {
		this.fa = fa;
	}
	public String getFb() {
		return fb;
	}
	public void setFb(String fb) {
		this.fb = fb;
	}
	public String getWind() {
		return wind;
	}
	public void setWind(String wind) {
		this.wind = wind;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
