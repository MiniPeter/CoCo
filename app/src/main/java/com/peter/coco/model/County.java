package com.peter.coco.model;
/**
 * @Title: County.java
 * @Package com.peter.model
 * @Description: TODO对应城市下的县、区
 * @author: PeterChen E-mail:neu20133788@163.com
 * @date: 2016-7-31 下午4:23:11
 * @version V1.0
 */
public class County {
	//Sql key id
	private int id;
	//County name
	private String countyName;
	//县所属的城市在数据库中的键id
	private int cityId;
	//县所属的district在数据库的键id
	private int districtId;

	public County() {
		id = -1;
		countyName = "null";
		cityId = -1;
		districtId = -1;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public int getDistrictId() {
		return districtId;
	}
	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}
}
