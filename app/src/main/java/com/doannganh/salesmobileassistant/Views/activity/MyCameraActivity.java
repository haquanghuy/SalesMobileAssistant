package com.doannganh.salesmobileassistant.Views.activity;


import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.doannganh.salesmobileassistant.Manager.OcrManager;
import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.Views.fragment.CameraSourceFragment;
import com.doannganh.salesmobileassistant.util.ConstantUtil;
import com.doannganh.salesmobileassistant.util.PermissionUtil;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class MyCameraActivity extends AppCompatActivity {
    // key INTENT_FLAP  : value 3 orther
    public static final String INTENT_FLAP = "flap_intent";
    public static final String SHORT_TEXT_TITLE = "text_title";
    public static final String IS_AUTOSCAN = "is_auto_scan";
    public static final String IS_AUTORETURN = "is_auto_return";

    private boolean isFirstPermission = false;

    public enum ACTION {
        FLAP_BASE("flap_base"),
        FLAP_SCAN_QR("flap_scan_qr"),
        FLAP_DETECT_TEXT("flap_detect_text"),
        RETURN_VALUE_TEXT_DETECT("text_detect"),
        RETURN_VALUE_IMAGE_DETECT("image_byte_array")
        ;

        private final String text;

        ACTION(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    String flapType;

    TextView txtBaseText;
    Button btnTryAgain;
    ImageButton btnBack;
    ImageView imageView;
    FrameLayout container, frameLayoutRoot;

    // test
    Button load, phantich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        getSupportActionBar().hide();
        imageView = findViewById(R.id.test);

        // ------------------------------------
        load = findViewById(R.id.btnLoad);
        phantich = findViewById(R.id.btnPhantich);

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 88);
            }
        });

        phantich.setText("Return");
        phantich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getResult("No result");
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        // ------------------------------------------------------------------------

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txtBaseText = findViewById(R.id.txtBaseText);
        btnTryAgain = findViewById(R.id.btnCameraTryAgain);
        btnBack = findViewById(R.id.btnBack);
        container = findViewById(R.id.frameLayourContainer);
        frameLayoutRoot = findViewById(R.id.frameLayoutRoot);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            flapType = bundle.getString(INTENT_FLAP, ACTION.FLAP_BASE.toString());
        }

        EventClickTryAgain();
        EventClickBack();

        //Check camera permission
        if (!PermissionUtil.checkPermission(getApplicationContext(), Manifest.permission.CAMERA)){
            isFirstPermission = true;
            PermissionUtil.showDialogPermission(this, ConstantUtil.REQUEST_CODE_111, new String[]{Manifest.permission.CAMERA});
            SetEnableView(false);
        } else {
            LoadFragment();
        }
    }

    private void EventClickBack() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void EventClickTryAgain() {
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFirstPermission){
                    //Check camera permission
                    if (PermissionUtil.checkPermission(getApplicationContext(), Manifest.permission.CAMERA)){
                        isFirstPermission = false;
                        SetEnableView(true);
                        LoadFragment();
                    } else {
                        PermissionUtil.showDialogPermission(MyCameraActivity.this, ConstantUtil.REQUEST_CODE_111, new String[]{Manifest.permission.CAMERA});
                    }
                } else LoadFragment();
            }
        });
    }

    private void SetEnableView(boolean b) {
        int val = b?View.VISIBLE:View.GONE;
        imageView.setVisibility(val);
        load.setVisibility(val);
        phantich.setVisibility(val);
    }

    public void huy(Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }

    private void LoadFragment() {
//        clearStack();
//        container.removeAllViews();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        CameraSourceFragment cameraFragment = (CameraSourceFragment) getFragmentManager().findFragmentByTag(flapType);
        if (cameraFragment != null) {
            //fragmentTransaction.replace(R.id.frameLayourNewOrder, fragmentThree, fragmentThree.getTAG());

        } else {
            cameraFragment = CameraSourceFragment.newInstance(flapType);
            cameraFragment.setArguments(getIntent().getExtras());
            fragmentTransaction.replace(container.getId(), cameraFragment, cameraFragment.getTAG());
            fragmentTransaction.addToBackStack(cameraFragment.getTAG());
            fragmentTransaction.commit();
        }

        if(container.getVisibility() == View.GONE){
            container.setVisibility(View.VISIBLE);
            frameLayoutRoot.setVisibility(View.GONE);
        }
    }

    public void clearStack() { // khong su dung duoc
        //Here we are clearing back stack fragment entries
        int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }
        //Here we are removing all the fragment that are shown here
        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
            for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
                Fragment mFragment = getSupportFragmentManager().getFragments().get(i);
                if (mFragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(mFragment).commit();
                }
            }
        }
    }

    public void setResult(String s){
        if(s != null)
            txtBaseText.setText(s);
    }

    public void setResult(Bundle bundle){
        if(bundle != null) {
            // set text detect
            setResult(bundle.getString(MyCameraActivity.ACTION.RETURN_VALUE_TEXT_DETECT.toString(), ""));

            // set image
            byte[] bytes = bundle.getByteArray(ACTION.RETURN_VALUE_IMAGE_DETECT.toString());
            if(bytes != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap b = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                huy(b);
            }
        }
    }

    public void hideContainer(){
        if(container.getVisibility() == View.VISIBLE){
            frameLayoutRoot.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 88 && resultCode == RESULT_OK) {
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream, null, options);
                imageView.setImageBitmap(imageBitmap);
                OcrManager manager = new OcrManager(this);
                manager.initAPI();
                String value = manager.startRecognize(imageBitmap);
                txtBaseText.setText(value);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == ConstantUtil.REQUEST_CODE_111){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LoadFragment();
                SetEnableView(true);
            }
        } else super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
