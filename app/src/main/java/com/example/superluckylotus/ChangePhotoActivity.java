package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * @version: 2.0
 * @author: 黄诗雯
 * @className: ChangePhotoActivity
 * @packageName:com.example.superluckylotus
 * @description: 更改头像界面
 * @data: 2020.07.14 16:38
 **/
public class ChangePhotoActivity extends AppCompatActivity {

    ImageButton mbacktosetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_photo);
        mbacktosetting=(ImageButton)findViewById(R.id.backtosettting);
        setListeners();
    }
    private void setListeners(){
        ChangePhotoActivity.OnClick onClick = new ChangePhotoActivity.OnClick();
        mbacktosetting.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.backtosettting:
                    intent = new Intent(ChangePhotoActivity.this,SettingActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}