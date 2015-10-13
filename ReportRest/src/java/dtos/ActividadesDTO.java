/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import com.mim.model.Actividades;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author NORE
 */
public class ActividadesDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    static List<ActividadesDTO> convertList(List<Actividades> actividadesList) {
        List<ActividadesDTO> list = new ArrayList<ActividadesDTO>();
        for (Actividades ac : actividadesList) {
            ActividadesDTO temp = new ActividadesDTO(ac.getIdactividades());
            temp.setDescripcion(ac.getDescripcion());
            list.add(temp);
        }
        return list;
    }
    private Integer idactividades;
    private String descripcion;
    private TrabajoDTO trabajoIdtrabajo;

    public ActividadesDTO() {
    }

    public ActividadesDTO(Integer idactividades) {
        this.idactividades = idactividades;
    }

    public ActividadesDTO(Integer idactividades, String descripcion) {
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

    public TrabajoDTO getTrabajoIdtrabajo() {
        return trabajoIdtrabajo;
    }

    public void setTrabajoIdtrabajo(TrabajoDTO trabajoIdtrabajo) {
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
        if (!(object instanceof ActividadesDTO)) {
            return false;
        }
        ActividadesDTO other = (ActividadesDTO) object;
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
