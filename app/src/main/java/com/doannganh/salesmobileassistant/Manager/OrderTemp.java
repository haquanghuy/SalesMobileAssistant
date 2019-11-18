package com.doannganh.salesmobileassistant.Manager;
import com.doannganh.salesmobileassistant.model.OrderProductTemp;

import java.util.Date;
import java.util.List;

public class OrderTemp {
    // screen 1
    String OrdeID = "";
    String EmplID = "";
    String EmplName = "";
    String CustID = "";
    String CustomerName = "";
    String CustomerAddress1 = "";
    String CustomerAddress2 = "";
    Date NeedByDate;

    // screen 2
    List<OrderProductTemp> list;
    String totalPriceProduct = "";

    // screen 3
    double DocTotalCharges = 0;
    double TrueDiscountPercent = 0;
    double DocExtPriceDtl = 0;
    double DocTotalTax = 0;
    double DocOrderAmt = 0;

    private static OrderTemp orderTemp;

    public static OrderTemp myInstance() {
        if(orderTemp == null)
            orderTemp = new OrderTemp();

        return orderTemp;
    }

    public void saveTempOrderCustomer(String ordeID, String emplID, String emplName, String custID,
                                      String custName, String custAddress1, String custAddress2, Date needByDate){
        orderTemp.OrdeID = ordeID;
        orderTemp.EmplID = emplID;
        orderTemp.EmplName = emplName;
        orderTemp.CustID = custID;

        orderTemp.CustomerName = custName;
        orderTemp.CustomerAddress1 = custAddress1;
        orderTemp.CustomerAddress2 = custAddress2;

        orderTemp.NeedByDate = needByDate;
    }

    public void saveTempOrderProduct(List<OrderProductTemp> list, String totalPriceProduct){
        orderTemp.list = list;
        orderTemp.totalPriceProduct = totalPriceProduct;
    }

    public void saveTempOrderTotal(double DocTotalCharges, double TrueDiscountPercent, double DocExtPriceDtl,
                                   double DocTotalTax, double DocOrderAmt){
        orderTemp.DocTotalCharges = DocTotalCharges;
        orderTemp.TrueDiscountPercent = TrueDiscountPercent;
        orderTemp.DocExtPriceDtl = DocExtPriceDtl;
        orderTemp.DocTotalTax = DocTotalTax;
        orderTemp.DocOrderAmt = DocOrderAmt;
    }

    public OrderTemp exportOrderTemp(){
        return orderTemp;
    }

    public String getOrdeID() {
        if(orderTemp == null) orderTemp = new OrderTemp();
        return OrdeID;
    }

    public void setOrdeID(String ordeID) {
        OrdeID = ordeID;
    }

    public String getEmplID() {
        return EmplID;
    }

    public void setEmplID(String emplID) {
        EmplID = emplID;
    }

    public String getEmplName() {
        return EmplName;
    }

    public void setEmplName(String emplName) {
        EmplName = emplName;
    }

    public String getCustID() {
        return CustID;
    }

    public void setCustID(String custID) {
        CustID = custID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerAddress1() {
        return CustomerAddress1;
    }

    public void setCustomerAddress1(String customerAddress1) {
        CustomerAddress1 = customerAddress1;
    }

    public String getCustomerAddress2() {
        return CustomerAddress2;
    }

    public void setCustomerAddress2(String customerAddress2) {
        CustomerAddress2 = customerAddress2;
    }

    public Date getNeedByDate() {
        return NeedByDate;
    }

    public void setNeedByDate(Date needByDate) {
        NeedByDate = needByDate;
    }

    public List<OrderProductTemp> getList() {
        return list;
    }

    public void setList(List<OrderProductTemp> list) {
        this.list = list;
    }

    public String getTotalPriceProduct() {
        return totalPriceProduct;
    }

    public void setTotalPriceProduct(String totalPriceProduct) {
        this.totalPriceProduct = totalPriceProduct;
    }

    public double getDocTotalCharges() {
        return DocTotalCharges;
    }

    public void setDocTotalCharges(double docTotalCharges) {
        DocTotalCharges = docTotalCharges;
    }

    public double getTrueDiscountPercent() {
        return TrueDiscountPercent;
    }

    public void setTrueDiscountPercent(double trueDiscountPercent) {
        TrueDiscountPercent = trueDiscountPercent;
    }

    public double getDocExtPriceDtl() {
        return DocExtPriceDtl;
    }

    public void setDocExtPriceDtl(double docExtPriceDtl) {
        DocExtPriceDtl = docExtPriceDtl;
    }

    public double getDocTotalTax() {
        return DocTotalTax;
    }

    public void setDocTotalTax(double docTotalTax) {
        DocTotalTax = docTotalTax;
    }

    public double getDocOrderAmt() {
        return DocOrderAmt;
    }

    public void setDocOrderAmt(double docOrderAmt) {
        DocOrderAmt = docOrderAmt;
    }
}
