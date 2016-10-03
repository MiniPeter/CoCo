package com.peter.coco.model;
/**
 * @Title: Weather.java
 * @Package com.peter.model
 * @Description: TODO对应数据库的天气类型，来源于聚合数据
 * @author: PeterChen E-mail:neu20133788@163.com
 * @date: 2016-7-31 下午9:35:01
 * @version V1.0
 */
public class WeatherType {
	//sql key id
	private int id;
	//类型编码
	private String wid;
	//类型名称
	private String weather;

	public WeatherType() {
		id = -1;
		wid = "null";
		weather = "null";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWid() {
		return wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
}
