/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import com.mim.model.Actividades;
import com.mim.model.Reporte2;
import com.mim.model.Trabajo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author NORE
 */
public class TrabajoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public static List<TrabajoDTO> convertList(List<Trabajo> trabajoList) {
        List<TrabajoDTO> list = new ArrayList<TrabajoDTO>();
        for (Trabajo el : trabajoList) {
            TrabajoDTO jb = new TrabajoDTO(el.getIdtrabajo());
            jb.setDescripcion(el.getDescripcion());
            if (el.getImagen() != null) {
                jb.setImagen(el.getImagen());
            }

            jb.setActividadesList(ActividadesDTO.convertList(el.getActividadesList()));
            //jb.setReporteIdreporte(Reporte2DTO.convertRep(el.getReporteIdreporte());
            list.add(jb);
        }
        return list;
    }
    private Integer idtrabajo;
    private String descripcion;
    private String imagen;
    private Reporte2DTO reporteIdreporte;
    private List<ActividadesDTO> actividadesList;

    public TrabajoDTO() {
    }

    public TrabajoDTO(Integer idtrabajo) {
        this.idtrabajo = idtrabajo;
    }

    public TrabajoDTO(Integer idtrabajo, String descripcion) {
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

    public Reporte2DTO getReporteIdreporte() {
        return reporteIdreporte;
    }

    public void setReporteIdreporte(Reporte2DTO reporteIdreporte) {
        this.reporteIdreporte = reporteIdreporte;
    }

    public List<ActividadesDTO> getActividadesList() {
        return actividadesList;
    }

    public void setActividadesList(List<ActividadesDTO> actividadesList) {
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
        if (!(object instanceof TrabajoDTO)) {
            return false;
        }
        TrabajoDTO other = (TrabajoDTO) object;
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
