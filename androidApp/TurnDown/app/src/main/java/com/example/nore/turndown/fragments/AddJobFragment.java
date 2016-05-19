package com.example.nore.turndown.fragments;


import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nore.turndown.CustomListView.ExpandableCustomAdapter;
import com.example.nore.turndown.MainActivity;
import com.example.nore.turndown.R;
import com.example.nore.turndown.backEnd.ConvertManager;
import com.example.nore.turndown.backEnd.models.Reporte2;
import com.example.nore.turndown.backEnd.models.Trabajo;
import com.example.nore.turndown.backEnd.services.ReportService;
import com.example.nore.turndown.customDialog.CustomDialogSubTask;
import com.example.nore.turndown.entity.dao.DaoSession;
import com.example.nore.turndown.entity.dao.ImageInfo;
import com.example.nore.turndown.entity.dao.Job;
import com.example.nore.turndown.entity.dao.Reporte;
import com.example.nore.turndown.entity.dao.ReporteDao;
import com.example.nore.turndown.entity.dao.TaskJob;
import com.example.nore.turndown.entity.dao.Usuario;
import com.example.nore.turndown.entity.dao.UsuarioDao;
import com.example.nore.turndown.util.asynckTasks.CompressImageTask;
import com.example.nore.turndown.util.asynckTasks.CompressImageTask2;
import com.example.nore.turndown.util.asynckTasks.CreteReportTask;
import com.example.nore.turndown.util.asynckTasks.CreteReportTask.ReportBuilder;
import com.example.nore.turndown.util.asynckTasks.DeleteGarbage;
import com.example.nore.turndown.util.asynckTasks.SaveTask;
import com.example.nore.turndown.util.asynckTasks.SendImageTask;
import com.example.nore.turndown.util.asynckTasks.UpadateTask;
import com.example.nore.turndown.util.interfaces.LoadReport;
import com.example.nore.turndown.util.interfaces.ReportFragment;
import com.example.nore.turndown.util.interfaces.SaveListener;
import com.example.nore.turndown.util.interfaces.UpdateReport;
import com.example.nore.turndown.util.interfaces.WidgetPortable;
import com.example.nore.turndown.util.tags.FragmentTags;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by NORE on 11/06/2015.
 */
