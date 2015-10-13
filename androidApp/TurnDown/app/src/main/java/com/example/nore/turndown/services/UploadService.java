package com.example.nore.turndown.services;


import android.database.sqlite.SQLiteDatabase;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import android.os.Process;

import com.example.nore.turndown.backEnd.models.Reporte2;
import com.example.nore.turndown.backEnd.models.Trabajo;
import com.example.nore.turndown.backEnd.services.ReportService;
import com.example.nore.turndown.entity.dao.DaoMaster;
import com.example.nore.turndown.entity.dao.DaoSession;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by NORE on 17/07/2015.
 */
public class UploadService extends Service {
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private UploadService upload;


    public interface FinishService {
        void stop();
    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler implements FinishService {


        private DaoMaster master;
        private DaoSession session;
        private int startId;

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            startId = msg.arg1;

            try {
                DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(upload, "mimDb4", null);
                SQLiteDatabase db = openHelper.getWritableDatabase();
                master = new DaoMaster(db);
                session = master.newSession();

                final Long id = msg.getData().getLong("idRep");
                Reporte2 reporte = (Reporte2) msg.getData().getSerializable("repi");
                String us = (String) msg.getData().get("usr");
                final String[] rutas = msg.getData().getStringArray("rutasImg");

                Toast.makeText(upload, "Enviando.....", Toast.LENGTH_LONG).show();
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(ReportService.BASE_URL)
                        .build();

                final ReportService apiService =
                        restAdapter.create(ReportService.class);


                final Gson gson = new Gson();


                apiService.uploadReportService(us, reporte, new retrofit.Callback<Reporte2>() {
                    @Override
                    public void success(Reporte2 rep, Response response) {
                        //Toast.makeText(getActivity(), gson.toJson(rep), Toast.LENGTH_LONG).show();
                        sendImages(rep, rutas, id);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(upload, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        stop();
                    }
                });


            } catch (Exception e) {
                Log.d("d", e.getMessage());
            }
        }

        private void sendImages(Reporte2 rep, String[] rutas, Long idRep) {
            List<Trabajo> listTrabajo = rep.getTrabajoList();
            final boolean[] resulsts = new boolean[listTrabajo.size()];
            for (int i = 0; i < listTrabajo.size(); i++) {
                String ruta = rutas[i];
                int id = listTrabajo.get(i).getIdtrabajo();
                if (ruta != null) {
                    //File file = new File(jb.getImgRoute());
               /* Bitmap original = BitmapFactory.decodeFile(jb.getImgRoute());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                original.compress(Bitmap.CompressFormat.JPEG, 80, out);
                Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));*/

                    RestAdapter restAdapter = new RestAdapter.Builder()
                            .setEndpoint(ReportService.BASE_URL)
                            .build();

                    ReportService service = restAdapter.create(ReportService.class);

                    File imageFileName = new File(ruta);

                    TypedFile image = new TypedFile("image/jpeg", imageFileName);
                    if (imageFileName.exists()) {
                        //Response res = service.uploadImage(new TypedString(String.valueOf(id)), image);
                        //int status = res.getStatus();

                        service.uploadImage(new TypedString(String.valueOf(id)), image, new retrofit.Callback<String>() {
                            @Override
                            public void success(String s, Response response) {
                                Toast.makeText(upload, "ESTATUS: " + response.getStatus(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(upload, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                        /*if (status == 204) {
                            resulsts[i] = true;
                        } else {
                            resulsts[i] = false;
                            stop();
                        }*/
                    }
                }
            }

            /*boolean update = true;
            for (boolean r : resulsts) {
                if (!r) {
                    update = false;
                }
            }

            if (update) {
                if (idRep != null) {
                    ReporteDao repDao = session.getReporteDao();
                    Reporte repo = null;
                    List<Reporte> res = repDao.queryBuilder().where(ReporteDao.Properties.Id.eq(idRep)).list();
                    if (res.size() > 0) {
                        repo = res.get(0);
                        repo.setStatus(ReportStatus.ENVIADO);
                        repDao.update(repo);
                        stop();
                    }
                }

            }*/
        }

        @Override
        public void stop() {
            stopSelf(startId);
        }
    }

    @Override
    public void onCreate() {
        upload = this;
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        upload = this;
        //android.os.Debug.waitForDebugger();
        Toast.makeText(this, "service starting: " + intent.getExtras().getLong("repId"), Toast.LENGTH_LONG).show();


        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();

        Bundle bundle = new Bundle();
        bundle.putLong("idRep", intent.getExtras().getLong("repId"));
        bundle.putSerializable("repi", intent.getExtras().getSerializable("reporte"));
        bundle.putString("usr", intent.getStringExtra("usuario"));
        bundle.putStringArray("rutas", intent.getStringArrayExtra("rutasImg"));

        msg.setData(bundle);
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}
