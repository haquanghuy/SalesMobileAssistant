package com.doannganh.salesmobileassistant;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.doannganh.salesmobileassistant.Presenter.ProductPresenter;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        getSupportActionBar().setTitle("Test activity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //MyDate.getCurrentWeekDate(0);
        //MyDate.getCurrentWeekDate(-7);
        //MyDate.getCurrentWeekDate(7);
        MyExec m = new MyExec();
        m.execute();
    }

    class MyExec extends AsyncTask<Void, Void, Integer>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer q) {
            super.onPostExecute(q);
            //TextView textView = findViewById(R.id.abc);
            //textView.setText(s.toString());

            Toast.makeText(TestActivity.this, q + "", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Integer doInBackground(Void... voids) {/*
            HashMap hashMap = new HashMap();
            hashMap.put(RoutePlan.COMPANY, "EPIC06");
            hashMap.put(RoutePlan.EMPLID, "CHUCK");
            hashMap.put(RoutePlan.CUSTID, 86);
            hashMap.put(RoutePlan.DATEPLAN, "2019-10-11T00:00:00");
            hashMap.put(RoutePlan.PRIORITIZE, 1);
            hashMap.put(RoutePlan.VISITED, false);
            hashMap.put(RoutePlan.NOTE, "good");

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date d;
            try {
                d = df.parse( "2019-10-11T00:00:00");
                String url = "https://portal.3ssoft.com.vn:2443/SalesMobileAPI/api/RoutePlan/EPIC06,CHUCK,86,2019-10-11" ;
                return ExecMethodHTTP.putJSONToServer(url, hashMap);
            } catch (ParseException e) {
                Log.d("LLLRoutePlanPutDate", e.getMessage());
            }*/

            ProductPresenter p = ProductPresenter.Instance(getApplicationContext());
            return p.getQuantityCurent("BUNGOI03");
        }
    }

}
