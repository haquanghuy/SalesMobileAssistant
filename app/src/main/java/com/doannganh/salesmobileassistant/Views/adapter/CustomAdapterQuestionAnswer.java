package com.doannganh.salesmobileassistant.Views.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.model.Custom_question_answer;

import java.util.List;

public class CustomAdapterQuestionAnswer extends BaseAdapter implements View.OnFocusChangeListener {
    Activity activity;
    int layout;
    List<Custom_question_answer> list;
    int position = -1;

    OnQAListener onQAListener = null;

    TextView question;
    EditText answer;

    public CustomAdapterQuestionAnswer(@NonNull Activity activity, @NonNull List<Custom_question_answer> objects) {
        this.activity = activity;
        this.layout = R.layout.custom_list_item_question_answer;
        this.list = objects;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(layout, null);

        question = view.findViewById(R.id.txtItemQuestion);
        answer = view.findViewById(R.id.edtItemAnswer);

        Custom_question_answer c = list.get(position);
        question.setText(c.getQuestion());
        answer.setText(c.getAnswer());
        this.position = position;

        answer.setOnFocusChangeListener(this);
        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v.getId() == answer.getId()){
            list.get(position).setAnswer(((EditText) v).getText().toString());
            answer.setText(((EditText) v).getText());
            onQAListener.onAnswerFocusChange(position, ((EditText) v).getText().toString());
        }
        //notifyDataSetChanged();
    }

    public void setOnQAListener(OnQAListener onQAListener){
        this.onQAListener = onQAListener;
    }

    public interface OnQAListener{
        void onAnswerFocusChange(int position, String stringChange);
    }
}
