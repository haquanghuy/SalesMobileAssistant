package com.doannganh.salesmobileassistant.Views.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.Views.util.MyDate;
import com.doannganh.salesmobileassistant.model.MyJob;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>{
    List<MyJob> list;
    List<MyJob> listTemp;

    private OnClickPlus onClickPlus;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnClickPlus onClickPlus;

        // each data item is just a string in this case
        public EditText id;
        public EditText name;
        public EditText address;
        public EditText date;
        public ImageView status;
        public EditText note;
        public MyViewHolder(View view, OnClickPlus onClickPlus) {
            super(view);
            id = view.findViewById(R.id.edtMyJobCustID);
            name = view.findViewById(R.id.edtMyJobCustName);
            address = view.findViewById(R.id.edtMyJobAddress);
            date = view.findViewById(R.id.edtMyJobCustDate);
            note = view.findViewById(R.id.edtMyJobNote);
            status = view.findViewById(R.id.imgMyJobStatus);
            this.onClickPlus = onClickPlus;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickPlus.setOnItemClickListerner(getAdapterPosition());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyRecyclerAdapter(List<MyJob> list, OnClickPlus onClickPlus)
    {
        this.list = list;
        this.listTemp = list;
        this.onClickPlus = onClickPlus;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_list_job_item, parent, false);
        MyViewHolder vh = new MyViewHolder(view, onClickPlus);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        MyJob m = list.get(position);
        holder.id.setText(m.getRoutePlan().getCustID() + "");
        holder.name.setText(m.getCustName());
        holder.address.setText(m.getAddress());

        DateFormat sp = MyDate.df;
        Date d = null;
        try {
            d = sp.parse(m.getRoutePlan().getDatePlan());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.date.setText(sp.format(d));
        //holder.status.setText(m.getRoutePlan().isVisited() + "");

        if(m.getRoutePlan().isVisited())
            holder.status.setBackgroundResource(R.drawable.checked);
        else holder.status.setBackgroundResource(0);
        holder.note.setText(m.getRoutePlan().getNote());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnClickPlus{
        void setOnItemClickListerner(int position);
    }

    public void setMyListTemp(){
        this.listTemp = new ArrayList<>(list);
    }

    public void filterByWeek (CharSequence charSequence){
        String charString = charSequence.toString();
        this.list.clear();

        if (charString.isEmpty()) {
            list.addAll(listTemp);
        } else {
            for (MyJob row : listTemp) {

                MyDate myDate = new MyDate();
                // filter by date
                try {
                    int week = 0;
                    if(!charSequence.equals(""))
                        week = Integer.parseInt(charSequence.toString());

                    String[] date = myDate.getCurrentWeekDate(week);
                    boolean b = myDate.isIn(date, row.getRoutePlan().getDatePlan());

                    if (b) {
                        list.add(row);
                    }

                } catch (Exception e) {
                    Log.d("LLLMyRecyclerDate", e.getMessage());
                }

            }

            notifyDataSetChanged();
        }

    }

    public void filterByDay (){
        list.clear();

        for (MyJob row : listTemp) {

            MyDate myDate = new MyDate();
            // filter by date
            try {
                Date strDate = myDate.df.parse(row.getRoutePlan().getDatePlan());
                Date now = MyDate.df.parse(MyDate.df.format(Calendar.getInstance().getTime()));
                if (now.getTime() == strDate.getTime()) {
                    list.add(row);
                }

            } catch (Exception e) {
                Log.d("LLLMyRecyclerDate", e.getMessage());
            }

        }

        notifyDataSetChanged();
    }



}
