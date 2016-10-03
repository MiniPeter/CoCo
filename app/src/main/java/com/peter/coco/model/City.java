package com.peter.coco.model;
/**
 * @Title: City.java
 * @Package com.peter.model
 * @Description: TODO对应数据库的City表
 * @author: PeterChen E-mail:neu20133788@163.com
 * @date: 2016-7-31 下午3:54:49
 * @version V1.0
 */
public class City {
	//数据库键id
	private int id;
	//City name
	private String cityName;
	//城市所属省份的数据库键id
	private int provinceId;

	public City() {
		id = -1;
		cityName = "null";
		provinceId = -1;
	}
	public int getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
}
