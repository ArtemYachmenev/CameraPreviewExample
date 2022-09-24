package com.vk.camerapreviewexample.view.activity;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class ColorScheme {
    private static final int colorChangeFrameRate=20;
    private static int colorChangeFrameSeq=0;

    public Paint CanvasPaint, CirclePaint, LinePaint;

    public ColorScheme(){
        LinePaint=new Paint();
        LinePaint.setStrokeWidth(1);
        LinePaint.setColor(Color.GREEN);
        CirclePaint=new Paint();
        CirclePaint.setStrokeWidth(5);
        CirclePaint.setColor(Color.GREEN);
        CanvasPaint=new Paint();
        CanvasPaint.setColor(Color.WHITE);
    }
    public void shuffle(){
        if (colorChangeFrameSeq-- >=0){
            return ;
        }
        colorChangeFrameSeq=colorChangeFrameRate;
        LinePaint.setColor(randomColor());
        CirclePaint.setColor(randomColor());
    }

    private int randomColor() {
        Random r=new Random();
        int ra=r.nextInt(255);
        int g=r.nextInt(255);
        int b=r.nextInt(255);
        return Color.rgb(ra,g,b);
    }

}
