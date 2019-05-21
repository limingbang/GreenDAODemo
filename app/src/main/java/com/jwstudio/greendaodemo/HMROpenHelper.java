package com.jwstudio.greendaodemo;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jwstudio.greendao.DaoMaster;
import com.jwstudio.greendao.UserDao;

import org.greenrobot.greendao.database.Database;

public class HMROpenHelper extends DaoMaster.OpenHelper{
    public HMROpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.d("TAG","更新了");
//        DaoMaster.dropAllTables(db, true);
//        onCreate(db);

        // 目前用USER表来测试
        MigrationHelper.migrate(db, UserDao.class);
    }
}
