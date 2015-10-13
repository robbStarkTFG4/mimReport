/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import com.mim.model.Reporte2;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author NORE
 */
public class Reporte2DTO implements Serializable {

    private static final long serialVersionUID = 1L;

    static Reporte2DTO convertRep(Reporte2 reporteIdreporte) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private Integer idreporte;
    private String trabajo;
    private String sitio;
    private Date fecha;
    private int estatus;
    private List<TrabajoDTO> trabajoList;
    private Usuario2DTO usuarioIdusuario;

    public Reporte2DTO() {
    }

    public Reporte2DTO(Integer idreporte) {
        this.idreporte = idreporte;
    }

    public Reporte2DTO(Integer idreporte, String trabajo, String sitio, int estatus) {
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

    public List<TrabajoDTO> getTrabajoList() {
        return trabajoList;
    }

    public void setTrabajoList(List<TrabajoDTO> trabajoList) {
        this.trabajoList = trabajoList;
    }

    public Usuario2DTO getUsuarioIdusuario() {
        return usuarioIdusuario;
    }

    public void setUsuarioIdusuario(Usuario2DTO usuarioIdusuario) {
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
        if (!(object instanceof Reporte2DTO)) {
            return false;
        }
        Reporte2DTO other = (Reporte2DTO) object;
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
