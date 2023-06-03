package com.example.notebook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "notebook.db";

    // 记事本表的属性
    public static final String TABLE_NOTEBOOK = "tb_notebook";


    public static final String NOTEBOOK_ID = "notebookId";
    public static final String CONTENT = "content";
    public static final String EDIT_TIME = "editTime";


    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建表SQL语句
        String sqlStore = "create table if not exists " + TABLE_NOTEBOOK +
                " (" +
                NOTEBOOK_ID + " integer primary key ," +
                CONTENT + " varchar ," +
                EDIT_TIME + " integer " +
                ")";
        // 执行建store表语句
        db.execSQL(sqlStore);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
