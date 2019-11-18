package com.doannganh.salesmobileassistant.model;

public class Custom_combobox_click {
    String show = "";
    String idString = "";
    int idInt = -1;

    public Custom_combobox_click(String show, String id){
        this.show = show;
        this.idString = id;
    }

    public Custom_combobox_click(String show, int id) {
        this.show = show;
        this.idInt = idInt;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getIdString() {
        return idString;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }

    public int getIdInt() {
        return idInt;
    }

    public void setIdInt(int idInt) {
        this.idInt = idInt;
    }
}
