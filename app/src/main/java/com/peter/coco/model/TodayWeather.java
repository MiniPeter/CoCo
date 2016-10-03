package com.peter.coco.model;
/**
 * @Title: TodayWeather.java
 * @Package com.peter.model
 * @Description: TODO聚合数据中返回的今天天气情况
 * @author: PeterChen E-mail:neu20133788@163.com
 * @date: 2016-7-31 下午9:53:54
 * @version V1.0
 */
public class TodayWeather {
	//所在城市或者所在县，表明查询某个城市或者某个城市下的某个县
	private String city;
	//日期
	private String date_y;
	//星期几
	private String week;
	//今天温度
	private String temperature;
	//今天天气
	private String weather;
	//天气编码1
	private String fa;
	//天气编码2
	private String fb;
	//风向和风力
	private String wind;
	//穿衣指数
	private String dressingIndex;
	//穿着建议
	private String dressingAdvice;
	//紫外线强度
	private String uvIndex;
	//舒适度指数
	private String comfortIndex;
	//洗车指数
	private String washIndex;
	//旅游指数
	private String travelIndex;
	//晨练指数
	private String exerciseIndex;
	//干燥指数
	private String dryingIndex;

	public TodayWeather() {
		city = "null";
		date_y = "null";
		week = "null";
		temperature = "null";
		weather = "null";
		fa = "null";
		fb = "null";
		wind = "null";
		dressingIndex = "null";
		dressingAdvice = "null";
		uvIndex = "null";
		comfortIndex = "null";
		washIndex = "null";
		travelIndex = "null";
		exerciseIndex = "null";
		dryingIndex = "null";
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDate() {
		return date_y;
	}
	public void setDate_y(String date_y) {
		this.date_y = date_y;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
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
	public String getDressingIndex() {
		return dressingIndex;
	}
	public void setDressingIndex(String dressingIndex) {
		this.dressingIndex = dressingIndex;
	}
	public String getDressingAdvice() {
		return dressingAdvice;
	}
	public void setDressingAdvice(String dressingAdvice) {
		this.dressingAdvice = dressingAdvice;
	}
	public String getUvIndex() {
		return uvIndex;
	}
	public void setUvIndex(String uvIndex) {
		this.uvIndex = uvIndex;
	}
	public String getComfortIndex() {
		return comfortIndex;
	}
	public void setComfortIndex(String comfortIndex) {
		this.comfortIndex = comfortIndex;
	}
	public String getWashIndex() {
		return washIndex;
	}
	public void setWashIndex(String washIndex) {
		this.washIndex = washIndex;
	}
	public String getTravelIndex() {
		return travelIndex;
	}
	public void setTravelIndex(String travelIndex) {
		this.travelIndex = travelIndex;
	}
	public String getExerciseIndex() {
		return exerciseIndex;
	}
	public void setExerciseIndex(String exerciseIndex) {
		this.exerciseIndex = exerciseIndex;
	}
	public String getDryingIndex() {
		return dryingIndex;
	}
	public void setDryingIndex(String dryingIndex) {
		this.dryingIndex = dryingIndex;
	}
}
