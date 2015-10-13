package com.example.nore.turndown.customDialog;

import android.app.Dialog;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nore.turndown.CustomListView.ExpandableCustomAdapter;
import com.example.nore.turndown.R;
import com.example.nore.turndown.entity.dao.Job;


/**
 * Created by NORE on 04/07/2015.
 */
public class CustomDialogFrag extends DialogFragment {


    public static CustomDialogFrag newInstance(int num, Job job, ExpandableCustomAdapter.Portable port) {
        CustomDialogFrag f = new CustomDialogFrag();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putSerializable("job", job);
        args.putSerializable("port", port);
        f.setArguments(args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final Job jo = (Job) getArguments().getSerializable("job");
        final ExpandableCustomAdapter.Portable pot = (ExpandableCustomAdapter.Portable) getArguments()
                .getSerializable("port");

        View root = inflater.inflate(R.layout.custom_dialog, null);
        final EditText edit = (EditText) root.findViewById(R.id.descriptionArea);
        Button btnAceptar = (Button) root.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = edit.getText().toString();
                jo.setJob(description);

                pot.task.setText(description);

                getDialog().dismiss();
            }
        });
        edit.setText(jo.getJob());
        getDialog().setTitle("Escribe descripcion");

        return root;
    }

}
