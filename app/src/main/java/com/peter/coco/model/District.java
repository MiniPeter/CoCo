package com.peter.coco.model;
/**
 * @Title: District.java
 * @Package com.peter.model
 * @Description: TODO对应数据库的District表，即从网站读取的全国市县目标
 * @author: PeterChen E-mail:neu20133788@163.com
 * @date: 2016-7-31 下午4:19:09
 * @version V1.0
 */
public class District {
	//sql key id
	private int id;
	//聚合网站中读取的JSONObject key
	private String districtId;
	//县所属省份
	private String districtProvince;
	//县所属城市
	private String districtCity;
	//县名称
	private String districtDistrict;

	public District() {
		id = -1;
		districtId = "null";
		districtProvince = "null";
		districtCity = "null";
		districtDistrict = "null";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDistrictId() {
		return districtId;
	}
	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}
	public String getDistrictProvince() {
		return districtProvince;
	}
	public void setDistrictProvince(String districtProvince) {
		this.districtProvince = districtProvince;
	}
	public String getDistrictCity() {
		return districtCity;
	}
	public void setDistrictCity(String districtCity) {
		this.districtCity = districtCity;
	}
	public String getDistrictDistrict() {
		return districtDistrict;
	}
	public void setDistrictDistrict(String districtDistrict) {
		this.districtDistrict = districtDistrict;
	}
}
