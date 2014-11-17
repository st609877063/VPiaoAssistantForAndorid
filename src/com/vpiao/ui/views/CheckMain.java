package com.vpiao.ui.views;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.RadioGroup;
import com.example.VpiaoAssistant.R;
import com.vpiao.fragments.CheckWayIdCard;
import com.vpiao.fragments.CheckWayScanCode;
import com.vpiao.fragments.CheckWayTypeCode;

/**
 * Created by TaoSun on 2014/11/16.
 */
public class CheckMain extends Activity {

    private static final String TAG="CheckMain";
    private FragmentManager fragmentManager;
    private RadioGroup radioGroup;
    private Fragment fragment;
    private FragmentTransaction transaction;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_main);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        fragmentManager=getFragmentManager();
        radioGroup= (RadioGroup) findViewById(R.id.rg_tab);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                transaction=fragmentManager.beginTransaction();
                switch (checkedId){
                    case 1:
                        fragment=new CheckWayTypeCode();
                        break;
                    case 2:
                        fragment=new CheckWayScanCode();
                        break;
                    case 3:
                        fragment=new CheckWayIdCard();
                        break;
                }
                transaction.replace(R.id.check_way_frame,fragment);
                transaction.commit();
                Log.v(TAG,checkedId+"");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(fragment!=null){
            transaction.replace(R.id.check_way_frame,fragment);
            transaction.commit();
        }
    }
}