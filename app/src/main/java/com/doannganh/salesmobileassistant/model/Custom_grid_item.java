package com.doannganh.salesmobileassistant.model;

public class Custom_grid_item {
    private int picture;
    private String name;

    public Custom_grid_item(int picture, String name) {
        this.picture = picture;
        this.name = name;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
