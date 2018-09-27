package com.jlu.etutor.adpter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.jlu.etutor.R;
import com.jlu.etutor.gson.TeacherInfo;
import com.jlu.etutor.util.Server;

import java.util.List;

/**
 * Created by 程杰 on 2018/2/8.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class TeaInfoAdapter extends ArrayAdapter {


    private final int resId;

    /**
     * 构造函数
     * @param context   上下文
     * @param resource  每一个项目的布局
     * @param list      每一个对象的数组
     */
    public TeaInfoAdapter(@NonNull Context context, int resource, List<TeacherInfo> list) {
        super(context, resource,list);
        resId=resource;
    }

    /**
     *  覆写getView函数来自定义ListView显示方式
     * @param position  处于列表第position个
     * @param convertView  我也不知道这是干嘛的。。。
     * @param parent    这个我也不知道是干嘛的。。。
     * @return View
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resId, null);
        }
        TeacherInfo info=(TeacherInfo)getItem(position);
        TextView name=convertView.findViewById(R.id.name);
        TextView college=convertView.findViewById(R.id.college);
        TextView major=convertView.findViewById(R.id.major);
        TextView tag=convertView.findViewById(R.id.tag);
        ImageView imageView=convertView.findViewById(R.id.header);
        Glide.with(getContext()).load(Server.getURL()+"image/"+info.getPhone())
                .signature(new StringSignature(String.valueOf(Server.mills))) .into(imageView);
        name.setText(info.getName());
        college.setText(info.getCollege());
        major.setText(info.getMajor());
        tag.setText(info.getTag());
        return convertView;
    }
}
