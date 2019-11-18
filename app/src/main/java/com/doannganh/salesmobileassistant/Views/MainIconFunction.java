package com.doannganh.salesmobileassistant.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.doannganh.salesmobileassistant.R;

public class MainIconFunction extends View {
    RectF recF;
    Bitmap imageResource;
    String text;

    public RectF getRecF() {
        return recF;
    }

    public void setRecF(RectF recF) {
        this.recF = recF;
    }

    public Bitmap getImageResource() {
        return imageResource;
    }

    public void setImageResource(Bitmap imageResource) {
        this.imageResource = imageResource;

        SetRecF();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MainIconFunction(Context context) {
        super(context);

        Init(null);
    }

    public MainIconFunction(Context context, @androidx.annotation. Nullable AttributeSet attrs) {
        super(context, attrs);
        Init(attrs);
    }

    public MainIconFunction(Context context, @androidx.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(attrs);
    }

    public MainIconFunction(Context context, @androidx.annotation.Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Init(attrs);
    }

    public void huy(String string){
        //canvas.drawBitmap(imageResource, new Rect(0,0,100,100), recF, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawColor(Color.GREEN);
        // draw img
        canvas.drawBitmap(imageResource, null, recF, null);

        // draw Text
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(32);

        float x = Math.abs((recF.bottom - text.length()) / 2)-text.length()*3;//text.length()-recF.bottom/2
        float y = recF.bottom+20;//recF.bottom + 20
        String text2 ="", text1 = text;
        int chiso = 0;

        if (text1.length() >= recF.bottom/10-8){
            while (text1.length()> 0){
                x = Math.abs((recF.bottom - text1.length()) / 2)-text1.length()*3;//text.length()-recF.bottom/2
                y = recF.bottom+20+chiso;//recF.bottom + 20

                if (text1.length() >= recF.bottom/10-8){
                    text2 = "\n" + text1.substring(0, (int)recF.bottom/10-8);
                    text1 = text1.substring((int)recF.bottom/10-8);
                } else {
                    text2 = text1;
                    text1 = "";
                }
                canvas.drawText(text2, x, y, paint);
                chiso = chiso + 20;
            }
        } else{
            text1 = text;
            canvas.drawText(text1, x, y, paint);
        }
    }

    private void Init(@Nullable AttributeSet attributeSet){
        imageResource = BitmapFactory.decodeResource( getResources()
                , R.drawable.settup);

        text = "";
        recF = new RectF();
        SetRecF();
    }

    private void SetRecF() {
        recF.right = (getWidth()+imageResource.getWidth())/4;
        recF.bottom = (getWidth()+imageResource.getWidth())/4;
        recF.left = recF.right/5;
        recF.top = recF.right/6;
    }
}
