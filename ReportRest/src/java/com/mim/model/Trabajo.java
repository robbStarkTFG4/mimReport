/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mim.model;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author NORE
 */
@Entity
@Table(name = "trabajo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Trabajo.findAll", query = "SELECT t FROM Trabajo t"),
    @NamedQuery(name = "Trabajo.findByIdtrabajo", query = "SELECT t FROM Trabajo t WHERE t.idtrabajo = :idtrabajo"),
    @NamedQuery(name = "Trabajo.findByDescripcion", query = "SELECT t FROM Trabajo t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "Trabajo.findByImagen", query = "SELECT t FROM Trabajo t WHERE t.imagen = :imagen")})
public class Trabajo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idtrabajo")
    private Integer idtrabajo;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "imagen")
    private String imagen;
    @JoinColumn(name = "reporte_idreporte", referencedColumnName = "idreporte")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Reporte2 reporteIdreporte;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trabajoIdtrabajo", fetch = FetchType.LAZY)
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Reporte2 getReporteIdreporte() {
        return reporteIdreporte;
    }

    public void setReporteIdreporte(Reporte2 reporteIdreporte) {
        this.reporteIdreporte = reporteIdreporte;
    }

    public List<Actividades> getActividadesList() {
        return actividadesList;
    }

    public void setActividadesList(List<Actividades> actividadesList) {
        this.actividadesList = actividadesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtrabajo != null ? idtrabajo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Trabajo)) {
            return false;
        }
        Trabajo other = (Trabajo) object;
        if ((this.idtrabajo == null && other.idtrabajo != null) || (this.idtrabajo != null && !this.idtrabajo.equals(other.idtrabajo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mim.model.Trabajo[ idtrabajo=" + idtrabajo + " ]";
    }
    
}
