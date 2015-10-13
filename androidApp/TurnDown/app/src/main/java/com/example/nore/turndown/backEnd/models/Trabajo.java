package com.example.nore.turndown.backEnd.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author NORE
 */
public class Trabajo implements Serializable {

    private Integer idtrabajo;
    private String descripcion;
    private Reporte2 reporteIdreporte;
    private List<Imagen> imagenList;
    private List<Actividades> actividadesList;

    public Trabajo() {
    }

    public Trabajo(Integer idtrabajo) {
        this.idtrabajo = idtrabajo;
    }

    public Trabajo(Integer idtrabajo, String descripcion) {
        this.idtrabajo = idtrabajo;
        this.descripcion = descripcion;
    }

    public Integer getIdtrabajo() {
        return idtrabajo;
    }

    public void setIdtrabajo(Integer idtrabajo) {
        this.idtrabajo = idtrabajo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Reporte2 getReporteIdreporte() {
        return reporteIdreporte;
    }

    public void setReporteIdreporte(Reporte2 reporteIdreporte) {
        this.reporteIdreporte = reporteIdreporte;
    }

    public List<Imagen> getImagenList() {
        return imagenList;
    }

    public void setImagenList(List<Imagen> imagenList) {
        this.imagenList = imagenList;
    }
    
    public List<Actividades> getActividadesList() {
        return actividadesList;
    }

    public void setActividadesList(List<Actividades> actividadesList) {
        this.actividadesList = actividadesList;
    }
    
}
