package com.vk.camerapreviewexample.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.view.View;

import com.vk.camerapreviewexample.R;
import androidx.appcompat.app.AppCompatActivity;
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




    private CameraPreviewViewModel cameraPreviewViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFullScreenPreviewBinding activityFullScreenPreviewBinding = DataBindingUtil.setContentView(this, R.layout.activity_full_screen_preview);
        cameraPreviewViewModel = new CameraPreviewViewModel(this, activityFullScreenPreviewBinding);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            cameraPreviewViewModel.startCameraPreview();
            cameraPreviewViewModel.captureImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 1) {
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
