package com.example.nore.turndown.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.nore.turndown.CustomListView.SendAdapter;
import com.example.nore.turndown.MainActivity;
import com.example.nore.turndown.R;
import com.example.nore.turndown.entity.dao.DaoSession;
import com.example.nore.turndown.entity.dao.Reporte;
import com.example.nore.turndown.entity.dao.ReporteDao;
import com.example.nore.turndown.util.asynckTasks.DeleteRepTask;
import com.example.nore.turndown.util.dtos.RepoMen;
import com.example.nore.turndown.util.interfaces.sendLoad;
import com.example.nore.turndown.util.tags.ReportStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NORE on 11/06/2015.
 */
public class EnviadosFragment extends Fragment implements sendLoad, DeleteRepTask.DeleteResult {


    private ListView list;
    private List<RepoMen> objects;
    private DaoSession session;
    private SendAdapter adapt;
    private RepoMen[] repArray;
    View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((MainActivity)getActivity()).setReportFrag(null);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment frag = fragmentManager.findFragmentByTag("pending");
        if (frag != null) {
            fragmentManager.beginTransaction().remove(frag);
        }

        setHasOptionsMenu(true);
        root = inflater.inflate(R.layout.layout_fragment_three, container, false);
        objects = new ArrayList<>();

        adapt = new SendAdapter(getActivity(), R.layout.send_item, objects);
        adapt.setEliminateList(new ArrayList<RepoMen>());
        list = (ListView) root.findViewById(R.id.sendListView);
        list.setAdapter(adapt);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RepoMen rep = adapt.getItem(position);

                AddJobFragment objT = AddJobFragment.newInstance(objects.get(position).getId(), (MainActivity) getActivity());
                ((MainActivity) getActivity()).setReportFrag(objT);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, objT
                                , "addJob")
                        .addToBackStack("enviados")
                        .commit();

                Fragment frag = fragmentManager.findFragmentByTag("pending");
                if (frag != null) {
                    fragmentManager.beginTransaction().remove(frag);
                }
                session = null;

            }
        });

        Snackbar
                .make(((MainActivity) getActivity()).getParentLayout(), "Cargando...", Snackbar.LENGTH_LONG)
                .show();
        //Toast.makeText(getActivity(), "Cargando...", Toast.LENGTH_SHORT).show();
        LoadSendTask loadTask = new LoadSendTask();
        loadTask.res = this;
        loadTask.execute();
        return root;
    }

    @Override
    public void result(Boolean result) {
        if (result) {
            adapt.notifyDataSetChanged();
        } else {
            if (getActivity() != null) {
                Snackbar
                        .make(((MainActivity) getActivity()).getParentLayout(), "No hay reportes enviados", Snackbar.LENGTH_LONG)
                        .show();
                //Toast.makeText(getActivity(), "hubo algun error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //if (!((MainActivity) getActivity()).mNavigationDrawerFragment.isDrawerOpen()) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        menu.clear();// clears previous option menu to inflate a new one.
        getActivity().getMenuInflater().inflate(R.menu.send_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
        //}


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.borrarSend:
                if (adapt != null) {

                    List<RepoMen> list = adapt.getEliminateList();
                    if (!list.isEmpty()) {
                        DeleteRepTask delTask = new DeleteRepTask();
                        delTask.setRes(this);
                        delTask.setSession(session);
                        delTask.execute(list.toArray(new RepoMen[list.size()]));
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public void result(boolean res) {
        if (getActivity() != null) {
            if (res) {
                Snackbar
                        .make(((MainActivity) getActivity()).getParentLayout(), "elementos borrados con exito", Snackbar.LENGTH_LONG)
                        .show();
                //Toast.makeText(getActivity(), "elementos borrados con exito", Toast.LENGTH_SHORT).show();
                objects = new ArrayList<>();
                adapt = new SendAdapter(getActivity(), R.layout.send_item, objects);
                adapt.setEliminateList(new ArrayList<RepoMen>());
                list = (ListView) root.findViewById(R.id.sendListView);
                list.setAdapter(adapt);

                LoadSendTask loadTask = new LoadSendTask();
                loadTask.res = this;
                loadTask.execute();

                objects.clear();
                adapt.clear();
                adapt.notifyDataSetChanged();
                //list.clearChoices();
            }
        }
    }

    private class LoadSendTask extends AsyncTask<Void, Void, Boolean> {

        public sendLoad res;

        @Override
        protected Boolean doInBackground(Void... params) {
            if (getActivity() != null) {
                session = ((MainActivity) getActivity()).session;
                ReporteDao repDao = session.getReporteDao();

                List<Reporte> list = repDao.queryBuilder().where(ReporteDao.Properties.Status
                        .eq(ReportStatus.ENVIADO)).list();

                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        objects.add(RepoMen.transformToRepoMen(list.get(i)));
                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            res.result(aBoolean);
        }

        public void setRes(sendLoad res) {
            this.res = res;
        }
    }
}
