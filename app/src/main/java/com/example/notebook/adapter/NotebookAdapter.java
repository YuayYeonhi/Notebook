package com.example.notebook.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.notebook.R;
import com.example.notebook.bean.NotebookBean;
import com.example.notebook.tool.Util;

import java.util.List;

public class NotebookAdapter extends BaseQuickAdapter<NotebookBean, BaseViewHolder> {

    public NotebookAdapter(int layoutResId, @Nullable List<NotebookBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, NotebookBean item) {
        helper.setText(R.id.tv_content, item.getContent());
        long time = item.getEditTime();
        helper.setText(R.id.tv_time, Util.getTime(time));
        helper.addOnClickListener(R.id.layout_notebook);
        helper.addOnLongClickListener(R.id.layout_notebook);
    }

}

