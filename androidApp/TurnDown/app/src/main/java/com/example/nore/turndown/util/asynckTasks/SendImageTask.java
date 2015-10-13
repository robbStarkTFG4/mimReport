package com.example.nore.turndown.util.asynckTasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.nore.turndown.MainActivity;
import com.example.nore.turndown.backEnd.models.Reporte2;
import com.example.nore.turndown.backEnd.services.ReportService;
import com.example.nore.turndown.entity.dao.DaoSession;
import com.example.nore.turndown.entity.dao.ImageInfo;
import com.example.nore.turndown.entity.dao.Job;
import com.example.nore.turndown.entity.dao.JobDao;
import com.example.nore.turndown.entity.dao.Reporte;
import com.example.nore.turndown.entity.dao.ReporteDao;
import com.example.nore.turndown.util.tags.ReportStatus;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by NORE on 19/07/2015.
 */
public class SendImageTask extends AsyncTask<Job, Void, Boolean> {
    public interface ImageUpload {
        public void imageResult(boolean res);
    }

    private DaoSession session;
    private ImageUpload imUpload;
    private Reporte2 repServer;
    private Long reporteId;

    @Override
    protected Boolean doInBackground(Job... params) {
        if (session != null && repServer != null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(ReportService.BASE_URL)
                    .build();

            ReportService service = restAdapter.create(ReportService.class);

            for (int i = 0; i < params.length; i++) {

                Job jb = params[i];
                session.refresh(jb);

                List<ImageInfo> list = jb.getImageInfo2();

                if (!list.isEmpty()) {
                    for (int j = 0; j < list.size(); j++) {
                        ImageInfo inf = list.get(j);
                        if (inf.getCompressedImage() != null) {


                            File imageFileName = new File(inf.getCompressedImage());

                            TypedFile image = new TypedFile("image/jpeg", imageFileName);
                            if (imageFileName.exists()) {
                                if (repServer.getTrabajoList() != null) {
                                    Response res = service.uploadImage2(new TypedString(String.valueOf(repServer
                                            .getTrabajoList().get(i).getIdtrabajo())), image);
                                    if (res != null) {
                                        if (!(res.getStatus() == 204)) {
                                            return false;
                                        }
                                    } else {
                                        return false;
                                    }
                                } else {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }

            if (repServer.getIdreporte() != null) {
                try {
                    Response res = service.markRep(repServer.getIdreporte(), "dasdas");
                    if (res != null) {
                        if (res.getStatus() == 200) {
                            ReporteDao repDao = session.getReporteDao();
                            Reporte repo = repDao.queryBuilder().where(ReporteDao.Properties.Id.eq(reporteId)).unique();
                            repo.setStatus(ReportStatus.ENVIADO);
                            repDao.update(repo);
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }catch(Exception f){
                    return false;
                }
                //end
            } else {
                return false;
            }


        } else {
            return false;
        }
    }

    public void setSession(DaoSession session) {
        this.session = session;
    }

    public void setImUpload(ImageUpload imUpload) {
        this.imUpload = imUpload;
    }

    public void setRepServer(Reporte2 repServer) {
        this.repServer = repServer;
    }

    public void setReporteId(Long reporteId) {
        this.reporteId = reporteId;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (imUpload != null) {
            imUpload.imageResult(aBoolean);
        }
    }
}
