package com.example.notebook.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.notebook.R;
import com.example.notebook.adapter.NotebookAdapter;
import com.example.notebook.bean.NotebookBean;

import com.example.notebook.db.DBManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int QR_CODE = 1001;
    private static final int REQUEST_CODE = 1002;

    private RecyclerView recyclerView;
    private Button btnAdd;

    private List<NotebookBean> mNotebookList;
    private NotebookAdapter mAdapter;
    private DBManager mDBManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        initView();
        initRecyclerView();
        mDBManager = new DBManager(this);
        getNotebookList();
    }

    /**
     * 绑定视图VIEW
     */
    private void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
    }

    private void initRecyclerView(){
        mNotebookList = new ArrayList<>();
        mAdapter = new NotebookAdapter(R.layout.item_notebook, mNotebookList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(recyclerView);
        mAdapter.setEmptyView(R.layout.view_no_data);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MainActivity.this, AddNotebookActivity.class);
                intent.putExtra("isAdd", false);
                intent.putExtra("notebook", mNotebookList.get(position));
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        mAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                showDeleteDialog(position, mNotebookList.get(position).getNotebookId());
                return true;
            }
        });
    }

    private void getNotebookList(){
        mNotebookList.clear();
        mNotebookList.addAll(mDBManager.selectNotebookList());
        mAdapter.notifyDataSetChanged();
    }

    private void showDeleteDialog(final int position, final int notebookId){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_delete_notebook, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDBManager.deleteNotebook(notebookId);
                alertDialog.dismiss();
                mNotebookList.remove(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, mNotebookList.size() - position);
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:// 点击添加按钮
                Intent intent = new Intent(MainActivity.this, AddNotebookActivity.class);
                intent.putExtra("isAdd", true);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            getNotebookList();
        }

    }

    /*************连续点击两次退出应用******************/

    // 用来计算返回键的点击间隔时间
    private boolean keyPress = true;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断按下的键是否是返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (keyPress) {
                Toast.makeText(MainActivity.this, "再按一次返回键退出记事本", Toast.LENGTH_SHORT).show();

                keyPress = false;

                //使用定时器修改keyPress的值，按下两秒后将keyPress设为true
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        keyPress = true;
                    }
                }, 2000);

            } else {
                //关闭页面
                finish();
            }
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }



    /************************检查授权相关********************************/
    private List<String> requestPermissions;
    // 申请权限相关
    private static String[] permissions = new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private boolean checkPermission() {
        boolean flag = true;
        requestPermissions = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_DENIED) {
            requestPermissions.add(permissions[0]);
            flag = false;
        }
        if (ContextCompat.checkSelfPermission(this, permissions[1]) == PackageManager.PERMISSION_DENIED) {
            requestPermissions.add(permissions[1]);
            flag = false;
        }
        return flag;
    }



}
