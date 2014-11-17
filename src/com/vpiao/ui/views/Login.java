package com.vpiao.ui.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import com.example.VpiaoAssistant.R;
import com.vpiao.VpiaoApp;
import com.vpiao.domain.User;
import com.vpiao.ui.dialogs.NoUserDataAlterDialog;
import com.vpiao.utils.domains.UserHelper;
import java.util.List;

/**
 * 用户登录
 * Created by suntao on 2014/11/13.
 */
public class Login extends Activity {
    /**
     * 选中用户列表
     */
    private List<User> users;

    private User currentUser;

    private EditText txtPassword;

    private VpiaoApp vpiaoApp;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        txtPassword= (EditText) findViewById(R.id.txt_password);
        txtPassword.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        try {
            users= UserHelper.getUserList();
            if(users==null||users.isEmpty()){
                new NoUserDataAlterDialog(this);
                return;
            }
            Spinner userSpinner= (Spinner) findViewById(R.id.select_user_name);
            ArrayAdapter<User> adapter=new ArrayAdapter<User>(this,R.layout.login_list_item,R.id.user_item, users);
            userSpinner.setAdapter(adapter);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        //绑定事件
        selectUserChanged();
        btnLoginClick();
    }

    /**
     * 登陆button
     */
    private void btnLoginClick(){
        final Context _this=this;
        Button btn= (Button) findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password=txtPassword.getText().toString();
                if(password==null||password.isEmpty()){
                    Toast.makeText(_this,R.string.password_empty,Toast.LENGTH_SHORT).show();
                }else{
                    if(UserHelper.checkLogin(currentUser.getLoginName(),password,currentUser)){
                        vpiaoApp= (VpiaoApp) getApplication();
                        vpiaoApp.LoggedUser=currentUser;
                        Toast.makeText(_this,R.string.login_success,Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(_this,R.string.password_error,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * 用户下拉框变更
     */
    private void selectUserChanged(){
        Spinner spinner= (Spinner) findViewById(R.id.select_user_name);
        //选中事件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentUser=users.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }




}