package com.doannganh.salesmobileassistant.model;

public class MyJob {
    RoutePlan routePlan;
    String CustName;
    String Address;

    public MyJob(RoutePlan routePlan, String custName, String address) {
        this.routePlan = routePlan;
        CustName = custName;
        Address = address;
    }

    public RoutePlan getRoutePlan() {
        return routePlan;
    }

    public void setRoutePlan(RoutePlan routePlan) {
        this.routePlan = routePlan;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
