package com.doannganh.salesmobileassistant.Views.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.Views.activity.MainActivity;
import com.doannganh.salesmobileassistant.Views.activity.NewOrderActivity;
import com.doannganh.salesmobileassistant.Views.customView.CustomDialogWithListViewCustom;
import com.doannganh.salesmobileassistant.util.UtilFilter;
import com.doannganh.salesmobileassistant.util.UtilMethod;
import com.doannganh.salesmobileassistant.model.Custom_list_item;

import java.util.ArrayList;
import java.util.List;

import com.doannganh.salesmobileassistant.Views.adapter.CustomAdapterListView;
import com.doannganh.salesmobileassistant.model.OrderDetail;
import com.doannganh.salesmobileassistant.model.Product;
import com.doannganh.salesmobileassistant.model.ProductInSite;
import com.doannganh.salesmobileassistant.util.ConstantUtil;
import com.doannganh.salesmobileassistant.util.StringUtil;

public class NewOrderProduct extends Fragment {
    Activity context;

    RelativeLayout relativeLayout;
    ListView listView;
    EditText editTextQuantity;
    Button btnAdd;
    public static TextView textView;
    TextView txtTotal;
    Button btnShowProduct;

    List<OrderDetail> orderDetailS; // show order co san
    ArrayList<Custom_list_item> list, listProduct;
    CustomAdapterListView customAdapterListView;
    public static int positionProductTemp = -1; // product chon hien tai
    List<OrderDetail> listOrderDetail; // luu du lieu for sync

    String TAG;
    static double totalMoney = 0.0;
    public static double quantityProductChoose = 0;

    public static double getTotalMoney(){
        return totalMoney;
    }

    public String getTAG() {
        return TAG;
    }

    public static NewOrderProduct myInstance(String tag) {
        NewOrderProduct newOrderProduct = new NewOrderProduct();
        newOrderProduct.TAG = tag;
        return newOrderProduct;
    }

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = new ArrayList<>();
        if(NewOrderActivity.orderDetail != null)
            NewOrderActivity.orderDetail.clear();
        NewOrderActivity.orderDetail = new ArrayList<OrderDetail>();

        listProduct = new ArrayList<>();
        for(Product p : MainActivity.listProductSource){
            ProductInSite pIn = UtilFilter.getProductInSite(MainActivity.listProductInSite, p.getProdID());

            Custom_list_item pNew = new Custom_list_item(p.getProdName()
                    , StringUtil.formatVnCurrence(context, String.valueOf(p.getUnitPrice()))
                    , getString(R.string.neworder_product_textLableQuantity) + UtilMethod.formartDoubleToStringInt(pIn.getQuantity()) + "");

            pNew.setTAG(p.getProdID());

            listProduct.add(pNew);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        if (orderDetailS != null) totalMoney = 0.0;
        relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_new_order_product, null);

        // tham chieu
        AnhXa();

        if(NewOrderActivity.orderShow != null){
            if(listOrderDetail == null) {
                orderDetailS = NewOrderActivity.orderDetailShow;
                if (NewOrderActivity.orderShow.getOrderStatus() > ConstantUtil.DB_ORDER_STATUS_PENDING)
                    btnAdd.setEnabled(false);
            }
        }

        SetTotalMoney(0);

        if(quantityProductChoose != 0)
            textView.setText(getString(R.string.neworder_product_textbeforequantity) + quantityProductChoose);

        KhoiTaoList();

        EventShowProduct();
        EventClickAdd();

