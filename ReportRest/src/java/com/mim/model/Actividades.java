/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mim.model;

import java.io.Serializable;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author NORE
 */
@Entity
@Table(name = "actividades")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Actividades.findAll", query = "SELECT a FROM Actividades a"),
    @NamedQuery(name = "Actividades.findByIdactividades", query = "SELECT a FROM Actividades a WHERE a.idactividades = :idactividades"),
    @NamedQuery(name = "Actividades.findByDescripcion", query = "SELECT a FROM Actividades a WHERE a.descripcion = :descripcion")})
public class Actividades implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idactividades")
    private Integer idactividades;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "trabajo_idtrabajo", referencedColumnName = "idtrabajo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idactividades != null ? idactividades.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Actividades)) {
            return false;
        }
        Actividades other = (Actividades) object;
        if ((this.idactividades == null && other.idactividades != null) || (this.idactividades != null && !this.idactividades.equals(other.idactividades))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mim.model.Actividades[ idactividades=" + idactividades + " ]";
    }
    
}
