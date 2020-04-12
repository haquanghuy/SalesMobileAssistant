package com.doannganh.salesmobileassistant.Manager.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SalesMobileAssistant extends SQLiteOpenHelper {
    private static SalesMobileAssistant sInstance;
    public static SalesMobileAssistant getsInstance(Context context) {
        if (sInstance == null)
            sInstance = new SalesMobileAssistant(context.getApplicationContext());
        return sInstance;
    }

    public SalesMobileAssistant(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tbEMPLOYEE = "CREATE TABLE " + TB_EMPLOYEE + " ( " + TB_EMPLOYEE_COMPANY + " TEXT, "
                + TB_EMPLOYEE_TYPEID + " TEXT, " + TB_EMPLOYEE_ID + " TEXT, " + TB_EMPLOYEE_NAME + " TEXT, " +
                 "PRIMARY KEY (CompID, EmplID))";

        String tbPRODUCT = "CREATE TABLE " + TB_PRODUCT + " ( " + TB_PRODUCT_COMPANY + " TEXT, "
                + TB_PRODUCT_TYPE + " TEXT, " + TB_PRODUCT_ID + " TEXT, " + TB_PRODUCT_NAME + " TEXT, "
                + TB_PRODUCT_UNITPRICE + " DOUBLE, " + TB_PRODUCT_UOM + " TEXT, " + TB_PRODUCT_DATEUPDATE + " TEXT, "
                + "PRIMARY KEY(CompID, ProdID))";

        String tbPRODUCTINSITE = "CREATE TABLE " + TB_PRODUCTINSITE + " ( " + TB_PRODUCTINSITE_COMPANY + " TEXT, "
                + TB_PRODUCTINSITE_SITEID + " TEXT, " + TB_PRODUCTINSITE_PRODUCTID + " TEXT, "
                + TB_PRODUCTINSITE_QUANTITY + " INTEGER, " + TB_PRODUCTINSITE_UOM+ " TEXT, "
                + "PRIMARY KEY(" + TB_PRODUCTINSITE_COMPANY + ", " + TB_PRODUCTINSITE_SITEID + ", " + TB_PRODUCTINSITE_PRODUCTID + "))";

        String tbCUSTOMER = "CREATE TABLE " + TB_CUSTOMER + " ( " + TB_CUSTOMER_COMPANY_ + " TEXT, "
                + TB_CUSTOMER_EMPLOYEEID_ + " TEXT, " + TB_CUSTOMER_ID_ + " INTEGER, " + TB_CUSTOMER_NAME+ " TEXT, "
                + TB_CUSTOMER_ADDRESS1 + " TEXT, " + TB_CUSTOMER_ADDRESS2 + " TEXT, " + TB_CUSTOMER_ADDRESS3 + " TEXT, "
                + TB_CUSTOMER_CITY + " TEXT, " + TB_CUSTOMER_COUNTRY + " TEXT, " + TB_CUSTOMER_PHONENUM + " TEXT, "
                + TB_CUSTOMER_DISCOUNT + " DECIMAL, "+ "PRIMARY KEY(CompID, CustID))";

        String tbORDER = "CREATE TABLE " + TB_ORDERS + " ( " + TB_ORDER_COMPANY + " TEXT,"
                + TB_ORDER_MYORDERID + " TEXT," + TB_ORDER_ORDERID + " INT," + TB_ORDER_CUSTOMERID+ " TEXT,"
                + TB_ORDER_EMPLOYEEID + " TEXT," + TB_ORDER_ORDERDATE + " TEXT," + TB_ORDER_NEEDBYDATE + " TEXT,"
                + TB_ORDER_REQUESTDATE + " TEXT, " + TB_ORDER_ORDERSTATUS + " INTEGER , PRIMARY KEY(CompID, MyOrderID))";

        String tbORDERDETAIL = "CREATE TABLE " + TB_ORDERDETAIL + " ( " + TB_ORDERDETAIL_COMPANY + " TEXT, "
                + TB_ORDERDETAIL_SITEID + " TEXT, " + TB_ORDERDETAIL_MYORDERID + " TEXT, " + TB_ORDERDETAIL_ORDERNUM + " INTEGER, "
                + TB_ORDERDETAIL_ORDERLINE + " INTEGER, " + TB_ORDERDETAIL_PRODUCTID+ " TEXT, " + TB_ORDERDETAIL_QUANTITY + " DECIMAL, " + TB_ORDERDETAIL_UNITPRICE + " DECIMAL, "
                + "PRIMARY KEY (CompID, SiteID, MyOrderID, ProdID))";

        String tbROUTEPLAN = "CREATE TABLE " + TB_ROUTEPLAN + " ( " + TB_ROUTEPLAN_COMPANY + " TEXT, "
                + TB_ROUTEPLAN_EMPLOYEEID + " TEXT, " + TB_ROUTEPLAN_CUSTOMERID + " INTEGER, " + TB_ROUTEPLAN_DATEPLAN + " TEXT, "
                + TB_ROUTEPLAN_PRIORITIZE + " INTEGER, " + TB_ROUTEPLAN_VISITED + " INTEGER, " + TB_ROUTEPLAN_NOTE + " TEXT, "
                + "PRIMARY KEY (CompID, EmplID, CustID, DatePlan))";

        String tbACCOUNT = "CREATE TABLE " + TB_ACCOUNT + " ( " + TB_ACCOUNT_COMPANYID + " TEXT, "
                + TB_ACCOUNT_ID + " INTEGER, " + TB_ACCOUNT_USERNAME + " TEXT, "
                + TB_ACCOUNT_PASSWORD + " TEXT, " + TB_ACCOUNT_TYPE + " BOOLEAN, "
                + TB_ACCOUNT_EMPLOYEEID + " TEXT, " + TB_ACCOUNT_NOTE + " TEXT, "
                + " Primary Key(CompID, ID))";
        String tbCONNECTIONCONFIG = "CREATE TABLE " + TB_CONNECTIONCONFIG + " ( " + TB_CONNECTIONCONFIG_API + " TEXT, "
                + TB_CONNECTIONCONFIG_USER + " TEXT, "
                + TB_CONNECTIONCONFIG_PASS+ " TEXT, " + TB_CONNECTIONCONFIG_COMPANY + " TEXT)";

        String tbIMAGES = "CREATE TABLE " + TB_IMAGES + " ( " + TB_IMAGES_KEY + " TEXT, "
                + TB_IMAGES_FIENAME + " TEXT, "
                + TB_IMAGES_DATA + " BLOB, " + TB_IMAGES_COMPANY + " TEXT)";

        db.execSQL(tbEMPLOYEE);
        db.execSQL(tbPRODUCT);
        db.execSQL(tbPRODUCTINSITE);
        db.execSQL(tbCUSTOMER);
        db.execSQL(tbORDER);
        db.execSQL(tbORDERDETAIL);
        db.execSQL(tbROUTEPLAN);
        db.execSQL(tbACCOUNT);
        db.execSQL(tbCONNECTIONCONFIG);
        db.execSQL(tbIMAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion != oldVersion ){
            db.execSQL("DROP TABLE IF EXISTS " + TB_EMPLOYEE + ";");
            db.execSQL("DROP TABLE IF EXISTS " + TB_PRODUCT + ";");
            db.execSQL("DROP TABLE IF EXISTS " + TB_CUSTOMER + ";");
            db.execSQL("DROP TABLE IF EXISTS " + TB_ORDERS + ";");
            db.execSQL("DROP TABLE IF EXISTS " + TB_ORDERDETAIL + ";");
            db.execSQL("DROP TABLE IF EXISTS " + TB_ROUTEPLAN + ";");
            db.execSQL("DROP TABLE IF EXISTS " + TB_ACCOUNT + ";");
            db.execSQL("DROP TABLE IF EXISTS " + TB_CONNECTIONCONFIG + ";");
            db.execSQL("DROP TABLE IF EXISTS " + TB_IMAGES + ";");
            onCreate(db);
        }
    }

    private static final String DB_NAME = "SalesMobileAssistant";
    private static final int DB_VERSION = 1;


    public static final String COMPANY = "CompID";

    public static final String TB_EMPLOYEE = "EMPLOYEE";
    public static final String TB_PRODUCT = "PRODUCT";
    public static final String TB_PRODUCTGROUP = "PRODUCTGROUP";
    public static final String TB_PRODUCTINSITE = "PRODUCINSITE";
    public static final String TB_CUSTOMER = "CUSTOMER";
    public static final String TB_ORDERS = "ORDERS";
    public static final String TB_ORDERDETAIL = "ORDERDETAIL";
    public static final String TB_ROUTEPLAN = "ROUTEPLAN";
    public static final String TB_ACCOUNT = "ACCOUNT";
    public static final String TB_CONNECTIONCONFIG = "CONNECTIONCONFIG";
    public static final String TB_IMAGES = "IMAGES";

    public static final String TB_EMPLOYEE_COMPANY = "CompID";
    public static final String TB_EMPLOYEE_TYPEID= "ETypeID";
    public static final String TB_EMPLOYEE_ID = "EmplID";
    public static final String TB_EMPLOYEE_NAME = "EmplName";

    public static final String TB_PRODUCT_COMPANY = "CompID";
    public static final String TB_PRODUCT_TYPE = "PGroupID";
    public static final String TB_PRODUCT_ID = "ProdID";
    public static final String TB_PRODUCT_NAME = "ProdName";
    public static final String TB_PRODUCT_UNITPRICE = "UnitPrice";
    public static final String TB_PRODUCT_UOM = "UOM";
    public static final String TB_PRODUCT_DATEUPDATE = "DateUpdate";

    public static final String TB_PRODUCTINSITE_COMPANY = "CompID";
    public static final String TB_PRODUCTINSITE_SITEID = "SiteID";
    public static final String TB_PRODUCTINSITE_PRODUCTID = "ProdID";
    public static final String TB_PRODUCTINSITE_QUANTITY = "Quantity";
    public static final String TB_PRODUCTINSITE_UOM = "UOM";

    public static final String TB_CUSTOMER_COMPANY_ = "CompID";
    public static final String TB_CUSTOMER_EMPLOYEEID_ = "EmplID";
    public static final String TB_CUSTOMER_ID_ = "CustID";
    public static final String TB_CUSTOMER_NAME = "CustName";
    public static final String TB_CUSTOMER_ADDRESS1 = "Address1";
    public static final String TB_CUSTOMER_ADDRESS2 = "Address2";
    public static final String TB_CUSTOMER_ADDRESS3 = "Address3";
    public static final String TB_CUSTOMER_CITY = "City";
    public static final String TB_CUSTOMER_COUNTRY = "Country";
    public static final String TB_CUSTOMER_PHONENUM = "PhoneNum";
    public static final String TB_CUSTOMER_DISCOUNT = "Discount";

    public static final String TB_ORDER_COMPANY = "CompID";
    public static final String TB_ORDER_MYORDERID = "MyOrderID";
    public static final String TB_ORDER_ORDERID = "OrdeID";
    public static final String TB_ORDER_CUSTOMERID = "CustID";
    public static final String TB_ORDER_EMPLOYEEID = "EmplID";
    public static final String TB_ORDER_ORDERDATE = "OrderDate";
    public static final String TB_ORDER_NEEDBYDATE = "NeedByDate";
    public static final String TB_ORDER_REQUESTDATE = "RequestDate";
    public static final String TB_ORDER_ORDERSTATUS = "OrderStatus";

    public static final String TB_ORDERDETAIL_COMPANY = "CompID";
    public static final String TB_ORDERDETAIL_SITEID = "SiteID";
    public static final String TB_ORDERDETAIL_MYORDERID = "MyOrderID";
    public static final String TB_ORDERDETAIL_ORDERNUM = "OrderNum";
    public static final String TB_ORDERDETAIL_ORDERLINE = "OrderLine";
    public static final String TB_ORDERDETAIL_PRODUCTID = "ProdID";
    public static final String TB_ORDERDETAIL_QUANTITY = "SellingQuantity";
    public static final String TB_ORDERDETAIL_UNITPRICE = "UnitPrice";

    public static final String TB_ROUTEPLAN_COMPANY = "CompID";
    public static final String TB_ROUTEPLAN_EMPLOYEEID = "EmplID";
    public static final String TB_ROUTEPLAN_CUSTOMERID = "CustID";
    public static final String TB_ROUTEPLAN_DATEPLAN = "DatePlan";
    public static final String TB_ROUTEPLAN_PRIORITIZE = "Prioritize";
    public static final String TB_ROUTEPLAN_VISITED = "Visited";
    public static final String TB_ROUTEPLAN_NOTE = "Note";

    public static final String TB_ACCOUNT_COMPANYID = "CompID";
    public static final String TB_ACCOUNT_ID = "ID";
    public static final String TB_ACCOUNT_USERNAME = "Username";
    public static final String TB_ACCOUNT_PASSWORD = "Password";
    public static final String TB_ACCOUNT_TYPE = "Type";
    public static final String TB_ACCOUNT_EMPLOYEEID = "EmplID";
    public static final String TB_ACCOUNT_NOTE = "Note";

    public static final String TB_CONNECTIONCONFIG_API = "EpicorRestAPIpath";
    public static final String TB_CONNECTIONCONFIG_USER = "EpicorUser";
    public static final String TB_CONNECTIONCONFIG_PASS = "EpicorPassword";
    public static final String TB_CONNECTIONCONFIG_COMPANY = "EpicorCompany";

    public static final String TB_IMAGES_COMPANY = "ImageCompany";
    public static final String TB_IMAGES_KEY = "ImageKey";
    public static final String TB_IMAGES_FIENAME = "ImageName";
    public static final String TB_IMAGES_DATA = "ImageData";
}