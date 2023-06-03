package com.example.notebook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.example.notebook.bean.NotebookBean;

import java.util.ArrayList;
import java.util.List;

import static com.example.notebook.db.SQLiteHelper.CONTENT;
import static com.example.notebook.db.SQLiteHelper.EDIT_TIME;
import static com.example.notebook.db.SQLiteHelper.NOTEBOOK_ID;
import static com.example.notebook.db.SQLiteHelper.TABLE_NOTEBOOK;

/**
 * 数据库管理类
 */

public class DBManager {

    private static final String TAG = DBManager.class.getSimpleName();
    private SQLiteHelper helper;

    public DBManager(Context context) {
        if (helper == null) {
            helper = new SQLiteHelper(context);
        }
    }

    // 插入数据到记事本表中
    public boolean insertNotebook(NotebookBean notebookBean) {
        boolean flag = false;
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(CONTENT, notebookBean.getContent());
            cv.put(EDIT_TIME, notebookBean.getEditTime());
            long num = db.insert(SQLiteHelper.TABLE_NOTEBOOK, null, cv);
            Log.d(TAG, "insert_num:" + num);
            db.setTransactionSuccessful();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "insert:" + e.toString());
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return flag;
    }


    // 更新数据到记事本表中
    public boolean updateNotebook(NotebookBean notebookBean) {
        boolean flag = false;
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CONTENT, notebookBean.getContent());
            values.put(EDIT_TIME, notebookBean.getEditTime());
            db.update(TABLE_NOTEBOOK, values, "notebookId = ?", new String[] { notebookBean.getNotebookId() + "" });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "updateNotebook:" + e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return flag;
    }



    //查询记事列表信息
    public List<NotebookBean> selectNotebookList() {
        ArrayList<NotebookBean> list = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getReadableDatabase();
            String sql = "select * from " + SQLiteHelper.TABLE_NOTEBOOK + " order by " + EDIT_TIME + " desc ";
            cursor = db.rawQuery(sql, null);
            list = new ArrayList<>();
            while (cursor.moveToNext()) {
                NotebookBean notebookBean = new NotebookBean();
                notebookBean.setNotebookId(cursor.getInt(cursor.getColumnIndex(NOTEBOOK_ID)));
                notebookBean.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
                notebookBean.setEditTime(cursor.getLong(cursor.getColumnIndex(EDIT_TIME)));
                list.add(notebookBean);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "selectNotebookList:" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return null;
    }

    /**
     * 删除一条记事
     * @param notebookId
     */
    public void deleteNotebook(int notebookId){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getWritableDatabase();
            String sql = "delete  from " + SQLiteHelper.TABLE_NOTEBOOK + " where " + NOTEBOOK_ID + " = " + notebookId;
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor)
                cursor.close();
            if (null != db)
                db.close();
        }
    }

}
