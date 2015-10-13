
package com.example.nore.turndown.backEnd.models;

import java.io.Serializable;
import java.util.List;


/**
 *
 * @author NORE
 */

public class Usuario2 implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer idusuario;
    private String nombre;
    private String apellidos;
    private String usuario;
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

    public List<Reporte2> getReporte2List() {
        return reporte2List;
    }

    public void setReporte2List(List<Reporte2> reporte2List) {
        this.reporte2List = reporte2List;
    }

  
 
 
}