public class AddJobFragment extends Fragment implements LoadReport, UpdateReport, SaveListener
        , CompressImageTask.CompressState, CompressImageTask2.CompressState2, SendImageTask.ImageUpload
        , ExpandableCustomAdapter.WatchMen, ReportBuilder, ReportFragment, ExpandableCustomAdapter.CouplerSaver {

    private int controller = 0;
    private boolean smartSave = false;

    @Override
    public void buildResult(boolean res) {
        if (getActivity() != null) {
            if (res) {
                Snackbar
                        .make(((MainActivity) getActivity()).getParentLayout(), "Reporte creado", Snackbar.LENGTH_INDEFINITE)
                        .show();
            } else {
                Snackbar
                        .make(((MainActivity) getActivity()).getParentLayout(), "Hubo un error", Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    }

    @Override
    public void notifyFragmentSmartSave() {
        smartSave = true;
    }

    public interface TransitionCommunicator {
        public void allowed(boolean res);
        public void processBackEvent();
    }

    private TransitionCommunicator trans;

    private View root;
    private ExpandableListView listView;
    private List<Job> jobList;
    private int listsize = 0;
    private int should = 0;
    private ExpandableCustomAdapter adapter;
    private Usuario usuario;
    private Reporte report = null;
    private Long currentReporTid;

    private List<Job> blackList;
    private List<TaskJob> taskBlackList;

    private DaoSession session;


    // WIDGETS
    private EditText jobField;
    private EditText locationField;

    //END WIDGETS

    public static AddJobFragment newInstance(Long repId, MainActivity act) {
        AddJobFragment fragment = new AddJobFragment();
        fragment.setTrans(act);

        Bundle args = new Bundle();
        args.putLong("repId", repId);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true); // needed for placing fragment own option menu.
        root = inflater.inflate(R.layout.layout_fragment_one, container, false);
        session = ((MainActivity) getActivity()).session;

        setUpWidgets(root);
        listViewConfig();

        if (getArguments() != null) {
            Long reportId = getArguments().getLong("repId");

            if (session != null) {
                loadReport(reportId);
            }
        } else {
            setUpTestData();
            verifyUser();
        }

        return root;
    }

    /**
     * Carga un reporte con "reportId" usando asynckTask, el resultado se va a procesar en el metodo
     * "LoadReportResult(Reporte rep)
     * @param reportId
     */
    private void loadReport(Long reportId) {
        Long[] array = new Long[1];
        array[0] = reportId;
        loadReportTask load = new loadReportTask();
        load.execute(array);
        load.load = this;
    }

    private void setUpWidgets(View root) {
        jobField = (EditText) root.findViewById(R.id.jobField);
        jobField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                smartSave = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                smartSave = true;
            }
        });
        locationField = (EditText) root.findViewById(R.id.sitioField);
        locationField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                smartSave = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void verifyUser() {

        UsuarioDao userDao = session.getUsuarioDao();
        List<Usuario> userList = userDao.queryBuilder().where(UsuarioDao.Properties.ActiveUser.eq(true)).list();
        if (userList != null) {
            if ((userList.size() > 0)) {
                usuario = userList.get(0);
                Snackbar
                        .make(((MainActivity) getActivity()).getParentLayout(), "Usuario: " + usuario.getUsuario(), Snackbar.LENGTH_LONG)
                        .show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Warn");
                builder.setMessage("No hay usuario registrado, deseas registrarlo ahora?");
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO

                        dialog.dismiss();
                        Fragment obj = new RegisterAccount();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, obj, FragmentTags.REGISTER)
                                .commit();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    @Override
    public void deletedJob(int position) {
        if (report != null) {
            if (blackList == null) {
                blackList = new ArrayList<>();
            }
            blackList.add(jobList.get(position));
        }
    }

    private void listViewConfig() {

        if (jobList == null) {
            jobList = new ArrayList<>();
            adapter = new ExpandableCustomAdapter(getActivity(), jobList);
            adapter.setFacil((MainActivity) getActivity());
            adapter.setWatch(this);
            adapter.setCouplerSaver(this);

            listView = (ExpandableListView) root.findViewById(R.id.jobList);
            listView.setAdapter(adapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    TextView subText = (TextView) v.findViewById(R.id.descriptionTask);
                    FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    android.app.Fragment prev = getActivity().getFragmentManager()
                            .findFragmentByTag("subTaskDialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    subTaskPort subPort = new subTaskPort();
                    subPort.subTextView = subText;

                    CustomDialogSubTask newFragment = CustomDialogSubTask.newInstance(1,
                            jobList.get(groupPosition).getTasks2().get(childPosition), subPort);
                    newFragment.setSave(AddJobFragment.this);
                    newFragment.show(getActivity().getFragmentManager(), FragmentTags.SUB_TASK_DIALOG);
                    return false;
                }
            });

            registerForContextMenu(listView);
        }
    }

    /**
     * Metodo de interface  "LoadReport" que recibe un reporte cargado en una asyncTask
     * @param rep
     */
    @Override
    public void LoadReportResult(Reporte rep) {
        if (rep != null) {
            report = rep;
            jobField.setText(rep.getTrabajo());
            locationField.setText(rep.getSitio());
            jobList.clear();

            for (int i = 0; i < report.getJobs2().size(); i++) {
                Job jc = report.getJobs2().get(i);
                Job jb = new Job();

                jb.setReporte(report);
                jb.setReportId(report.getId());
                jb.setId(jc.getId());
                jb.setJob(jc.getJob());

                // Build cloned List
                List<ImageInfo> infList = new ArrayList<>();

                for (int j = 0; j < jc.getImageInfo2().size(); j++) {
                    ImageInfo temp = jc.getImageInfo2().get(j);
                    ImageInfo inf = new ImageInfo();

                    //inf.setJob();
                    inf.setId(temp.getId());
                    inf.setJobId(jc.getId());
                    inf.setJob(jc);
                    inf.setImgRoute(temp.getImgRoute());
                    inf.setCompressedImage(temp.getCompressedImage());

                    infList.add(inf);
                }


                jb.setImageInfo(infList);// added cloned jobList

                //Build cloned task List
                List<TaskJob> taskList = new ArrayList<>();

                for (int n = 0; n < jc.getTasks2().size(); n++) {
                    TaskJob temp = jc.getTasks2().get(n);
                    TaskJob task = new TaskJob();

                    task.setId(temp.getId());
                    task.setJobId(jc.getId());
                    task.setJob(jc);
                    task.setDescripcion(temp.getDescripcion());

                    taskList.add(task);
                }
                jb.setTasks(taskList);//added cloned jobList
                jobList.add(jb);
            }

            //jobList.addAll(rep.getJobs2());
            listsize = jobList.size();
            usuario = report.getUsuario();

            adapter.notifyDataSetChanged();
            if (getActivity() != null) {
                Snackbar
                        .make(((MainActivity) getActivity()).getParentLayout(), "Reporte cargado", Snackbar.LENGTH_LONG)
                        .show();
                //Toast.makeText(getActivity(), "Reporte cargado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     *
     * @param res
     * @param idRep
     */
    @Override
    public void updateResult(Boolean res, Long idRep) {
        if (res) {
            if (getActivity() != null) {
                switch (should) {
                    case 0:
                        //Toast.makeText(getActivity(), "Cambios guardados: " + idRep, Toast.LENGTH_SHORT).show();
                        showMessage(idRep);
                        break;
                    case 1:
                        //sendReport(id); old paths
                        currentReporTid = idRep;
                        compressIfNecesary();
                        break;
                    case 2:
                        launchReportBuilder();
                        break;
                    case 3:
                        smartSave = false;
                        ((MainActivity) getActivity()).performChange();
                        showMessage(idRep);
                        break;
                    case 4:
                        smartSave = false;
                        ((MainActivity) getActivity()).backEvent();
                        showMessage(idRep);
                        break;
                }
            }
        } else {
            if (getActivity() != null) {
                Snackbar
                        .make(((MainActivity) getActivity()).getParentLayout(), "Hubo algun error.. intenta de nuevo", Snackbar.LENGTH_LONG)
                        .show();
                //Toast.makeText(getActivity(), "Hubo algun error.. intenta de nuevo", Toast.LENGTH_SHORT).show();
                trans.allowed(false);
            }
        }
    }

    private void showMessage(Long idRep) {
        Snackbar
                .make(((MainActivity) getActivity()).getParentLayout(), "Cambios guardados: " + idRep, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void saveResult(Boolean res, Long id, Reporte repo) {
        if (res) {
            if (getActivity() != null) {
                report = repo;
                switch (should) {
                    case 0:
                        Snackbar
                                .make(((MainActivity) getActivity()).getParentLayout(), "Reporte guardado: " + id, Snackbar.LENGTH_LONG)
                                .show();
                        //Toast.makeText(getActivity(), "Reporte guardado: " + id, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        currentReporTid = id;
                        compressIfNecesary();
                        break;
                    case 2:
                        launchReportBuilder();
                        break;
                    case 3:
                        smartSave = false;
                        ((MainActivity) getActivity()).performChange();
                        showMessage(id);
                        break;
                }
            }
        } else {
            if (getActivity() != null) {
                trans.allowed(false);
                Snackbar
                        .make(((MainActivity) getActivity()).getParentLayout(), "Hubo algun error", Snackbar.LENGTH_LONG)
                        .show();
                //Toast.makeText(getActivity(), "Hubo algun error", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void launchReportBuilder() {
        String jobDes = jobField.getText().toString();
        String locacion = locationField.getText().toString();


        if ((jobDes.length() > 0) && (locacion.length() > 0)) {

            CreteReportTask save = new CreteReportTask();
            save.setSession(session);
            save.setContext(getActivity());

            Reporte rep = new Reporte();

            rep.setUsuario2(usuario);
            rep.setJobsList(jobList);

            //save.setUsuario(usuario);
            rep.setTrabajo(jobDes);
            rep.setSitio(locacion);
            save.setRes(this);

            Reporte[] array = new Reporte[1];
            array[0] = rep;
            save.execute(array);

            Snackbar snackbar = Snackbar
                    .make(((MainActivity) getActivity()).getParentLayout(), "Creando reporte.....", Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(getResources().getColor(R.color.secondary_texto));

            ViewGroup group = (ViewGroup) snackbar.getView();
            group.setBackgroundColor(getResources().getColor(R.color.alerta));

            snackbar.show();
        }
    }

    public class subTaskPort implements WidgetPortable {
        public TextView subTextView;
    }

    private void setUpTestData() {
        Job job = new Job();
        job.setJob("Paquete refacciones");
        List<TaskJob> taskList = new ArrayList<>();
        TaskJob task = new TaskJob();
        task.setDescripcion("refaccion # pzas");
        taskList.add(task);
        job.setTasks(taskList);

        Job job2 = new Job();
        job2.setJob("Actividades ejecucion");
        List<TaskJob> taskList2 = new ArrayList<>();
        TaskJob task2 = new TaskJob();
        task2.setDescripcion("cambio cadena");
        taskList2.add(task2);
        job2.setTasks(taskList2);

        Job job3 = new Job();
        job3.setJob("Cierre");
        List<TaskJob> taskList3 = new ArrayList<>();
        TaskJob task3 = new TaskJob();
        task3.setDescripcion("observacion 1");
        taskList3.add(task3);
        job3.setTasks(taskList3);

        Job job4 = new Job();
        job4.setJob("Mejoras");
        List<TaskJob> taskList4 = new ArrayList<>();
        TaskJob task4 = new TaskJob();
        task4.setDescripcion("mejora 1");
        taskList4.add(task4);
        job4.setTasks(taskList4);

        jobList.add(job);
        jobList.add(job2);
        jobList.add(job3);
        jobList.add(job4);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        int groupPosition = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        int childPosition = ExpandableListView.getPackedPositionChild(info.packedPosition);

        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            menu.setHeaderTitle("Warn");
            menu.add(groupPosition, childPosition, 1, "Delete");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getOrder()) {
            case 1:
                //Toast.makeText(getActivity(), "borra sub", Toast.LENGTH_LONG).show();
                if (taskBlackList == null) {
                    taskBlackList = new ArrayList<>();
                }
                taskBlackList.add(jobList.get(item.getGroupId()).getTasks2().get(item.getItemId()));
                jobList.get(item.getGroupId()).getTasks2().remove(item.getItemId());
                adapter.notifyDataSetChanged();
                smartSave = true;
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // if (!((MainActivity) getActivity()).mNavigationDrawerFragment.isDrawerOpen()) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        menu.clear();// clears previous option menu to inflate a new one.
        getActivity().getMenuInflater().inflate(R.menu.job_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
        //}


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addTaskOption:
                addJobItem();
                break;
           /* case R.id.saveReport:
                should = 0;
                persistReport();
                break;*/
            case R.id.send:
                trans.allowed(true);
                uploadToServer();
                break;
            case R.id.buildReport:
                should = 2;
                persistReport();
                break;
        }
        return true;
    }

    private void addJobItem() {
        Job job = new Job();
        job.setJob("Descripcion...");

        List<TaskJob> taskList = new ArrayList<>();
        TaskJob task = new TaskJob();
        task.setDescripcion("Nota...");
        taskList.add(task);
        job.setTasks(taskList);
        jobList.add(job);
        adapter.notifyDataSetChanged();
        smartSave = true;
        listView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listView.setSelection(adapter.getGroupCount() - 1);
            }
        });

    }

    /**
     * actualizar reporte o persister en db local
     */
    private void persistReport() {
        if (report != null) {
            //Toast.makeText(, "Actualizando.. ", Toast.LENGTH_LONG).show();
            Snackbar
                    .make(((MainActivity) getActivity()).getParentLayout(), "Actualizando.. ", Snackbar.LENGTH_INDEFINITE)
                    .show();
            updateReport();
        } else {
            //Toast.makeText(getActivity(), "Saving Report...", Toast.LENGTH_LONG).show();
            //Toast.makeText(getActivity(), "Crear Nuevo", Toast.LENGTH_LONG).show();
            Snackbar
                    .make(((MainActivity) getActivity()).getParentLayout(), "Saving Report...", Snackbar.LENGTH_LONG)
                    .show();
            buildReport();
        }
    }

    /**
     * Empieza secuencia para subir al servidor
     */
    private void uploadToServer() {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Warn");
        builder.setMessage("Enviar?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                dialog.dismiss();
                should = 1;
                persistReport();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    @Override
    public void compressResult2(boolean res) {
        if (res) {
            //continue to send
            sendReport2(currentReporTid);
        } else {
            if (getActivity() != null) {
                trans.allowed(false);
                //Toast.makeText(getActivity(), "hubo algun error....", Toast.LENGTH_LONG).show();
                Snackbar
                        .make(((MainActivity) getActivity()).getParentLayout(), "hubo algun error....", Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    }

    private void compressIfNecesary() {
        //Toast.makeText(getActivity(), "Comprimiendo imagenes.....", Toast.LENGTH_LONG).show();
        Snackbar
                .make(((MainActivity) getActivity()).getParentLayout(), "Comprimiendo imagenes.....", Snackbar.LENGTH_INDEFINITE)
                .show();

        CompressImageTask2 imgTask2 = new CompressImageTask2();
        imgTask2.setCompress(this);
        imgTask2.setSession(session);
        Job[] jbArray = new Job[jobList.size()];
        for (int i = 0; i < jobList.size(); i++) {
            jbArray[i] = jobList.get(i);
        }
        imgTask2.execute(jbArray);
    }

    private void sendReport2(final Long repId) {
        //Toast.makeText(getActivity(), "Enviando.....", Toast.LENGTH_LONG).show();
        Snackbar snackbar = Snackbar
                .make(((MainActivity) getActivity()).getParentLayout(), "Enviando.....", Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(getResources().getColor(R.color.secondary_texto));

        ViewGroup group = (ViewGroup) snackbar.getView();
        group.setBackgroundColor(getResources().getColor(R.color.alerta));

        snackbar.show();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ReportService.BASE_URL)
                .build();

        final ReportService apiService =
                restAdapter.create(ReportService.class);


        final Gson gson = new Gson();

        String trabajo = jobField.getText().toString();
        String sitio = locationField.getText().toString();

        Reporte2 reporteObj = new Reporte2();
        reporteObj.setEstatus(0);
        reporteObj.setSitio(sitio);
        reporteObj.setTrabajo(trabajo);
        reporteObj.setTrabajoList(ConvertManager.Convert(jobList));

        String us = usuario.getUsuario();

        apiService.uploadReportService(us, reporteObj, new Callback<Reporte2>() {
            @Override
            public void success(Reporte2 rep, Response response) {
                launchTaskImage(rep, repId);
            }

            @Override
            public void failure(RetrofitError error) {
                if (getActivity() != null) {
                    Snackbar
                            .make(((MainActivity) getActivity()).getParentLayout(), error.getLocalizedMessage(), Snackbar.LENGTH_INDEFINITE)
                            .show();
                    //Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    trans.allowed(false);
                }
            }
        });
    }

    @Override
    public void imageResult(boolean res) {
        if (res) {
            if (getActivity() != null) {

                Snackbar
                        .make(((MainActivity) getActivity()).getParentLayout(), "Reporte enviado", Snackbar.LENGTH_LONG)
                        .show();
                //Toast.makeText(getActivity(), "Reporte enviado", Toast.LENGTH_LONG).show();
                trans.allowed(false);
                should = 0;
            }
        } else {
            if (getActivity() != null) {
                Snackbar
                        .make(((MainActivity) getActivity()).getParentLayout(), "hubo algun error", Snackbar.LENGTH_LONG)
                        .show();
                //Toast.makeText(getActivity(), "hubo algun error", Toast.LENGTH_LONG).show();
                trans.allowed(false);
            }
        }
    }

    private void launchTaskImage(Reporte2 rep, Long repId) {
        SendImageTask imageTask = new SendImageTask();
        imageTask.setSession(session);
        imageTask.setImUpload(this);
        imageTask.setReporteId(repId);
        imageTask.setRepServer(rep);

        Job[] jbArray = new Job[jobList.size()];
        for (int i = 0; i < jobList.size(); i++) {
            jbArray[i] = jobList.get(i);
        }
        imageTask.execute(jbArray);
    }


    private void sendReport(final Long id) {
        Snackbar
                .make(((MainActivity) getActivity()).getParentLayout(), "Enviando.....", Snackbar.LENGTH_INDEFINITE)
                .show();
        //Toast.makeText(getActivity(), "Enviando.....", Toast.LENGTH_LONG).show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ReportService.BASE_URL)
                .build();

        final ReportService apiService =
                restAdapter.create(ReportService.class);


        final Gson gson = new Gson();

        String trabajo = jobField.getText().toString();
        String sitio = locationField.getText().toString();

        Reporte2 reporteObj = new Reporte2();
        reporteObj.setEstatus(0);
        reporteObj.setSitio(sitio);
        reporteObj.setTrabajo(trabajo);
        //reporteObj.setFecha(new Date());
        reporteObj.setTrabajoList(ConvertManager.Convert(jobList));

        String us = usuario.getUsuario();

        apiService.uploadReportService(us, reporteObj, new Callback<Reporte2>() {
            @Override
            public void success(Reporte2 rep, Response response) {
                //Toast.makeText(getActivity(), gson.toJson(rep), Toast.LENGTH_LONG).show();
                should = 0;
                //sendImages(rep, id);
                sendImages2(rep, id);
            }

            @Override
            public void failure(RetrofitError error) {
                if (getActivity() != null) {
                    Snackbar
                            .make(((MainActivity) getActivity()).getParentLayout(), error.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                            .show();
                    //Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void compressResult(boolean res, Long jobId, int serverId) {
        if (res) {
            sendRepAfterCompress(jobId);
        } else {

        }
    }

    private void sendImages2(Reporte2 rep, final Long idRep) {
        List<Trabajo> listTrabajo = rep.getTrabajoList();
        final int last = listTrabajo.get(listTrabajo.size() - 1).getIdtrabajo();
        for (int i = 0; i < listTrabajo.size(); i++) {
            Job jb = jobList.get(i);
            final int id = listTrabajo.get(i).getIdtrabajo();
            if (!jb.getImageInfo2().isEmpty()) {
                for (int k = 0; k < jb.getImageInfo2().size(); k++) {
                    ImageInfo inf = jb.getImageInfo2().get(k);
                    if (inf.getImgRoute() != null) {

                        CompressImageTask imageTask = new CompressImageTask();
                        imageTask.setSession(session);
                        imageTask.setCompress(this);
                        imageTask.setJobId(jb.getId());
                        imageTask.setServerId(id);
                        Job[] jobs = new Job[1];
                        jobs[0] = jb;
                        imageTask.execute(jobs);
                    }
                }
            }
        }
    }

    private void sendRepAfterCompress(Long jobId) {
        Snackbar
                .make(((MainActivity) getActivity()).getParentLayout(), "Enviando.....", Snackbar.LENGTH_INDEFINITE)
                .show();
        //Toast.makeText(getActivity(), "Enviando.....", Toast.LENGTH_LONG).show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ReportService.BASE_URL)
                .build();

        final ReportService apiService =
                restAdapter.create(ReportService.class);


        final Gson gson = new Gson();

        String trabajo = jobField.getText().toString();
        String sitio = locationField.getText().toString();

        Reporte2 reporteObj = new Reporte2();
        reporteObj.setEstatus(0);
        reporteObj.setSitio(sitio);
        reporteObj.setTrabajo(trabajo);
        //reporteObj.setFecha(new Date());
        reporteObj.setTrabajoList(ConvertManager.Convert(jobList));

        String us = usuario.getUsuario();

        apiService.uploadReportService(us, reporteObj, new Callback<Reporte2>() {
            @Override
            public void success(Reporte2 rep, Response response) {
                //Toast.makeText(getActivity(), gson.toJson(rep), Toast.LENGTH_LONG).show();
                should = 0;
                //sendImages(rep, id);
                sendImages2(rep, currentReporTid);
            }

            @Override
            public void failure(RetrofitError error) {
                if (getActivity() != null) {
                    Snackbar
                            .make(((MainActivity) getActivity()).getParentLayout(), error.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                            .show();
                    //Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void markReport(Long id, int enviado) {
        ReporteDao repDao = session.getReporteDao();
        Reporte repo = null;
        List<Reporte> res = repDao.queryBuilder().where(ReporteDao.Properties.Id.eq(id)).list();
        if (res.size() > 0) {
            repo = res.get(0);
            repo.setStatus(enviado);
            repDao.update(repo);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //Toast.makeText(getActivity(), "HOLA CRAYOLA!!!!!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*if (smartSave && report != null) {
            Toast.makeText(getActivity(), "Grabar Automaticamente", Toast.LENGTH_LONG).show();
            smartSave = false;
        }


        if (smartSave && (report == null)) {
            smartSave = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(AddJobFragment.this.getActivity());
            builder.setTitle("Warn");
            builder.setMessage("Descartar reporte?");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    garbageAssets();
                    AddJobFragment.super.onDestroy();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //TODO
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (report == null) {
            garbageAssets();
            AddJobFragment.super.onDestroy();
        } else {
            AddJobFragment.super.onDestroy();
        }*/
    }

    private void garbageAssets() {
        if (report == null) {

            Job[] jobs = new Job[jobList.size()];
            for (int i = 0; i < jobList.size(); i++) {
                jobs[i] = jobList.get(i);
            }

            new DeleteGarbage().execute(jobs);
        } else {
            //Toast.makeText(getActivity(), "Conservarlas", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * actualiza reporte existente
     */
    private void updateReport() {

        String jobDes = jobField.getText().toString();
        String locacion = locationField.getText().toString();

        if ((jobDes.length() > 0) && (locacion.length() > 0)) {
            UpadateTask updater = new UpadateTask();
            updater.setSession(session);

            Reporte[] reps = new Reporte[1];
            report.setTrabajo(jobField.getText().toString());
            report.setSitio(locationField.getText().toString());

            if (report.getJobs2() != null) {
                report.getJobs2().clear();
            }

            report.setJobsList(jobList);

            reps[0] = report;
            updater.update = this;
            updater.setListSize(listsize);
            updater.setBlackList(blackList);
            updater.setBlackJobs(taskBlackList);
            updater.execute(reps);
        } else {
            controller = 3;
            Snackbar
                    .make(((MainActivity) getActivity()).getParentLayout(), "Añade descripcion y locacion", Snackbar.LENGTH_LONG)
                    .show();
            trans.allowed(false);
        }

    }

    private void buildReport() {
        String jobDes = jobField.getText().toString();
        String locacion = locationField.getText().toString();


        if ((jobDes.length() > 0) && (locacion.length() > 0)) {

            SaveTask save = new SaveTask();
            save.setSession(session);
            save.setList(jobList);
            save.setUsuario(usuario);
            save.setJobDes(jobDes);
            save.setLocacion(locacion);
            save.setSaveRes(this);
            save.execute(new Reporte[1]);


        } else {
            controller = 3;
            Snackbar
                    .make(((MainActivity) getActivity()).getParentLayout(), "Añade descripcion y locacion", Snackbar.LENGTH_LONG)
                    .show();
            trans.allowed(false);
            //Toast.makeText(getActivity(), "Añade descripcion y locasion", Toast.LENGTH_LONG).show();
        }
    }

    public List<Job> getJobList() {
        return jobList;
    }

    public void setTrans(TransitionCommunicator trans) {
        this.trans = trans;
    }

    public class loadReportTask extends AsyncTask<Long, Void, Reporte> {
        public LoadReport load;

        @Override
        protected Reporte doInBackground(Long... params) {
            Long reportId = params[0];
            ReporteDao repDao = session.getReporteDao();
            Reporte reporte = null;
            List<Reporte> res = repDao.queryBuilder().where(ReporteDao.Properties.Id.eq(reportId)).list();
            if (res.size() > 0) {
                reporte = res.get(0);
                List<Job> jobs = reporte.getJobs();
                if (jobs.size() > 0) {
                    for (int i = 0; i < jobs.size(); i++) {
                        jobs.get(i).getTasks();
                        jobs.get(i).getImageInfo();
                    }
                }
            }
            return reporte;
        }

        @Override
        protected void onPostExecute(Reporte reporte) {
            super.onPostExecute(reporte);
            load.LoadReportResult(reporte);
        }
    }

    @Override
    public void destroyFragment() {
        //Toast.makeText(getActivity(), "entre al destroy", Toast.LENGTH_LONG).show();
        //Fragment frag = getActivity().getSupportFragmentManager().findFragmentByTag("addJob");
        List<Fragment> list = getActivity().getSupportFragmentManager().getFragments();

        if (list != null) {
            if (list.size() > 1) {
                controller = 0;
            } else {
                controller = 1;
            }
        }
    }

    @Override
    public int actionControl() {
        return controller;
    }

    @Override
    public void triggerAction() {
        should = 4;
        // persistReport();
        autoSave();


        if (smartSave && (report == null)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(AddJobFragment.this.getActivity());
            builder.setTitle("Warn");
            builder.setMessage("Guardar Reporte?");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    persistReport();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //TODO
                    dialog.dismiss();
                    garbageAssets();
                    ((MainActivity) getActivity()).backEvent();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (report == null) {
            ((MainActivity) getActivity()).backEvent();
        } else {
            ((MainActivity) getActivity()).backEvent();
        }

    }

    @Override
    public void triggerActionFromDrawer() {
        should = 3;
        // persistReport();
        autoSave();


        if (smartSave && (report == null)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(AddJobFragment.this.getActivity());
            builder.setTitle("Warn");
            builder.setMessage("Guardar Reporte?");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    persistReport();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //TODO
                    garbageAssets();
                    dialog.dismiss();
                    ((MainActivity) getActivity()).performChange();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (report == null) {
            ((MainActivity) getActivity()).performChange();
        } else {
            ((MainActivity) getActivity()).performChange();
        }

    }

    private void autoSave() {
        if (smartSave && report != null) {
            //testToast.makeText(getActivity(), "Grabar Automaticamente", Toast.LENGTH_LONG).show();
            persistReport();
        }
    }

    @Override
    public boolean performSave() {
        return false;
    }
}
