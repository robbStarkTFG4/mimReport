package com.example.nore.turndown.backEnd;

import com.example.nore.turndown.backEnd.models.Actividades;
import com.example.nore.turndown.backEnd.models.Trabajo;
import com.example.nore.turndown.entity.dao.ImageInfo;
import com.example.nore.turndown.entity.dao.Job;
import com.example.nore.turndown.entity.dao.TaskJob;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NORE on 13/08/2015.
 */
public class ConvertManager {

    public static List<Trabajo> Convert(List<Job> list) {
        List<Trabajo> trabajoList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Job jb = list.get(i);
            trabajoList.add(convertToTrabajo(jb));
        }
        return trabajoList;
    }

    public static Trabajo convertToTrabajo(Job jb) {
        Trabajo tb = new Trabajo();
        tb.setDescripcion(jb.getJob());
        tb.setActividadesList(convertAct(jb.getTasks2()));

        List<String> imgList = new ArrayList<>();

        List<ImageInfo> list = jb.getImageInfo();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                ImageInfo img = list.get(i);
                imgList.add(img.getCompressedImage());
            }
        }
        return tb;
    }

    private static List<Actividades> convertAct(List<TaskJob> tasks2) {
        List<Actividades> actList = new ArrayList<>();

        for (int i = 0; i < tasks2.size(); i++) {
            TaskJob task = tasks2.get(i);
            Actividades act = new Actividades();

            act.setDescripcion(task.getDescripcion());

            actList.add(act);
        }
        return actList;
    }
}
