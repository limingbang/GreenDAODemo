package com.jwstudio.greendaodemo;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.jwstudio.greendao.DaoMaster;
import com.jwstudio.greendao.DaoSession;

public class MyGreenDAOApplication extends Application {

    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    public static MyGreenDAOApplication instances;

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        setDatabase();
    }

    // GreenDAO各应用对象的初始化
    private void setDatabase() {
        // 创建数据库
        HMROpenHelper mHelper = new HMROpenHelper(this, "myGDDb", null);
        db = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public static MyGreenDAOApplication getInstances() {
        return instances;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public DaoMaster getmDaoMaster() {
        return mDaoMaster;
    }

    public DaoSession getmDaoSession() {
        return mDaoSession;
    }
}
