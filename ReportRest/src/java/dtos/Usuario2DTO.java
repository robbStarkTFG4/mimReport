/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;


import java.io.Serializable;
import java.util.List;


/**
 *
 * @author NORE
 */

public class Usuario2DTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer idusuario;
    private String nombre;
    private String apellidos;
    private String usuario;
    private List<Reporte2DTO> reporte2List;

    public Usuario2DTO() {
    }

    public Usuario2DTO(Integer idusuario) {
        this.idusuario = idusuario;
    }

    public Usuario2DTO(Integer idusuario, String nombre, String apellidos, String usuario) {
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

    public List<Reporte2DTO> getReporte2List() {
        return reporte2List;
    }

    public void setReporte2List(List<Reporte2DTO> reporte2List) {
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
        if (!(object instanceof Usuario2DTO)) {
            return false;
        }
        Usuario2DTO other = (Usuario2DTO) object;
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
