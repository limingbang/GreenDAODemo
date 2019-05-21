package com.jwstudio.greendaodemo;

import android.database.Cursor;
import android.text.TextUtils;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.internal.DaoConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MigrationHelper {

    // 调用的升级方法，第二个参数表示，只要继承了AbstractDao的实体类的Dao类都可以
    @SafeVarargs
    public static void migrate(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        // 生成临时表，将旧表更名，编程临时表
        generateTempTables(db, daoClasses);

        // 创建新表
        createAllTables(db, false, daoClasses);

        // 将临时表的数据拷贝到新表，并删除临时表
        restorData(db, daoClasses);
    }

    @SafeVarargs
    private static void generateTempTables(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        // 循环参数中的Dao类，对参数中所有Dao类进行更名操作
        for (int i = 0; i < daoClasses.length; i++) {
            // 获得DaoConfing对象，这个对象里面封装了表名
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
            String tableName = daoConfig.tablename;
            // 如果表不存在跳过后续的操作
            if (!checkTable(db, tableName)) {
                continue;
            }

            String tempTableName = daoConfig.tablename.concat("_TEMP");// 临时表名
            StringBuilder insertTableStringBuilder = new StringBuilder();

            // 注意空格，拼接sql语句，将表改名变成临时表
            insertTableStringBuilder.append("alter table ")
                    .append(tableName)
                    .append(" rename to ")
                    .append(tempTableName)
                    .append(";");
            db.execSQL(insertTableStringBuilder.toString());
        }
    }

    private static boolean checkTable(Database db, String tableName) {
        StringBuffer query = new StringBuffer();
        query.append("select count(*) from sqlite_master where type='table' and name='")
                .append(tableName)
                .append("'");
        Cursor c = db.rawQuery(query.toString(), null);

        if (c.moveToNext()) {
            int count = c.getInt(0);
            if (count > 0) {
                return true;
            }
            return false;
        }

        return false;
    }

    @SafeVarargs
    private static void createAllTables(Database db, boolean ifNotExists,
                                        @NotNull Class<? extends AbstractDao<?, ?>>... daoClasses) {
        reflectMethod(db, "createTable", ifNotExists, daoClasses);
    }

    // 用反射的方法来获得createTable方法，创建个Dao类相对应的实体类的表
    @SafeVarargs
    private static void reflectMethod(Database db, String methodName, boolean ifNotExists,
                                      @NotNull Class<? extends AbstractDao<?, ?>>... daoClasses) {
        if (daoClasses.length < 1) return;

        try {
            for (Class<?> cls : daoClasses) {
                /* 以User实体类为例，在生成的UserDao中有一个createTable()方法。
                 * 当User类的属性发生改变时，编译后UserDao的相应代码会发生改变，比如createTable()方法。
                 * 但是，表名并没有改变，当以下代码执行成功后，也就是新表创建了。
                 * 它的表名还是USER，而与其对应的临时表名时USER_TEMP。*/
                Method method = cls.getDeclaredMethod(methodName, Database.class, boolean.class);
                method.invoke(null, db, ifNotExists);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    @SafeVarargs
    private static void restorData(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");

            if (!checkTable(db, tempTableName)) continue;

            // 获得临时表也就是旧表中的所有列
            List<String> columns = getColums(db, tempTableName);
            // 如果旧表的列包含此新表列，将其添加到properties集合中
            ArrayList<String> properties = new ArrayList<>(columns.size());

            for (int j = 0; j < daoConfig.properties.length; j++) {
                // 获得新表的列
                String columnName = daoConfig.properties[j].columnName;
                // 如果旧表的列包含此新表列，将其添加到properties集合中
                if (columns.contains(columnName)) {
                    properties.add(columnName);
                }
            }

            if (properties.size() > 0) {
                // 在集合中每个字符串元素（即列名）之间用英文下的逗号相连，用于构建sql语句中的列名
                final String columnSQL = TextUtils.join(",", properties);
                StringBuilder insertTableStringBuilder = new StringBuilder();

                // 注意空格，拼接sql语句，拷贝旧表数据到新表
                insertTableStringBuilder.append("insert into ")
                        .append(tableName)
                        .append("(")
                        .append(columnSQL)
                        .append(") select ")
                        .append(columnSQL)
                        .append(" from ")
                        .append(tempTableName)
                        .append(";");
                db.execSQL(insertTableStringBuilder.toString());
            }

            // 删除临时表
            StringBuilder dropTableStringBuilder = new StringBuilder();
            dropTableStringBuilder.append("drop table ").append(tempTableName);
            db.execSQL(dropTableStringBuilder.toString());
        }
    }

    // 获得表中所有的列名
    private static List<String> getColums(Database db, String tableName) {
        List<String> columns = null;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from " + tableName + " limit 0", null);
            if (null != cursor && cursor.getColumnCount() > 0) {
                columns = Arrays.asList(cursor.getColumnNames());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            if (columns == null) {
                columns = new ArrayList<>();
            }
        }

        return columns;
    }

}
