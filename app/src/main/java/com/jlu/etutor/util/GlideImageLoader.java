package com.jlu.etutor.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by 程杰 on 2018/2/8.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class GlideImageLoader extends ImageLoader {

    public GlideImageLoader(){
    }

    /**
     *
     * @param context 活动上下文
     * @param path     图片路径
     * @param imageView 图片显示控件
     */
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        Glide.with(context.getApplicationContext())
                .load(path)
                .into(imageView);
    }

}
