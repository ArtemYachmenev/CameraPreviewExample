package com.vk.camerapreviewexample.view.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.vk.camerapreviewexample.R;

public class VisualizerView extends View {
    public byte[] mBytes;
    public float[] mPoints;
    public Rect mRect=new Rect();
    public Paint mForcePaint =new Paint();

    public VisualizerView(Context context) {
        super(context);
        init();
        setupVisualizerFxAndUi(context);
    }

    public VisualizerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        setupVisualizerFxAndUi(context);
    }

    public VisualizerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        setupVisualizerFxAndUi(context);
    }

    public void init (){

        mBytes=null;
        mForcePaint.setStrokeWidth(5f);
        mForcePaint.setAntiAlias(true);
        mForcePaint.setColor(Color.RED);

    }
    public void updateVisualizer(byte[] bytes){
        mBytes=bytes;
        invalidate();

    }
    @Override
    public void onDraw(Canvas canvas){
super.onDraw(canvas);
if (mBytes==null){
    return;
}
if (mPoints==null||mPoints.length<mBytes.length){

    mPoints = new float[mBytes.length * 4];
}
mRect.set(0,0,getWidth(),getHeight());
for (int i=0;i<mBytes.length-1;i++){
    mPoints[i*4]=mRect.width()*i/(mBytes.length-1);
    mPoints[i*4+1]=mRect.height()/2+((byte)(mBytes[i]+128))*(mRect.height()/2)/128;
    mPoints[i*4+2]=mRect.width()*(i+1)/(mBytes.length-1);
    mPoints[i*4+3]=mRect.height()/2+((byte)(mBytes[i+1]+128))*(mRect.height()/2)/128;


}
canvas.drawLines(mPoints,mForcePaint);
    }
    public void setupVisualizerFxAndUi(Context context){

        ((MainActivity)context).setVolumeControlStream(AudioManager.STREAM_MUSIC);
       // ((MainActivity)context).setVolumeControlStream(MediaRecorder.AudioSource.MIC);
        final VisualizerView mVisualizerView=(VisualizerView) findViewById(R.id.visu);
        try{
Visualizer mVisualizer=new Visualizer(0);
mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int i) {
        mVisualizerView.updateVisualizer(bytes);

    }

    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int i) {

    }
}, Visualizer.getMaxCaptureRate()/2,true ,false);
            mVisualizer.setEnabled(true);
        }
        catch (Exception e){

        }

    }
}
