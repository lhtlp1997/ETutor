package com.jlu.etutor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jlu.etutor.R;

/**
 * Created by 程杰 on 2018/2/7.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class PersonalFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.personal,container,false);
    }
}
