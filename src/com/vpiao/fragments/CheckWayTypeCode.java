package com.vpiao.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.VpiaoAssistant.R;

/**
 * Created by TaoSun on 2014/11/16.
 */
public class CheckWayTypeCode extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.check_type_code,null);
        return view;
    }
}