package com.doannganh.salesmobileassistant.Views.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.doannganh.salesmobileassistant.Presenter.CustomerPresenter;
import com.doannganh.salesmobileassistant.Presenter.ImagesPresenter;
import com.doannganh.salesmobileassistant.Presenter.OrderDetailPresenter;
import com.doannganh.salesmobileassistant.Presenter.OrderPresenter;
import com.doannganh.salesmobileassistant.Presenter.ProductInSitePresenter;
import com.doannganh.salesmobileassistant.Presenter.ProductPresenter;
import com.doannganh.salesmobileassistant.Presenter.RoutePlanPresenter;
import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.TestActivity;
import com.doannganh.salesmobileassistant.Views.MainIconFunction;
import com.doannganh.salesmobileassistant.Views.adapter.CustomAdapterGridView;
import com.doannganh.salesmobileassistant.util.PermissionUtil;
import com.doannganh.salesmobileassistant.util.ImageUtil;
import com.doannganh.salesmobileassistant.util.LanguageChange;
import com.doannganh.salesmobileassistant.model.Account;
import com.doannganh.salesmobileassistant.model.Custom_grid_item;
import com.doannganh.salesmobileassistant.model.Customer;
import com.doannganh.salesmobileassistant.model.ImagesDB;
import com.doannganh.salesmobileassistant.model.Order;
import com.doannganh.salesmobileassistant.model.OrderDetail;
import com.doannganh.salesmobileassistant.model.Product;
import com.doannganh.salesmobileassistant.model.ProductInSite;
import com.doannganh.salesmobileassistant.model.RoutePlan;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public volatile static Account account;
    //MainIconFunction icon1, icon2, icon3, icon4, icon5, icon6;
    ViewFlipper viewFlipper;
    TextView title;
    GridView gridView;
    DrawerLayout drawerLayout;

    public static List<Customer> listCustomer;
    public static List<Product> listProductSource; // new order product
    public static List<ProductInSite> listProductInSite;
    CustomerPresenter customerPresenter;
    List<String> listAds;
    public static String employeeName;
    boolean coutRe = false; // khi logout lai

    private GestureDetector productGestureDetector; // scroll viewflipper

    ArrayList<Custom_grid_item> list;
    CustomAdapterGridView customAdapterGridView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadHistory();
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        GetActi(); // change config and close

        AnhXa();

        Login();

        // get lai giao dien khi back
        if(account != null){//if(listCustomer != null && account != null){
            LoadInfo();
        }

        LoadMainFunction();

        OnClickEachOneIcon();


    }

    private void LoadHistory() {
        LanguageChange languageChange;
        languageChange = new LanguageChange(MainActivity.this);
        languageChange.loadLocale();
    }

    private void GetActi() {
        Intent intent = getIntent();
        boolean isRes = intent.getBooleanExtra("isRestart", false);
        if(isRes) finish();
    }

    private void LoadInfo() {
        LoadHeaderNavigation();
        // load toolbar
        LoadToolbar();

        LoadViewFlipper();

        // loa title
        title = findViewById(R.id.txtMainTitle);
        SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
        title.setText(df.format(Calendar.getInstance().getTime()));
    }

    private void LoadViewFlipper() {
        final ImagesPresenter imagesPresenter = ImagesPresenter.Instance(getApplicationContext());
        // load img and name
        if(PermissionUtil.haveNetworkConnection(getApplicationContext())) {
            listAds = new ArrayList<>();
            listAds.add("https://acecookvietnam.vn/wp-content/uploads/2017/08/BANNER-MI-LAU-THAI-1440x550.png");
            listAds.add("http://kenh14cdn.com/crop/640_360/2018/2018-photo-1-1546082632100785856682-250-53-1396-1886-crop-1546083078181-636817334373248437.jpg");
            listAds.add("https://acecookvietnam.vn/wp-content/uploads/2019/04/KEV_9394.jpg");
            listAds.add("https://acecookvietnam.vn/wp-content/uploads/2017/08/BANNER-MI-DE-NHAT-1440x550.png");

            for (int i = 0; i < listAds.size(); i++) {
                final ImageView imageView = new ImageView(getApplicationContext()); // storage img
                // for save
                int indexStart = listAds.get(i).lastIndexOf("/");
                final String namePic = listAds.get(i).substring(indexStart + 1);

                Glide.with(getApplicationContext())
                        .load(listAds.get(i))
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>(100, 100) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                imageView.setImageBitmap(resource);
                                viewFlipper.addView(imageView);

                                //save image to db
                                imagesPresenter.saveImageToDB(new ImagesDB(account.getCompany()
                                        , ImagesPresenter.KEY_IMAGES_HEADER_MAIN
                                        , namePic
                                        , ImageUtil.getBytes(resource)));
                            }
                        });

            }
        } else {
            // get from db
            List<ImagesDB> imageList = imagesPresenter.getImagesFromDB(
                    account.getCompany(), ImagesPresenter.KEY_IMAGES_HEADER_MAIN);
            for (ImagesDB img : imageList) {
                ImageView imageView = new ImageView(getApplicationContext()); // storage img
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageBitmap(ImageUtil.getImage(img.getImageData()));
                viewFlipper.addView(imageView);
            }
        }

        viewFlipper.setFlipInterval(5000);// chay trong 5s
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);

        // su kien cham viewflpper onTouchEvent

    }

    private void LoadHeaderNavigation() {
        NavigationView ngvNavigationView =
                findViewById(R.id.ngvNavi);
        ngvNavigationView.removeHeaderView(ngvNavigationView.getHeaderView(0));
        ngvNavigationView.setNavigationItemSelectedListener(this);
        View v = ngvNavigationView.inflateHeaderView(R.layout.navigation_header);
        TextView txtName = v.findViewById(R.id.txtNaviHeader);
        TextView txtDes = v.findViewById(R.id.txtNaviSubtext);
        if(account.isType())
            txtDes.setText(getString(R.string.navihead_salesmanage));
        else txtDes.setText(getString(R.string.navihead_salesman));
        txtName.setText(employeeName);
        ngvNavigationView.setItemIconTintList(null);
    }

    private void LoadToolbar() {
        drawerLayout = this.findViewById(R.id.layoutDrawer);// for below
        Toolbar toolbar = findViewById(R.id.tlbHome);

        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Hiển thị Drawer khi button được nhấn
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.main_actionbar_open, R.string.main_actionbar_close);
        if (drawerLayout != null)
            drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void OnClickEachOneIcon() {/*
        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeActivity(OrdersActivity.class);
            }
        });

        icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeActivity(PartsActivity.class);
            }
        });

        icon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeActivity(RoutePlanActivity.class);
            }
        });

        icon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "4", Toast.LENGTH_SHORT).show();
                ChangeActivity(TestActivity.class);
            }
        });

        icon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "5", Toast.LENGTH_SHORT).show();
                List<String> l = new ArrayList<>();
                for(int i=0; i<32; i++){
                    l.add(i + "");

                }
            }
        });

        icon6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeActivity(SettingActivity.class);
            }
        });*/

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        ChangeActivity(OrdersActivity.class);
                        break;
                    case 1:
                        ChangeActivity(PartsActivity.class);
                        break;
                    case 2:
                        ChangeActivity(RoutePlanActivity.class);
                        break;/*
                    case 3:
                        Toast.makeText(MainActivity.this, "Developing", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(MainActivity.this, "Developing", Toast.LENGTH_SHORT).show();
                        break;*/
                    case 3:
                        ChangeActivity(SettingActivity.class);
                        break;
                }
            }
        });

        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }
        });
    }

    private void ChangeActivity(java.lang.Class c){
        Intent intent = new Intent(MainActivity.this, c);
        startActivity(intent);
    }

    private void LoadMainFunction() {/*
        LoadIconFunction(BitmapFactory.decodeResource( getResources()
                , R.drawable.settup), getString(R.string.main_icon_func1), icon1);
        LoadIconFunction(BitmapFactory.decodeResource( getResources()
                , R.drawable.settup), getString(R.string.main_icon_func2), icon2);
        LoadIconFunction(BitmapFactory.decodeResource( getResources()
                , R.drawable.settup), getString(R.string.main_icon_func3), icon3);
        LoadIconFunction(BitmapFactory.decodeResource( getResources()
                , R.drawable.settup), getString(R.string.main_icon_func4), icon4);
        LoadIconFunction(BitmapFactory.decodeResource( getResources()
                , R.drawable.settup), getString(R.string.main_icon_func5), icon5);
        LoadIconFunction(BitmapFactory.decodeResource( getResources()
                , R.drawable.settup), getString(R.string.main_icon_func6), icon6);*/

        LoadGridView();

    }

    private void LoadGridView() {
        list = new ArrayList<>();
        list.add(new Custom_grid_item(R.drawable.order, getString(R.string.main_icon_func1)));
        list.add(new Custom_grid_item(R.drawable.product, getString(R.string.main_icon_func2)));
        list.add(new Custom_grid_item(R.drawable.myjob, getString(R.string.main_icon_func3)));
        //list.add(new Custom_grid_item(R.drawable.settup, getString(R.string.main_icon_func4)));
        //list.add(new Custom_grid_item(R.drawable.settup, getString(R.string.main_icon_func5)));
        list.add(new Custom_grid_item(R.drawable.setting, getString(R.string.main_icon_func6)));

        customAdapterGridView = new CustomAdapterGridView(this, R.layout.custom_grid_item, list);
        gridView.setAdapter(customAdapterGridView);
    }


    private void LoadIconFunction(Bitmap bitmap, String text, MainIconFunction icon) {
        icon.setImageResource(bitmap);
        icon.setText(text);
    }

    private void AnhXa() {/*
        icon1 = findViewById(R.id.mainIcon1);
        icon2 = findViewById(R.id.mainIcon2);
        icon3 = findViewById(R.id.mainIcon3);
        icon4 = findViewById(R.id.mainIcon4);
        icon5 = findViewById(R.id.mainIcon5);
        icon6 = findViewById(R.id.mainIcon6);*/

        viewFlipper = findViewById(R.id.viewFlipper);

        gridView = findViewById(R.id.grvMain);

    }

    private void Login() {
        if(account == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, 101);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            Bundle myResultBundle = data.getBundleExtra("bundle");
            account = (Account) myResultBundle.getSerializable("account");
            employeeName = myResultBundle.getString("EmployeeName");
            if(!coutRe){
                LoadInfo();
                coutRe = true;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AsyncTaskGetJSONArray asyncTaskGetJSONArray = new AsyncTaskGetJSONArray();
                    asyncTaskGetJSONArray.execute();
                }
            });
        }
        else finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        switch (menuItem.getItemId()) {

            case R.id.menuHome: {
                break;
            }
            case R.id.menuHome1: {
                startActivity(new Intent(MainActivity.this, TestActivity.class));
                break;
            }
            case R.id.menuHomeLogout:
                account = null;
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                account=null;
                i.putExtra("isLogout", true);
                startActivityForResult(i, 101);
                //finish();
                break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public class AsyncTaskGetJSONArray extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage(getString(R.string.dialog_init));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            customerPresenter = CustomerPresenter.Instance(getApplicationContext());
            ProductPresenter productPresenter = ProductPresenter.Instance(getApplicationContext());//get list product name from db
            ProductInSitePresenter p = ProductInSitePresenter.Instance(getApplicationContext());
            OrderPresenter orderPresenter = OrderPresenter.Instance(getApplicationContext());
            OrderDetailPresenter orderDetailPresenter = OrderDetailPresenter.Instance(getApplicationContext());
            RoutePlanPresenter routePlanPresenter = RoutePlanPresenter.Instance(getApplicationContext());

            if(PermissionUtil.haveNetworkConnection(getApplicationContext())) {
                try {
                    listCustomer = customerPresenter.GetListCustomer(account.getEmplID());

                    listProductSource = productPresenter.GetListProduct();

                    listProductInSite = p.getListProductInSite();

                    List<Order> listOrder = orderPresenter.getListOrderFromAPI(account.getEmplID());

                    if(listOrder != null) {
                        for (Order o : listOrder) {
                            orderPresenter.saveOrderToDB(o);

                            List<OrderDetail> listOrderDetail = orderDetailPresenter.getListOrderDetail(o.getMyOrderID());
                            orderDetailPresenter.saveListOrderDetailToDB(listOrderDetail);
                        }
                    }

                    List<RoutePlan> routePlanList = routePlanPresenter.getListRoutePlan(account.getEmplID());
                    if(routePlanList != null) {
                        for (RoutePlan r : routePlanList)
                            routePlanPresenter.saveRoutePlanToDB(r);
                    }

                    // save db
                    customerPresenter.saveCustomerToDB(listCustomer);
                    productPresenter.saveProductToDB(listProductSource);
                    p.saveProductInSiteToDB(listProductInSite);
                } catch (Exception e) {
                    Log.d("LLLMainActGetInit", e.getMessage());
                    onBackPressed();
                }
            } else {
                listCustomer = customerPresenter.getListCustomerFromDB(account.getEmplID());

                listProductSource = productPresenter.getListProductFromDB(account.getCompany());

                listProductInSite = p.getListProductInSiteFromDB();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            progressDialog.dismiss();

        }
    }
}
