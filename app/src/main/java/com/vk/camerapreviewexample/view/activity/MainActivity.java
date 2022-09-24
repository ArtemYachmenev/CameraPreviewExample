package com.vk.camerapreviewexample.view.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.ImageReader;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MicrophoneInfo;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
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
import android.widget.TextView;
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
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    CameraCharacteristics characteristics;


private ScreenVisualization screenVisualization;
private MediaRecorder recorder;

private Handler handler=new Handler();
TextView txtName;


//
    private MediaRecorder mediaRecorder;
AudioRecord audioRecord;
MediaPlayer mediaPlayer;
    int myBufferSize = 1024;
    boolean isReading = false;
//

final Runnable updater=new Runnable() {
    @Override
    public void run() {
        handler.postDelayed(this,50);
        if (recorder!=null&&screenVisualization!=null){

            int maxAmplitude=0;
            try {
                maxAmplitude=recorder.getMaxAmplitude();
                txtName.setVisibility(View.INVISIBLE);
            }
            catch (Exception e){
e.printStackTrace();
            }
            if (maxAmplitude>0){
                screenVisualization.addAmplitude(maxAmplitude);

            }
        }
    }
};


    void createAudioRecorder() {
        int sampleRate = 8000;
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

        int minInternalBufferSize = AudioRecord.getMinBufferSize(sampleRate,
                channelConfig, audioFormat);
        int internalBufferSize = minInternalBufferSize * 4;

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                sampleRate, channelConfig, audioFormat, internalBufferSize);
    }


    private CameraPreviewViewModel cameraPreviewViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ActivityFullScreenPreviewBinding activityFullScreenPreviewBinding = DataBindingUtil.setContentView(this, R.layout.activity_full_screen_preview);
        cameraPreviewViewModel = new CameraPreviewViewModel(this, activityFullScreenPreviewBinding);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO}, 2);

        } else {
            cameraPreviewViewModel.startCameraPreview();
            cameraPreviewViewModel.captureImage();
        }

        //voice
//        screenVisualization=(ScreenVisualization) findViewById(R.id.visualization);
//        txtName=(TextView) findViewById(R.id.label_name);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//           screenVisualization.setOnClickListener(new View.OnClickListener() {
//               @Override
//               public void onClick(View view) {
//                   startVoiceListening();
//               }
//           });
//        }
        startVoiceListening();






        //тестировал аудиорекордер
  //      createAudioRecorder();
   //     audioRecord.startRecording();
            VisualizerView visualizerView =(VisualizerView) findViewById(R.id.visu);
        isReading = true;
        new Thread(new Runnable() {
            int vol;

            @Override
            public void run() {
                byte[] myBuffer = new byte[myBufferSize];
                byte[] buf= new byte[myBufferSize];

                int i=0;
                while (isReading) {
                    vol=  getVolume()/5;

                 //   System.out.println(vol);



                    buf[i]= (byte) vol;

                    //   System.out.println(buf);
                    System.out.println(vol);
                    visualizerView.updateVisualizer( buf);

                    i++;


                    if (i==myBufferSize-1){
                        buf=new byte[myBufferSize];
                        i=0;

                    }


                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                if (audioRecord == null)
//                    return;
//
//                byte[] myBuffer = new byte[myBufferSize];
//                byte[] buf= new byte[myBufferSize];
//                int readCount = 0;
//                int totalCount = 0;
//                int i=0;
//                while (isReading) {
//                    readCount = audioRecord.read(myBuffer, 0, myBufferSize);
//                    totalCount += readCount;
//                    buf[i]= (byte) totalCount;
//                    //   System.out.println(buf);
//                    System.out.println(totalCount+" hui");
//                    visualizerView.updateVisualizer(buf);
//                    i++;
//                }
//               // System.out.println(buf);
//
//            }
//        }).start();


    }

    //
    public int getVolume(){
        if (recorder==null){
            return 0;
        }
        else {
            return recorder.getMaxAmplitude();
        }
    }
    //

    private void startVoiceListening() {
        recorder=new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try{
            file=File.createTempFile("prefix",".extension",getApplicationContext().getCacheDir());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                recorder.setOutputFile(file);
            }
        }
        catch (Exception e){
            e.printStackTrace();

        }
        try {
        //    Toast.makeText(getApplicationContext(), "ready listen the voice", Toast.LENGTH_SHORT).show();
            recorder.prepare();
       //     Toast.makeText(getApplicationContext(), "trying listening the voice", Toast.LENGTH_SHORT).show();
recorder.start();
       //     Toast.makeText(getApplicationContext(), "started listening the voice", Toast.LENGTH_SHORT).show();

        }
        catch (Exception e){
            e.printStackTrace();

        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updater);

        if (recorder!=null){
            try {
recorder.stop();
recorder.reset();
recorder.release();
recorder=null;
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }



    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        handler.post(updater);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 2) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraPreviewViewModel.startCameraPreview();
            } else {
                Toast.makeText(this, R.string.camera_permission_error, Toast.LENGTH_SHORT).show();
            }
            if (grantResults.length > 1
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                cameraPreviewViewModel.captureImage();
            } else {
                Toast.makeText(this, R.string.storage_permission_error, Toast.LENGTH_SHORT).show();
            }
            if (grantResults.length > 2
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, R.string.record_audio_permission_error, Toast.LENGTH_SHORT).show();
            }
        }
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
}