/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.nore.turndown.backEnd.models;

import java.io.Serializable;

/**
 *
 * @author NORE
 */
public class Imagen implements Serializable {

    private Integer idimagen;
    private String imagen;
    private Trabajo trabajoIdtrabajo;

    public Imagen() {
    }

    public Imagen(Integer idimagen) {
        this.idimagen = idimagen;
    }

    public Integer getIdimagen() {
        return idimagen;
    }

    public void setIdimagen(Integer idimagen) {
        this.idimagen = idimagen;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Trabajo getTrabajoIdtrabajo() {
        return trabajoIdtrabajo;
    }

    public void setTrabajoIdtrabajo(Trabajo trabajoIdtrabajo) {
        this.trabajoIdtrabajo = trabajoIdtrabajo;
    }

}
