package com.peter.coco.util;
/**
 * @Title: HttpCallBackListence.java
 * @Package com.peter.util
 * @Description: TODO应用于网络请求线程回调
 * @author: PeterChen E-mail:neu20133788@163.com
 * @date: 2016-8-4 上午10:16:46
 * @version V1.0
 */
public interface HttpCallbackListence {
	//网络请求成功响应时调用
	void onFinish(String response);
	//网络请求错误时调用
	void onError(Exception e);
}
