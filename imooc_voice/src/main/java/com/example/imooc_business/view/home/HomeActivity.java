package com.example.imooc_business.view.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.example.imooc_business.R;
import com.example.imooc_business.model.CHANNEL;
import com.example.imooc_business.model.login.LoginEvent;
import com.example.imooc_business.view.home.adapter.HomePagerAdapter;
import com.example.imooc_business.view.login.LoginActivity;
import com.example.imooc_business.view.login.manager.UserManager;
import com.example.lib_commin_ui.base.BaseActivity;
import com.example.lib_commin_ui.pager_indictor.ScaleTransitionPagerTitleView;
import com.example.lib_image_loader.app.ImageLoaderManager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private View unLogginLayout;
    private ImageView mPhotoView;//头像

    //指定首页要出现的卡片
    private static final CHANNEL[] CHANNELS =
            new CHANNEL[]{CHANNEL.MY, CHANNEL.DISCORY, CHANNEL.FRIEND};

    private DrawerLayout mDrawerLayout;
    private View mToggleView;
    private View mSearchView;
    private ViewPager mViewPager;
    private HomePagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册EventBus
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_home);
        initView();

    }

    private void initView() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggleView = findViewById(R.id.toggle_view);
        mSearchView = findViewById(R.id.search_view);
        mViewPager = findViewById(R.id.view_pager);
        //初始化adapter
        mAdapter = new HomePagerAdapter(getSupportFragmentManager(), CHANNELS);
        mViewPager.setAdapter(mAdapter);
        initMagicIndicator();
        //登录相关UI
        unLogginLayout = findViewById(R.id.unloggin_layout);
        unLogginLayout.setOnClickListener(this);
        mPhotoView = findViewById(R.id.avatr_view);

    }

    //初始化指示器
    private void initMagicIndicator() {
        /**
         * 引用lib_commin_ui的依赖，不能使用implementation声明
         * 因为implementation声明的依赖只能在本module模块内使用,跨module使用就要使用api声明
         * from:https://blog.csdn.net/ljphhj/article/details/86262031
         */
        MagicIndicator magicIndicator = findViewById(R.id.magic_indicator);
        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return CHANNELS == null ? 0 : CHANNELS.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(CHANNELS[index].getKey());
                simplePagerTitleView.setTextSize(19);
                simplePagerTitleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                simplePagerTitleView.setNormalColor(Color.parseColor("#999999"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#333333"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.unloggin_layout://登录
                if (!UserManager.getInstance().hasLogin()) {
                    LoginActivity.start(this);
                } else {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event){
        unLogginLayout.setVisibility(View.GONE);
        mPhotoView.setVisibility(View.VISIBLE);
        ImageLoaderManager.getInstance().displayImageForCircle(mPhotoView,UserManager.getInstance().getUser().data.photoUrl);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
