
package com.example.nore.turndown.backEnd.models;

import java.io.Serializable;


/**
 *
 * @author NORE
 */
public class Actividades implements Serializable {

    private Integer idactividades;
    private String descripcion;
    private Trabajo trabajoIdtrabajo;

    public Actividades() {
    }

    public Actividades(Integer idactividades) {
        this.idactividades = idactividades;
    }

    public Actividades(Integer idactividades, String descripcion) {
        this.idactividades = idactividades;
        this.descripcion = descripcion;
    }

    public Integer getIdactividades() {
        return idactividades;
    }

    public void setIdactividades(Integer idactividades) {
        this.idactividades = idactividades;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Trabajo getTrabajoIdtrabajo() {
        return trabajoIdtrabajo;
    }

    public void setTrabajoIdtrabajo(Trabajo trabajoIdtrabajo) {
        this.trabajoIdtrabajo = trabajoIdtrabajo;
    }

}
