package com.mim.marcoisaac.videorecord;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {

    private MediaRecorder recorder;
    private CameraPreview mCameraPreview;
    private boolean recording = false;
    private Camera mCamera;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recorder = new MediaRecorder();

        mCamera = getCameraInstance();


        FrameLayout cameraView = (FrameLayout) view.findViewById(R.id.camera_prev);

        mCameraPreview = new CameraPreview(getActivity(), mCamera);
        cameraView.addView(mCameraPreview);


        cameraView.setClickable(true);
        cameraView.setOnClickListener(this);
        return view;
    }

    private void initRecorder() {

        Random r = new Random();

        int i1 = r.nextInt(200 - 70) + 17;
        int i2 = r.nextInt(67 - 12) - 6;
        int i3 = r.nextInt(89 - 27) - 6;
        int i4 = r.nextInt(200 - 70) + 17;
        int i5 = r.nextInt(89 - 27) - 6;
        String photoTitle = "vid_" + i1 + i2 + "uncompressed_" + i3 + i4 + "-" + i5;

        File image = new File(Environment.  //THIS WORKS
                getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                photoTitle + ".mp4");

        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        CamcorderProfile cpHigh = CamcorderProfile
                .get(CamcorderProfile.QUALITY_HIGH);
        recorder.setProfile(cpHigh);
        recorder.setOutputFile(image.getAbsolutePath());

        recorder.setMaxDuration(50000); // 50 seconds
        recorder.setMaxFileSize(5000000); // Approximately 5 megabytes
    }

    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            // cannot get camera or does not exist
        }
        return camera;
    }


    private void prepareRecorder() {
        recorder.setPreviewDisplay(mCameraPreview.getSurfaceHolder().getSurface());

        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            //finish();
        } catch (IOException e) {
            e.printStackTrace();
            //finish();
        }
    }

   /* @Override
    public void surfaceCreated(SurfaceHolder holder) {
        prepareRecorder();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (recording) {
            recorder.stop();
            recording = false;
        }
        recorder.release();
    }*/

    @Override
    public void onClick(View v) {
        if (recording) {
            recorder.stop();
            recording = false;

            // Let's initRecorder so we can record again
            //initRecorder();
            //prepareRecorder();
        } else {
            recording = true;
            mCameraPreview.stopPreview();
            initRecorder();
            prepareRecorder();
            recorder.start();
        }
    }
}
