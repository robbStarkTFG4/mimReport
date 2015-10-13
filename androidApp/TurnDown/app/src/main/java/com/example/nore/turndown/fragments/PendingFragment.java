package com.example.nore.turndown.fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.nore.turndown.CustomListView.PendingAdapter;
import com.example.nore.turndown.MainActivity;
import com.example.nore.turndown.R;
import com.example.nore.turndown.entity.dao.DaoSession;
import com.example.nore.turndown.entity.dao.Reporte;
import com.example.nore.turndown.entity.dao.ReporteDao;
import com.example.nore.turndown.util.asynckTasks.DeleteRepTask;
import com.example.nore.turndown.util.dtos.RepoMen;
import com.example.nore.turndown.util.interfaces.PendingResult;
import com.example.nore.turndown.util.tags.ReportStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NORE on 11/06/2015.
 */
public class PendingFragment extends Fragment implements PendingResult, DeleteRepTask.DeleteResult {

    private ListView list;
    private List<Reporte> objects;
    private DaoSession session;
    private PendingAdapter adapt;
    private Reporte delRep;
    View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((MainActivity)getActivity()).setReportFrag(null);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment frag = fragmentManager.findFragmentByTag("enviados");
        if (frag != null) {
            fragmentManager.beginTransaction().remove(frag);
        }

        root = inflater.inflate(R.layout.layout_fragment_two, container, false);
        objects = new ArrayList<>();
        adapt = new PendingAdapter(getActivity(), R.layout.pending_item, objects);
        list = (ListView) root.findViewById(R.id.pendingListView);
        list.setAdapter(adapt);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AddJobFragment objT = AddJobFragment.newInstance(objects.get(position).getId(), (MainActivity) getActivity());
                ((MainActivity) getActivity()).setReportFrag(objT);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, objT
                                , "addJob")
                        .addToBackStack("pending")
                        .commit();

                Fragment frag = fragmentManager.findFragmentByTag("enviados");
                if (frag != null) {
                    fragmentManager.beginTransaction().remove(frag);
                }
                session = null;
            }
        });

        registerForContextMenu(list);

        //Toast.makeText(getActivity(), "Cargando pendientes...", Toast.LENGTH_SHORT).show();
        Snackbar
                .make(((MainActivity) getActivity()).getParentLayout(), "Cargando pendientes...", Snackbar.LENGTH_LONG)
                .show();
        LoadPendings loadTask = new LoadPendings();
        loadTask.res = this;
        loadTask.execute();
        return root;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo inf = (AdapterView.AdapterContextMenuInfo) menuInfo;
        delRep = objects.get(inf.position);

        /*Snackbar
                .make(((MainActivity) getActivity()).getParentLayout(), objects.get(inf.position).getTrabajo(), Snackbar.LENGTH_LONG)
                .show();*/
        //Toast.makeText(getActivity(), objects.get(inf.position).getTrabajo(), Toast.LENGTH_LONG).show();

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Warn");
        builder.setMessage("delete pending?");
        builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RepoMen[] repArray = new RepoMen[1];

                        RepoMen repi = new RepoMen();
                        repi.setId(delRep.getId());
                        repi.setTrabajo(delRep.getTrabajo());
                        repi.setLocasion(delRep.getSitio());

                        repArray[0] = repi;

                        DeleteRepTask delTask = new DeleteRepTask();
                        delTask.setRes(PendingFragment.this);
                        delTask.setSession(session);
                        delTask.execute(repArray);

                        objects.remove(delRep);
                        adapt.notifyDataSetChanged();
                    }
                }
        );
        builder.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }


    @Override
    public void showPendings(Boolean bol) {
        //Toast.makeText(getActivity(), "RESPONSE", Toast.LENGTH_SHORT).show();
        if (bol) {
            adapt.notifyDataSetChanged();
        } else {
            Snackbar
                    .make(((MainActivity) getActivity()).getParentLayout(), "No hay pendientes", Snackbar.LENGTH_LONG)
                    .show();
            //Toast.makeText(getActivity(), "No hay pendientes", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void result(boolean res) {
        if (res) {
            if (getActivity() != null) {
                Snackbar
                        .make(((MainActivity) getActivity()).getParentLayout(), "pendiente borrado", Snackbar.LENGTH_LONG)
                        .show();
                //Toast.makeText(getActivity(), "pendiente borrado", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (getActivity() != null) {
                Snackbar
                        .make(((MainActivity) getActivity()).getParentLayout(), "hubo un error", Snackbar.LENGTH_LONG)
                        .show();
                //Toast.makeText(getActivity(), "hubo un error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class LoadPendings extends AsyncTask<Void, Void, Boolean> {

        public PendingResult res;

        @Override
        protected Boolean doInBackground(Void... params) {
            session = ((MainActivity) getActivity()).session;
            ReporteDao repDao = session.getReporteDao();

            List<Reporte> list = repDao.queryBuilder().where(ReporteDao.Properties.Status
                    .eq(ReportStatus.PENDIENTE)).list();

            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    objects.add(list.get(i));
                }
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            res.showPendings(aBoolean);
        }
    }
}
