package com.example.nore.turndown.util.asynckTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.nore.turndown.entity.dao.DaoSession;
import com.example.nore.turndown.entity.dao.ImageInfo;
import com.example.nore.turndown.entity.dao.ImageInfoDao;
import com.example.nore.turndown.entity.dao.Job;
import com.example.nore.turndown.entity.dao.JobDao;
import com.example.nore.turndown.entity.dao.Reporte;
import com.example.nore.turndown.entity.dao.ReporteDao;
import com.example.nore.turndown.entity.dao.TaskJob;
import com.example.nore.turndown.entity.dao.TaskJobDao;
import com.example.nore.turndown.util.interfaces.UpdateReport;

import java.io.File;
import java.util.List;

/**
 * Created by NORE on 09/07/2015.
 */
public class UpadateTask extends AsyncTask<Reporte, Void, Boolean> {
    public UpdateReport update;
    private DaoSession session;
    private int listSize = 0;

    private List<Job> blackList;
    private List<TaskJob> blackJobs;
    private Long idGenerated;

    @Override
    protected Boolean doInBackground(Reporte... params) {
        if (session != null) {
            Log.d("d", "TAMAÑOOOOOOOOOOOOOO: " + listSize);
            Reporte report = params[0];

            ReporteDao repDao = session.getReporteDao();
            JobDao jbDao = session.getJobDao();
            TaskJobDao taskDao = session.getTaskJobDao();
            ImageInfoDao infDao = session.getImageInfoDao();
            try {
                List<Job> jbs = report.getJobs2();
                for (int i = 0; i < jbs.size(); i++) {

                    Job currentJob = jbs.get(i);

                    if (currentJob.getId() != null) {
                        List<TaskJob> tasks = currentJob.getTasks2();

                        for (int j = 0; j < tasks.size(); j++) {

                            TaskJob task = tasks.get(j);
                            if (task.getId() != null) {
                                taskDao.update(task);
                            } else {
                                task.setJob(currentJob);
                                taskDao.insert(task);
                            }
                        }

                        List<ImageInfo> infList = currentJob.getImageInfo2();

                        if (infList != null) {
                            for (int n = 0; n < infList.size(); n++) {
                                ImageInfo inf = infList.get(n);
                                if (inf.getId() != null) {
                                    infDao.update(inf);
                                } else {
                                    inf.setJob(currentJob);
                                    infDao.insert(inf);
                                    Log.d("INSERTANDO IMAGEN", inf.getImgRoute());
                                }
                            }
                        }

                        jbDao.update(currentJob);
                    } else {
                        currentJob.setReporte(report);
                        jbDao.insert(currentJob);

                        List<TaskJob> tasks = currentJob.getTasks2();

                        for (int j = 0; j < tasks.size(); j++) {
                            TaskJob task = tasks.get(j);
                            task.setJob(currentJob);
                            taskDao.insert(task);
                        }

                        List<ImageInfo> infList = currentJob.getImageInfo2();
                        if (infList != null) {
                            for (int n = 0; n < infList.size(); n++) {
                                ImageInfo inf = infList.get(n);
                                inf.setJob(currentJob);
                                infDao.insert(inf);

                            }
                        }

                    }
                }

                if (blackList != null) {
                    if (!blackList.isEmpty()) {
                        for (int i = 0; i < blackList.size(); i++) {
                            Job black = blackList.get(i);

                            List<TaskJob> taskList = black.getTasks();

                            for (int j = 0; j < taskList.size(); j++) {
                                TaskJob task = taskList.get(j);
                                taskDao.delete(task);
                            }

                            List<ImageInfo> list = black.getImageInfo2();

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

                            jbDao.delete(black);
                        }
                    }
                }

                if (blackJobs != null) {
                    if (!blackJobs.isEmpty()) {
                        for (int h = 0; h < blackJobs.size(); h++) {
                            TaskJob task = blackJobs.get(h);
                            if (task.getId() != null) {
                                taskDao.delete(task);
                            }
                        }
                    }
                }

                repDao.update(report);
                idGenerated = report.getId();
                return true;
            } catch (Exception e) {
                Log.d("d", e.getMessage());
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(update!=null){
            update.updateResult(aBoolean, idGenerated);
        }
    }

    public void setSession(DaoSession session) {
        this.session = session;
    }

    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    public void setBlackList(List<Job> blackList) {
        this.blackList = blackList;
    }

    public void setBlackJobs(List<TaskJob> blackJobs) {
        this.blackJobs = blackJobs;
    }
}
