package com.example.nore.turndown.util.asynckTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

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
public class CompressImageTask2 extends AsyncTask<Job, Integer, Boolean> {

    public interface CompressState2 {
        public void compressResult2(boolean res);
    }

    private CompressState2 compress;
    private DaoSession session;

    @Override
    protected Boolean doInBackground(Job... params) {
        for (Job jb : params) {

            List<ImageInfo> list = jb.getImageInfo2();

            if (list != null) {
                if (!list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        ImageInfo inf = list.get(i);
                        if (inf.getType().compareTo(1) == 0) {
                            if (inf.getImgRoute() != null) {
                                if (inf.getCompressedImage() == null) { // aqui identifico si es video o imagen si pasa es imagen
                                    Bitmap bit = BitmapFactory.decodeFile(inf.getImgRoute());
                                    if (bit != null) {
                                        try {
                                            // SAVE IMAGE NAME TO OBJECT
                                            Random r = new Random();
                                            int i1 = r.nextInt(200 - 70) + 17;
                                            int i2 = r.nextInt(67 - 12) - 6;
                                            int i3 = r.nextInt(82 - 24) - 6;
                                            String photoTitle = "JPEG_" + i1 + i2 + "compressed_" + i3;

                                            File image = new File(Environment.
                                                    getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                                                    photoTitle + ".jpg");

                                            OutputStream stream = new FileOutputStream(image);
                                            bit.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                                            stream.flush();
                                            stream.close();
                                            inf.setCompressedImage(image.getPath());

                                            if (session != null) {
                                                session.getJobDao().update(jb);
                                            }

                                        } catch (FileNotFoundException e) {
                                            Log.d("TAG", e.getMessage());
                                            return false;
                                        } catch (IOException e) {
                                            Log.d("TAG", e.getMessage());
                                            return false;
                                        }

                                    }
                                }
                            }
                        }
                        //end
                    }
                }
            }
            //end
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (compress != null) {
            compress.compressResult2(aBoolean);
        }
    }

    public void setCompress(CompressState2 compress) {
        this.compress = compress;
    }

    public void setSession(DaoSession session) {
        this.session = session;
    }

}