package com.example.nore.turndown.customDialog;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nore.turndown.R;
import com.example.nore.turndown.entity.dao.ImageInfo;
import com.example.nore.turndown.entity.dao.Job;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by NORE on 06/07/2015.
 */
public class ImaveViewDialog extends DialogFragment {
    public static final String TAG = "StorageClientFragment";

    public static ImaveViewDialog newInstance(Job job) {
        ImaveViewDialog f = new ImaveViewDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putSerializable("job", job);
        //args.putSerializable("port", port);
        f.setArguments(args);

        return f;
    }

    private int index = 0;
    private int size = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle("Imagen");
        final Job jo = (Job) getArguments().getSerializable("job");
        size = jo.getImageInfo2().size() - 1;
        View root = inflater.inflate(R.layout.img_drialog, null);

        final ImageView img = (ImageView) root.findViewById(R.id.jobImage);

        DisplayMetrics metrics = new DisplayMetrics();

        final WindowManager windowManager = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);

        windowManager.getDefaultDisplay().getMetrics(metrics);


        File fil = new File(jo.getImageInfo2().get(index).getImgRoute());//------------- //begin old code
        if (fil.exists()) {
            Picasso.with(getActivity()).load(fil).resize((int) (metrics.widthPixels * .75)// fil as parameter
                    , (int) (metrics.heightPixels * .50)) // instead of Uri was file path in ExpandableCustomAdp
                    .into(img);


        } else {
            Toast.makeText(getActivity(), "No existe: -> " + jo.getImageInfo2().get(index).getImgRoute(), Toast.LENGTH_LONG).show();
        }


        LinearLayout delBtn = (LinearLayout) root.findViewById(R.id.delPicture);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                builder.setTitle("Warn");
                builder.setMessage("delete image?");
                builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ImageInfo target = jo.getImageInfo2().get(index);
                        final String imagen = target.getImgRoute();
                        index--;
                        size--;
                        if (index < 0) {
                            index = size;
                        }
                        if (size > 0) {
                            handleDeletedImagefinal(target, imagen, jo);
                            switchImage(index, jo, img);
                        } else {
                            //Toast.makeText(getActivity(), "entre aqui", Toast.LENGTH_LONG).show();
                            handleDeletedImagefinal(target, imagen, jo);
                            ImaveViewDialog.this.dismiss();
                        }
                    }
                });
                builder.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });



        LinearLayout rightBtn = (LinearLayout) root.findViewById(R.id.swipeRight);
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                if (index > size) {
                    index = 0;
                }
                switchImage(index, jo, img);
            }
        });

        LinearLayout leftBtn = (LinearLayout) root.findViewById(R.id.swipeLeft);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index--;
                if (index < 0) {
                    index = size;
                }
                switchImage(index, jo, img);
            }
        });


        return root;
    }

    private void handleDeletedImagefinal(ImageInfo target, final String imagen, Job jo) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if (imagen != null) {
                    File fil = new File(imagen);
                    if (fil.exists()) {
                        fil.delete();
                    }
                }
                return null;
            }
        }.execute();
        jo.getImageInfo2().remove(target);
    }

    private void switchImage(int index, Job jo, ImageView img) {

        DisplayMetrics metrics = new DisplayMetrics();

        WindowManager windowManager = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);

        windowManager.getDefaultDisplay().getMetrics(metrics);

        File fil = new File(jo.getImageInfo2().get(index).getImgRoute());//------------- //begin old code
        if (fil.exists()) {
            Picasso.with(getActivity()).load(fil).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .resize((int) (metrics.widthPixels * .75)// fil as parameter
                            , (int) (metrics.heightPixels * .50)) // instead of Uri was file path in ExpandableCustomAdp
                    .into(img);


        } else {
            Toast.makeText(getActivity(), "No existe: -> " + jo.getImageInfo2().get(index).getImgRoute(), Toast.LENGTH_LONG).show();
        }
    }
}
