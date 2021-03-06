package org.viking.coolweather.db;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.viking.coolweather.model.City;
import org.viking.coolweather.model.County;
import org.viking.coolweather.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB 
{
	public static final String DB_NAME="cool_weather";//数据库名称
	public static final int DB_VERSION=1;//版本号
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;
	
	/*
	 * 将构造函数私有化，采用单例模式，防止多线程同时访问数据库
	 * */
	private CoolWeatherDB(Context context)
	{
		CoolWeatherOpenHelper openHelper=new CoolWeatherOpenHelper(context, DB_NAME, null, DB_VERSION);
		db=openHelper.getWritableDatabase();
	}
	
	/*
	 * 用此方法得到CoolWeatherDB对象
	 * */
	public synchronized static CoolWeatherDB getInstance(Context context)
	{
		if(coolWeatherDB==null)
		{
			coolWeatherDB=new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	/*
	 * 存储省份
	 * */
	public void saveProvince(Province province)
	{
		if(province!=null)
		{
			ContentValues values=new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("procince_code",province.getProvinceCode());
			db.insert("Province", null, values);
		}
		
	}
	
	/*
	 * 从数据库中读取省份信息
	 * */
	public List<Province> loadProvince()
	{
		List<Province> list=new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst())
		{
			do 
			{
				Province province=new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	/*
	 * 存储城市
	 * */
	public void saveCity(City city)
	{
		if (city!=null) 
		{
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);	
		}
		
	}
	
	/*
	 * 读取某省份下的所有城市
	 * */
	public List<City> loadCity(int provinceId)
	{
		List<City> list=new ArrayList<City>();
		Cursor cursor=db.query("City", null, "province_id=?", new String[]{String.valueOf(provinceId)}, null, null, null);
		if(cursor.moveToFirst())
		{
			do
			{
				City city=new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	/*
	 *将县城存入数据库
	 * */
	public void saveCounty(County county)
	{
		if(county!=null)
		{
			ContentValues values=new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			db.insert("City", null, values);
		}
	}
	
	/*
	 * 读取某城市下面的县城
	 * */
	public List<County> loadCounty(int cityId)
	{
		List<County> list=new ArrayList<County>();
		Cursor cursor=db.query("County", null, "city_id=?", new String[]{String.valueOf(cityId)},null, null,null);
		if (cursor.moveToFirst())
		{
			do 
			{
				County county=new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cityId);
				list.add(county);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
}
