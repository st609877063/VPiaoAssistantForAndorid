package com.vpiao.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.VpiaoAssistant.R;
import com.zxing.client.android.CaptureActivity;

/**
 * Created by TaoSun on 2014/11/16.
 */
public class CheckWayScanCode extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.check_way_scan_code,null);
        Intent intent = new Intent();
        intent.setClass(this.getActivity(), CaptureActivity.class);
        startActivity(intent);
        return view;
    }
}