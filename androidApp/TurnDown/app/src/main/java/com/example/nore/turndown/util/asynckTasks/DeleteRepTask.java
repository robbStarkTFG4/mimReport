package com.example.nore.turndown.util.asynckTasks;

import android.os.AsyncTask;

import com.example.nore.turndown.entity.dao.DaoSession;
import com.example.nore.turndown.entity.dao.ImageInfo;
import com.example.nore.turndown.entity.dao.Job;
import com.example.nore.turndown.entity.dao.JobDao;
import com.example.nore.turndown.entity.dao.Reporte;
import com.example.nore.turndown.entity.dao.ReporteDao;
import com.example.nore.turndown.entity.dao.TaskJob;
import com.example.nore.turndown.entity.dao.TaskJobDao;
import com.example.nore.turndown.util.dtos.RepoMen;

import java.io.File;
import java.util.List;

/**
 * Created by NORE on 18/07/2015.
 */
public class DeleteRepTask extends AsyncTask<RepoMen, Void, Boolean> {

    public interface DeleteResult {
        public void result(boolean res);
    }

    private DaoSession session;
    private DeleteResult res;

    @Override
    protected Boolean doInBackground(RepoMen... params) {
        if (session != null) {

            ReporteDao repDao = session.getReporteDao();
            JobDao jobDao = session.getJobDao();
            TaskJobDao taskDao = session.getTaskJobDao();

            for (RepoMen men : params) {

                Reporte reporte = repDao.queryBuilder().where(ReporteDao.Properties.Id.eq(men.getId())).unique();

                List<Job> jobList = reporte.getJobs();

                for (int i = 0; i < jobList.size(); i++) {
                    Job jb = jobList.get(i);
                    List<TaskJob> taskList = jb.getTasks();

                    for (int j = 0; j < taskList.size(); j++) {
                        TaskJob task = taskList.get(j);
                        taskDao.delete(task);
                    }

                    List<ImageInfo> list = jb.getImageInfo2();

                    if (list != null) {
                        if (!list.isEmpty()) {
                            for (int n = 0; n < list.size(); n++) {
                                ImageInfo inf = list.get(n);

                                if (inf.getImgRoute() != null) {
                                    File imagen = new File(inf.getImgRoute());
                                    if (imagen.exists()) {
                                        imagen.delete();
                                    }
                                }

                                if (inf.getCompressedImage() != null) {
                                    File imagen = new File(inf.getCompressedImage());
                                    if (imagen.exists()) {
                                        imagen.delete();
                                    }
                                }
                            }
                        }
                    }
                    //end

                    jobDao.delete(jb);
                }
                repDao.delete(reporte);
            }
            return true;
        } else {
            return false;
        }

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (res != null) {
            res.result(aBoolean);
        }
    }

    public void setSession(DaoSession session) {
        this.session = session;
    }

    public void setRes(DeleteResult res) {
        this.res = res;
    }
}
