package com.example.notebook.activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebook.R;
import com.example.notebook.bean.NotebookBean;
import com.example.notebook.db.DBManager;

public class AddNotebookActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView imageBack;
    private ImageView imageDelete;
    private EditText editContent;
    private Button btnSave;
    private TextView tvTitle;

    private boolean bIsAdd;

    private DBManager mDBManager;
    private int mNotebookId;
    private NotebookBean mNotebookBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notebook);

        initView();
        mDBManager = new DBManager(this);
        bIsAdd = getIntent().getBooleanExtra("isAdd", true);
        if (bIsAdd) {
            imageDelete.setVisibility(View.GONE);
        } else {
            mNotebookBean = getIntent().getParcelableExtra("notebook");
            imageDelete.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);
            editContent.setText(mNotebookBean.getContent());
        }


    }

    private void initView() {
        imageBack = findViewById(R.id.image_back);
        imageDelete = findViewById(R.id.image_delete);
        editContent = findViewById(R.id.edit_notebook);
        btnSave = findViewById(R.id.btn_save);
        tvTitle = findViewById(R.id.tv_title);

        imageBack.setOnClickListener(this);
        imageDelete.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        editContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b && !bIsAdd){
                    btnSave.setVisibility(View.VISIBLE);
                    imageDelete.setVisibility(View.GONE);
                    tvTitle.setText("编辑记事本");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.image_delete:
                // TODO 弹出提示窗是否删除
                showDeleteDialog();
                break;
            case R.id.btn_save:
                save(editContent.getText().toString().trim());
                break;
        }
    }

    private void save(String content) {
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(AddNotebookActivity.this, "您还未输入内容", Toast.LENGTH_SHORT).show();
        } else {
            if (mNotebookBean != null){
                mNotebookBean.setContent(content);
                mNotebookBean.setEditTime(System.currentTimeMillis());
                mDBManager.updateNotebook(mNotebookBean);
            }else {
                NotebookBean notebookBean = new NotebookBean();
                notebookBean.setContent(content);
                notebookBean.setEditTime(System.currentTimeMillis());
                mDBManager.insertNotebook(notebookBean);
            }

            setResult(RESULT_OK);
            finish();
        }
    }

    private void showDeleteDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_delete_notebook, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDBManager.deleteNotebook(mNotebookBean.getNotebookId());
                setResult(RESULT_OK);
                finish();
                alertDialog.dismiss();
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }
}
