package com.example.nore.turndown.CustomListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.nore.turndown.R;
import com.example.nore.turndown.util.dtos.RepoMen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NORE on 18/07/2015.
 */
public class SendAdapter extends ArrayAdapter<RepoMen> {
    private List<RepoMen> objects;
    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<RepoMen> eliminateList;

    public SendAdapter(Context context, int resource, List<RepoMen> objects) {
        super(context, resource, objects);
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolderSend viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.send_item, parent, false);
            viewHolder = new ViewHolderSend();
            viewHolder.trabajo = (TextView) convertView.findViewById(R.id.trabajoSend);
            viewHolder.sitio = (TextView) convertView.findViewById(R.id.sitioSend);
            viewHolder.check = (CheckBox) convertView.findViewById(R.id.checkSend);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderSend) convertView.getTag();
        }

        final RepoMen rep = objects.get(position);
        viewHolder.trabajo.setText(rep.getTrabajo());
        viewHolder.sitio.setText(rep.getLocasion());
        viewHolder.check.setSelected(rep.isSelected());

        viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    eliminateList.add(rep);
                } else {
                    eliminateList.remove(rep);
                }
            }
        });
        return convertView;
    }

    public void setEliminateList(ArrayList<RepoMen> eliminateList) {
        this.eliminateList = eliminateList;
    }

    public List<RepoMen> getEliminateList() {
        return eliminateList;
    }

    public static class ViewHolderSend {
        public CheckBox check;
        TextView trabajo;
        TextView sitio;
    }
}
