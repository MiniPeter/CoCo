package com.peter.coco.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.peter.coco.R;

/**
 * @Title: StartScreen.java
 * @Package com.peter.activity
 * @Description: TODO启动界面
 * @author: PeterChen E-mail:neu20133788@163.com
 * @date: 2016-8-4 下午9:03:53
 * @version V1.0
 */
public class StartScreen extends Activity
{
    //存放is_first and countyName;
    SharedPreferences pref;
    //延迟计数值
    private int countDown = 3;
    // 3秒启动界面延迟
    private final int START_SCREEN_DISPLAY_LENGHT = 3000; // 3秒启动界面延迟
    //跳过启动延迟
    private Button enterButton;
    //显示剩余延迟时间
    private TextView countDownText;
    //是否第一次启动
    private boolean isFirst;
    //默认显示天气地区
    //private String countyName;
    //处理线程句柄
    private final Handler handler = new Handler();
    //线程
    private final Runnable runable = new Runnable()
    {
        @Override
        public void run()
        {
            // TODO Auto-generated method stub
            if (isFirst == true)
            {
                Intent intent = new Intent(StartScreen.this, Loading.class);
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(StartScreen.this, Weather.class);
                startActivity(intent);
            }
            (StartScreen.this).finish();
        }
    };

    //定时器
    private Runnable task = new Runnable()
    {

        @Override
        public void run()
        {
            // TODO Auto-generated method stub
            if ( countDown > 0)
            {
                countDownText.setText("" + --countDown + "秒");
            }
            else
            {
                handler.removeCallbacks(task);
            }
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start_screen);
        pref = getSharedPreferences("data", MODE_PRIVATE);
        isFirst = pref.getBoolean("is_first", true);
        countDownText = (TextView) findViewById(R.id.count_down);
        enterButton = (Button) findViewById(R.id.enter);
        enterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                // 移除handler延迟加载里面的线程，就不会存在执行两次的情况
                handler.removeCallbacks(runable);
                if (isFirst == true)
                {
                    Intent intent = new Intent(StartScreen.this, Loading.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(StartScreen.this, Weather.class);
                    startActivity(intent);
                }
                (StartScreen.this).finish();
            }
        });
        //启动定时任务
        handler.postDelayed(task, 1000);
        //使用handler对象来定时启动线程运行
        handler.postDelayed(runable, START_SCREEN_DISPLAY_LENGHT);
    }

    @Override
    protected void onDestroy()
    {
        // TODO Auto-generated method stub
        handler.removeCallbacks(task);
        handler.removeCallbacks(task);
        super.onDestroy();
    }
}
