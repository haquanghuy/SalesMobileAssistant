package com.doannganh.salesmobileassistant.Views.fragment;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.doannganh.salesmobileassistant.Manager.OcrManager;
import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.Views.activity.MyCameraActivity;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CameraSourceFragment extends Fragment {

    private CameraSource camera;

    SurfaceView surfaceView;
    Button btnTakePicture;
    TextView txtShortTitle;
    ImageButton imbBack;

    String flapType;
    private String TAG;
    private String textTitle;
    private boolean isAutoScan;
    private boolean isAutoReturn = false;

    public CameraSourceFragment() {
        // Required empty public constructor
    }

    public static CameraSourceFragment newInstance(String tag) {
        CameraSourceFragment fragment = new CameraSourceFragment();
        fragment.TAG = tag;
        return fragment;
    }

    public String getTAG() {
        return TAG;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get bundle
        Bundle bundle = getArguments();
        if(bundle!=null) {
            flapType = bundle.getString(MyCameraActivity.INTENT_FLAP, MyCameraActivity.ACTION.FLAP_BASE.toString());
            textTitle = bundle.getString(MyCameraActivity.SHORT_TEXT_TITLE, "");
            isAutoScan = bundle.getBoolean(MyCameraActivity.IS_AUTOSCAN, false);
            isAutoReturn = bundle.getBoolean(MyCameraActivity.IS_AUTORETURN, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_source, container, false);

        //scrollImages = view.findViewById(R.id.scrollImages);
        surfaceView = view.findViewById(R.id.surfaceView);
        btnTakePicture = view.findViewById(R.id.btnTakePicture);
        txtShortTitle = view.findViewById(R.id.txtMyCameraTitle);
        imbBack = view.findViewById(R.id.imbCameraBack);

        // set view
        txtShortTitle.setText(textTitle);
        btnTakePicture.setVisibility(isAutoScan?View.GONE:View.VISIBLE);

        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback(){

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                StartCameraSource(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                StopCamera();
            }
        });

        EventClickTakePhoto();

        EventClickBack();

        return view;
    }

    private void EventClickBack() {
        imbBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                StopCamera();
                getActivity().finish();
            }
        });
    }

    private void EventClickTakePhoto() {
        btnTakePicture.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                camera.takePicture(new CameraSource.ShutterCallback() {
                    @Override
                    public void onShutter() {

                    }
                }, new CameraSource.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        //Bitmap b = BitmapFactory.decodeStream(is, null, options);
                        Bitmap b = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);



                        OcrManager manager = new OcrManager(getActivity());
                        manager.initAPI();
                        String value = manager.startRecognize(b);

                        Bundle bundle = new Bundle();
                        bundle.putString(MyCameraActivity.ACTION.RETURN_VALUE_TEXT_DETECT.toString(), value);
                        bundle.putByteArray(MyCameraActivity.ACTION.RETURN_VALUE_IMAGE_DETECT.toString(), bytes);

                        //StopCamera();
                        if(isAutoReturn) {
                            Intent intent = getActivity().getIntent();
                            intent.putExtras(bundle);
                            getActivity().setResult(Activity.RESULT_OK, intent);
                            getActivity().finish();
                        } else {
                            ((MyCameraActivity) getActivity()).setResult(bundle);
                            ((MyCameraActivity) getActivity()).hideContainer();
                            getActivity().getFragmentManager().popBackStack();
                        }
                    }
                });
            }
        });
    }

    private void StartCameraSource(SurfaceHolder holder) {
        StopCamera();

        // setup
        SetupCamera();

        // if granted permission
        try {
            camera.start(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void SetupCamera() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getActivity())
                .setBarcodeFormats(Barcode.QR_CODE).build();
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                int key = detections.getDetectedItems().keyAt(0);
                final String value = detections.getDetectedItems().get(key).rawValue;

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        ((MyCameraActivity) getActivity()).setResult(value);
                        ((MyCameraActivity) getActivity()).hideContainer();
                    }
                });
            }
        });

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getActivity()).build();
        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {
                final SparseArray<TextBlock> items = detections.getDetectedItems();

                if (items.size() == 0) {
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i=0; i<items.size(); i++) {
                            TextBlock item = items.valueAt(i);
                            stringBuilder.append(item.getValue());
                            stringBuilder.append("\n");
                        }

                        txtShortTitle.setText(
                                stringBuilder.toString()
                        );

                        StopCamera();
//                        MyCameraActivity activity = (MyCameraActivity) getActivity();
//                        if(activity != null){
//                            activity.setResult(stringBuilder.toString());
//                            ((MyCameraActivity) getActivity()).hideContainer();
//                            //getActivity().getFragmentManager().popBackStack();
//                        }

                    }
                });



            }
        });

        if(flapType.equals(MyCameraActivity.ACTION.FLAP_SCAN_QR.toString())) {
            camera = new CameraSource.Builder(getActivity(), barcodeDetector)
                    .setAutoFocusEnabled(isAutoScan)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .build();
        } else if(flapType.equals(MyCameraActivity.ACTION.FLAP_DETECT_TEXT.toString())) {
//            camera = new CameraSource.Builder(getActivity(), textRecognizer)
//                    .setFacing(CameraSource.CAMERA_FACING_BACK)
//                    .setAutoFocusEnabled(isAutoScan)
//                    .setRequestedFps(2.0f)
//                    .build();

            camera = new CameraSource.Builder(getActivity(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(isAutoScan)
                    .setRequestedFps(2.0f)
                    .build();
        }
    }

    private void StopCamera() {
        // stop camera if on
        if(camera != null){
            camera.stop();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        surfaceView = null;
        StopCamera();
    }
/*
    public static void detectText(String filePath, PrintStream out) throws Exception, IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    out.printf("Text: %s\n", annotation.getDescription());
                    out.printf("Position : %s\n", annotation.getBoundingPoly());
                }
            }
        }
    }

 */
}
