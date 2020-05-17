package com.doannganh.salesmobileassistant.Manager;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by vu.tien.tung on 4/24/17.
 */

public class MainApplication {

    private static MainApplication instance =null;
    private Context context;
    private boolean isCopy = false;

    public static MainApplication instance(Context context){
        if(instance == null)
            instance = new MainApplication();

        instance.context = context;
        return instance;
    }

    public void prepare(){
        copyTessDataForTextRecognizor();
    }

    private String tessDataPath()
    {
        return instance.context.getExternalFilesDir(null)+"/tessdata/";
    }

    public String getTessDataParentDirectory()
    {
        return instance.context.getExternalFilesDir(null).getAbsolutePath();
    }

    private void copyTessDataForTextRecognizor() {
        isCopy = true;
        AssetManager assetManager = instance.context.getAssets();
        OutputStream out = null;
        try {
            InputStream in = assetManager.open("vie.traineddata");
            String tesspath = instance.tessDataPath();
            File tessFolder = new File(tesspath);
            if (!tessFolder.exists())
                tessFolder.mkdir();
            String tessData = tesspath + "/" + "vie.traineddata";
            File tessFile = new File(tessData);
            if (!tessFile.exists()) {
                out = new FileOutputStream(tessData);
                byte[] buffer = new byte[1024];
                int read = in.read(buffer);
                while (read != -1) {
                    out.write(buffer, 0, read);
                    read = in.read(buffer);
                }
                Log.d("MainApplication", " Did finish copy tess file  ");


            } else
                Log.d("MainApplication", " tess file exist  ");

        } catch (Exception e) {
            Log.d("MainApplication", "couldn't copy with the following error : " + e.toString());
        } finally {
            isCopy = false;
            try {
                if (out != null)
                    out.close();
            } catch (Exception exx) {

            }
        }

    }
}

