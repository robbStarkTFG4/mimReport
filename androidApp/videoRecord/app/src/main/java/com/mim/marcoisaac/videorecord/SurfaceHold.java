package com.mim.marcoisaac.videorecord;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by marcoisaac on 10/26/2015.
 */
public class SurfaceHold extends SurfaceView {
    private SurfaceHolder mSurfaceHolder;

    public SurfaceHold(Context context) {
        super(context);
    }

    public SurfaceHold(Context context, MainActivityFragment frag) {
        super(context);

        this.mSurfaceHolder = this.getHolder();
        //this.mSurfaceHolder.addCallback(frag);
        this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
}
