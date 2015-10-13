package com.example.nore.turndown.backEnd.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author NORE
 */
public class Reporte2 implements Serializable {

    private Integer idreporte;
    private String trabajo;
    private String sitio;
    private Date fecha;
    private int estatus;
    private List<Trabajo> trabajoList;
    private Usuario2 usuarioIdusuario;

    public Reporte2() {
    }

    public Reporte2(Integer idreporte) {
        this.idreporte = idreporte;
    }

    public Reporte2(Integer idreporte, String trabajo, String sitio, Date fecha, int estatus) {
        this.idreporte = idreporte;
        this.trabajo = trabajo;
        this.sitio = sitio;
        this.fecha = fecha;
        this.estatus = estatus;
    }

    public Integer getIdreporte() {
        return idreporte;
    }

    public void setIdreporte(Integer idreporte) {
        this.idreporte = idreporte;
    }

    public String getTrabajo() {
        return trabajo;
    }

    public void setTrabajo(String trabajo) {
        this.trabajo = trabajo;
    }

    public String getSitio() {
        return sitio;
    }

    public void setSitio(String sitio) {
        this.sitio = sitio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public List<Trabajo> getTrabajoList() {
        return trabajoList;
    }

    public void setTrabajoList(List<Trabajo> trabajoList) {
        this.trabajoList = trabajoList;
    }

    public Usuario2 getUsuarioIdusuario() {
        return usuarioIdusuario;
    }

    public void setUsuarioIdusuario(Usuario2 usuarioIdusuario) {
        this.usuarioIdusuario = usuarioIdusuario;
    }

}
