package com.doannganh.salesmobileassistant.Views.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.doannganh.salesmobileassistant.Presenter.OrderPresenter;
import com.doannganh.salesmobileassistant.Presenter.RoutePlanPresenter;
import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.Views.Interface.InterfaceReturnEventListerner;
import com.doannganh.salesmobileassistant.Views.adapter.MyRecyclerAdapter;
import com.doannganh.salesmobileassistant.Views.customView.CustomDialogWithListRadio;
import com.doannganh.salesmobileassistant.util.PermissionUtil;
import com.doannganh.salesmobileassistant.util.MyDate;
import com.doannganh.salesmobileassistant.util.UtilFilter;
import com.doannganh.salesmobileassistant.util.UtilMethod;
import com.doannganh.salesmobileassistant.model.Customer;
import com.doannganh.salesmobileassistant.model.MyJob;
import com.doannganh.salesmobileassistant.model.Order;
import com.doannganh.salesmobileassistant.model.RoutePlan;
import com.doannganh.salesmobileassistant.util.ConstantUtil;

import java.util.ArrayList;
import java.util.List;

public class RoutePlanActivity extends AppCompatActivity
        implements InterfaceReturnEventListerner, MyRecyclerAdapter.OnClickPlus {

    Button btnOption, btnToday, btnThisWeek;
    ImageButton imbRefresh;
    RecyclerView recyclerView;
    TextView txtEmpty, txtJobNum;

    MyRecyclerAdapter adapter;
    List<MyJob> listJob;
    List<String> listOption;
    RoutePlan myJobTemp; // chọn và gán về status true

    int pos = 0; // dia chi gui nhan du lieu
    private RecyclerView.LayoutManager layoutManager;
    String messResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_job);
        if(!messResult.equals(""))
            Toast.makeText(RoutePlanActivity.this, R.string.post_json_success
                + R.string.routeplan_cannot_change_status, Toast.LENGTH_SHORT).show();

        AnhXa();
        LoadActionBar();
        SetListOption();
        LoadListJob();

        EventClickOption();
        EventClickToday();
        EventClickThisWeek();
        EventClickRefresh();

    }

    private void SetListOption() {
        listOption = new ArrayList<>();
        listOption.add(getString(R.string.routeplan_option_all));
        listOption.add(getString(R.string.routeplan_option_justNew));
        btnOption.setText(listOption.get(1));
    }

    private void EventClickRefresh() {
        imbRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(RoutePlanActivity.this, "Reload", Toast.LENGTH_SHORT).show();
                LoadListJob();

                ChangeButtonAppearence(0);
            }
        });
    }

    private void EventClickThisWeek() {
        btnThisWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(RoutePlanActivity.this, "shwo list route in " + calendar.get(Calendar.DAY_OF_MONTH)
                  //      + " day, month " + calendar.get(Calendar.SATURDAY), Toast.LENGTH_SHORT).show();
                adapter.filterByWeek(MyDate.WEEK_NOW + "");
                btnOption.setText(listOption.get(1));
                adapter.filterJustIsActive(true);
                setViewWhenChangeListJob();

                ChangeButtonAppearence(btnThisWeek.getId());
            }
        });
    }

    private void EventClickToday() {
        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(RoutePlanActivity.this, "shwo list route in " + calendar.get(Calendar.DAY_OF_MONTH)
                  //      + " day, month " + calendar.get(Calendar.MONTH), Toast.LENGTH_SHORT).show();
                if(listJob == null) return;
                adapter.filterByDay();
                btnOption.setText(listOption.get(1));
                adapter.filterJustIsActive(true);
                setViewWhenChangeListJob();

                ChangeButtonAppearence(btnToday.getId());
            }
        });
    }

    private void EventClickOption() {
        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogWithListRadio customDialogWithListRadio = new CustomDialogWithListRadio(
                        RoutePlanActivity.this, listOption, btnOption.getId());
                customDialogWithListRadio.setItemChecked(1);
                customDialogWithListRadio.show();
            }
        });
    }

    private void LoadActionBar() {
        if(PermissionUtil.haveNetworkConnection(RoutePlanActivity.this))
            getSupportActionBar().setTitle(getString(R.string.routeplan_tag));
        else getSupportActionBar().setTitle(getString(R.string.routeplan_tag) + " (Offline)");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void ShowJob() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //DividerItemDecoration div = new DividerItemDecoration(this, lay.getOrientation());
        //recyclerView.addItemDecoration(div); //thêm viền ngăn cách các item
        // add id các A vào arr
        adapter = new MyRecyclerAdapter(listJob, this);
        adapter.setMyListTemp();
        recyclerView.setAdapter(adapter);

        adapter.filterJustIsActive(true);
        btnOption.setText(listOption.get(1));
        adapter.notifyDataSetChanged();
    }

    private void LoadListJob() {
        LoadActionBar();

        AsyncTaskGetJSONArray asyncTaskGetJSONArray = new AsyncTaskGetJSONArray();
        asyncTaskGetJSONArray.execute();
    }


    private void AnhXa() {
        btnOption = findViewById(R.id.btnJobByDay);
        recyclerView = findViewById(R.id.my_recycler_view);
        btnToday = findViewById(R.id.btnRoutePlanToday);
        btnThisWeek = findViewById(R.id.btnRoutePlanThisMonth);
        imbRefresh = findViewById(R.id.imbRoutePlanRefresh);
        txtEmpty = findViewById(R.id.txtRoutePlanEmptyView);
        txtJobNum = findViewById(R.id.txtJobNum);
    }

    void ChangeButtonAppearence(int buttonEnable){
        if(buttonEnable == btnToday.getId()){
            btnToday.setEnabled(false);
            btnThisWeek.setEnabled(true);
        } else if(buttonEnable == btnThisWeek.getId()){
            btnToday.setEnabled(true);
            btnThisWeek.setEnabled(false);
        } else {
            btnToday.setEnabled(true);
            btnThisWeek.setEnabled(true);
        }
    }

    @Override
    public void returnEvent(@Nullable int id, int positionSelect) {
        // choose option
        if(id == btnOption.getId()){
            String itemSelect = listOption.get(positionSelect);
            if (itemSelect == getString(R.string.routeplan_option_all)){
                adapter.filterJustIsActive(false);
                btnOption.setText(listOption.get(positionSelect));
            } else if (itemSelect == getString(R.string.routeplan_option_justNew)){
                adapter.filterJustIsActive(true);
                btnOption.setText(listOption.get(positionSelect));
            }
            adapter.notifyDataSetChanged();
            setViewWhenChangeListJob();
        }
    }

    @Override
    public void setOnItemClickListerner(int position) {

        if(listJob.get(position).getRoutePlan().isVisited()==RoutePlan.VISITED){
            RouteVisited(position);
            return;
        }

        postBundle(position);
    }

    private void postBundle(int position) {
        Intent intent1 = new Intent(RoutePlanActivity.this, MapsRoutePlanActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putInt("job_position", position);
        bundle1.putString("job_address", listJob.get(position).getAddress());
        intent1.putExtras(bundle1);
        startActivityForResult(intent1, ConstantUtil.REQUEST_CODE_111);

    }

    private void RouteVisited(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RoutePlanActivity.this);
        builder.setTitle("Exists");
        builder.setMessage(getString(R.string.routeplan_plan_check));

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                postBundle(pos);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == ConstantUtil.REQUEST_CODE_111) {
            int position = data.getExtras().getInt("job_position", -1);
            if(position == -1){
                Toast.makeText(this, "ERROR: Can't go to create order.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, NewOrderActivity.class);
            // get offline
            OrderPresenter orderPresenter = OrderPresenter.Instance(getApplicationContext());
            Order o = orderPresenter.getOrderFromDBJob(listJob.get(position).getRoutePlan().getCustID(),
                    listJob.get(position).getRoutePlan().getDatePlan());

            if (o != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("JobOrder", o);
                bundle.putString("DatePlan", listJob.get(position).getRoutePlan().getDatePlan());
                intent.putExtra("bundle", bundle);
                pos = position;
                myJobTemp = listJob.get(position).getRoutePlan();
                startActivityForResult(intent, ConstantUtil.REQUEST_CODE_222);
            } else {
                Customer customer = UtilFilter.getCustomerByID(
                        MainActivity.listCustomer, listJob.get(position).getRoutePlan().getCustID());
                Bundle bundle = new Bundle();
                bundle.putSerializable("Customer", customer);
                bundle.putString("DatePlan", listJob.get(position).getRoutePlan().getDatePlan());
                intent.putExtra("bundle", bundle);
                pos = position;
                myJobTemp = listJob.get(position).getRoutePlan();
                startActivityForResult(intent, ConstantUtil.REQUEST_CODE_222);
            }

        } else if (requestCode == ConstantUtil.REQUEST_CODE_222) {
            if(resultCode == Activity.RESULT_OK){
                final int visit = data.getIntExtra("isFinish", RoutePlan.UNVISIT);

                if(visit == RoutePlan.UNVISIT){
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AsyncTaskPutRoutePlan a = new AsyncTaskPutRoutePlan();
                        a.execute(visit);
                    }
                });
            }
        }
    }

    public class AsyncTaskGetJSONArray extends AsyncTask<Void, Void, Boolean> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(RoutePlanActivity.this);
            progressDialog.setMessage(getString(R.string.dialog_init));
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            List<RoutePlan> listPlan;
            RoutePlanPresenter routePlanPresenter = RoutePlanPresenter.Instance(getApplicationContext());
            if(PermissionUtil.haveNetworkConnection(RoutePlanActivity.this)) {
                listPlan = routePlanPresenter.getListRoutePlan(MainActivity.account.getEmplID());
            } else {
                // get from db
                listPlan = routePlanPresenter.getListRoutePlanFromDB(MainActivity.account.getEmplID());
            }

            if (listPlan == null || listPlan.size() < 1) {
                return false;
            }
            listJob = new ArrayList<>();

            for (RoutePlan r : listPlan) {
                Customer c = UtilFilter.getCustomerByID(MainActivity.listCustomer, r.getCustID());
                if (c == null)
                    Log.d("RoutePlanActivity", "Can't determined this customer: " + r.getCustID());
                else {
                    RoutePlan re = routePlanPresenter.saveRoutePlanToDB(r);
                    listJob.add(new MyJob(re, c.getCustName(), c.getAddress1() + ", " + c.getAddress2()));
                }

            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool) {
                ShowJob();
            }
            setViewWhenChangeListJob();

            progressDialog.dismiss();

            UtilMethod.hideKeyboardFrom(RoutePlanActivity.this);
        }
    }

        public class AsyncTaskPutRoutePlan extends AsyncTask<Integer, Void, Boolean> {

            private ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {/*
                progressDialog = new ProgressDialog(RoutePlanActivity.this);
                progressDialog.setMessage("Refreshing your job...");
                progressDialog.setCancelable(false);
                progressDialog.show();*/
            }

            @Override
            protected Boolean doInBackground(Integer... integers) {
                // check for safe
                if(integers[0] == RoutePlan.UNVISIT) return false;

                RoutePlanPresenter routePlanPresenter = RoutePlanPresenter.Instance(getApplicationContext());
                // save to db
                myJobTemp.setVisited(integers[0]);
                long num = routePlanPresenter.setRoutePlanToDB(myJobTemp);
                if(num == -1){
                 // delete this job
                }

                // put api
                if(PermissionUtil.haveNetworkConnection(getApplicationContext()) && integers[0] == RoutePlan.VISITED)
                    return routePlanPresenter.setMadeOrder(myJobTemp);
                else return num > 0;
            }

            @Override
            protected void onPostExecute(Boolean bool) {
                if (bool) {
                    listJob.get(pos).getRoutePlan().setVisited(myJobTemp.isVisited());
                    adapter.notifyDataSetChanged();
                } else
                    messResult = getString(R.string.post_json_success) + getString(R.string.routeplan_cannot_change_status);
            }
    }

    public void setViewWhenChangeListJob(){
        int num = 0;
        if (listJob != null) {
            if(listJob.size()!=0) {
                recyclerView.setVisibility(View.VISIBLE);
                txtEmpty.setVisibility(View.GONE);
            }
            else{
                recyclerView.setVisibility(View.GONE);
                txtEmpty.setVisibility(View.VISIBLE);
            }
            num = listJob.size();
        }
        else{
            recyclerView.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        }
        txtJobNum.setText(String.valueOf(num));
    }
}
