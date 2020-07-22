package com.example.superluckylotus;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

//import com.example.superluckylotus.MainActivity.MyLocationListener;



/**
 * @version: 1.0
 * @author: 黄诗雯
 * @className: NearFragment
 * @packageName:com.example.superluckylotus
 * @description: 附近界面
 * @data: 2020.07.11 22:14
 **/

/**
 * @version: 2.0
 * @author: 黄诗雯
 * @className: NearFragment
 * @packageName:com.example.superluckylotus
 * @description: 更多弹窗和评论弹窗
 * @data: 2020.07.17 15:29
 **/

/**
 * @version: 3.0
 * @author: 宋佳容
 * @className: NearFragment
 * @packageName:com.example.superluckylotus
 * @description: 修改打赏功能，增加分享弹窗
 * @data: 2020.07.20 18:19
 **/

/**
 * @version: 4.0
 * @author: 张贝琦
 * @className: NearFragment
 * @packageName:com.example.superluckylotus
 * @description: 附近界面
 * @data: 2020.07.20 20:14
 **/

public class NearFragment extends Fragment {

    private Button mMore;
    private Button mComment;
    private  Button mShare;
    MoreDialog md;
    CommentDialog cd;
    ShareDialog sd;

    private RecyclerView recyclerView;

    private DouYinLayoutManager douYinLayoutManager;


        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_near,null);

            md=new MoreDialog(getActivity());
            cd=new CommentDialog(getActivity());
            sd=new ShareDialog(getActivity());
            NearFragment.OnClick onclick=new NearFragment.OnClick();
            recyclerView = view.findViewById(R.id.near_recyclerView_dy);
            initView();
            return view;
        }


    private void initView() {
        douYinLayoutManager = new DouYinLayoutManager(getActivity(), OrientationHelper.VERTICAL,false);
        recyclerView.setLayoutManager(douYinLayoutManager);
        recyclerView.setAdapter(new MyAdapter());

        douYinLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onPageRelease(boolean isNest, View position) {
                releaseVideo(position);

            }

            @Override
            public void onPageSelected(boolean isButten, View position) {
                int index = 0;
                if (isButten){
                    index = 0;
                }else {
                    index = 1;
                }
                playVideo(position);
            }
        });


       /* MainActivity mainActivity = new MainActivity();
        BDLocation bdLocation = new BDLocation();
        mainActivity.myLocationListener.initLocation();
        mainActivity.myLocationListener.onReceiveLocation(bdLocation);*/
    }



    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        //添加封面
        private int[] imgs = {R.mipmap.img_video_1,R.mipmap.img_video_2,R.mipmap.img_video_1,R.mipmap.img_video_2,R.mipmap.img_video_1,R.mipmap.img_video_2};
        //添加视频
        private int[] videos = {R.raw.video_1,R.raw.video_2,R.raw.video_1,R.raw.video_2,R.raw.video_1,R.raw.video_2};
        public MyAdapter(){
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video,parent,false);
            mMore=view.findViewById(R.id.video_more);
            mComment=view.findViewById(R.id.video_comment);
            mShare=view.findViewById(R.id.video_share);
            mMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    md.popupWindowDialog(view);
                }
            });
            mComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cd.popupWindowDialog(view);
                }
            });
            mShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sd.popupWindowDialog(view);
                }
            });

            return new MyAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.img_thumb.setImageResource(imgs[position]);
            holder.videoView.setVideoURI(Uri.parse("android.resource://"+getActivity().getPackageName()+"/"+ videos[position]));
        }

        @Override
        public int getItemCount() {
            return 6;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            ImageView img_thumb;
            VideoView videoView;
            ImageView img_play;
            RelativeLayout rootView;
            public ViewHolder(View itemView) {
                super(itemView);
                img_thumb = itemView.findViewById(R.id.img_thumb);
                videoView = itemView.findViewById(R.id.video_view);
                img_play = itemView.findViewById(R.id.img_play);
                rootView = itemView.findViewById(R.id.root_view);
            }
        }
    }


    /**
     * 停止播放
     * @param itemView
     */
    private void releaseVideo(View  itemView){
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        videoView.stopPlayback();
        imgThumb.animate().alpha(1).start();
        imgPlay.animate().alpha(0f).start();
    }

    /**
     * 开始播放
     * @param itemView
     */

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void playVideo(View itemView) {
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final RelativeLayout rootView = itemView.findViewById(R.id.root_view);
        final MediaPlayer[] mediaPlayer = new MediaPlayer[1];
        videoView.start();
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                mediaPlayer[0] = mp;
                mp.setLooping(true);
                imgThumb.animate().alpha(0).setDuration(200).start();
                return false;
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });


        imgPlay.setOnClickListener(new View.OnClickListener() {
            boolean isPlaying = true;
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()){
                    imgPlay.animate().alpha(1f).start();
                    videoView.pause();
                    isPlaying = false;
                }else {
                    imgPlay.animate().alpha(0f).start();
                    videoView.start();
                    isPlaying = true;
                }
            }
        });
    }


    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intent = new Intent();
            switch (v.getId()){

            }
        }
    }



/*//利用百度地图API获取定位，显示所在城市名
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
                option.setScanSpan(0);

                LocationClient locationClient = new LocationClient(getActivity().getApplicationContext());
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

        private void initLocation(){
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

            LocationClient locationClient = new LocationClient(getActivity().getApplicationContext());
            locationClient.setLocOption(option);
        }


    }*/


}