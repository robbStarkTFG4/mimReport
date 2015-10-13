package com.example.nore.turndown.CustomListView;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nore.turndown.MainActivity;
import com.example.nore.turndown.R;
import com.example.nore.turndown.customDialog.CameraDialog;
import com.example.nore.turndown.customDialog.CustomDialogFrag;
import com.example.nore.turndown.customDialog.ImaveViewDialog;
import com.example.nore.turndown.entity.dao.Job;
import com.example.nore.turndown.entity.dao.TaskJob;
import com.example.nore.turndown.util.tags.FragmentTags;
import com.example.nore.turndown.util.interfaces.WidgetPortable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by NORE on 03/07/2015.
 */
public class ExpandableCustomAdapter extends BaseExpandableListAdapter {

    public interface ObjectFacilitator {
        public void selectedJob(Job job, String title);
    }

    public interface WatchMen {
        public void deletedJob(int position);
    }

    private Context context;
    private List<Job> objects;
    private LayoutInflater mInflater;
    public String photoTitle;
    private ObjectFacilitator facil;
    private WatchMen watch;
    private int currentPos;
    private Dialog dialogo;

    public ExpandableCustomAdapter(Context context, List<Job> objects) {
        this.context = context;
        this.objects = objects;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return objects.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return objects.get(groupPosition).getTasks2().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return objects.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return objects.get(groupPosition).getTasks2().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public static final int REQUEST_IMAGE_CAPTURE = 4231;

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            convertView.setPadding(0, 3, 0, 5);

            viewHolder = new ViewHolder();
            viewHolder.task = (TextView) convertView.findViewById(R.id.subTask);

            viewHolder.photo = (Button) convertView.findViewById(R.id.photoBtn);
            viewHolder.photo.setBackgroundColor(context.getResources().getColor(R.color.resalta));
            viewHolder.photo.setTextColor(context.getResources().getColor(R.color.text_icons));

            //viewHolder.photo.setTextColor();
            viewHolder.addSubTask = (Button) convertView.findViewById(R.id.subTaskBtn);
            viewHolder.addSubTask.setBackgroundColor(context.getResources().getColor(R.color.bajito));
            viewHolder.addSubTask.setTextColor(context.getResources().getColor(R.color.text_icons));

            viewHolder.delete = (Button) convertView.findViewById(R.id.deleteBtn);
            viewHolder.delete.setBackgroundColor(context.getResources().getColor(R.color.oscurito));
            viewHolder.delete.setTextColor(context.getResources().getColor(R.color.text_icons));

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Job job = (Job) getGroup(groupPosition);

        String des = job.getJob();
        viewHolder.task.setText(des);


        viewHolder.task.setText(job.getJob());
        viewHolder.task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction ft = ((Activity) context).getFragmentManager().beginTransaction();
                Fragment prev = ((Activity) context).getFragmentManager().findFragmentByTag(FragmentTags.JOB_DIALOG);
                if (prev != null) {
                    ft.remove(prev);
                }
                //ft.addToBackStack(null);

                // Create and show the dialog.
                Portable port = new Portable(viewHolder.task);

                CustomDialogFrag newFragment = CustomDialogFrag.newInstance(1, job, port);
                newFragment.show(((Activity) context).getFragmentManager(), "dialog");

            }
        });
        
        viewHolder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPos = groupPosition;
                if (job.getImageInfo2() != null) {
                    if (job.getImageInfo2().isEmpty()) {
                        takePicture(groupPosition);
                    } else {
                        takePicture(groupPosition);
                    }
                } else {
                    takePicture(groupPosition);
                }
                //end
            }
        });

        viewHolder.photo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                currentPos = groupPosition;
                if (job.getImageInfo2() != null) {
                    if (job.getImageInfo2().isEmpty()) {
                        takePicture(groupPosition);
                    } else {
                        dialogo = onCreateDialogSingleChoice();
                        if (dialogo != null) {
                            dialogo.show();
                        }
                    }
                } else {
                    takePicture(groupPosition);
                }
                return true;
            }
        });

        viewHolder.addSubTask.setFocusable(false);
        viewHolder.addSubTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (job.getTasks2() != null) {
                    if (!isExpanded) {
                        ((ExpandableListView) parent).expandGroup(groupPosition);
                    } else {
                        ((ExpandableListView) parent).collapseGroup(groupPosition);
                    }
                }
            }
        });
        viewHolder.addSubTask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                List<TaskJob> list = job.getTasks2();
                if (list == null) {
                    list = new ArrayList<TaskJob>();
                    Log.d("d", "lista asignada");
                }
                TaskJob task = new TaskJob();
                task.setDescripcion("Nota...");
                list.add(task);
                notifyDataSetChanged();
                return true;
            }
        });

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Warn");
                builder.setMessage("Seguro en eliminar?");
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO
                        watch.deletedJob(groupPosition);
                        objects.remove(groupPosition);
                        notifyDataSetChanged();
                        dialog.dismiss();
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
                //objects.remove(groupPosition);
                //notifyDataSetChanged();
            }
        });
        return convertView;
    }

    private void takePicture(int groupPosition) {
        //cameraIntent(groupPosition);
        cameraApiDeprecated(groupPosition);
    }

    private void cameraApiDeprecated(int groupPosition) {
        FragmentTransaction ft = ((Activity) context).getFragmentManager().beginTransaction();
        android.app.Fragment prev = ((Activity) context).getFragmentManager().findFragmentByTag("cameraDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);


        CameraDialog newFragment = CameraDialog.newInstance((Job) getGroup(groupPosition));
        newFragment.show(((Activity) context).getFragmentManager(), "cameraDialog");
    }

    private void cameraApi2(int groupPosition) {
        //not implemented
    }

    private void cameraIntent(int groupPosition) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(((Activity) context).getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                facil.selectedJob(objects.get(groupPosition), photoFile.getPath());
            } catch (IOException ex) {
                // Error occurred while creating the File
                //...
                Toast.makeText(context, "exception in file creation", Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                ((Activity) context).startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Toast.makeText(context, "no se creo el archivo temporal", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "package manager es nulo", Toast.LENGTH_LONG).show();
        }
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public class Portable implements WidgetPortable {
        public TextView task;

        public Portable(TextView task) {
            this.task = task;
        }
    }

    static class ViewHolder {
        public TextView task;
        public Button photo;
        public Button addSubTask;
        public Button delete;
    }

    static class ViewHolderChild {
        public TextView description;
    }

    private File createImageFile() throws IOException {

        Random r = new Random();

        int i1 = r.nextInt(200 - 70) + 17;
        int i2 = r.nextInt(67 - 12) - 6;
        int i3 = r.nextInt(89 - 27) - 6;
        int i4 = r.nextInt(200 - 70) + 17;
        int i5 = r.nextInt(89 - 27) - 6;
        photoTitle = "JPEG_" + i1 + i2 + "uncompressed_" + i3 + i4 + "-" + i5;

        File image = new File(Environment.  //THIS WORKS
                getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                photoTitle + ".jpg");

        return image;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderChild viewHolderChild;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_child_item, parent, false);
            viewHolderChild = new ViewHolderChild();
            viewHolderChild.description = (TextView) convertView.findViewById(R.id.descriptionTask);


            convertView.setTag(viewHolderChild);
        } else {
            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }

        TaskJob task = objects.get(groupPosition).getTasks2().get(childPosition);
        viewHolderChild.description.setText(task.getDescripcion());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setFacil(ObjectFacilitator facil) {
        this.facil = facil;
    }

    public void setWatch(WatchMen watch) {
        this.watch = watch;
    }

    // util


    //Dialog
    private Dialog onCreateDialogSingleChoice() {
        if (context != null) {
            int index = 0;
            Dialog diag = null;
            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setTitle("Escoge accion");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    context,
                    android.R.layout.select_dialog_item);
            arrayAdapter.add("take picture");
            arrayAdapter.add("browse");

            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialogInput(which);
                }
            });

            return builder.create();
        } else {
            return null;
        }
    }

    private void dialogInput(int which) {
        dialogo.dismiss();
        switch (which) {
            case 0:
                takePicture(currentPos);
                break;
            case 1:
                browsePictures();
                break;
        }
    }

    private void browsePictures() {
        if (context != null) {
            FragmentTransaction ft = ((Activity) context).getFragmentManager().beginTransaction();
            Fragment prev = ((Activity) context).getFragmentManager().findFragmentByTag(FragmentTags.DIALOG_IMAGE);
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            if (objects != null) {
                ImaveViewDialog newFragment = ImaveViewDialog.newInstance(objects.get(currentPos));
                newFragment.show(((Activity) context).getFragmentManager(), FragmentTags.DIALOG_IMAGE);
            }
        }
    }

}
