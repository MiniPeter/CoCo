package com.peter.coco.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @Title: CoCoWeatherOpenHelper.java
 * @Package com.peter.db
 * @Description: TODO数据库帮助类，继承于SQLiteOpenHelper
 * @author: PeterChen E-mail:neu20133788@163.com
 * @date: 2016-8-4 下午5:33:12
 * @version V1.0
 */
public class CoCoWeatherOpenHelper extends SQLiteOpenHelper {
	// 网站返回城市数据JSON格式
	// {"id":"2","province":"北京","city":"北京","district":"海淀"}
	public static final String CREATE_DISTRICT = "create table District ("
			+ "id integer primary key autoincrement, "
			+ "district_id text, "
			+ "district_province text, "
			+ "district_city text, "
			+ "district_district text)";
	//Province information
	public static final String CREATE_PROVAINCE = "create table Province ("
			+ "id integer primary key autoincrement, "
			+ "province_name text)";
	//sql key id, city name, Province key id;
	public static final String CREATE_CITY = "create table City ("
			+ "id integer primary key autoincrement, "
			+ "city_name text, "
			+ "province_id integer)";
	//sql key id, county name, District key id, City key id.
	public static final String CREATE_COUNTY = "create table County ("
			+ "id integer primary key autoincrement, "
			+ "county_name text, "
			+ "district_id integer,"
			+ "city_id integer)";
	//sql key id, wid 天气类型编码，天气类型
	public static final String CREATE_WEATHER = "create table Weather ("
			+ "id integer primary key autoincrement, "
			+ "wid text, "
			+ "weather text)";

	public CoCoWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	//数据库初始化逻辑
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_DISTRICT);
		db.execSQL(CREATE_PROVAINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTY);
		db.execSQL(CREATE_WEATHER);
	}

	//后续版本更新数据库逻辑
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