        registerForContextMenu(listView);
        return relativeLayout;
    }


    private void KhoiTaoList() {
        customAdapterListView = new CustomAdapterListView(context, list);
        listView.setAdapter(customAdapterListView);

        if(orderDetailS != null){
            List<OrderDetail> odt;
            if(listOrderDetail == null)
                odt = orderDetailS;
            else odt = listOrderDetail;
            list.clear();
            for (OrderDetail o : odt){
                Product p = UtilFilter.getProductByID( MainActivity.listProductSource, o.getProdID());
                if(p == null) continue;
                int qua = (int) o.getSellingQuantity();
                Custom_list_item c = new Custom_list_item(p.getProdName(), qua + ""
                        , StringUtil.formatVnCurrence( context,o.getUnitPrice() + ""));
                c.setTAG(p.getProdID());
                list.add(c);
                SetTotalMoney(p.getUnitPrice() * qua);
            }

            customAdapterListView.notifyDataSetChanged();
        }
    }

    private void AnhXa() {
        listView = relativeLayout.findViewById(R.id.lvNewOrderProduct);
        editTextQuantity = relativeLayout.findViewById(R.id.edtNewOrderProductQuantity);
        btnAdd = relativeLayout.findViewById(R.id.btnNewOrderProductAdd);
        textView = relativeLayout.findViewById(R.id.txtNewOrderProductGetQuantity);
        btnShowProduct = relativeLayout.findViewById(R.id.btnNewOrderProductGetProduct);
        txtTotal = relativeLayout.findViewById(R.id.txtNewOrderProductTotal);
    }

    void SaveToOrderDetail(){
        if(listOrderDetail != null)
            listOrderDetail.clear();
        listOrderDetail = new ArrayList<>();
        int orLine = 1;

        for (int index = 0; index<list.size(); index++){
            OrderDetail orderDetail = new OrderDetail(NewOrderActivity.orderHead.getCompID(),
                    NewOrderActivity.orderHead.getMyOrderID());
            orderDetail.setOrderDetail( orLine, list.get(index).getTAG(),
                    (double) Integer.parseInt(list.get(index).getSubTitle()),
                    StringUtil.parseDoubleFromCurrence(list.get(index).getText()));

            orLine++;
            listOrderDetail.add(orderDetail);
        }

        NewOrderActivity.orderDetail = listOrderDetail;
    }

    private void SetTotalMoney(double money) {
        totalMoney += money;
        txtTotal.setText(StringUtil.formatVnCurrence(context, String.valueOf(totalMoney)));
    }

    private void EventShowProduct() {
        btnShowProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogWithListViewCustom customDialog = new CustomDialogWithListViewCustom(context,
                        listProduct, btnShowProduct.getId());
                customDialog.show();
            }
        });
    }

    private void EventClickAdd() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positionProductTemp == -1) {
                    Toast.makeText(context, R.string.neworder_product_pleaseChooswProduct, Toast.LENGTH_SHORT).show();
                    return;
                }

                final Product pTemp = MainActivity.listProductSource.get(positionProductTemp);
                int quantity = 1;
                try {
                    quantity = Integer.parseInt(editTextQuantity.getText() + "");
                    if(quantity < 1) {
                        Toast.makeText(context, getString(R.string.neworder_product_pleaseChooswProduct), Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    quantity = 1;
                }

                // xu ly luu trung product da ton tai
                if (list.size() >= 1) {
                    for (final Custom_list_item c : list
                    ) {
                        if (c.getTitle().equals(pTemp.getProdName())) {
                            final int index = list.indexOf(c);
                            final int oldQ = Integer.parseInt(c.getSubTitle());
                            final int newQ = oldQ + quantity;

                            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Exists");
                            builder.setMessage(getString(R.string.neworder_product_dialog_message_existsproduct)
                                    + "\nOld: " + oldQ + "\nNew: " + newQ);

                            final int finalQuantity = quantity;
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    list.get(index).setSubTitle(newQ + "");
                                    customAdapterListView.notifyDataSetChanged();

                                    // neu nhap 0
                                    if (oldQ != Integer.parseInt(list.get(index).getSubTitle())) {
                                        SetTotalMoney(pTemp.getUnitPrice() * finalQuantity);
                                    }
                                    //AddToOrderDetail(index);
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                            return;
                        }
                    }
                }

                Custom_list_item c = new Custom_list_item(pTemp.getProdName()
                        , quantity + "", StringUtil.formatVnCurrence(context,pTemp.getUnitPrice() + ""));
                c.setTAG(pTemp.getProdID());
                list.add(c);
                customAdapterListView.notifyDataSetChanged();
                // change money
                SetTotalMoney(pTemp.getUnitPrice() * quantity);
            }
        });
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menucontext_neworderproduct_listview, menu );
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(list.get(info.position).getTitle());
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.menuNewOrderProductEdit:
                // new dialog
                android.support.v7.app.AlertDialog.Builder b = new android.support.v7.app.AlertDialog.Builder(context);
                final View view = context.getLayoutInflater().inflate(R.layout.custom_layout_with_textview_edittext, null);

                b.setView(view);
                b.setTitle("Edit product");
                b.setMessage(list.get(info.position).getTitle());

                TextView tv = view.findViewById(R.id.txtCustomLayoutTextView);
                tv.setText(R.string.neworder_product_inputTextLableQuantity);

                b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                b.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText edt = view.findViewById(R.id.edtCustomLayoutEditText);

                                int quan = Integer.parseInt(list.get(info.position).getSubTitle());
                                double unit = StringUtil.parseDoubleFromCurrence(list.get(info.position).getText());
                                SetTotalMoney(quan*unit*(-1));

                                list.get(info.position).setSubTitle(edt.getText().toString() + "");
                                customAdapterListView.notifyDataSetChanged();

                                int quanN = Integer.parseInt(list.get(info.position).getSubTitle());
                                SetTotalMoney(quanN*unit);
                            }
                        }).create().show();
                break;

            case R.id.menuNewOrderProductDel:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete").setMessage("Are you sure?");
                builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int quan = Integer.parseInt(list.get(info.position).getSubTitle());
                        double unit = StringUtil.parseDoubleFromCurrence(list.get(info.position).getText());
                        SetTotalMoney(quan*unit*(-1));

                        list.remove(info.position);
                        customAdapterListView.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        SaveToOrderDetail();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        totalMoney = 0;
        editTextQuantity.setText("");
    }
}

