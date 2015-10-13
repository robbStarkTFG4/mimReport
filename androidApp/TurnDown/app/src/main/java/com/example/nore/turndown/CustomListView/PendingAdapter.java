package com.example.nore.turndown.CustomListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nore.turndown.R;
import com.example.nore.turndown.entity.dao.Reporte;

import java.util.List;

/**
 * Created by NORE on 09/07/2015.
 */
public class PendingAdapter extends ArrayAdapter<Reporte> {
    private List<Reporte> objects;
    private LayoutInflater mLayoutInflater;

    public PendingAdapter(Context context, int resource, List<Reporte> objects) {
        super(context, resource, objects);
        mLayoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderPending viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.pending_item, parent, false);
            viewHolder = new ViewHolderPending();
            viewHolder.labelTrabajo = (TextView) convertView.findViewById(R.id.labelTrabajoView);
            viewHolder.trabajo = (TextView) convertView.findViewById(R.id.jobView);
            viewHolder.labelSitio = (TextView) convertView.findViewById(R.id.labelSitoView);
            viewHolder.sitio = (TextView) convertView.findViewById(R.id.sitioView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderPending) convertView.getTag();
        }

        Reporte rep = objects.get(position);
        viewHolder.trabajo.setText(rep.getTrabajo());
        viewHolder.sitio.setText(rep.getSitio());
        return convertView;
    }

    static class ViewHolderPending {
        TextView labelTrabajo;
        TextView trabajo;
        TextView labelSitio;
        TextView sitio;
    }
}
