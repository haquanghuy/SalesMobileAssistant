package com.doannganh.salesmobileassistant.Views.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doannganh.salesmobileassistant.Presenter.OrderPresenter;
import com.doannganh.salesmobileassistant.Presenter.RoutePlanPresenter;
import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.Views.Interface.InterfaceReturnEventListerner;
import com.doannganh.salesmobileassistant.Views.adapter.MyRecyclerAdapter;
import com.doannganh.salesmobileassistant.Views.customView.CustomDialogWithListRadio;
import com.doannganh.salesmobileassistant.Views.util.UtilFilter;
import com.doannganh.salesmobileassistant.model.Customer;
import com.doannganh.salesmobileassistant.model.MyJob;
import com.doannganh.salesmobileassistant.model.Order;
import com.doannganh.salesmobileassistant.model.RoutePlan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class RoutePlanActivity extends AppCompatActivity
        implements InterfaceReturnEventListerner, MyRecyclerAdapter.OnClickPlus {

    Button btnDay, btnToday, btnThisWeek;
    ImageButton imbRefresh;
    RecyclerView recyclerView;
    TextView txtEmpty;

    MyRecyclerAdapter adapter;
    List<MyJob> listJob;
    List<String> listDay;
    RoutePlan myJobTemp; // chọn và gán về status true

    int pos = 0; // dia chi gui nhan du lieu
    private RecyclerView.LayoutManager layoutManager;
    String messResult = "";

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_job);
        if(!messResult.equals(""))
            Toast.makeText(RoutePlanActivity.this, R.string.post_json_success
                + R.string.routeplan_cannot_change_status, Toast.LENGTH_SHORT).show();

        AnhXa();
        LoadActionBar();
        SetListDay();
        LoadListJob();

        EventClickDay();
        EventClickToday();
        EventClickThisWeek();
        EventClickRefresh();

    }

    private void SetListDay() {
        listDay = new ArrayList<>();
        listDay.add("--Select day--");
        for(int i=1; i<calendar.getActualMaximum(Calendar.DAY_OF_MONTH)+1; i++){
            listDay.add(i + "");
        }
        btnDay.setText(listDay.get(0));
    }

    private void EventClickRefresh() {
        imbRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(RoutePlanActivity.this, "Reload", Toast.LENGTH_SHORT).show();

                LoadListJob();
            }
        });
    }

    private void EventClickThisWeek() {
        btnThisWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(RoutePlanActivity.this, "shwo list route in " + calendar.get(Calendar.DAY_OF_MONTH)
                  //      + " day, month " + calendar.get(Calendar.SATURDAY), Toast.LENGTH_SHORT).show();
                adapter.filterByWeek("0");
                setViewWhenListJobNull();
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
                setViewWhenListJobNull();
            }
        });
    }

    private void EventClickDay() {
        btnDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogWithListRadio customDialogWithListRadio = new CustomDialogWithListRadio(
                        RoutePlanActivity.this, listDay, btnDay.getId());
                customDialogWithListRadio.show();
            }
        });
    }

    private void LoadActionBar() {
        getSupportActionBar().setTitle(getString(R.string.routeplan_tag));
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
    }

    private void LoadListJob() {
                AsyncTaskGetJSONArray asyncTaskGetJSONArray = new AsyncTaskGetJSONArray();
                asyncTaskGetJSONArray.execute();

    }


    private void AnhXa() {
        btnDay = findViewById(R.id.btnJobByDay);
        recyclerView = findViewById(R.id.my_recycler_view);
        btnToday = findViewById(R.id.btnRoutePlanToday);
        btnThisWeek = findViewById(R.id.btnRoutePlanThisMonth);
        imbRefresh = findViewById(R.id.imbRoutePlanRefresh);
        txtEmpty = findViewById(R.id.txtRoutePlanEmptyView);
    }

    @Override
    public void returnEvent(@Nullable int id, int positionSelect) {
        if(id == btnDay.getId()){
            int day = 0;
            try {
                day = Integer.parseInt(listDay.get(positionSelect));
                FilterByDay(day);
            } catch (NumberFormatException e) {
                btnDay.setText(listDay.get(positionSelect) + "");
            }
            if(day==0)
                btnDay.setText(listDay.get(0));
        }
    }

    private void FilterByDay(int day) {
        Toast.makeText(this, "show list route in " + day + " day, month " + calendar.get(Calendar.MONTH)
                , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setOnItemClickListerner(int position) {

        if(listJob.get(position).getRoutePlan().isVisited()){
            RouteVisited(position);
            return;
        }

        postBundle(position);
    }

    private void postBundle(int position) {
        Intent intent = new Intent(this, NewOrderActivity.class);
        // get offline
        OrderPresenter orderPresenter = OrderPresenter.Instance(getApplicationContext());
        Order o = orderPresenter.getOrderFromDBJob(listJob.get(position).getRoutePlan().getDatePlan());
        if(o != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("JobOrder", o);
            bundle.putString("DatePlan", listJob.get(position).getRoutePlan().getDatePlan());
            intent.putExtra("bundle", bundle);
            pos = position;
            myJobTemp = listJob.get(position).getRoutePlan();
            startActivityForResult(intent, pos);
        }
        else {
            Customer customer = UtilFilter.getCustomerByID(
                    MainActivity.listCustomer, listJob.get(position).getRoutePlan().getCustID());
            Bundle bundle = new Bundle();
            bundle.putSerializable("Customer", customer);
            bundle.putString("DatePlan", listJob.get(position).getRoutePlan().getDatePlan());
            intent.putExtra("bundle", bundle);
            pos = position;
            myJobTemp = listJob.get(position).getRoutePlan();
            startActivityForResult(intent, pos);
        }
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

        if (requestCode == pos) {
            if(resultCode == Activity.RESULT_OK){
                boolean isfinish =data.getBooleanExtra("isFinish", false);
                if(isfinish){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AsyncTaskPutRoutePlan a = new AsyncTaskPutRoutePlan();
                            a.execute();
                        }
                    });

                }
            }
        }
    }

    public class AsyncTaskGetJSONArray extends AsyncTask<Void, Void, Boolean> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(RoutePlanActivity.this);
            progressDialog.setMessage(getString(R.string.dialog_init));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            RoutePlanPresenter routePlanPresenter = RoutePlanPresenter.Instance(getApplicationContext());
            List<RoutePlan> listPlan = routePlanPresenter.GetListRoutePlan(MainActivity.account.getEmplID());

            if (listPlan == null || listPlan.size() < 1) {
                return false;

            }
            listJob = new ArrayList<>();

            for (RoutePlan r : listPlan) {
                Customer c = UtilFilter.getCustomerByID(MainActivity.listCustomer, r.getCustID());
                if (c == null)
                    Toast.makeText(RoutePlanActivity.this, "Can't determined this customer",
                            Toast.LENGTH_SHORT).show();
                else
                    listJob.add(new MyJob(r, c.getCustName(), c.getAddress1() + " " + c.getAddress2()));
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean abool) {

            setViewWhenListJobNull();
            if (abool) {
                ShowJob();
            }
                //Toast.makeText(RoutePlanActivity.this, "No job", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

        public class AsyncTaskPutRoutePlan extends AsyncTask<Void, Void, Boolean> {

            private ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {/*
                progressDialog = new ProgressDialog(RoutePlanActivity.this);
                progressDialog.setMessage("Refreshing your job...");
                progressDialog.setCancelable(false);
                progressDialog.show();*/
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                RoutePlanPresenter routePlanPresenter = RoutePlanPresenter.Instance(getApplicationContext());
                return routePlanPresenter.setMadeOrder(myJobTemp);
            }

            @Override
            protected void onPostExecute(Boolean abool) {

                if(abool) {

                    listJob.get(pos).getRoutePlan().setVisited(true);
                    adapter.notifyDataSetChanged();
                }
                else
                    messResult = getString(R.string.post_json_success) + getString(R.string.routeplan_cannot_change_status);
                //Toast.makeText(RoutePlanActivity.this, R.string.post_json_success
                //    + R.string.routeplan_cannot_change_status, Toast.LENGTH_SHORT).show();
            }
    }

    public void setViewWhenListJobNull(){
        if (listJob != null) {
            if(listJob.size()!=0) {
                recyclerView.setVisibility(View.VISIBLE);
                txtEmpty.setVisibility(View.GONE);
            }
            else{
                recyclerView.setVisibility(View.GONE);
                txtEmpty.setVisibility(View.VISIBLE);
            }
        }
        else{
            recyclerView.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        }
    }
}
