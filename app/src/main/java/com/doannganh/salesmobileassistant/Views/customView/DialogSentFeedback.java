package com.doannganh.salesmobileassistant.Views.customView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doannganh.salesmobileassistant.R;

import static android.app.Activity.RESULT_OK;

public class DialogSentFeedback extends DialogFragment {
    TextView btnClose, btnClearAll, btnSubmit, txtShowPath;
    Button btnChooseImage;
    EditText edtMessage;

    //Được dùng khi khởi tạo dialog mục đích nhận giá trị
    public static DialogSentFeedback newInstance(String data) {
        DialogSentFeedback dialog = new DialogSentFeedback();
        Bundle args = new Bundle();
        args.putString("data", data);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_sent_feedback, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCancelable(false);

        btnClose = view.findViewById(R.id.txtDialogSentClose);
        btnClearAll = view.findViewById(R.id.txtDialogSentClear);
        btnSubmit = view.findViewById(R.id.txtDialogSentSubmit);
        btnChooseImage = view.findViewById(R.id.btnDialogSentChoose);
        edtMessage = view.findViewById(R.id.edtDialogSentMessage);
        txtShowPath = view.findViewById(R.id.txtDialogSentShowPath);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtMessage.setText("");
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), R.string.feedback_senok, Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                getActivity().startActivityForResult(pickPhoto, 123);//one can be replaced with any action code
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //Uri selectedImage = imageReturnedIntent.getData();
            //imageview.setImageURI(selectedImage);
            Toast.makeText(getActivity(), "hello co hinh roi", Toast.LENGTH_SHORT).show();
            txtShowPath.setText("path--------path");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

