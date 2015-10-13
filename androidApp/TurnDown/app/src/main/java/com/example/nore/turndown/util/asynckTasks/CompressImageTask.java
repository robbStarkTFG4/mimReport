package com.example.nore.turndown.util.asynckTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


import com.example.nore.turndown.entity.dao.DaoSession;
import com.example.nore.turndown.entity.dao.ImageInfo;
import com.example.nore.turndown.entity.dao.Job;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

/**
 * Created by NORE on 06/07/2015.
 */
public class CompressImageTask extends AsyncTask<Job, Integer, Boolean> {

    public interface CompressState {
        public void compressResult(boolean res, Long jobId, int serverId);
    }

    private CompressState compress;
    private DaoSession session;
    private Long jobId;
    private int serverId;

    @Override
    protected Boolean doInBackground(Job... params) {
        if (session != null) {
            Job jb = params[0];

            List<ImageInfo> list = jb.getImageInfo2();

            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    ImageInfo inf = list.get(i);

                    Bitmap bit = BitmapFactory.decodeFile(inf.getImgRoute());
                    if (bit != null) {
                        try {
                            // SAVE IMAGE NAME TO OBJECT
                            Random r = new Random();
                            int i1 = r.nextInt(200 - 70) + 17;
                            int i2 = r.nextInt(67 - 12) - 6;
                            int i3 = r.nextInt(82 - 24) - 6;
                            String photoTitle = "JPEG_" + i1 + i2 + "compressed_" + i3;

                            //END SAVE

                            String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();


                            File image = new File(Environment.
                                    getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                                    photoTitle + ".jpg");

                            //OutputStream stream = new FileOutputStream(dir + "/" + photoTitle);
                            OutputStream stream = new FileOutputStream(image);
                            bit.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                            stream.flush();
                            stream.close();
                            inf.setCompressedImage(image.getPath());
                            session.getJobDao().update(jb);

                            return true;
                        } catch (FileNotFoundException e) {
                            Log.d("TAG", e.getMessage());
                            return false;
                        } catch (IOException e) {
                            Log.d("TAG", e.getMessage());
                            return false;
                        }

                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
            return false;
        } else {
            return false;
        }
        //end
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (compress != null) {
            compress.compressResult(aBoolean, jobId, serverId);
        }
    }

    public void setCompress(CompressState compress) {
        this.compress = compress;
    }

    public void setSession(DaoSession session) {
        this.session = session;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}