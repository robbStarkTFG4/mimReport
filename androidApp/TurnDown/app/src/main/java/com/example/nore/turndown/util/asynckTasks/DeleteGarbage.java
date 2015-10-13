package com.example.nore.turndown.util.asynckTasks;

import android.os.AsyncTask;

import com.example.nore.turndown.entity.dao.ImageInfo;
import com.example.nore.turndown.entity.dao.Job;

import java.io.File;
import java.util.List;

/**
 * Created by NORE on 20/07/2015.
 */
public class DeleteGarbage extends AsyncTask<Job, Void, Void> {
    @Override
    protected Void doInBackground(Job... params) {
        for (Job jb : params) {

            List<ImageInfo> list = jb.getImageInfo2();
            if (list != null) {
                if (!list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        ImageInfo inf = list.get(i);

                        if (inf.getImgRoute() != null) {
                            File fil = new File(inf.getImgRoute());
                            if (fil.exists()) {
                                fil.delete();
                            }
                        }

                    }
                }
            }
            //end
        }
        return null;
    }
}
