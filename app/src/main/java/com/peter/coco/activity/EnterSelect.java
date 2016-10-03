package com.peter.coco.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.peter.coco.R;
import com.peter.coco.db.CoCoWeatherDB;
import com.peter.coco.model.City;
import com.peter.coco.model.County;
import com.peter.coco.model.Province;

/**
 * @Title: EnterSelect.java
 * @Package com.peter.activity
 * @Description: TODO
 * @author: PeterChen E-mail:neu20133788@163.com
 * @date: 2016-8-5 上午10:32:43
 * @version V1.0
 */
public class EnterSelect extends Activity {
	//enter_select.xml
	private Button back;
	private EditText etSearch;
	private Button btnSearch;
	private ImageView ivDeleteText;
	private ListView listView;
	private String stringTem = "Test";
	//

	//
	private List<String> list;
	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;
	private ArrayAdapter<String> adapter;
	private CoCoWeatherDB coCoWeatherDB;
	private SharedPreferences pref;
	private int currentLevel = -1;
	private static final int PROVINCE_LEVEL = 1;
	private static final int CITY_LEVEL = 2;
	private static final int COUNTY_LEVEL = 3;
	private String provinceName;
	private String cityName;
	private String countyName;
	//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.enter_select);

		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				(EnterSelect.this).finish();
			}
		});
		ivDeleteText = (ImageView) findViewById(R.id.iv_delete_text);
		ivDeleteText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etSearch.setText("");
			}
		});
		etSearch = (EditText) findViewById(R.id.et_search);
		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() == 0) {
					ivDeleteText.setVisibility(View.GONE);
				} else {
					ivDeleteText.setVisibility(View.VISIBLE);
				}
			}
		});
		btnSearch = (Button) findViewById(R.id.btn_search);
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String searchText = etSearch.getText().toString();
				County county = coCoWeatherDB.getCounty(searchText);
				City city = coCoWeatherDB.getCity(searchText);
				Province province = coCoWeatherDB.getProvince(searchText);
				if (province != null) {
					synchronized (this) {
						list.clear();
						list.add(province.getProvinceName());
						adapter.notifyDataSetChanged();
						listView.setSelection(0);
						currentLevel = PROVINCE_LEVEL;
					}
				} else if (city != null) {
					synchronized (this) {
						list.clear();
						list.add(city.getCityName());
						adapter.notifyDataSetChanged();
						listView.setSelection(0);
						currentLevel = CITY_LEVEL;
					}
				} else if (county != null) {
					synchronized (this) {
						list.clear();
						list.add(county.getCountyName());
						adapter.notifyDataSetChanged();
						listView.setSelection(0);
						currentLevel = COUNTY_LEVEL;
					}
				} else {
					new AlertDialog.Builder(EnterSelect.this)
					.setTitle("系统提示")
					.setMessage("请输入准确的地点")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
								}
							}).show();
				}
				//etSearch.clearFocus();
			}
		});
		listView = (ListView) findViewById(R.id.list_view);

		list = new ArrayList<String>();
		provinceList = new ArrayList<Province>();
		cityList = new ArrayList<City>();
		countyList = new ArrayList<County>();
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);
		coCoWeatherDB = CoCoWeatherDB.getInstance(this);
		pref = getSharedPreferences("data", MODE_PRIVATE);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				switch(currentLevel) {
				case PROVINCE_LEVEL : {
					provinceName = list.get(position);
					showCitiesOfProvince();
				}
					break;
				case CITY_LEVEL : {
					cityName = list.get(position);
					showCountiesOfCity();
				}
					break;
				case COUNTY_LEVEL : {
					countyName = list.get(position);
					new AlertDialog.Builder(EnterSelect.this)
					.setTitle("系统提示")
					.setMessage("是否设置" + countyName + "为默认地点？")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							SharedPreferences.Editor editor = pref.edit();
							editor.putString("default_county_name", countyName);
							editor.putString("display_county_name", countyName);
							editor.commit();
							editor.clear();
							(EnterSelect.this).finish();
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					}).show();
				}
					break;
				default :
					break;
				}
			}

		});
		showProvinces();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.finish();
		super.onBackPressed();
	}

	private void showProvinces() {
		synchronized (this) {
			list.clear();
			provinceList = coCoWeatherDB.loadProvinces();
			for(Province province : provinceList) {
				list.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			currentLevel = PROVINCE_LEVEL;
		}
	}

	private synchronized void showCitiesOfProvince() {
		synchronized (this) {
			list.clear();
			cityList = coCoWeatherDB.loadCities(coCoWeatherDB.getProvinceId(provinceName));
			for(City city : cityList) {
				list.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			currentLevel = CITY_LEVEL;
		}
	}

	private synchronized void showCountiesOfCity() {
		synchronized (this) {
			list.clear();
			countyList = coCoWeatherDB.loadCounties(coCoWeatherDB.getCityId(cityName));
			for(County county : countyList) {
				list.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			currentLevel = COUNTY_LEVEL;
		}
	}

	 // 获取点击事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 判定是否需要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            return !(ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom);
        }
        return false;
    }

    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
