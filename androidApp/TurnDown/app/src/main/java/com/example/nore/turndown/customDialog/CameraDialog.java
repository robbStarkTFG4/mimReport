package com.example.nore.turndown.customDialog;

import android.app.DialogFragment;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.nore.turndown.CustomListView.ExpandableCustomAdapter;
import com.example.nore.turndown.R;
import com.example.nore.turndown.customCamera.CameraPreview;
import com.example.nore.turndown.entity.dao.ImageInfo;
import com.example.nore.turndown.entity.dao.Job;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by NORE on 15/08/2015.
 */
public class CameraDialog extends DialogFragment {

    private CustomDialogFrag.SaverMedium save;

    public static CameraDialog newInstance(Job job) {
        CameraDialog f = new CameraDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putSerializable("jobCam", job);
        //args.putSerializable("port", port);
        f.setArguments(args);

        return f;
    }

    private Camera mCamera;
    private CameraPreview mCameraPreview;
    String rutaImagen = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.camara_dialog, null);

        final Job jb = (Job) getArguments().getSerializable("jobCam");

        mCamera = getCameraInstance();
        mCameraPreview = new CameraPreview(getActivity(), mCamera);
        FrameLayout preview = (FrameLayout) root.findViewById(R.id.camera_prev);
        preview.addView(mCameraPreview);

        final Button acceptBtn = (Button) root.findViewById(R.id.btnAceptarImagen);
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rutaImagen != null) {
                    if (jb != null) {
                        List<ImageInfo> list = jb.getImageInfo2();
                        if (list != null) {
                            ImageInfo img = new ImageInfo();
                            img.setImgRoute(rutaImagen);
                            list.add(img);
                        } else {
                            list = new ArrayList<>();
                            ImageInfo img = new ImageInfo();
                            img.setImgRoute(rutaImagen);
                            list.add(img);

                            if (jb.getImageInfo2() == null) {
                                jb.setImageInfo(list);
                            }
                        }
                    }
                }
                if (save != null) {
                    save.notifySave();
                }
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
                CameraDialog.this.dismiss();
            }
        });

        final Button cancelBtn = (Button) root.findViewById(R.id.btnCancelarImagn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        if (rutaImagen != null) {
                            File fil = new File(rutaImagen);
                            if (fil.exists()) {
                                fil.delete();
                            }
                        }
                        return null;
                    }
                }.execute();
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
                CameraDialog.this.dismiss();
            }
        });

        final Button captureButton = (Button) root.findViewById(R.id.btnCapture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null, null, mPicture);
                captureButton.setVisibility(View.GONE);
                acceptBtn.setVisibility(View.VISIBLE);
                cancelBtn.setVisibility(View.VISIBLE);
            }
        });

        return root;
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

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                return;
            }
            rutaImagen = pictureFile.getPath();
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }
    };

    private File getOutputMediaFile() {

        Random r = new Random();

        int i1 = r.nextInt(200 - 70) + 17;
        int i2 = r.nextInt(67 - 12) - 6;
        int i3 = r.nextInt(89 - 27) - 6;
        int i4 = r.nextInt(200 - 70) + 17;
        int i5 = r.nextInt(89 - 27) - 6;
        String photoTitle = "JPEG_" + i1 + i2 + "uncompressed_" + i3 + i4 + "-" + i5;

        File image = new File(Environment.  //THIS WORKS
                getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                photoTitle + ".jpg");

        return image;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void setSave(CustomDialogFrag.SaverMedium save) {
        this.save = save;
    }
}
