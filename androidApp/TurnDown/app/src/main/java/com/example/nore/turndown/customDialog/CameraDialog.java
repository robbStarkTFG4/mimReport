package com.example.nore.turndown.customDialog;

import android.app.DialogFragment;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

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
    private MediaRecorder recorder;

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
    private String rutaImagen = null;
    private boolean cameraState = false;
    private boolean recording = false;

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
                if (!cameraState) {
                    imagePersist(jb);
                } else {
                    videoPersist(jb);
                }

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

        final Button switchButton = (Button) root.findViewById(R.id.switch_btn);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cameraState) {
                    cameraState = true;
                } else {
                    cameraState = false;
                }

                if (cameraState) {
                    setUpForVideos();
                } else {
                    setUpForPictures();
                    recorder = null;
                }
            }
        });

        final Button captureButton = (Button) root.findViewById(R.id.btnCapture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cameraState) {
                    //setUpForVideos();
                    if (!recording) {
                        Toast.makeText(getActivity(), "Tomando video", Toast.LENGTH_LONG).show();
                        recording = true;
                        mCameraPreview.stopPreview();
                        initRecorder(videoFileName());
                        prepareRecorder();
                        recorder.start();
                    } else {
                        Toast.makeText(getActivity(), "Video tomado", Toast.LENGTH_LONG).show();
                        stopRecorder();
                        //mCameraPreview.resumePreview();
                        recording = false;
                        captureButton.setVisibility(View.GONE);
                        switchButton.setVisibility(View.GONE);
                        acceptBtn.setVisibility(View.VISIBLE);
                        cancelBtn.setVisibility(View.VISIBLE);
                    }

                } else {
                    Toast.makeText(getActivity(), "Tomando foto", Toast.LENGTH_LONG).show();
                    mCamera.takePicture(null, null, mPicture);
                    captureButton.setVisibility(View.GONE);
                    switchButton.setVisibility(View.GONE);
                    acceptBtn.setVisibility(View.VISIBLE);
                    cancelBtn.setVisibility(View.VISIBLE);
                }

            }
        });

        return root;
    }

    private void videoPersist(Job jb) {

    }

    private void imagePersist(Job jb) {
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
       /* mCamera.stopPreview();
        mCamera.release();
        mCamera = null;*/
    }

    private void stopRecorder() {
        recorder.stop();
    }

    private void prepareRecorder() {
        recorder.setPreviewDisplay(mCameraPreview.getmSurfaceHolder().getSurface());

        try {
            recorder.prepare();
            Thread.sleep(1000);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            //finish();
        } catch (IOException e) {
            e.printStackTrace();
            //finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setUpForVideos() {
        Toast.makeText(getActivity(), "Videos", Toast.LENGTH_LONG).show();
        //initRecorder(videoFileName());
        //prepareRecorder();

    }

    private void setUpForPictures() {
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        Toast.makeText(getActivity(), "Fotos", Toast.LENGTH_LONG).show();
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
            File pictureFile = getOutputMediaFile(1);
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

    private File getOutputMediaFile(int type) {

        switch (type) {
            case 1:
                return pictureFile();
            default:
                return null;
        }

    }

    @NonNull
    private File pictureFile() {
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

    @NonNull
    private File videoFileName() {
        Random r = new Random();

        int i1 = r.nextInt(200 - 70) + 17;
        int i2 = r.nextInt(67 - 12) - 6;
        int i3 = r.nextInt(89 - 27) - 6;
        int i4 = r.nextInt(200 - 70) + 17;
        int i5 = r.nextInt(89 - 27) - 6;
        String photoTitle = "vid" + i1 + i2 + "uncompressed_" + i3 + i4 + "-" + i5;

        File image = new File(Environment.  //THIS WORKS
                getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                photoTitle + ".mp4");

        return image;
    }

    @Override
    public void onPause() {
        super.onPause();
       /* if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }*/
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }

    public void setSave(CustomDialogFrag.SaverMedium save) {
        this.save = save;
    }

    private void initRecorder(File file) {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        //recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        //recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        CamcorderProfile cpHigh = CamcorderProfile
                .get(0, CamcorderProfile.QUALITY_HIGH);

        recorder.setProfile(cpHigh);
        recorder.setOutputFile(file.getAbsolutePath());
        recorder.setMaxDuration(50000); // 50 seconds
        recorder.setMaxFileSize(5000000); // Approximately 5 megabytes
    }
}
