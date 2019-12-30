package com.example.lib_audio.mediaplayer.db;

import android.database.sqlite.SQLiteDatabase;

import com.example.lib_audio.app.AudioHelper;
import com.example.lib_audio.mediaplayer.model.AudioBean;

/**
 * 操作greenDao数据库帮助类
 */
public class GreenDaoHelper {

    private static final String DB_NAME = "music_db";
    //数据库帮助类，用来创建数据库，升级数据库
    private static DaoMaster.DevOpenHelper mHelper;
    //最终创建好的数据库
    private static SQLiteDatabase mDb;
    //管理数据库
    private static DaoMaster mDaoMaster;
    //管理表--管理各种实体Dao
    private static DaoSession mDaoSession;

    public static void  initDatabase(){
        mHelper = new DaoMaster.DevOpenHelper(AudioHelper.getContext(),DB_NAME,null);
        mDb = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mDb);
        mDaoSession = mDaoMaster.newSession();
    }

    public static void addFavourite(AudioBean bean){

    }


}
