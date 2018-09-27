package com.jlu.etutor.adpter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jlu.etutor.R;

import java.util.List;

/**
 * Created by cj597 on 2018/2/21.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class QuestionAdapter extends ArrayAdapter {
    private int resId;
    public QuestionAdapter(@NonNull Context context, int resource, List<String> list) {
        super(context, resource,list);
        resId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resId, null);
        }
        String text=(String)getItem(position);
        TextView view=convertView.findViewById(R.id.text);
        view.setText(text);
        return convertView;
    }
}
