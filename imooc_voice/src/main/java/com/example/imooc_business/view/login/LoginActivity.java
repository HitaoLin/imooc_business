package com.example.imooc_business.view.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.imooc_business.R;
import com.example.imooc_business.api.RequestCenter;
import com.example.imooc_business.model.login.LoginEvent;
import com.example.imooc_business.model.user.User;
import com.example.imooc_business.view.login.manager.UserManager;
import com.example.lib_commin_ui.base.BaseActivity;
import com.example.lib_network.okhttp.listener.DisposeDataListener;

import org.greenrobot.eventbus.EventBus;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements DisposeDataListener{


    public static void start(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.login_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestCenter.login(LoginActivity.this);
            }
        });

    }

    @Override
    public void onSuccess(Object responseObj) {
        //处理正常逻辑
        User user = (User) responseObj;
        UserManager.getInstance().saveUser(user);
        EventBus.getDefault().post(new LoginEvent());
        finish();
    }

    @Override
    public void onFailure(Object responseObj) {
        //登录失败逻辑
    }
}
