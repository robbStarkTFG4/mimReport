package com.example.nore.turndown.util.dtos;

import com.example.nore.turndown.entity.dao.Reporte;

import java.io.Serializable;

/**
 * Created by NORE on 18/07/2015.
 */
public class RepoMen implements Serializable {
    private Long id;
    private String trabajo;
    private String locasion;
    private boolean selected = false;

    public RepoMen(Long id, String trabajo, String locasion) {
        this.id = id;
        this.trabajo = trabajo;
        this.locasion = locasion;
    }

    public RepoMen() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrabajo() {
        return trabajo;
    }

    public void setTrabajo(String trabajo) {
        this.trabajo = trabajo;
    }

    public String getLocasion() {
        return locasion;
    }

    public void setLocasion(String locasion) {
        this.locasion = locasion;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public static RepoMen transformToRepoMen(Reporte reporte) {
        RepoMen rep = new RepoMen();
        rep.setLocasion(reporte.getSitio());
        rep.setTrabajo(reporte.getTrabajo());
        rep.setId(reporte.getId());
        return rep;
    }
}
