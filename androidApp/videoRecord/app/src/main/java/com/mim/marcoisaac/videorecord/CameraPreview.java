package com.mim.marcoisaac.videorecord;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by NORE on 15/08/2015.
 */
public class CameraPreview extends SurfaceView implements
        SurfaceHolder.Callback {

    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private boolean released = false;

    // Constructor that obtains context and camera
    @SuppressWarnings("deprecation")
    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.mCamera = camera;
        this.mSurfaceHolder = this.getHolder();
        this.mSurfaceHolder.addCallback(this);
        this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        startPreview(surfaceHolder);
    }

    private void startPreview(SurfaceHolder surfaceHolder) {
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (IOException e) {
            // left blank for now
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        stopPreview();
    }

    public void stopPreview() {
        if (!released) {
            mCamera.stopPreview();
            mCamera.release();
            released = true;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format,
                               int width, int height) {
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            // intentionally left blank for a test
        }
    }

    public SurfaceHolder getSurfaceHolder() {
        return mSurfaceHolder;
    }
}
