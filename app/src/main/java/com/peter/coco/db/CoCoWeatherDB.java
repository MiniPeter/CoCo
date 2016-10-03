package com.peter.coco.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.peter.coco.model.City;
import com.peter.coco.model.County;
import com.peter.coco.model.District;
import com.peter.coco.model.Province;
import com.peter.coco.model.WeatherType;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: CoCoWeatherDB.java
 * @Package com.peter.db
 * @Description: TODO封装常用数据库操作
 * @author: PeterChen E-mail:neu20133788@163.com
 * @date: 2016-8-4 下午5:45:37
 * @version V1.0
 */
public class CoCoWeatherDB {
	//sql name
	public static final String DB_NAME = "cool_weather.db";
	//sql version
	public static final int VERSION = 1;
	//CoCoWeatherDB object
	private static CoCoWeatherDB coCoWeatherDB;
	//SQLiteDatabase object
	private SQLiteDatabase db;

	public SQLiteDatabase getSQLiteDatabase() {
		// TODO Auto-generated method stub
		return db;
	}
	//构造函数私有化，保证全局只有一个数据库操作接口
	private CoCoWeatherDB(Context context) {
		//context应用程序环境的信息，即上下文，我们通过它访问资源和类，相当于app的管家
		//第三个参数允许我们在查询数据的时候返回一个自定义的 Cursor，一般都是传入 null
		CoCoWeatherOpenHelper dbHelper = new CoCoWeatherOpenHelper(context, DB_NAME, null, VERSION);
		//返回一个对数据库读写的对象
		db = dbHelper.getWritableDatabase();
	}

	//synchronzed 修饰时，同一时间只有一个CoCoWeatherDB访问这个方法
	public synchronized static CoCoWeatherDB getInstance(Context context) {
		if(coCoWeatherDB == null) {
			coCoWeatherDB = new CoCoWeatherDB(context);
		}
		return coCoWeatherDB;
	}

	//insert District value to sql
	public void saveDistrict(District district) {
		if(district != null) {
			ContentValues values = new ContentValues();
			values.put("district_id", district.getDistrictId());
			values.put("district_province", district.getDistrictProvince());
			values.put("district_city", district.getDistrictCity());
			values.put("district_district", district.getDistrictDistrict());
			db.insert("District", null, values);
		}
	}

	//load District list from sql
	public List<District> loadDistrict() {
		List<District> list = new ArrayList<District>();
		Cursor cursor = db.query("District", null, null, null, null, null, null);
		if(cursor.moveToFirst()) {
			do {
				District district = new District();
				district.setId(cursor.getInt(cursor.getColumnIndex("id")));
				district.setDistrictId(cursor.getString(cursor
						.getColumnIndex("district_id")));
				district.setDistrictProvince(cursor.getString(cursor
						.getColumnIndex("district_province")));
				district.setDistrictCity(cursor.getString(cursor
						.getColumnIndex("district_city")));
				district.setDistrictDistrict(cursor.getString(cursor
						.getColumnIndex("district_district")));
				list.add(district);
			}while(cursor.moveToNext());
		}
		if(!cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	//insert province into sql
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			db.insert("Province", null, values);
		}
	}

	//search province friom sql
	public Province getProvince(String provinceName) {
		Province province = null;
		Cursor cursor = db.query("Province", null, "province_name = ?",
				new String[] { provinceName }, null, null, null);
		if (cursor.moveToFirst()) {
			province = new Province();
			province.setId(cursor.getInt(cursor.getColumnIndex("id")));
			province.setProvinceName(provinceName);
		}
		if (!cursor.isClosed()) {
			cursor.close();
		}
		return province;
	}

	//load province list from sql
	public List<Province> loadProvinces() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor
						.getColumnIndex("province_name")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		if(!cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	//对于某一个省的名称，得到数据库中该省存储Id
	public int getProvinceId(String provinceName) {
		int provinceId = -1;
		Cursor cursor = db.query("Province", null, "province_name = ?", new String[]
				{ provinceName }, null, null, null);
		if(cursor.moveToFirst()) {
			provinceId = cursor.getInt(cursor.getColumnIndex("id"));
		}
		if(!cursor.isClosed()) {
			cursor.close();
		}
		return provinceId;
	}

	//insert city into sql
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}

	//search city from sql
	public City getCity(String cityName) {
		City city = null;
		Cursor cursor = db.query("City", null, "city_name = ?",
				new String[] { cityName }, null, null, null);
		if (cursor.moveToFirst()) {
			city = new City();
			city.setId(cursor.getInt(cursor.getColumnIndex("id")));
			city.setCityName(cityName);
			city.setProvinceId(cursor.getInt(cursor
					.getColumnIndex("province_id")));
		}
		if (!cursor.isClosed()) {
			cursor.close();
		}
		return city;
	}

	//load city list from sql
	public List<City> loadCities(int provinceId) {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?",
				new String[] { String.valueOf(provinceId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor
						.getColumnIndex("city_name")));
				city.setProvinceId(provinceId);
				list.add(city);
			} while (cursor.moveToNext());
		}
		if(!cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	//输入一个城市名称，得到它在数据库中存储的id
	public int getCityId(String cityName) {
		int cityId = -1;
		Cursor cursor = db.query("City", null, "city_name = ?", new String[]
				{ cityName }, null, null, null);
		if(cursor.moveToFirst()) {
			cityId = cursor.getInt(cursor.getColumnIndex("id"));;
		}
		if(!cursor.isClosed()) {
			cursor.close();
		}
		return cityId;
	}

	//insert county into sql
	public void saveCounty(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("city_id", county.getCityId());
			values.put("district_id", county.getDistrictId());
			db.insert("County", null, values);
		}
	}

	//search couonty from sql
	public County getCounty(String countyName) {
		County county = null;
		Cursor cursor = db.query("County", null, "county_name = ?",
				new String[] { countyName }, null, null, null);
		if (cursor.moveToFirst()) {
			county = new County();
			county.setId(cursor.getInt(cursor.getColumnIndex("id")));
			county.setCountyName(countyName);
			county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
			county.setDistrictId(cursor.getInt(cursor
					.getColumnIndex("district_id")));
		}
		if (!cursor.isClosed()) {
			cursor.close();
		}
		return county;
	}

	//load county list from sql
	public List<County> loadCounties(int cityId) {
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?",
				new String[] { String.valueOf(cityId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor
						.getColumnIndex("county_name")));
				county.setDistrictId(cursor.getInt(cursor
						.getColumnIndex("district_id")));
				county.setCityId(cityId);
				list.add(county);
			} while (cursor.moveToNext());
		}
		if(!cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	//insert weather into sql
	public void saveWeather(WeatherType weather) {
		if (weather !=null) {
			ContentValues values = new ContentValues();
			values.put("wid", weather.getWid());
			values.put("weather", weather.getWeather());
			db.insert("Weather", null, values);
		}
	}

	//load weatherType list from sql
	public List<WeatherType> loadWeather() {
		List<WeatherType> list = new ArrayList<WeatherType>();
		Cursor cursor = db.query("Weather", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				WeatherType weather = new WeatherType();
				weather.setWid(cursor.getString(cursor.getColumnIndex("wid")));
				weather.setWeather(cursor.getString(cursor.getColumnIndex("weather")));
				list.add(weather);
			} while (cursor.moveToNext());
		}
		if(!cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}
}
