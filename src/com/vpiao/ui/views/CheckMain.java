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
import com.vpiao.fragments.PrintFrament;

/**
 * Created by TaoSun on 2014/11/16.
 */
public class CheckMain extends Activity {

    private static final String TAG="CheckMain";
    private FragmentManager fragmentManager;
    private RadioGroup radioGroup;
    private Fragment fragment;
    private FragmentTransaction transaction;
    private int checkedIndex;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.v(TAG,"onCreate");
        setContentView(R.layout.check_main);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        fragmentManager=getFragmentManager();
        radioGroup= (RadioGroup) findViewById(R.id.rg_tab);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedIndex=checkedId;
                transaction=fragmentManager.beginTransaction();
                Log.d(TAG,"group.getCheckedRadioButtonId();"+group.getCheckedRadioButtonId()+"checkedId:"+checkedId);

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
                    case 4:
                        fragment=new PrintFrament();
                    default:
                        fragment=new CheckWayIdCard();
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
        Log.v(TAG,"onResume");
        if(radioGroup.getCheckedRadioButtonId()>0){
            radioGroup.clearCheck();
        }
        if(fragment!=null){
            radioGroup.check(checkedIndex);
//            transaction=fragmentManager.beginTransaction();
//            transaction.replace(R.id.check_way_frame,fragment);
//            transaction.commit();
        }
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG,"onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG,"onPause");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.v(TAG,"onPostResume");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.v(TAG,"onPostCreate");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"onDestroy");
    }
}