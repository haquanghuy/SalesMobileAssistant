package com.doannganh.salesmobileassistant.model;

public class Custom_list_item {
    String title, subTitle, text;
    String symbol = ">";
    String TAG;

    public Custom_list_item(String title, String subTitle, String text) {
        this.title = title;
        this.subTitle = subTitle;
        this.text = text;
    }

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
