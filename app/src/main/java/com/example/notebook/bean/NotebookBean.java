package com.example.notebook.bean;

import android.os.Parcel;
import android.os.Parcelable;


public class NotebookBean implements Parcelable {
    private int notebookId;
    private String content;
    private long editTime;

    public NotebookBean(){}

    protected NotebookBean(Parcel in) {
        notebookId = in.readInt();
        content = in.readString();
        editTime = in.readLong();
    }

    public static final Creator<NotebookBean> CREATOR = new Creator<NotebookBean>() {
        @Override
        public NotebookBean createFromParcel(Parcel in) {
            return new NotebookBean(in);
        }

        @Override
        public NotebookBean[] newArray(int size) {
            return new NotebookBean[size];
        }
    };

    public int getNotebookId() {
        return notebookId;
    }

    public void setNotebookId(int notebookId) {
        this.notebookId = notebookId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getEditTime() {
        return editTime;
    }

    public void setEditTime(long editTime) {
        this.editTime = editTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(notebookId);
        parcel.writeString(content);
        parcel.writeLong(editTime);
    }
}
