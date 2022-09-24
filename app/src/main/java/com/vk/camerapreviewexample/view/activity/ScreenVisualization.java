package com.vk.camerapreviewexample.view.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ScreenVisualization extends View {



    private static final int MAX_AMPLITUDE=32767;

    private DotsPointArray vectors;
    private ColorScheme colorScheme;
    private DotsPointArray amplitudes;
    private int widht, height;

    public ScreenVisualization(Context context) {
        super(context);
    }

    public ScreenVisualization(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScreenVisualization(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.widht=w;
        this.height=h;
        this.amplitudes=new DotsPointArray(this.widht/2,1);
        this.vectors=new DotsPointArray(this.widht/2,2);
    }

    public void addAmplitude(int amplitude){
        invalidate();
        float scaledHeigjt=((float)amplitude/MAX_AMPLITUDE )*(height-1);
        amplitudes.add(0,height-scaledHeigjt);
        vectors.add(0,height,0,height-scaledHeigjt);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        colorScheme.shuffle();
        canvas.drawPaint(colorScheme.CanvasPaint);
        canvas.drawLines(vectors.getIndexedArray(0), colorScheme.LinePaint);
        canvas.drawLine(widht/2,height,widht/2,0,colorScheme.LinePaint);
        canvas.drawPoints(amplitudes.getIndexedArray(this.widht/2),colorScheme.CirclePaint);
    }
}
