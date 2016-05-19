package com.example.nore.turndown.customDialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nore.turndown.CustomListView.ExpandableCustomAdapter;
import com.example.nore.turndown.fragments.AddJobFragment;
import com.example.nore.turndown.R;
import com.example.nore.turndown.entity.dao.TaskJob;

/**
 * Created by NORE on 04/07/2015.
 */
public class CustomDialogSubTask extends DialogFragment {

    private ExpandableCustomAdapter.CouplerSaver save;

    public static CustomDialogSubTask newInstance(int num, TaskJob job, AddJobFragment.subTaskPort port) {
        CustomDialogSubTask f = new CustomDialogSubTask();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putSerializable("task", job);
        args.putSerializable("port", port);
        f.setArguments(args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final TaskJob jo = (TaskJob) getArguments().getSerializable("task");
        final AddJobFragment.subTaskPort port = (AddJobFragment.subTaskPort) getArguments().getSerializable("port");


        View root = inflater.inflate(R.layout.sub_task_dialog, null);
        final EditText edit = (EditText) root.findViewById(R.id.descriptionSubTaskArea);
        Button btnAceptar = (Button) root.findViewById(R.id.btnAceptarSubTask);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"nota agregada",Toast.LENGTH_LONG).show();
                if (save != null) {
                    save.notifyFragmentSmartSave();
                }
                String sub = edit.getText().toString();
                jo.setDescripcion(sub);
                port.subTextView.setText(sub);
                dismiss();
            }
        });
        edit.setText(jo.getDescripcion());
        getDialog().setTitle("Escribe Nota");

        return root;
    }

    public void setSave(ExpandableCustomAdapter.CouplerSaver save) {
        this.save = save;
    }
}
