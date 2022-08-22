package com.vk.camerapreviewexample.view.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MicrophoneInfo;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.view.View;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;
import com.vk.camerapreviewexample.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.vk.camerapreviewexample.R;
import com.vk.camerapreviewexample.databinding.ActivityFullScreenPreviewBinding;
import com.vk.camerapreviewexample.viewmodel.CameraPreviewViewModel;
import static com.vk.camerapreviewexample.constant.Constant.INPUT_HEIGHT;
import static com.vk.camerapreviewexample.constant.Constant.INPUT_WIDTH;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    public static final int CAMERA_FRONT =  1;
    public static final int CAMERA_BACK =  0;
    private int cameraId = CAMERA_FRONT;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 300;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    CameraCharacteristics characteristics;


    BarVisualizer visualizer;
    Visualizer visualizerV;
    VisualizerView visualizerView;


    private static int MICROPHONE_PERMISSION_CODE=200;
    private static int REQUEST_AUDIO_PERMISSION_CODE=101;

    private CameraPreviewViewModel cameraPreviewViewModel;
  static   MediaRecorder mediaRecorder;
    public MediaPlayer music;
    boolean isRecording=false;
    boolean isPlayning=false;
    VisualizerView ibRecord;
    ExecutorService executorService= Executors.newSingleThreadExecutor();
    Handler handler;
    String path;



    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        visualizerView=new VisualizerView(this);
        ActivityFullScreenPreviewBinding activityFullScreenPreviewBinding = DataBindingUtil.setContentView(this, R.layout.activity_full_screen_preview);
        cameraPreviewViewModel = new CameraPreviewViewModel(this, activityFullScreenPreviewBinding);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            cameraPreviewViewModel.startCameraPreview();
            cameraPreviewViewModel.captureImage();
        }
        //getSupportActionBar().hide();

      //  ibRecord=findViewById(R.id.visu);

//        ibRecord.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (checkRecordingPermission()){
//
//                    if (!isRecording){
//
//                        isRecording=true;
//                        executorService.execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                mediaRecorder=new MediaRecorder();
//                                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//                                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//
//                                //тут должен по хорошему быть еще путь
//                                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//                                try {
//                                    mediaRecorder.prepare();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                mediaRecorder.start();
//
//                            }
//                        });
//                    }
//
//                    else{
//                        executorService.execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                mediaRecorder.stop();
//                                mediaRecorder.release();
//                                mediaRecorder=null;
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        mBackgroundHandler.removeCallbacksAndMessages(null);
//                                    }
//                                });
//                            }
//                        });
//                    }
//                }
//                else
//                {
//                    requestRecordingPermissions();
//                }
//            }
//        });

        checkRecordingPermission();
               start();




        //запись гоолоса
//        mediaRecorder=new MediaRecorder();
//        //проверка микрофона
//        if (isMicrophonePresent()){
//            getMicrophonePermission();
//        }
        //  visualizerV=new Visualizer(MediaRecorder.OutputFormat.THREE_GPP);
//        List<MicrophoneInfo> audioSessionId;
//        int id=1;
//        try {
//            audioSessionId = mediaRecorder.getActiveMicrophones();
//
//            id=1;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (id!=-1){
//            // visualizer.setAudioSessionId(id);
//        }
     //   music=MediaPlayer


      //  visualizer=findViewById(R.id.visu);


//visualizerV=new Visualizer(0);
//
//



    }
    public void start(){
        if (checkRecordingPermission()==true){
            System.out.println("yes");


            mediaRecorder=new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOutputFile(getRecordingFilePath());

            path=getRecordingFilePath();
            //тут должен по хорошему быть еще путь

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
mediaRecorder



            } catch (IOException e) {
                e.printStackTrace();
            }

        }




        else
        {
            requestRecordingPermissions();
        }
    }

    public int getVolume(){
     return    mediaRecorder.getMaxAmplitude();
    }

    public boolean isAudioRecording(){
      return   mediaRecorder!=null;

    }

    private String getRecordingFilePath() {

        ContextWrapper contextWrapper=new ContextWrapper(getApplicationContext());
        File voice=contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file=new File(voice,"voiceFile"+".wav");
        return file.getPath();

    }

    private boolean checkRecordingPermission() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)==
        PackageManager.PERMISSION_DENIED){
            requestRecordingPermissions();
            return false;
        }else{
            return true;
        }


    }





    private void requestRecordingPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},
               REQUEST_AUDIO_PERMISSION_CODE );
    }

    public void recordMic(){

        try {

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            //test
            mediaRecorder.setOutputFile("/dev/null");

            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode==REQUEST_AUDIO_PERMISSION_CODE){
            if (grantResults.length>0){
                boolean permissionToRecord=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                if (permissionToRecord){
                    Toast.makeText(getApplicationContext(),"permission Given", Toast.LENGTH_SHORT);
                }
                else{
                    Toast.makeText(getApplicationContext(),"permission denited", Toast.LENGTH_SHORT);
                }
            }
        }


//        if (requestCode == 1) {
//            if (grantResults.length > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                cameraPreviewViewModel.startCameraPreview();
//            } else {
//                Toast.makeText(this, R.string.camera_permission_error, Toast.LENGTH_SHORT).show();
//            }
//            if (grantResults.length > 1
//                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                cameraPreviewViewModel.captureImage();
//            } else {
//                Toast.makeText(this, R.string.storage_permission_error, Toast.LENGTH_SHORT).show();
//            }
//        }
    }


    public void changingTheCamera(View view) {
        if (cameraId==(CAMERA_FRONT)) {
            cameraId = CAMERA_BACK;
            cameraPreviewViewModel.setCameraInstance(cameraId);
            cameraPreviewViewModel.startCameraPreview();
            cameraPreviewViewModel.captureImage();
        } else if (cameraId==(CAMERA_BACK)) {
            cameraId = CAMERA_FRONT;
            cameraPreviewViewModel.setCameraInstance(cameraId);
            cameraPreviewViewModel.startCameraPreview();
            cameraPreviewViewModel.captureImage();
        }

    }

    //если микрофон есть
    private boolean isMicrophonePresent(){
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        }
        else {
            return false;
        }
    }

    //запрашиваем разрешения
    private void getMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    MICROPHONE_PERMISSION_CODE);

        } else {
        }
    }
}