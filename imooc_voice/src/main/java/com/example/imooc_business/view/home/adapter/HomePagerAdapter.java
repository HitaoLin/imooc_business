package com.example.imooc_business.view.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.imooc_business.model.CHANNEL;
import com.example.imooc_business.view.discory.DiscoryFragment;
import com.example.imooc_business.view.friend.FriendFragment;
import com.example.imooc_business.view.mine.MineFragment;

/**
 * 首页viewpager adapter
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

    private CHANNEL[] mList;

    public HomePagerAdapter(FragmentManager fm, CHANNEL[] datas) {
        super(fm);
        mList = datas;
    }

    //初始化对应的fragment
    @Override
    public Fragment getItem(int i) {
        int type = mList[i].getValue();
        switch (type) {
            case CHANNEL.MINE_ID:
                return MineFragment.newInstant();
            case CHANNEL.DISCORY_ID:
                return DiscoryFragment.newInstant();
            case CHANNEL.FRIEND_ID:
                return FriendFragment.newInstant();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.length;
    }
}
