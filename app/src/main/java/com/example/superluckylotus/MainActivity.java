package com.example.superluckylotus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import android.util.Log;

/**
 * @version: 1.0
 * @author: 宋佳容
 * @className: MainFragment
 * @packageName:com.example.superluckylotus
 * @description: 主界面
 * @data: 2020.07.11 21:12
 **/

public class MainActivity extends AppCompatActivity  {

    private RadioGroup mTabRadioGroup;
    private MeFragment meFragment;
    private EarthFragment earthFragment;
    private NearFragment nearFragment;
    private NoticeFragment noticeFragment;

    private FragmentManager fm;


    private Button near_Btn;
    private Button notice_Btn;
    private Button me_Btn;
    private Button shoot_Btn;
    private Button earth_Btn;

    public  LocationClient locationClient = null;
    public MyLocationListener myLocationListener = new MyLocationListener();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //没有标题栏
        setContentView(R.layout.activity_main);
        shoot_Btn = (Button)findViewById(R.id.btn_addshoot);
        setListeners();

        //实例化EarthFragment
        earthFragment = new EarthFragment();
        //把EarthFragment添加到Avtivity中
        getFragmentManager().beginTransaction().add(R.id.fragment_container,earthFragment).commitAllowingStateLoss();

        earth_Btn = (Button)findViewById(R.id.earth_tab);
        earth_Btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(earthFragment == null)
                {
                    earthFragment = new EarthFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,earthFragment).commitAllowingStateLoss();
            }
        });

        near_Btn = (Button)findViewById(R.id.near_tab);
        near_Btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(nearFragment == null)
                {
                    nearFragment = new NearFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,nearFragment).commitAllowingStateLoss();
            }
        });

        notice_Btn = (Button)findViewById(R.id.notice_tab);
        notice_Btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(noticeFragment == null)
                {
                    noticeFragment = new NoticeFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,noticeFragment).commitAllowingStateLoss();
            }
        });

        me_Btn = (Button)findViewById(R.id.me_tab);
        me_Btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(meFragment == null)
                {
                    meFragment = new MeFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,meFragment).commitAllowingStateLoss();
            }
        });


        //声明LocationClient类
        locationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        locationClient.registerLocationListener(myLocationListener);

        BDLocation bdLocation = new BDLocation();
        locationClient.start();
       // myLocationListener.initLocation();
        myLocationListener.onReceiveLocation(bdLocation);
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        shoot_Btn.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.btn_addshoot:
                    intent = new Intent(MainActivity.this,ShootActivity.class);
                    startActivity(intent);
                    break;
            }

        }
    }


    //利用百度地图API获取定位，显示所在城市名
    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            if(bdLocation!=null){
                LocationClientOption option = new LocationClientOption();
                //设置需要地址信息
                option.setIsNeedAddress(true);
                //设置GPS打开
                option.setOpenGps(true);
                //设置定位模式设为高精度，定位模式可选高精度、低功耗、仅设备
                option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
                //设置返回的定位结果坐标系
                option.setCoorType("gcj02");
                //设置发起定位请求的时间间隔为0ms，即仅定位一次（每次进入附近页面就进行定位）
                option.setScanSpan(1000);

                LocationClient locationClient = new LocationClient(getApplicationContext());
                locationClient.setLocOption(option);




                //获取当前位置的经度
                double longitude = bdLocation.getLongitude();
                //获取当前位置的纬度
                double latitude = bdLocation.getLatitude();

                Log.i("Tag", "bdLocation.getAddStr()=" + bdLocation.getAddrStr());
                Log.i("TAG", "bdLocation.getCity=" + bdLocation.getCity());

                int i = bdLocation.getLocType();
            }
            else{
                Log.i("TAG","bdLocation" + "null");
            }
        }

        public void initLocation(){
            LocationClientOption option = new LocationClientOption();
            //设置需要地址信息
            option.setIsNeedAddress(true);
            //设置GPS打开
            option.setOpenGps(true);
            //设置定位模式设为高精度，定位模式可选高精度、低功耗、仅设备
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            //设置返回的定位结果坐标系
            option.setCoorType("gcj02");
            //设置发起定位请求的时间间隔为0ms，即仅定位一次（每次进入附近页面就进行定位）
            option.setScanSpan(0);


            locationClient.setLocOption(option);
        }


    }




}