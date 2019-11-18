package com.doannganh.salesmobileassistant.Views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.Views.adapter.CustomAdapterListViewWithTextView;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> list;
    CustomAdapterListViewWithTextView arrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        AnhXa();
        KhoiTaoList();
        LoadActionBar();
        arrayAdapter = new CustomAdapterListViewWithTextView(this, R.layout.custom_list_item_with_textview, list);
        listView.setAdapter(arrayAdapter);

        ClickEventListView();
    }

    private void LoadActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(getString(R.string.orders_tag));
    }

    private void ClickEventListView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (position){
                    case 0:
                        intent.setClass( OrdersActivity.this, NewOrderActivity.class);
                        startActivityForResult(intent, 0);
                        break;
                    case 1:
                        intent.setClass(OrdersActivity.this, ListOrdersActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(OrdersActivity.this, ListOrdersActivity.class);
                        intent.putExtra("isSync", true);
                        startActivity(intent);
                        break;
                    case 3:
                        Toast.makeText(OrdersActivity.this, "Developing", Toast.LENGTH_SHORT).show();
                        break;
                        default:return;
                }
            }
        });
    }

    private void KhoiTaoList() {
        list = new ArrayList<>();
        list.add(getString(R.string.orders_item0));
        list.add(getString(R.string.orders_item1));
        list.add(getString(R.string.orders_item2));
        list.add(getString(R.string.orders_item3));
    }

    private void AnhXa() {
        listView = findViewById(R.id.lvOrders);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(OrdersActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
