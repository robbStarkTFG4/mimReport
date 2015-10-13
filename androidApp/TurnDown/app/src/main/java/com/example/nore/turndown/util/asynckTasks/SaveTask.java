package com.example.nore.turndown.util.asynckTasks;

import android.os.AsyncTask;

import com.example.nore.turndown.entity.dao.DaoSession;
import com.example.nore.turndown.entity.dao.ImageInfo;
import com.example.nore.turndown.entity.dao.ImageInfoDao;
import com.example.nore.turndown.entity.dao.Job;
import com.example.nore.turndown.entity.dao.JobDao;
import com.example.nore.turndown.entity.dao.Reporte;
import com.example.nore.turndown.entity.dao.ReporteDao;
import com.example.nore.turndown.entity.dao.TaskJob;
import com.example.nore.turndown.entity.dao.TaskJobDao;
import com.example.nore.turndown.entity.dao.Usuario;
import com.example.nore.turndown.util.interfaces.SaveListener;
import com.example.nore.turndown.util.tags.ReportStatus;

import java.util.Date;
import java.util.List;

/**
 * Created by NORE on 11/07/2015.
 */
public class SaveTask extends AsyncTask<Reporte, Void, Boolean> {

    private SaveListener saveRes;
    private DaoSession session;
    private String jobDes;
    private String locacion;
    private Usuario usuario;
    private List<Job> list;

    private Long reporteGeneratedId;

    private Reporte report;

    @Override
    protected Boolean doInBackground(Reporte... params) {

        if (session != null) {
            ReporteDao repDao = session.getReporteDao();
            JobDao jbDao = session.getJobDao();
            TaskJobDao taskDao = session.getTaskJobDao();
            ImageInfoDao infDao = session.getImageInfoDao();

            try {
                report = new Reporte();
                report.setDate(new Date());
                report.setSitio(locacion);
                report.setTrabajo(jobDes);
                report.setUsuario(usuario);
                report.setStatus(ReportStatus.PENDIENTE);
                repDao.insert(report);
                reporteGeneratedId = report.getId();
                for (int i = 0; i < list.size(); i++) {
                    Job jb = list.get(i);
                    jb.setReporte(report);
                    //jb.setCompressedImage("non");
                    jbDao.insert(jb);

                    for (int j = 0; j < jb.getTasks2().size(); j++) {
                        TaskJob task = jb.getTasks2().get(j);
                        task.setJob(jb);
                        taskDao.insert(task);
                    }

                    if (jb.getImageInfo2() != null) {

                        for (int k = 0; k < jb.getImageInfo2().size(); k++) {
                            ImageInfo inf = jb.getImageInfo2().get(k);
                            inf.setJob(jb);
                            infDao.insert(inf);
                        }
                    }
                }
                return true;
            } catch (Exception e) {

                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (saveRes != null) {
            saveRes.saveResult(aBoolean, reporteGeneratedId, report);
        }
    }

    public void setSession(DaoSession session) {
        this.session = session;
    }

    public void setSaveRes(SaveListener saveRes) {
        this.saveRes = saveRes;
    }

    public void setJobDes(String jobDes) {
        this.jobDes = jobDes;
    }

    public void setLocacion(String locacion) {
        this.locacion = locacion;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setList(List<Job> list) {
        this.list = list;
    }
}
