package com.example.nore.turndown.CustomListView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.nore.turndown.R;
import com.example.nore.turndown.entity.dao.Job;


import java.util.List;

/**
 * Custom adapter for listview
 * Created by NORE on 03/07/2015.
 */
public class CustomAdapter extends ArrayAdapter<Job> {
    private List<Job> objects;
    private LayoutInflater mLayoutInflater;

    public CustomAdapter(Context context, int resource, Job[] objects) {
        super(context, resource, objects);
    }

    public CustomAdapter(Context context, int resource, List<Job> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    public CustomAdapter(Context context, int resource) {
        super(context, resource);
    }

    public CustomAdapter(Context context, List<Job> objects) {
        super(context, R.layout.list_item, objects);
        mLayoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.task = (TextView) convertView.findViewById(R.id.subTask);
            viewHolder.photo = (Button) convertView.findViewById(R.id.photoBtn);
            viewHolder.addSubTask = (Button) convertView.findViewById(R.id.subTaskBtn);
            viewHolder.delete = (Button) convertView.findViewById(R.id.deleteBtn);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Job job = objects.get(position);
        viewHolder.task.setText(job.getJob());
        //return super.getView(position, convertView, parent);
        return convertView;
    }

    static class ViewHolder {
        public TextView task;
        public Button photo;
        public Button addSubTask;
        public Button delete;
    }

}