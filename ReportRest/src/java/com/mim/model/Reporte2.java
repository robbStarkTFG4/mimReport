/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mim.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author NORE
 */
@Entity
@Table(name = "reporte2")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reporte2.findAll", query = "SELECT r FROM Reporte2 r"),
    @NamedQuery(name = "Reporte2.findByIdreporte", query = "SELECT r FROM Reporte2 r WHERE r.idreporte = :idreporte"),
    @NamedQuery(name = "Reporte2.findByTrabajo", query = "SELECT r FROM Reporte2 r WHERE r.trabajo = :trabajo"),
    @NamedQuery(name = "Reporte2.findBySitio", query = "SELECT r FROM Reporte2 r WHERE r.sitio = :sitio"),
    @NamedQuery(name = "Reporte2.findByFecha", query = "SELECT r FROM Reporte2 r WHERE r.fecha = :fecha"),
    @NamedQuery(name = "Reporte2.findByEstatus", query = "SELECT r FROM Reporte2 r WHERE r.estatus = :estatus")})
public class Reporte2 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idreporte")
    private Integer idreporte;
    @Basic(optional = false)
    @Column(name = "trabajo")
    private String trabajo;
    @Basic(optional = false)
    @Column(name = "sitio")
    private String sitio;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "estatus")
    private int estatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reporteIdreporte", fetch = FetchType.LAZY)
    private List<Trabajo> trabajoList;
    @JoinColumn(name = "usuario_idusuario", referencedColumnName = "idusuario")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario2 usuarioIdusuario;

    public Reporte2() {
    }

    public Reporte2(Integer idreporte) {
        this.idreporte = idreporte;
    }

    public Reporte2(Integer idreporte, String trabajo, String sitio, int estatus) {
        this.idreporte = idreporte;
        this.trabajo = trabajo;
        this.sitio = sitio;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idreporte != null ? idreporte.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reporte2)) {
            return false;
        }
        Reporte2 other = (Reporte2) object;
        if ((this.idreporte == null && other.idreporte != null) || (this.idreporte != null && !this.idreporte.equals(other.idreporte))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mim.model.Reporte2[ idreporte=" + idreporte + " ]";
    }
    
}
