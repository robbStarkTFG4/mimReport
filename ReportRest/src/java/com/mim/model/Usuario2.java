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
@Table(name = "usuario2")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario2.findAll", query = "SELECT u FROM Usuario2 u"),
    @NamedQuery(name = "Usuario2.findByIdusuario", query = "SELECT u FROM Usuario2 u WHERE u.idusuario = :idusuario"),
    @NamedQuery(name = "Usuario2.findByNombre", query = "SELECT u FROM Usuario2 u WHERE u.nombre = :nombre"),
    @NamedQuery(name = "Usuario2.findByApellidos", query = "SELECT u FROM Usuario2 u WHERE u.apellidos = :apellidos"),
    @NamedQuery(name = "Usuario2.findByUsuario", query = "SELECT u FROM Usuario2 u WHERE u.usuario = :usuario")})
public class Usuario2 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idusuario")
    private Integer idusuario;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "apellidos")
    private String apellidos;
    @Basic(optional = false)
    @Column(name = "usuario")
    private String usuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioIdusuario", fetch = FetchType.LAZY)
    private List<Reporte2> reporte2List;

    public Usuario2() {
    }

    public Usuario2(Integer idusuario) {
        this.idusuario = idusuario;
    }

    public Usuario2(Integer idusuario, String nombre, String apellidos, String usuario) {
        this.idusuario = idusuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.usuario = usuario;
    }

    public Integer getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Integer idusuario) {
        this.idusuario = idusuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @XmlTransient
    public List<Reporte2> getReporte2List() {
        return reporte2List;
    }

    public void setReporte2List(List<Reporte2> reporte2List) {
        this.reporte2List = reporte2List;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idusuario != null ? idusuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario2)) {
            return false;
        }
        Usuario2 other = (Usuario2) object;
        if ((this.idusuario == null && other.idusuario != null) || (this.idusuario != null && !this.idusuario.equals(other.idusuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mim.model.Usuario2[ idusuario=" + idusuario + " ]";
    }
    
}
