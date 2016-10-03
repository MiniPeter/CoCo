package com.peter.coco.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Title: HttpUtil.java
 * @Package com.peter.util
 * @Description: TODO网络请求工具类
 * @author: PeterChen E-mail:neu20133788@163.com
 * @date: 2016-8-4 上午10:21:26
 * @version V1.0
 */
/*
 * @param address:网络请求的地址
 */
public class HttpUtil {
	public static void sendHttpRequest(final String address,
			final HttpCallbackListence listence) {
		//网络请求是耗时任务，放在线程中
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try {
					//set network address
					URL url = new URL(address);
					//open network connection
					connection = (HttpURLConnection) url.openConnection();
					//using GET method
					connection.setRequestMethod("GET");
					//8秒连接限时
					connection.setConnectTimeout(8000);
					//8秒读取限时
					connection.setReadTimeout(8000);
					//get input stream
					InputStream in = connection.getInputStream();
					//封装输入流
					BufferedReader reader = new BufferedReader(new
							InputStreamReader(in));
					//string builder
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					if (listence !=null) {
						//callback onFinish();
						listence.onFinish(response.toString());
					}
				} catch (Exception e) {
					if (listence != null) {
						//Error, callback OnError();
						listence.onError(e);
					}
				} finally {
					//close connection.
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}
