package com.peter.coco.model;
/**
 * @Title: Province.java
 * @Package com.peter.model
 * @Description: TODO对应数据库的全国省表
 * @author: PeterChen E-mail:neu20133788@163.com
 * @date: 2016-7-31 下午4:21:57
 * @version V1.0
 */
public class Province {
	//sql 中省的键id
	private int id;
	//省名称
	private String provinceName;

	public Province() {
		id = -1;
		provinceName = "null";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
}
